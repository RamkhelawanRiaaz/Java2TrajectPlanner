import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCijfersGUI {
    private JFrame frame;
    private JTextField studentField, vakField, semesterField, cohortField, cijferField;
    private JButton saveButton, cancelButton;

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

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        saveButton = new JButton("Opslaan");
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));
        saveButton.setBackground(new Color(211, 85, 0)); // Oranje achtergrond
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCijfer();
            }
        });
        buttonPanel.add(saveButton);

        cancelButton = new JButton("Annuleren");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 20));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(cancelButton);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddCijfersGUI::new);
    }
}