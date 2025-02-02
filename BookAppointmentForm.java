import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class BookAppointmentForm extends JFrame {
    private JComboBox<String> cmbDoctors, cmbPatients;
    private JTextField txtAppointmentDate, txtAppointmentTime;
    private JButton btnBookAppointment, btnViewAppointments;
    private JTable table;
    private DefaultTableModel model;
    private Connection conn;

    // Color theme variables
    Color backgroundColor = new Color(193, 255, 193);
    Color buttonColor = new Color(46, 204, 113);
    Color buttonHoverColor = new Color(180, 220, 185);
    Color textColor = Color.WHITE;
    Color tableHeaderColor = new Color(46, 204, 113);

    public BookAppointmentForm() {
        setTitle("Book Appointments");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        
        getContentPane().setBackground(backgroundColor);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel labelStyle = new JLabel();
        labelStyle.setFont(new Font("Arial", Font.BOLD, 14));
        labelStyle.setForeground(new Color(70, 70, 70));

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Select Patient:"), gbc);
        gbc.gridx = 1;
        cmbPatients = new JComboBox<>();
        inputPanel.add(cmbPatients, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Select Doctor:"), gbc);
        gbc.gridx = 1;
        cmbDoctors = new JComboBox<>();
        inputPanel.add(cmbDoctors, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Appointment Date:"), gbc);
        gbc.gridx = 1;
        txtAppointmentDate = new JTextField("YYYY-MM-DD");
        inputPanel.add(txtAppointmentDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Appointment Time:"), gbc);
        gbc.gridx = 1;
        txtAppointmentTime = new JTextField("HH:MM");
        inputPanel.add(txtAppointmentTime, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        btnBookAppointment = createButton("Book Appointment", buttonColor, buttonHoverColor);
        inputPanel.add(btnBookAppointment, gbc);

        gbc.gridx = 1;
        btnViewAppointments = createButton("View Appointments", buttonColor, buttonHoverColor);
        inputPanel.add(btnViewAppointments, gbc);

        add(inputPanel, BorderLayout.NORTH);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Patient Name", "Doctor", "Date", "Time"});
        table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setSelectionBackground(buttonColor);
        table.getTableHeader().setBackground(tableHeaderColor);
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(pane, BorderLayout.CENTER);

        connectDatabase();
        loadDoctors();
        loadPatients();

        btnBookAppointment.addActionListener(e -> bookAppointment());
        btnViewAppointments.addActionListener(e -> viewAppointments());

        setVisible(true);
    }

    private JButton createButton(String text, Color color, Color hoverColor) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(textColor);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        return button;
    }

    private void connectDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doctordb", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed!");
            e.printStackTrace();
        }
    }

    private void loadDoctors() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doctors");
            while (rs.next()) {
                cmbDoctors.addItem(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPatients() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM patients");
            while (rs.next()) {
                cmbPatients.addItem(rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void bookAppointment() {
        try {
            String patientName = cmbPatients.getSelectedItem().toString();
            String doctorName = cmbDoctors.getSelectedItem().toString();
            String appointmentDate = txtAppointmentDate.getText();
            String appointmentTime = txtAppointmentTime.getText();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO appointments (patient_name, doctor_name, appointment_date, appointment_time) VALUES (?, ?, ?, ?)");
            stmt.setString(1, patientName);
            stmt.setString(2, doctorName);
            stmt.setString(3, appointmentDate);
            stmt.setString(4, appointmentTime);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Appointment Booked Successfully!");
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
            e.printStackTrace();
        }
    }

    private void viewAppointments() {
        try {
            model.setRowCount(0);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM appointments");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("patient_name"), rs.getString("doctor_name"),
                        rs.getString("appointment_date"), rs.getString("appointment_time")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BookAppointmentForm();
    }
}
