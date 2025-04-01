package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.border.Border;
import java.io.*;
import java.net.*;
import com.google.gson.*;
import API_calls.API;

import com.google.gson.reflect.TypeToken;
import models.Grade;


public class OverzichtCijfersGUI {
    private JFrame frame;
    private JTable gradeTable;
    private JButton visualizeButton;
    private static final String API_BASE_URL = "https://trajectplannerapi.dulamari.com/";


    public OverzichtCijfersGUI(List<Grade> grades) {
        System.out.println("GUI.OverzichtCijfersGUI wordt geopend met " + grades.size() + " cijfers.");

        // Maak het hoofdvenster
        frame = new JFrame("Overzicht Cijfers");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        // Kolomnamen voor de tabel
        String[] columnNames = {"Score ID", "models.Student ID", "Studentennummer", "models.Exam ID", "models.Cijfer", "Datum", "Bewerken", "Verwijderen"};

        // Data voor de tabel
        GradeTableModel model = new GradeTableModel(grades, columnNames);

        gradeTable = new JTable(model);
        gradeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gradeTable.setRowHeight(30);
        gradeTable.setBackground(new Color(45, 45, 45));
        gradeTable.setForeground(Color.WHITE);
        gradeTable.setGridColor(new Color(80, 80, 80));
        gradeTable.setSelectionBackground(new Color(0, 120, 215));
        gradeTable.setSelectionForeground(Color.WHITE);

        JTableHeader header = gradeTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < gradeTable.getColumnCount(); i++) {
            gradeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(gradeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));

        visualizeButton = new JButton("Visualiseer cijfers");
        visualizeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        visualizeButton.setBackground(new Color(0, 120, 215));
        visualizeButton.setForeground(Color.WHITE);
        visualizeButton.setFocusPainted(false);

        visualizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = gradeTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Haal student ID en naam op uit de geselecteerde rij
                    int studentId = (int) gradeTable.getValueAt(selectedRow, 1);  // Zorg ervoor dat het een String is
                    String studentName = (String) gradeTable.getValueAt(selectedRow, 2);
                    openCijferVisualisatie(studentId, studentName);
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecteer een student om de cijfers te visualiseren.", "Geen selectie", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(visualizeButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(30, 30, 30));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel, BorderLayout.CENTER);
        configureButtons();
        frame.setVisible(true);
    }

    private void openCijferVisualisatie(int studentId, String studentName) {
        new CijferVisualisatieGUI(studentId, studentName);
    }

    public static void main(String[] args) {
        try {

            API api_request = new API();
            List<Grade> grades = api_request.getScoresWithStudentNames();

            SwingUtilities.invokeLater(() -> new OverzichtCijfersGUI(grades));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Er is een fout opgetreden bij het ophalen van de gegevens.", "Fout", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configureButtons() {
        gradeTable.getColumn("Bewerken").setCellRenderer(new OverzichtCijfersGUI.ButtonRenderer());
        gradeTable.getColumn("Bewerken").setCellEditor(new ButtonEditor(new JCheckBox(), gradeTable, this, true));

        gradeTable.getColumn("Verwijderen").setCellRenderer(new OverzichtCijfersGUI.ButtonRenderer());
        gradeTable.getColumn("Verwijderen").setCellEditor(new ButtonEditor(new JCheckBox(), gradeTable, this, false));
    }

    private void updateGrade(int row) {
        GradeTableModel model = (GradeTableModel) gradeTable.getModel();
        Grade grade = model.getGradeAt(row);

        JDialog editDialog = new JDialog(frame, "models.Cijfer Bewerken", true);
        editDialog.setSize(800, 500);
        editDialog.setLayout(new BorderLayout());
        editDialog.getContentPane().setBackground(new Color(30, 30, 30));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(30, 30, 30));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(45, 45, 45));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = new Color(200, 200, 200);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color fieldBg = new Color(60, 60, 60);
        Color fieldFg = Color.WHITE;
        Border fieldBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );

        // models.Student ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel studentIdLabel = new JLabel("models.Student ID:");
        studentIdLabel.setFont(labelFont);
        studentIdLabel.setForeground(labelColor);
        formPanel.add(studentIdLabel, gbc);

        gbc.gridx = 1;
        JTextField studentIdField = new JTextField(String.valueOf(grade.getStudent_id()));
        styleTextField(studentIdField, fieldFont, fieldBg, fieldFg, fieldBorder);
        formPanel.add(studentIdField, gbc);

        // models.Student Number
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel studentNumberLabel = new JLabel("Studentnummer:");
        studentNumberLabel.setFont(labelFont);
        studentNumberLabel.setForeground(labelColor);
        formPanel.add(studentNumberLabel, gbc);

        gbc.gridx = 1;
        JTextField studentNumberField = new JTextField(grade.getStudent_number());
        styleTextField(studentNumberField, fieldFont, fieldBg, fieldFg, fieldBorder);
        formPanel.add(studentNumberField, gbc);

        // models.Exam ID
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel examIdLabel = new JLabel("models.Exam ID:");
        examIdLabel.setFont(labelFont);
        examIdLabel.setForeground(labelColor);
        formPanel.add(examIdLabel, gbc);

        gbc.gridx = 1;
        JTextField examIdField = new JTextField(String.valueOf(grade.getExam_id()));
        styleTextField(examIdField, fieldFont, fieldBg, fieldFg, fieldBorder);
        formPanel.add(examIdField, gbc);

        // Score ID
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel scoreIdLabel = new JLabel("Score ID:");
        scoreIdLabel.setFont(labelFont);
        scoreIdLabel.setForeground(labelColor);
        formPanel.add(scoreIdLabel, gbc);

        gbc.gridx = 1;
        JTextField scoreIdField = new JTextField(String.valueOf(grade.getId()));
        styleTextField(scoreIdField, fieldFont, fieldBg, fieldFg, fieldBorder);
        formPanel.add(scoreIdField, gbc);

        // Score Value
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel scoreValueLabel = new JLabel("Score:");
        scoreValueLabel.setFont(labelFont);
        scoreValueLabel.setForeground(labelColor);
        formPanel.add(scoreValueLabel, gbc);

        gbc.gridx = 1;
        JTextField scoreValueField = new JTextField(String.valueOf(grade.getScore_value()));
        styleTextField(scoreValueField, fieldFont, fieldBg, fieldFg, fieldBorder);
        formPanel.add(scoreValueField, gbc);

        // Score DateTime
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel scoreDateTimeLabel = new JLabel("Score Datum (YYYY-MM-DD HH:MM:SS):");
        scoreDateTimeLabel.setFont(labelFont);
        scoreDateTimeLabel.setForeground(labelColor);
        formPanel.add(scoreDateTimeLabel, gbc);

        gbc.gridx = 1;
        JTextField scoreDateTimeField = new JTextField(grade.getScore_datetime());
        styleTextField(scoreDateTimeField, fieldFont, fieldBg, fieldFg, fieldBorder);
        formPanel.add(scoreDateTimeField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.setBackground(new Color(30, 30, 30));

        JButton saveButton = new JButton("Opslaan");
        styleButton(saveButton, new Color(0, 120, 215), Color.WHITE);
        saveButton.addActionListener(e -> {
            try {
                Grade updatedGrade = new Grade();
                updatedGrade.setId(Integer.parseInt(scoreIdField.getText()));
                updatedGrade.setStudent_id(Integer.parseInt(studentIdField.getText()));
                updatedGrade.setStudent_number(studentNumberField.getText());
                updatedGrade.setExam_id(Integer.parseInt(examIdField.getText()));
                updatedGrade.setScore_value(Double.parseDouble(scoreValueField.getText()));
                updatedGrade.setScore_datetime(scoreDateTimeField.getText());

                new API().updateGrade(updatedGrade);
                frame.dispose();
                API request = new API();
                new OverzichtCijfersGUI(request.getGrades());
                editDialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editDialog, "Fout bij bijwerken: " + ex.getMessage(),
                        "Fout", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Annuleren");
        styleButton(cancelButton, new Color(80, 80, 80), Color.WHITE);
        cancelButton.addActionListener(e -> editDialog.dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        editDialog.add(mainPanel, BorderLayout.CENTER);
        editDialog.setLocationRelativeTo(frame);
        editDialog.setVisible(true);
    }

    private void styleTextField(JTextField field, Font font, Color bg, Color fg, Border border) {
        field.setFont(font);
        field.setBackground(bg);
        field.setForeground(fg);
        field.setBorder(border);
        field.setCaretColor(fg);
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
    }

    public void deleteGrade(int row) {
        GradeTableModel model = (GradeTableModel) gradeTable.getModel();
        Grade cijfer = model.getGradeAt(row);

        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Weet u zeker dat u het cijfer " + cijfer.getScore_value() + " voor student " +
                        (cijfer.getStudent_number() != null ? cijfer.getStudent_number() : "ID: " + cijfer.getStudent_id()) +
                        " wilt verwijderen?",
                "Bevestig verwijdering",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                new API().deleteGrade(cijfer.getId());
                frame.dispose();
                API request = new API();
                new OverzichtCijfersGUI(request.getGrades());
                JOptionPane.showMessageDialog(frame, "models.Cijfer succesvol verwijderd.", "Succes", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Fout bij verwijderen cijfer: " + e.getMessage(),
                        "Fout", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class GradeTableModel extends DefaultTableModel {
        private List<Grade> cijfers;

        public GradeTableModel(List<Grade> cijfers, String[] columnNames) {
            super(convertGradesToData(cijfers), columnNames);
            this.cijfers = cijfers;
        }

        private static Object[][] convertGradesToData(List<Grade> cijfers) {
            Object[][] data = new Object[cijfers.size()][8]; // Adjust column count based on your table

            for (int i = 0; i < cijfers.size(); i++) {
                Grade grade = cijfers.get(i);
                data[i][0] = grade.getId();
                data[i][1] = grade.getStudent_id();
                data[i][2] = grade.getStudent_number();
                data[i][3] = grade.getExam_id();
                data[i][4] = grade.getScore_value();
                data[i][5] = grade.getScore_datetime();
                data[i][6] = "Bewerken";
                data[i][7] = "Verwijderen";
            }
            return data;
        }
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 6 || column == 7;
        }

        public Grade getGradeAt(int row) {
            return cijfers.get(row);
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;
        private OverzichtCijfersGUI gui;
        private boolean isEditButton;

        public ButtonEditor(JCheckBox checkBox, JTable table, OverzichtCijfersGUI gui, boolean isEditButton) {
            super(checkBox);
            this.table = table;
            this.gui = gui;
            this.isEditButton = isEditButton;

            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                if (isEditButton) {
                    gui.updateGrade(row);
                } else {
                    gui.deleteGrade(row);
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
}