import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotificationApp extends JFrame {
    private JTextField emailField, phoneField;
    private JTextArea messageArea;
    private JButton sendEmailButton, sendSmsButton;

    // Color theme variables
    Color sidebarColor = new Color(211, 235, 214);
    Color buttonColor = new Color(109, 190, 118);
    Color buttonHoverColor = new Color(180, 220, 185);
    Color textColor = Color.WHITE; 
    Color exitButtonColor = new Color(61, 61, 61);
    Color exitHoverColor = new Color(80, 80, 80);

    public NotificationApp() {
        setTitle("Appointment Notification System");
        setSize(500, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        getContentPane().setBackground(sidebarColor);


        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(new Color(70, 70, 70));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        add(emailLabel, gbc);

        emailField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.8;
        add(emailField, gbc);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 14));
        phoneLabel.setForeground(new Color(70, 70, 70));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        add(phoneLabel, gbc);

        phoneField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.8;
        add(phoneField, gbc);

        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setForeground(new Color(70, 70, 70));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        add(messageLabel, gbc);

        messageArea = new JTextArea("Your Booked Appointment is on DATE at TIME", 4, 20);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0.8;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);


        sendEmailButton = createButton("Send Email");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.4;
        gbc.fill = GridBagConstraints.NONE;
        add(sendEmailButton, gbc);

        sendSmsButton = createButton("Send SMS");
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0.4;
        gbc.fill = GridBagConstraints.NONE;
        add(sendSmsButton, gbc);

        sendEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String message = messageArea.getText();
                EmailSender.sendEmail(email, "Appointment Reminder", message);
            }
        });

        sendSmsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phone = phoneField.getText();
                String message = messageArea.getText();
                SmsSender.sendSms(phone, message);
            }
        });

        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(buttonColor);
        button.setForeground(textColor);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(buttonHoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(buttonColor);
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NotificationApp::new);
    }
}
