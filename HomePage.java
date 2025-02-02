import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    public HomePage() {
        setTitle("Healthcare Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color sidebarColor = new Color(211, 235, 214);
        Color buttonColor = new Color(109, 190, 118);
        Color buttonHoverColor = new Color(180, 220, 185);
        Color textColor = Color.WHITE;
        Color exitButtonColor = new Color(61, 61, 61);
        Color exitHoverColor = new Color(80, 80, 80);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(250, 600));
        sidebar.setBackground(sidebarColor);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBackground(sidebarColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnAppointments = createStyledButton("Book Appointment", buttonColor, buttonHoverColor, textColor);
        JButton btnDoctors = createStyledButton(" Doctor Management", buttonColor, buttonHoverColor, textColor);
        JButton btnPatients = createStyledButton("Patient Records", buttonColor, buttonHoverColor, textColor);
        JButton btnPharmacy = createStyledButton("Pharmacy Inventory", buttonColor, buttonHoverColor, textColor);
        JButton btnNotifications = createStyledButton("Notifications", buttonColor, buttonHoverColor, textColor);
        JButton btnExit = createStyledButton("Exit", exitButtonColor, exitHoverColor, Color.WHITE); // Red exit button

        buttonPanel.add(btnAppointments);
        buttonPanel.add(btnDoctors);
        buttonPanel.add(btnPatients);
        buttonPanel.add(btnPharmacy);
        buttonPanel.add(btnNotifications);
        buttonPanel.add(btnExit);

        sidebar.add(buttonPanel, BorderLayout.CENTER);
        add(sidebar, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        ImageIcon icon = new ImageIcon("src/images/logo.png"); // Change path if needed
        Image img = icon.getImage().getScaledInstance(700, 600, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        rightPanel.add(imageLabel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.CENTER);

        btnAppointments.addActionListener(e -> openWindow(new BookAppointmentForm()));
        btnDoctors.addActionListener(e -> openWindow(new DoctorManagementForm()));
        btnPatients.addActionListener(e -> openWindow(new PatientRecordsForm()));
        btnPharmacy.addActionListener(e -> openWindow(new PharmacyInventoryForm()));
        btnNotifications.addActionListener(e -> openWindow(new NotificationApp()));
        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
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

    private void openWindow(JFrame frame) {
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage());
    }
}
