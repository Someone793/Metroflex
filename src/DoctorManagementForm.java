import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DoctorManagementForm extends JFrame {

    private JTextField txtName, txtSpecialization, txtPhone, txtEmail, txtSchedule;
    private JButton btnAddDoctor, btnViewDoctors, btnDeleteDoctor, btnUpdateDoctor;
    private JTable tblDoctors;
    private DefaultTableModel model;
    private JScrollPane tableScrollPane;

    private Connection connection;

    public DoctorManagementForm() {
        // Establish connection to the database
        connectToDatabase();

        setTitle("Doctor Management System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        Color buttonColor = new Color(109, 190, 118);
        Color buttonHoverColor = new Color(180, 220, 185);
        Color textColor = Color.WHITE;
        Color sidebarColor = new Color(211, 235, 214);
        Color tableHeaderColor = new Color(109, 190, 118);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Doctor Details"));
        inputPanel.setBackground(sidebarColor);

        txtName = new JTextField();
        txtSpecialization = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtSchedule = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(txtName);
        inputPanel.add(new JLabel("Specialization:"));
        inputPanel.add(txtSpecialization);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(txtPhone);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(txtEmail);
        inputPanel.add(new JLabel("Schedule:"));
        inputPanel.add(txtSchedule);

        JPanel buttonPanel = new JPanel();
        btnAddDoctor = createStyledButton("Add Doctor", buttonColor, buttonHoverColor, textColor);
        btnViewDoctors = createStyledButton("View Doctors", buttonColor, buttonHoverColor, textColor);
        btnDeleteDoctor = createStyledButton("Delete Doctor", buttonColor, buttonHoverColor, textColor);
        btnUpdateDoctor = createStyledButton("Update Doctor", buttonColor, buttonHoverColor, textColor);

        buttonPanel.add(btnAddDoctor);
        buttonPanel.add(btnViewDoctors);
        buttonPanel.add(btnDeleteDoctor);
        buttonPanel.add(btnUpdateDoctor);

        model = new DefaultTableModel(new String[]{"Name", "Specialization", "Phone", "Email", "Schedule"}, 0);
        tblDoctors = new JTable(model);
        tblDoctors.getTableHeader().setBackground(tableHeaderColor);
        tableScrollPane = new JScrollPane(tblDoctors);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        tableScrollPane.setVisible(false);

        loadDoctors();

        btnAddDoctor.addActionListener(e -> addDoctor());
        btnViewDoctors.addActionListener(e -> viewDoctors());
        btnDeleteDoctor.addActionListener(e -> deleteDoctor());
        btnUpdateDoctor.addActionListener(e -> updateDoctor());

        tblDoctors.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblDoctors.getSelectedRow();
            if (selectedRow != -1) {
                txtName.setText((String) model.getValueAt(selectedRow, 0));
                txtSpecialization.setText((String) model.getValueAt(selectedRow, 1));
                txtPhone.setText((String) model.getValueAt(selectedRow, 2));
                txtEmail.setText((String) model.getValueAt(selectedRow, 3));
                txtSchedule.setText((String) model.getValueAt(selectedRow, 4));
            }
        });
    }

    private void connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/doctordb"; // Change this to your DB URL
        String user = "root"; // Replace with your DB username
        String password = ""; // Replace with your DB password

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to database.");
        }
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void addDoctor() {
        String name = txtName.getText();
        String specialization = txtSpecialization.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        String schedule = txtSchedule.getText();

        try {
            String query = "INSERT INTO doctors (name, specialization, phone, email, schedule) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, specialization);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, schedule);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor added successfully.");
            loadDoctors();  // Refresh the list after adding a doctor
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding doctor.");
        }
    }

    private void loadDoctors() {
        try {
            String query = "SELECT * FROM doctors";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            model.setRowCount(0); // Clear existing data in the table

            while (rs.next()) {
                String name = rs.getString("name");
                String specialization = rs.getString("specialization");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String schedule = rs.getString("schedule");

                model.addRow(new Object[]{name, specialization, phone, email, schedule});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading doctors.");
        }
    }

    private void viewDoctors() {
        tableScrollPane.setVisible(true);
        loadDoctors();
        revalidate();
        repaint();
        JOptionPane.showMessageDialog(this, "Doctors are listed in the table.");
    }

    private void deleteDoctor() {
        int selectedRow = tblDoctors.getSelectedRow();
        if (selectedRow != -1) {
            String name = (String) model.getValueAt(selectedRow, 0);

            try {
                String query = "DELETE FROM doctors WHERE name = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, name);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully.");
                loadDoctors();  // Refresh the list after deletion
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting doctor.");
            }
        }
    }

    private void updateDoctor() {
        int selectedRow = tblDoctors.getSelectedRow();
        if (selectedRow != -1) {
            String name = (String) model.getValueAt(selectedRow, 0);
            String newName = txtName.getText();
            String newSpecialization = txtSpecialization.getText();
            String newPhone = txtPhone.getText();
            String newEmail = txtEmail.getText();
            String newSchedule = txtSchedule.getText();

            try {
                String query = "UPDATE doctors SET name = ?, specialization = ?, phone = ?, email = ?, schedule = ? WHERE name = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, newName);
                stmt.setString(2, newSpecialization);
                stmt.setString(3, newPhone);
                stmt.setString(4, newEmail);
                stmt.setString(5, newSchedule);
                stmt.setString(6, name);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Doctor updated successfully.");
                loadDoctors();  // Refresh the list after update
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating doctor.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DoctorManagementForm().setVisible(true));
    }
}
