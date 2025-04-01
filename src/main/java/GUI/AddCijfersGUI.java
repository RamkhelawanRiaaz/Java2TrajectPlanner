package GUI;

import API_calls.API;
import models.Grade;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddCijfersGUI {
    private JFrame frame;
    private JTextField student_idField, student_numberField, exam_idField, score_valueField, score_datetimeField;
    private JButton saveButton, cancelButton, clearButton;

    public AddCijfersGUI() {
        frame = new JFrame("Cijfer Toevoegen");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));

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

        //label student_id
        JLabel student_idLabel = new JLabel("Student ID:");
        student_idLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        student_idLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        mainPanel.add(student_idLabel, gbc);
        //inputField student_id
        student_idField = new JTextField(20);
        student_idField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(student_idField, gbc);
//---------------------------------------------------------------------------------------
        //label student_number
        JLabel student_numberLabel = new JLabel("Student Number:");
        student_numberLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        student_numberLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(student_numberLabel, gbc);
        //inputField student_number
        student_numberField = new JTextField(20);
        student_numberField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(student_numberField, gbc);
//---------------------------------------------------------------------------------------
        //label exam_id
        JLabel exam_idLabel = new JLabel("Exam ID:");
        exam_idLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        exam_idLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(exam_idLabel, gbc);
        //inputField exam_id
        exam_idField = new JTextField(20);
        exam_idField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(exam_idField, gbc);
//---------------------------------------------------------------------------------------
        //label score_value
        JLabel score_valueLabel = new JLabel("Score Value:");
        score_valueLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        score_valueLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(score_valueLabel, gbc);
        //inputField score_value
        score_valueField = new JTextField(20);
        score_valueField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        mainPanel.add(score_valueField, gbc);
//---------------------------------------------------------------------------------------
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String score_datetime = now.format(formatter);


        clearButton = new JButton("Clear");
        styleButton(clearButton, Color.GRAY);  // Gray
        clearButton.addActionListener(e -> clearFields());
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        mainPanel.add(clearButton, gbc);
//---------------------------------------------------------------------------------------
        //knoppen
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
        String studentidText = student_idField.getText().trim();
        String student_number = student_numberField.getText().trim();
        String examIdText = exam_idField.getText().trim();
        String scoreValueText = score_valueField.getText().trim();

        int student_id = 0;
        int exam_id = 0;
        double score_value = 0.0;

        try {
            if (!studentidText.isEmpty()) {
                student_id = Integer.parseInt(studentidText);
            }
            exam_id = Integer.parseInt(examIdText);
            score_value = Double.parseDouble(scoreValueText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Voer geldige numerieke waarden in.", "Fout", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((studentidText.isEmpty() && student_number.isEmpty()) || examIdText.isEmpty() || scoreValueText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Vul alle verplichte velden in!", "Fout", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String score_datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Grade cijfer = new Grade();

        if (!studentidText.isEmpty()) cijfer.setStudent_id(student_id);
        if (!student_number.isEmpty()) cijfer.setStudent_number(student_number);
        cijfer.setExam_id(exam_id);
        cijfer.setScore_value(score_value);
        cijfer.setScore_datetime(score_datetime);

        // API call
        API api_request = new API();
        api_request.postCijfer(cijfer);

        JOptionPane.showMessageDialog(frame, "Cijfer succesvol toegevoegd!", "Succes", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();
    }

    private void clearFields() {
        student_idField.setText("");
        student_numberField.setText("");
        exam_idField.setText("");
        score_valueField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddCijfersGUI::new);
    }
}