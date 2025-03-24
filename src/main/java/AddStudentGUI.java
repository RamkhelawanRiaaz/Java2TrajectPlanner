import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class AddStudentGUI {
    private JFrame frame;
    private JTextField nameField, studentNumberField, emailField, addressField, phoneField, cohortField;
    private JComboBox<String> genderComboBox;
    private JDatePickerImpl datePicker;
    private JButton saveButton, cancelButton, clearButton;

    public AddStudentGUI() {
        frame = new JFrame("Student Toevoegen");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Donkergrijze achtergrond

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

        JLabel titleLabel = new JLabel("Student Toevoegen", SwingConstants.CENTER);
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

        JLabel nameLabel = new JLabel("Naam:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        nameLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        mainPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);

        JLabel studentNumberLabel = new JLabel("Studentnummer:");
        studentNumberLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        studentNumberLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(studentNumberLabel, gbc);

        studentNumberField = new JTextField(20);
        studentNumberField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(studentNumberField, gbc);

        JLabel emailLabel = new JLabel("E-mail:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        emailLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        JLabel addressLabel = new JLabel("Adres:");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        addressLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(addressLabel, gbc);

        addressField = new JTextField(20);
        addressField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(addressField, gbc);

        JLabel phoneLabel = new JLabel("Telefoonnummer:");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        phoneLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(phoneLabel, gbc);

        phoneField = new JTextField(20);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(phoneField, gbc);

        JLabel dobLabel = new JLabel("Geboortedatum:");
        dobLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        dobLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(dobLabel, gbc);

        // Datumkiezer (JDatePicker)
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Vandaag");
        properties.put("text.month", "Maand");
        properties.put("text.year", "Jaar");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.getJFormattedTextField().setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(datePicker, gbc);

        JLabel genderLabel = new JLabel("Geslacht:");
        genderLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        genderLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(genderLabel, gbc);

        String[] genders = {"Man", "Vrouw", "Anders"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(genderComboBox, gbc);

        JLabel cohortLabel = new JLabel("Cohort:");
        cohortLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        cohortLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(cohortLabel, gbc);

        cohortField = new JTextField(20);
        cohortField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(cohortField, gbc);

        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 20));
        styleButton(clearButton, Color.GRAY);  // Gray
        clearButton.addActionListener(e -> clearFields());
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        clearButton.setPreferredSize(new Dimension(120, 25));
        mainPanel.add(clearButton, gbc);


        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        saveButton = new JButton("Opslaan");
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));
        styleButton(saveButton, new Color(211, 85, 0));  // Orange
        saveButton.addActionListener(e -> saveStudent());
        buttonPanel.add(saveButton);

        cancelButton = new JButton("Annuleren");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 20));
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

    private void saveStudent() {
        String name = nameField.getText();
        String studentNumber = studentNumberField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        java.util.Date dob = (java.util.Date) datePicker.getModel().getValue();
        String gender = (String) genderComboBox.getSelectedItem();
        String cohort = cohortField.getText();

        if (!name.isEmpty() && !studentNumber.isEmpty() && !email.isEmpty() && !address.isEmpty() && !phone.isEmpty() && dob != null && !cohort.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDob = sdf.format(dob);
            JOptionPane.showMessageDialog(frame, "Student succesvol toegevoegd!\nGeboortedatum: " + formattedDob, "Succes", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Vul alle velden in!", "Fout", JOptionPane.ERROR_MESSAGE);
        }
    }
         private void clearFields() {
            nameField.setText("");
            studentNumberField.setText("");
            emailField.setText("");
            addressField.setText("");
            phoneField.setText("");
            cohortField.setText("");
            genderComboBox.setSelectedIndex(0);
            datePicker.getModel().setValue(null);
         }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddStudentGUI::new);
    }
}

// Helper class voor datumformattering
class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
    private final String datePattern = "dd-MM-yyyy";
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String text) throws java.text.ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws java.text.ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
        return "";
    }
}