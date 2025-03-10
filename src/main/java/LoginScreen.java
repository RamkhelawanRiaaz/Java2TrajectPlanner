import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen {
    private JFrame loginFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public static boolean isLoggedInAsAdmin = false;  // Maak een globale inlogstatus

    public LoginScreen() {
        // Initialize and show the login screen
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        loginFrame = new JFrame("University of Applied Sciences and Technology (Suriname)");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Zet het venster in fullscreen
        loginFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        loginFrame.setUndecorated(false); // Behoud de titelbalk voor minimize en close buttons
        loginFrame.setVisible(true);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                // Gradient from light blue (#3fa9ff) to white
                GradientPaint gp = new GradientPaint(0, 0, new Color(63, 169, 255), 0, height, Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JPanel topPanel = new JPanel(new BorderLayout()); // Gebruik BorderLayout voor betere controle
        topPanel.setOpaque(false);

        // Logo aanpassen
        ImageIcon logoIcon = new ImageIcon("C:/Users/USER-ADEK004759/Downloads/rsz_unasat_2021_full_name-2 (2).png"); // Update het pad naar je logo
//        Image img = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Maak het logo groter (150x150)
//        logoIcon = new ImageIcon(img);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT); // Logo links uitlijnen
        topPanel.add(logoLabel, BorderLayout.WEST); // Plaats het logo in het westen (links)
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel for title and input fields
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Titel label in oranje en 2x grotere tekst
        JLabel titleLabel = new JLabel("Studenten Administratie");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, (int) (35 * 2))); // 2x grotere tekst
        titleLabel.setForeground(new Color(211, 85, 0)); // Oranje kleur (#d35500)
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing

        // Form panel using GridBagLayout for a modern look
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Gebruikersnaam:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        // Custom border using the light blue (#3fa9ff)
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(63, 169, 255), 2));
        formPanel.add(usernameField, gbc);

        // Password label and field
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

        // Bottom panel for the login button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton loginButton = new JButton("Inloggen");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setBackground(new Color(211, 85, 0)); // Oranje (#d35500)
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonPanel.add(loginButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Simple login validation (example: username and password both "admin")
                if (username.equals("admin") && password.equals("admin")) {
                    isLoggedInAsAdmin = true;  // Set login status
                    loginFrame.dispose();
                    new AdminDashboard(); // Launch the main application
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Ongeldige gebruikersnaam of wachtwoord", "Fout", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginFrame.add(mainPanel);
        loginFrame.setVisible(true);
    }
}