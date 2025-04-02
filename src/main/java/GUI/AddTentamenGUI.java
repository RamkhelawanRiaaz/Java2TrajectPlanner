package GUI;

import API_calls.API;
import models.Tentamen;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class AddTentamenGUI {
    //Instance fields die UI-componenten voorstellen
    private JFrame frame;
    private JTextField course_idField, codeField;
    private JComboBox<String>  exam_typeComboBox;
    private JDatePickerImpl exam_datePicker;
    private JButton saveButton, cancelButton, clearButton;

    //constructor van de class
    public AddTentamenGUI(){
        //frame aan maken
        frame = new JFrame("Tentamen aanmaken");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

        JLabel titleLabel = new JLabel("Tentamen aanmaken", SwingConstants.CENTER);
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

        //label Course_id
        JLabel Course_idLabel = new JLabel("Course id:");
        Course_idLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        Course_idLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        mainPanel.add(Course_idLabel, gbc);
        //InputField Course_id
        course_idField = new JTextField(20);
        course_idField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(course_idField, gbc);
//---------------------------------------------------------------------------------------
        //label Code
        JLabel CodeLabel = new JLabel("Code:");
        CodeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        CodeLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(CodeLabel, gbc);
        //InputField Code
        codeField = new JTextField(20);
        codeField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(codeField, gbc);
//---------------------------------------------------------------------------------------
        //label exam_type
        JLabel exam_typeLabel = new JLabel("Exam type:");
        exam_typeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        exam_typeLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(exam_typeLabel, gbc);
        //inputField exam_type
        String[] genders = {"Regulier", "Her"};
        exam_typeComboBox = new JComboBox<>(genders);
        exam_typeComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(exam_typeComboBox, gbc);
//---------------------------------------------------------------------------------------
        //label exam_date
        JLabel exam_dateLabel = new JLabel("Exam date:");
        exam_dateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        exam_dateLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(exam_dateLabel, gbc);

        // Datumkiezer (JDatePicker)
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Vandaag");
        properties.put("text.month", "Maand");
        properties.put("text.year", "Jaar");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        exam_datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        exam_datePicker.getJFormattedTextField().setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(exam_datePicker, gbc);
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
        saveButton.addActionListener(e -> saveTentamen());
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

    //method om alle buttons in een keer te stylen
    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(100, 30));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.BLACK, 0));

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

    //method die de functie van de opslaan button uitvoert
    private void saveTentamen(){
        //variabelen om invoer data te onvagen
        String courseIdText = course_idField.getText().trim();
        String code = codeField.getText().trim();
        String exam_type = exam_typeComboBox.getSelectedItem().toString();

        int course_id;
        // error handeler, kijken als course_id een valid input is
        try {
            course_id = Integer.parseInt(courseIdText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Course ID moet een geldig getal zijn.", "Fout", JOptionPane.ERROR_MESSAGE);
            return;
        }
        java.util.Date dob = (java.util.Date) exam_datePicker.getModel().getValue();

        SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd");
        String exam_date = apiFormat.format(dob);

        //object van Tentamen maken
        Tentamen tentamen = new Tentamen();
        //data sturen/zetten in object
        tentamen.setCourse_id(course_id);
        tentamen.setCode(code);
        tentamen.setExam_type(exam_type);
        tentamen.setExam_date(exam_date);

        //object van API class maken
        API api_request = new API();

        //Object Tentamen met data sturen naar postTentamen method in api object
        api_request.postTentamen(tentamen);

        //message als de code goed runt met api call
        JOptionPane.showMessageDialog(frame, "Tentamen succesfol aangemaakt", "Succes", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();
    }

    //method voor de de clear functie van de input velden
    public void clearFields(){
        course_idField.setText("");
        codeField.setText("");
        exam_typeComboBox.setSelectedIndex(0);
        exam_datePicker.getModel().setValue(null);
    }


    //main method om class aan te maken
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddTentamenGUI::new);
    }
}
