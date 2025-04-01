package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoginScreen {
    private JFrame loginFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public static boolean isLoggedInAsAdmin = false;

    public LoginScreen() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        loginFrame = new JFrame("University of Applied Sciences and Technology (Suriname)");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        loginFrame.setUndecorated(false); // default decorations

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(63, 169, 255), 0, height, Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        try {
            // Gebruik een relatief pad vanaf de project root
            ImageIcon logoIcon = new ImageIcon("unasatlogo.png");

            // Debug output
            System.out.println("Logo geladen vanaf: " + new File("unasatlogo.png").getAbsolutePath());

            // Schaal het logo
            Image image = logoIcon.getImage();
            logoIcon = new ImageIcon(image);

            JLabel logoLabel = new JLabel(logoIcon);
            logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
            topPanel.add(logoLabel, BorderLayout.WEST);
        } catch (Exception e) {
            System.err.println("Fout bij laden logo: " + e.getMessage());
            System.err.println("Huidige werkdirectory: " + System.getProperty("user.dir"));
            System.err.println("Bestand bestaat: " + new File("unasatlogo.png").exists());
        }

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Rest van je code blijft hetzelfde...
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Studenten Administratie");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 70));
        titleLabel.setForeground(new Color(211, 85, 0));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // padding/spacing tussen components
        gbc.fill = GridBagConstraints.HORIZONTAL; // expands width

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Gebruikersnaam:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(63, 169, 255), 2));
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Wachtwoord:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(63, 169, 255), 2));
        formPanel.add(passwordField, gbc);

        centerPanel.add(formPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton loginButton = new JButton("Inloggen");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setBackground(new Color(211, 85, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false); // haalt focus ring weg
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonPanel.add(loginButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("admin") && password.equals("admin")) {
                    isLoggedInAsAdmin = true;
                    loginFrame.dispose();
                    new AdminDashboard();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Ongeldige gebruikersnaam of wachtwoord", "Fout", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginFrame.add(mainPanel);
        loginFrame.setVisible(true);
    }
}