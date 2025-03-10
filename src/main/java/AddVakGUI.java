import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddVakGUI {
    private JFrame frame;
    private JTextField vakNaamField, semesterField, cohortField;
    private JButton saveButton, cancelButton;

    public AddVakGUI() {
        frame = new JFrame("Vak Toevoegen");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Donkergrijze achtergrond

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

        JLabel titleLabel = new JLabel("Vak Toevoegen", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(new Color(211, 85, 0));
        titlePanel.add(titleLabel);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel vakNaamLabel = new JLabel("Vak Naam:");
        vakNaamLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        vakNaamLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        mainPanel.add(vakNaamLabel, gbc);

        vakNaamField = new JTextField(20);
        vakNaamField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(vakNaamField, gbc);

        JLabel semesterLabel = new JLabel("Semester/Kwartaal:");
        semesterLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        semesterLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(semesterLabel, gbc);

        semesterField = new JTextField(20);
        semesterField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(semesterField, gbc);

        JLabel cohortLabel = new JLabel("Cohort:");
        cohortLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        cohortLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(cohortLabel, gbc);

        cohortField = new JTextField(20);
        cohortField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(cohortField, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        saveButton = new JButton("Opslaan");
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));
        saveButton.setBackground(new Color(211, 85, 0));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveVak();
            }
        });
        buttonPanel.add(saveButton);

        cancelButton = new JButton("Annuleren");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 20));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(cancelButton);

        // Voeg de panels toe aan het frame
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void saveVak() {
        String vakNaam = vakNaamField.getText();
        String semester = semesterField.getText();
        String cohort = cohortField.getText();

        if (!vakNaam.isEmpty() && !semester.isEmpty() && !cohort.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Vak succesvol toegevoegd!", "Succes", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Vul alle velden in!", "Fout", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddVakGUI::new);
    }
}