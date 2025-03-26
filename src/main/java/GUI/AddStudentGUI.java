package GUI;//packages voor het importeren van models
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import API_calls.API;
import models.Student;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class AddStudentGUI {
    private JFrame frame;
    private JTextField nameField, studentNumberField, passwordField, totalECField, cohortField, majorField;
    private JComboBox<String> genderComboBox;
    private JDatePickerImpl datePicker;
    private JButton saveButton, cancelButton, clearButton;

    public AddStudentGUI() {
        frame = new JFrame("models.Student Toevoegen");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

        JLabel titleLabel = new JLabel("models.Student Toevoegen", SwingConstants.CENTER);
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

        //label Naam
        JLabel nameLabel = new JLabel("Volledige Naam:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        nameLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        mainPanel.add(nameLabel, gbc);
        //InputField Naam
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);
//---------------------------------------------------------------------------------------
        //label StudentNummer
        JLabel studentNumberLabel = new JLabel("Studentnummer:");
        studentNumberLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        studentNumberLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(studentNumberLabel, gbc);
        //inputField StudentNummer
        studentNumberField = new JTextField(20);
        studentNumberField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(studentNumberField, gbc);
//---------------------------------------------------------------------------------------
        //label wachtwoord
        JLabel passwordLabel = new JLabel("Wachtwoord:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);
        //inputField Wachtwoord
        passwordField = new JTextField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);
//---------------------------------------------------------------------------------------
        //label totalEC
        JLabel totalECLabel = new JLabel("Totaal EC:");
        totalECLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        totalECLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(totalECLabel, gbc);
        //inputField totalEC
        totalECField = new JTextField(20);
        totalECField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(totalECField, gbc);
//---------------------------------------------------------------------------------------
        //label Studierichting
        JLabel majorLabel = new JLabel("Studierichting:");
        majorLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        majorLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(majorLabel, gbc);
        //inputField Studierichting
        majorField = new JTextField(20);
        majorField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(majorField, gbc);
//---------------------------------------------------------------------------------------
        //label Cohort
        JLabel cohortLabel = new JLabel("Cohort (1101 - 1199):");
        cohortLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        cohortLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 8;
        mainPanel.add(cohortLabel, gbc);
        //inputField Cohort
        cohortField = new JTextField(20);
        cohortField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(cohortField, gbc);
//---------------------------------------------------------------------------------------
        //label Geboortedatum
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
//---------------------------------------------------------------------------------------
        //label geslacht
        JLabel genderLabel = new JLabel("Geslacht:");
        genderLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        genderLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(genderLabel, gbc);
        //inputField Geslacht
        String[] genders = {"M", "F"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(genderComboBox, gbc);
//---------------------------------------------------------------------------------------
        //Clear knop
        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 20));
        styleButton(clearButton, Color.GRAY);
        clearButton.addActionListener(e -> clearFields());
        gbc.gridx = 0;
        gbc.gridy = 6;
        clearButton.setPreferredSize(new Dimension(120, 25));
        mainPanel.add(clearButton, gbc);
//---------------------------------------------------------------------------------------
        //knoppen
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
        String fullName = nameField.getText().trim();
        String studentNumber = studentNumberField.getText().trim();
        String password = passwordField.getText().trim();
        String totalECText = totalECField.getText().trim();
        String gender = genderComboBox.getSelectedItem().toString();

        String cohortText = cohortField.getText().trim();
        if (cohortText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Vul een geldig cohort in!", "Fout", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cohort;
        try {
            cohort = Integer.parseInt(cohortText);
            if (cohort < 1101 || cohort > 1199) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Cohort moet een getal zijn tussen 1101 en 1199.", "Fout", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String major = majorField.getText().trim();
        if (major.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Vul ook de studierichting in!", "Fout", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.util.Date dob = (java.util.Date) datePicker.getModel().getValue();
        if (fullName.isEmpty() || studentNumber.isEmpty() || password.isEmpty() || totalECText.isEmpty() || dob == null) {
            JOptionPane.showMessageDialog(frame, "Vul alle velden in!", "Fout", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] nameParts = fullName.split(" ", 2);
        String firstName = nameParts[0];
        String lastName = (nameParts.length > 1) ? nameParts[1] : "";

        int totalEC;
        try {
            totalEC = Integer.parseInt(totalECText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Totaal EC moet een getal zijn.", "Fout", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthdate = apiFormat.format(dob);

        Student student = new Student();
        student.setFirst_name(firstName);
        student.setLast_name(lastName);
        student.setStudent_number(studentNumber);
        student.setPassword(password);
        student.setTotal_ec(totalEC);
        student.setGender(gender.substring(0, 1));
        student.setBirthdate(birthdate);
        student.setMajor(major);
        student.setCohort(cohort);

        API api_request = new API();
        api_request.postStudent(student);

        JOptionPane.showMessageDialog(frame, "models.Student succesvol toegevoegd!", "Succes", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();
    }

    private void clearFields() {
        nameField.setText("");
        studentNumberField.setText("");
        passwordField.setText("");
        totalECField.setText("");
        majorField.setText("");
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