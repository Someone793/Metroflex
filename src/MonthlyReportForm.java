import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class MonthlyReportForm extends JFrame {
    public MonthlyReportForm() {
        setTitle("Monthly Report - Healthcare Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title Label - Centered and Large
        JLabel titleLabel = new JLabel("Monthly Report");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Bigger Font
        titleLabel.setForeground(new Color(0, 100, 0)); // Dark Green Color
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Reduced bottom space

        // Main panel with compact spacing
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Reduce space between tables and limit their height
        mainPanel.add(createTableSection("Appointments", new String[]{"Patient Name", "Doctor Name", "Date", "Time"}, "SELECT patient_name, doctor_name, appointment_date, appointment_time FROM appointments"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Small gap
        mainPanel.add(createTableSection("Patients", new String[]{"First Name", "Last Name", "Gender", "Contact", "Email"}, "SELECT first_name, last_name, gender, contact_number, email FROM patients"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Small gap
        mainPanel.add(createTableSection("Pharmacy Inventory", new String[]{"Name", "Quantity", "Price"}, "SELECT name, quantity, price FROM pharmacy_inventory"));

        // Add everything to a scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createTableSection(String title, String[] columnNames, String query) {
        JPanel sectionPanel = new JPanel(new BorderLayout());

        // Section Title Label with green color
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sectionLabel.setForeground(new Color(0, 128, 0)); // Green text
        sectionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Reduced space

        // Create Table
        JTable table = createTable(columnNames);
        fetchData(table, query);

        // Limit table height to make it more compact
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(850, 120)); // **Fixed table height**
        scrollPane.setMaximumSize(new Dimension(850, 120));

        // Add components to the section panel
        sectionPanel.add(sectionLabel, BorderLayout.NORTH);
        sectionPanel.add(scrollPane, BorderLayout.CENTER);

        return sectionPanel;
    }

    private JTable createTable(String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        // Style table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(144, 238, 144)); // Light green header
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 12));

        // Style table rows
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(22);

        return table;
    }

    private void fetchData(JTable table, String query) {
        try (Connection conn = DBConnection.connect()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);

                while (rs.next()) {
                    int columnCount = model.getColumnCount();
                    Object[] rowData = new Object[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        rowData[i] = rs.getObject(i + 1);
                    }
                    model.addRow(rowData);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching data from database.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MonthlyReportForm().setVisible(true));
    }
}
