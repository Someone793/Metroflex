import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PharmacyInventoryForm extends JFrame {
    private JTextField txtName, txtQuantity, txtPrice;
    private JButton btnAdd, btnUpdate, btnDelete;
    private JTable table;
    private DefaultTableModel model;
    private Connection conn;

    private static final int LOW_STOCK_THRESHOLD = 5;

    public PharmacyInventoryForm() {
        setTitle("Pharmacy Inventory Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        getContentPane().setBackground(new Color(193, 255, 193));
        Font font = new Font("Arial", Font.PLAIN, 14);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Medicine Name:"), gbc);
        JLabel nameLabel = new JLabel("Medicine Name:");
        nameLabel.setForeground(Color.BLACK);
        add(nameLabel, gbc);

        txtName = new JTextField();
        txtName.setFont(font);
        txtName.setBackground(new Color(230, 230, 230));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Quantity:"), gbc);

        txtQuantity = new JTextField();
        txtQuantity.setFont(font);
        txtQuantity.setBackground(new Color(230, 230, 230));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        add(txtQuantity, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Price:"), gbc);

        txtPrice = new JTextField();
        txtPrice.setFont(font);
        txtPrice.setBackground(new Color(230, 230, 230));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        add(txtPrice, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");

        Color buttonGreen = new Color(46, 204, 113);
        btnAdd.setBackground(buttonGreen);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(font);
        
        btnUpdate.setBackground(buttonGreen);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(font);
        
        btnDelete.setBackground(buttonGreen);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(font);
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        add(buttonPanel, gbc);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID", "Name", "Quantity", "Price"});
        table = new JTable(model);
        table.setFont(font);
        table.setBackground(new Color(44, 62, 80));
        table.setForeground(Color.WHITE);
        table.getTableHeader().setBackground(buttonGreen);
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane pane = new JScrollPane(table);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(pane, gbc);

        connectDatabase();
        loadInventory();

        btnAdd.addActionListener(e -> addMedicine());
        btnUpdate.addActionListener(e -> updateMedicine());
        btnDelete.addActionListener(e -> deleteMedicine());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtName.setText(model.getValueAt(row, 1).toString());
                txtQuantity.setText(model.getValueAt(row, 2).toString());
                txtPrice.setText(model.getValueAt(row, 3).toString());
            }
        });

        setVisible(true);
    }

    private void connectDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doctordb", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed!");
            e.printStackTrace();
        }
    }

    private void loadInventory() {
        try {
            model.setRowCount(0);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM pharmacy_inventory");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");

                model.addRow(new Object[]{id, name, quantity, price});

                // Low stock warning
                if (quantity < LOW_STOCK_THRESHOLD) {
                    String alertMessage = "⚠ Low Stock Alert: " + name + " has only " + quantity + " left!";
                    JOptionPane.showMessageDialog(this, alertMessage, "Low Stock Warning", JOptionPane.WARNING_MESSAGE);
                    
                    String pharmacistEmail = "dinilpathirana@gmail.com"; 
                    String emailSubject = "Low Stock Alert: " + name;
                    String emailBody = "Dear Pharmacist,\n\nStock for " + name + " is running low. Only " + quantity + " left in inventory.\nPlease restock soon.";
                    
                    EmailSender.sendEmail(pharmacistEmail, emailSubject, emailBody);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addMedicine() {
        try {
            String name = txtName.getText();
            int quantity = Integer.parseInt(txtQuantity.getText());
            double price = Double.parseDouble(txtPrice.getText());

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO pharmacy_inventory (name, quantity, price) VALUES (?, ?, ?)");
            stmt.setString(1, name);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Medicine Added Successfully!");
            loadInventory();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
            e.printStackTrace();
        }
    }

    private void updateMedicine() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row to update!");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            String name = txtName.getText();
            int quantity = Integer.parseInt(txtQuantity.getText());
            double price = Double.parseDouble(txtPrice.getText());

            PreparedStatement stmt = conn.prepareStatement("UPDATE pharmacy_inventory SET name=?, quantity=?, price=? WHERE id=?");
            stmt.setString(1, name);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.setInt(4, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Medicine Updated Successfully!");
            loadInventory();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
            e.printStackTrace();
        }
    }

    private void deleteMedicine() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row to delete!");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM pharmacy_inventory WHERE id=?");
            stmt.setInt(1, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Medicine Deleted Successfully!");
            loadInventory();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Deletion Failed!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PharmacyInventoryForm::new);
    }
}
