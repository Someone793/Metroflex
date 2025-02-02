import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PatientRecordsForm extends JFrame {
    private JTextField txtFirstName, txtLastName, txtDOB, txtContact, txtEmail, txtAddress;
    private JComboBox<String> cmbGender;
    private JButton btnAdd, btnUpdate, btnDelete;
    private JTable table;
    private DefaultTableModel model;
    private Connection conn;

    // Color theme variables
    Color sidebarColor = new Color(211, 235, 214);
    Color buttonColor = new Color(109, 190, 118);
    Color buttonHoverColor = new Color(180, 220, 185);
    Color textColor = Color.WHITE;
    Color exitButtonColor = new Color(61, 61, 61);
    Color exitHoverColor = new Color(80, 80, 80);

    public PatientRecordsForm() {
        setTitle("Patient Records Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        getContentPane().setBackground(sidebarColor);

        JLabel labelStyle = new JLabel();
        labelStyle.setFont(new Font("Arial", Font.BOLD, 14));
        labelStyle.setForeground(new Color(70, 70, 70));

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("First Name:"), gbc);

        txtFirstName = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        add(txtFirstName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Last Name:"), gbc);

        txtLastName = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        add(txtLastName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Date of Birth:"), gbc);

        txtDOB = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        add(txtDOB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Gender:"), gbc);

        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        add(cmbGender, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Contact:"), gbc);

        txtContact = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        add(txtContact, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Address:"), gbc);

        txtAddress = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        add(txtAddress, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(sidebarColor);
        btnAdd = createButton("Add", buttonColor, buttonHoverColor);
        btnUpdate = createButton("Update", buttonColor, buttonHoverColor);
        btnDelete = createButton("Delete", buttonColor, buttonHoverColor);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        add(buttonPanel, gbc);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID", "First Name", "Last Name", "DOB", "Gender", "Contact", "Email", "Address"});
        table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setSelectionBackground(buttonColor);
        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(pane, gbc);

        btnAdd.addActionListener(e -> addPatient());
        btnUpdate.addActionListener(e -> updatePatient());
        btnDelete.addActionListener(e -> deletePatient());

        connectDatabase();
        loadPatients();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtFirstName.setText(model.getValueAt(row, 1).toString());
                txtLastName.setText(model.getValueAt(row, 2).toString());
                txtDOB.setText(model.getValueAt(row, 3).toString());
                cmbGender.setSelectedItem(model.getValueAt(row, 4).toString());
                txtContact.setText(model.getValueAt(row, 5).toString());
                txtEmail.setText(model.getValueAt(row, 6).toString());
                txtAddress.setText(model.getValueAt(row, 7).toString());
            }
        });

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

    private void loadPatients() {
        try {
            model.setRowCount(0);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM patients");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("patient_id"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("date_of_birth"), rs.getString("gender"),
                        rs.getString("contact_number"), rs.getString("email"), rs.getString("address")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPatient() {
        try {
            String firstName = txtFirstName.getText();
            String lastName = txtLastName.getText();
            String dob = txtDOB.getText();
            String gender = cmbGender.getSelectedItem().toString();
            String contact = txtContact.getText();
            String email = txtEmail.getText();
            String address = txtAddress.getText();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO patients (first_name, last_name, date_of_birth, gender, contact_number, email, address) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, dob);
            stmt.setString(4, gender);
            stmt.setString(5, contact);
            stmt.setString(6, email);
            stmt.setString(7, address);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Patient Added Successfully!");
            loadPatients();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
            e.printStackTrace();
        }
    }

    private void updatePatient() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row to update!");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            String firstName = txtFirstName.getText();
            String lastName = txtLastName.getText();
            String dob = txtDOB.getText();
            String gender = cmbGender.getSelectedItem().toString();
            String contact = txtContact.getText();
            String email = txtEmail.getText();
            String address = txtAddress.getText();

            PreparedStatement stmt = conn.prepareStatement("UPDATE patients SET first_name=?, last_name=?, date_of_birth=?, gender=?, contact_number=?, email=?, address=? WHERE patient_id=?");
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, dob);
            stmt.setString(4, gender);
            stmt.setString(5, contact);
            stmt.setString(6, email);
            stmt.setString(7, address);
            stmt.setInt(8, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Patient Updated Successfully!");
            loadPatients();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
            e.printStackTrace();
        }
    }

    private void deletePatient() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete!");
                return;
            }

            int id = (int) model.getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this patient?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM patients WHERE patient_id = ?");
                stmt.setInt(1, id);
                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
                    loadPatients();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete patient!");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting patient!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PatientRecordsForm::new);
    }
}
