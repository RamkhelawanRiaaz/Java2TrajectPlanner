import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCijfersGUI {
    private JFrame frame;
    private JTextField studentField, vakField, semesterField, cohortField, cijferField;
    private JButton saveButton, cancelButton, clearButton, fictieveDataButton;

    public AddCijfersGUI() {
        frame = new JFrame("Cijfer Toevoegen");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Donkergrijze achtergrond

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

        JLabel titleLabel = new JLabel("Cijfer Toevoegen", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(new Color(211, 85, 0)); // Oranje tekst
        titlePanel.add(titleLabel);

        frame.add(titlePanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel studentLabel = new JLabel("Student Nummer:");
        studentLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        studentLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        mainPanel.add(studentLabel, gbc);

        studentField = new JTextField(20);
        studentField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(studentField, gbc);

        JLabel vakLabel = new JLabel("Vak Naam:");
        vakLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        vakLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(vakLabel, gbc);

        vakField = new JTextField(20);
        vakField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(vakField, gbc);

        JLabel semesterLabel = new JLabel("Semester/Kwartaal:");
        semesterLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        semesterLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(semesterLabel, gbc);

        semesterField = new JTextField(20);
        semesterField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(semesterField, gbc);

        JLabel cohortLabel = new JLabel("Cohort:");
        cohortLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        cohortLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(cohortLabel, gbc);

        cohortField = new JTextField(20);
        cohortField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(cohortField, gbc);

        JLabel cijferLabel = new JLabel("Cijfer:");
        cijferLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        cijferLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(cijferLabel, gbc);

        cijferField = new JTextField(20);
        cijferField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(cijferField, gbc);

        clearButton = new JButton("Clear");
        styleButton(clearButton, Color.GRAY);  // Gray
        clearButton.addActionListener(e -> clearFields());
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        mainPanel.add(clearButton, gbc);

        fictieveDataButton = new JButton("Simuleer");
        styleButton(fictieveDataButton, new Color(0, 0, 139));
        fictieveDataButton.addActionListener(e -> fillWithFictieveData());
        gbc.gridy = 9;
        mainPanel.add(fictieveDataButton, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        saveButton = new JButton("Opslaan");
        styleButton(saveButton, new Color(211, 85, 0));  // Orange
        saveButton.addActionListener(e -> saveCijfer());
        buttonPanel.add(saveButton);

        cancelButton = new JButton("Annuleren");
        cancelButton = new JButton("Annuleren");
        styleButton(cancelButton, Color.RED);  // Red
        cancelButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(cancelButton);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD,20));
        button.setPreferredSize(new Dimension(100, 30));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.BLACK,0));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }

    private void saveCijfer() {
        String student = studentField.getText();
        String vak = vakField.getText();
        String semester = semesterField.getText();
        String cohort = cohortField.getText();
        String cijfer = cijferField.getText();

        if (!student.isEmpty() && !vak.isEmpty() && !semester.isEmpty() && !cohort.isEmpty() && !cijfer.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Cijfer succesvol toegevoegd!", "Succes", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Vul alle velden in!", "Fout", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        studentField.setText("");
        vakField.setText("");
        semesterField.setText("");
        cohortField.setText("");
        cijferField.setText("");
    }

    private void fillWithFictieveData() {
        studentField.setText("SE/1123/000");
        vakField.setText("Database");
        semesterField.setText("Semester 2");
        cohortField.setText("2023");
        cijferField.setText("8.5");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddCijfersGUI::new);
    }
}