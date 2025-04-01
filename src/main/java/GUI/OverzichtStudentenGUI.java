package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

import API_calls.API;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.Exam;
import models.Grade;
import models.Student;

public class OverzichtStudentenGUI {
    private JFrame frame;
    private JTable studentTable;
    private JTextField searchField;
    private static final String API_BASE_URL = "https://trajectplannerapi.dulamari.com/";
    private String[] columnNames = {"ID", "Voornaam", "Achternaam", "Studentnummer", "Geslacht", "Geboortedatum", "Bewerken", "Verwijderen"};

    public OverzichtStudentenGUI(List<Student> students) {
        createAndShowGUI(students);
    }

    private void createAndShowGUI(List<Student> students) {
        frame = new JFrame("Overzicht Studenten");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));


        JPanel searchPanel = createSearchPanel();
        frame.add(searchPanel, BorderLayout.NORTH);

        // Maak de tabel
        StudentTableModel model = new StudentTableModel(students, columnNames);
        studentTable = new JTable(model);
        configureTable();

        // ScrollPane en hoofdpanel
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(mainPanel, BorderLayout.CENTER);

        // Configureer knoppen
        configureButtons();

        frame.setVisible(true);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(30, 30, 30));

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(45, 45, 45));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JButton searchButton = new JButton("Zoeken");
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchButton.setBackground(new Color(0, 120, 215));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        searchButton.addActionListener(e -> searchStudent());

        panel.add(searchField, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.EAST);

        return panel;
    }

    private void configureTable() {
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentTable.setRowHeight(30);
        studentTable.setBackground(new Color(45, 45, 45));
        studentTable.setForeground(Color.WHITE);
        studentTable.setGridColor(new Color(80, 80, 80));
        studentTable.setSelectionBackground(new Color(0, 120, 215));
        studentTable.setSelectionForeground(Color.WHITE);

        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = studentTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    String studentNumber = (String) studentTable.getValueAt(row, 3);
                    showStudentScores(studentNumber);
                }
            }
        });

        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void configureButtons() {
        studentTable.getColumn("Bewerken").setCellRenderer(new ButtonRenderer());
        studentTable.getColumn("Bewerken").setCellEditor(new ButtonEditor(new JCheckBox(), studentTable, this, true));

        studentTable.getColumn("Verwijderen").setCellRenderer(new ButtonRenderer());
        studentTable.getColumn("Verwijderen").setCellEditor(new ButtonEditor(new JCheckBox(), studentTable, this, false));
    }

    private void updateStudent(int row) {
        StudentTableModel model = (StudentTableModel) studentTable.getModel();
        Student student = model.getStudentAt(row);

        JDialog editDialog = new JDialog(frame, "Student Bewerken", true);
        editDialog.setSize(800, 700);
        editDialog.setLayout(new BorderLayout());
        editDialog.getContentPane().setBackground(new Color(30, 30, 30));

        // Hoofdpanel met padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(30, 30, 30));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(45, 45, 45));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Stijl voor labels
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = new Color(200, 200, 200);

        // Stijl voor input velden
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color fieldBg = new Color(60, 60, 60);
        Color fieldFg = Color.WHITE;
        Border fieldBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );

        // Voornaam
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel firstNameLabel = new JLabel("Voornaam:");
        firstNameLabel.setFont(labelFont);
        firstNameLabel.setForeground(labelColor);
        formPanel.add(firstNameLabel, gbc);

        gbc.gridx = 1;
        JTextField firstNameField = new JTextField(student.getFirst_name());
        styleTextField(firstNameField, fieldFont, fieldBg, fieldFg, fieldBorder);
        formPanel.add(firstNameField, gbc);

        // Achternaam
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lastNameLabel = new JLabel("Achternaam:");
        lastNameLabel.setFont(labelFont);
        lastNameLabel.setForeground(labelColor);
        formPanel.add(lastNameLabel, gbc);

        gbc.gridx = 1;
        JTextField lastNameField = new JTextField(student.getLast_name());
        styleTextField(lastNameField, fieldFont, fieldBg, fieldFg, fieldBorder);
        formPanel.add(lastNameField, gbc);

        // Studentnummer
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel studentNumberLabel = new JLabel("Studentnummer:");
        studentNumberLabel.setFont(labelFont);
        studentNumberLabel.setForeground(labelColor);
        formPanel.add(studentNumberLabel, gbc);

        gbc.gridx = 1;
        JTextField studentNumberField = new JTextField(student.getStudent_number());
        styleTextField(studentNumberField, fieldFont, fieldBg, fieldFg, fieldBorder);
        formPanel.add(studentNumberField, gbc);

        // Wachtwoord
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Wachtwoord:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(labelColor);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(student.getPassword());
        styleTextField(passwordField, fieldFont, fieldBg, fieldFg, fieldBorder);
        formPanel.add(passwordField, gbc);

        // Totaal EC
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel totalEcLabel = new JLabel("Totaal EC:");
        totalEcLabel.setFont(labelFont);
        totalEcLabel.setForeground(labelColor);
        formPanel.add(totalEcLabel, gbc);

        gbc.gridx = 1;
        JTextField totalEcField = new JTextField(String.valueOf(student.getTotal_ec()));
        styleTextField(totalEcField, fieldFont, fieldBg, fieldFg, fieldBorder);
        formPanel.add(totalEcField, gbc);

        // Geslacht
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel genderLabel = new JLabel("Geslacht:");
        genderLabel.setFont(labelFont);
        genderLabel.setForeground(labelColor);
        formPanel.add(genderLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> genderField = new JComboBox<>(new String[]{"M", "F"});
        genderField.setSelectedItem(student.getGender());
        styleComboBox(genderField, fieldFont, fieldBg, fieldFg);
        formPanel.add(genderField, gbc);

        // Geboortedatum
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel birthdateLabel = new JLabel("Geboortedatum (YYYY-MM-DD):");
        birthdateLabel.setFont(labelFont);
        birthdateLabel.setForeground(labelColor);
        formPanel.add(birthdateLabel, gbc);

        gbc.gridx = 1;
        JTextField birthdateField = new JTextField(student.getBirthdate());
        styleTextField(birthdateField, fieldFont, fieldBg, fieldFg, fieldBorder);
        formPanel.add(birthdateField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Knop panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.setBackground(new Color(30, 30, 30));

        JButton saveButton = new JButton("Opslaan");
        styleButton(saveButton, new Color(0, 120, 215), Color.WHITE);
        saveButton.addActionListener(e -> {
            try {
                Student updatedStudent = new Student();
                updatedStudent.setId(student.getId());
                updatedStudent.setFirst_name(firstNameField.getText());
                updatedStudent.setLast_name(lastNameField.getText());
                updatedStudent.setStudent_number(studentNumberField.getText());
                updatedStudent.setPassword(new String(passwordField.getPassword()));
                updatedStudent.setTotal_ec(Integer.parseInt(totalEcField.getText()));
                updatedStudent.setGender((String) genderField.getSelectedItem());
                updatedStudent.setBirthdate(birthdateField.getText());

                new API().updateStudent(updatedStudent);

                frame.dispose();
                new OverzichtStudentenGUI(fetchStudents());
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

    // Hulp methodes voor styling
    private void styleTextField(JTextField field, Font font, Color bg, Color fg, Border border) {
        field.setFont(font);
        field.setBackground(bg);
        field.setForeground(fg);
        field.setBorder(border);
        field.setCaretColor(fg);
    }

    private void styleComboBox(JComboBox<String> comboBox, Font font, Color bg, Color fg) {
        comboBox.setFont(font);
        comboBox.setBackground(bg);
        comboBox.setForeground(fg);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(bg);
                setForeground(fg);
                return this;
            }
        });
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
    }

    private void deleteStudent(int row) {
        StudentTableModel model = (StudentTableModel) studentTable.getModel();
        Student student = model.getStudentAt(row);

        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Weet u zeker dat u student " + student.getFirst_name() + " " + student.getLast_name() + " wilt verwijderen?",
                "Bevestig verwijdering",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                new API().deleteStudent(student.getId());
                frame.dispose();
                new OverzichtStudentenGUI(fetchStudents());
                JOptionPane.showMessageDialog(frame, "Student succesvol verwijderd.", "Succes", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Fout bij verwijderen student: " + e.getMessage(),
                        "Fout", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchStudent() {
        String query = searchField.getText().trim().toLowerCase();

        if (query.isEmpty()) {
            try {
                refreshStudentTable(fetchStudents());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Fout bij ophalen studenten: " + e.getMessage(),
                        "Fout", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        try {
            List<Student> allStudents = fetchStudents();
            List<Student> filteredStudents = new ArrayList<>();

            for (Student student : allStudents) {
                if (student.getFirst_name().toLowerCase().contains(query) ||
                        student.getLast_name().toLowerCase().contains(query) ||
                        student.getStudent_number().toLowerCase().contains(query)) {
                    filteredStudents.add(student);
                }
            }

            if (filteredStudents.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Geen studenten gevonden",
                        "Niet Gevonden", JOptionPane.INFORMATION_MESSAGE);
            } else {
                refreshStudentTable(filteredStudents);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Fout bij het zoeken: " + e.getMessage(),
                    "Fout", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Table refreshen met behulp van zoekoptie
    private void refreshStudentTable(List<Student> students) {
        StudentTableModel model = new StudentTableModel(students, columnNames);
        studentTable.setModel(model);
        configureButtons();
    }


    private Map<String, Double> calculateAveragePerCourse(String studentNumber) {
        Map<String, Double> averagePerCourse = new HashMap<>();
        try {
            List<Grade> grades = fetchGradesForStudent(studentNumber);

            // Eerst verzamelen we alle cijfers per vak
            Map<String, List<Double>> scoresPerCourse = new HashMap<>();
            for (Grade grade : grades) {
                String courseName = grade.getCourse_name();
                double score = grade.getScore_value();

                scoresPerCourse.computeIfAbsent(courseName, k -> new ArrayList<>()).add(score);
            }

            // Dan berekenen we het gemiddelde per vak
            for (Map.Entry<String, List<Double>> entry : scoresPerCourse.entrySet()) {
                double sum = 0;
                for (Double score : entry.getValue()) {
                    sum += score;
                }
                double average = sum / entry.getValue().size();
                averagePerCourse.put(entry.getKey(), average);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Fout bij berekenen gemiddelden: " + e.getMessage(),
                    "Fout", JOptionPane.ERROR_MESSAGE);
        }
        return averagePerCourse;
    }

    private void showStudentScores(String studentNumber) {
        try {
            List<Grade> grades = fetchGradesForStudent(studentNumber);
            List<Exam> exams = fetchExams();

            // Bereken gemiddelden per vak
            Map<String, Double> averagePerCourse = calculateAveragePerCourse(studentNumber);

            JDialog scoresDialog = new JDialog(frame, "Cijfers voor " + studentNumber, true);
            scoresDialog.setSize(800, 600);
            scoresDialog.setLayout(new BorderLayout());
            scoresDialog.getContentPane().setBackground(new Color(30, 30, 30));

            // Maak een panel voor de gemiddelden
            JPanel averagesPanel = new JPanel(new GridLayout(averagePerCourse.size(), 1, 10, 10));
            averagesPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            averagesPanel.setBackground(new Color(30, 30, 30));

            // Voeg gemiddelden toe aan het panel
            for (Map.Entry<String, Double> entry : averagePerCourse.entrySet()) {
                JLabel averageLabel = new JLabel(entry.getKey() + ": " + String.format("%.2f", entry.getValue()));
                averageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                averageLabel.setForeground(Color.WHITE);
                averageLabel.setHorizontalAlignment(JLabel.LEFT);
                averagesPanel.add(averageLabel);
            }

            // Maak de cijfertabel
            String[] columnNames = {"Vak", "models.Cijfer", "models.Semester", "Type"};
            Object[][] data = new Object[grades.size()][4];

            for (int i = 0; i < grades.size(); i++) {
                Grade grade = grades.get(i);
                Exam exam = exams.stream()
                        .filter(e -> e.getId() == grade.getExam_id())
                        .findFirst()
                        .orElse(null);

                data[i][0] = grade.getCourse_name();
                data[i][1] = grade.getScore_value();
                data[i][2] = exam != null ? exam.getSemester() : "Onbekend";
                data[i][3] = exam != null ? exam.getType() : "Onbekend";
            }

            JTable scoresTable = new JTable(data, columnNames);
            // Stijl toepassen op de tabel
            scoresTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            scoresTable.setRowHeight(30);
            scoresTable.setBackground(new Color(45, 45, 45));
            scoresTable.setForeground(Color.WHITE);
            scoresTable.setGridColor(new Color(80, 80, 80));
            scoresTable.setSelectionBackground(new Color(0, 120, 215));
            scoresTable.setSelectionForeground(Color.WHITE);
            scoresTable.setBorder(BorderFactory.createEmptyBorder());

            // Header stijl
            JTableHeader header = scoresTable.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));
            header.setBackground(new Color(0, 120, 215));
            header.setForeground(Color.WHITE);
            header.setBorder(BorderFactory.createEmptyBorder());

            // Center de tekst in de cellen
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            centerRenderer.setBackground(new Color(45, 45, 45));
            centerRenderer.setForeground(Color.WHITE);
            for (int i = 0; i < scoresTable.getColumnCount(); i++) {
                scoresTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(scoresTable);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(new Color(45, 45, 45));

            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            tablePanel.setBackground(new Color(30, 30, 30));
            tablePanel.add(scrollPane, BorderLayout.CENTER);

            // Voeg alles toe aan het dialoogvenster
            scoresDialog.add(averagesPanel, BorderLayout.NORTH);
            scoresDialog.add(tablePanel, BorderLayout.CENTER);

            // Sluiten knop
            JButton closeButton = new JButton("Sluiten");
            closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            closeButton.setBackground(new Color(0, 120, 215));
            closeButton.setForeground(Color.WHITE);
            closeButton.addActionListener(e -> scoresDialog.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            buttonPanel.setBackground(new Color(30, 30, 30));
            buttonPanel.add(closeButton);

            scoresDialog.add(buttonPanel, BorderLayout.SOUTH);
            scoresDialog.setLocationRelativeTo(frame);
            scoresDialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Geen Cijfers", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    class StudentTableModel extends DefaultTableModel {
        private List<Student> students;

        public StudentTableModel(List<Student> students, String[] columnNames) {
            super(convertStudentsToData(students), columnNames);
            this.students = students;
        }

        private static Object[][] convertStudentsToData(List<Student> students) {
            Object[][] data = new Object[students.size()][8];
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                data[i][0] = student.getId();
                data[i][1] = student.getFirst_name();
                data[i][2] = student.getLast_name();
                data[i][3] = student.getStudent_number();
                data[i][4] = student.getGender();
                data[i][5] = student.getBirthdate();
                data[i][6] = "Bewerken";
                data[i][7] = "Verwijderen";
            }
            return data;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 6 || column == 7;
        }

        public Student getStudentAt(int row) {
            return students.get(row);
        }

        public void removeStudent(int row) {
            students.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;
        private OverzichtStudentenGUI gui;
        private boolean isEditButton;

        public ButtonEditor(JCheckBox checkBox, JTable table, OverzichtStudentenGUI gui, boolean isEditButton) {
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
                    gui.updateStudent(row);
                } else {
                    gui.deleteStudent(row);
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

    // API_calls.API methodes
    private static List<Student> fetchStudents() throws Exception {
        String apiUrl = API_BASE_URL + "students";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return new Gson().fromJson(response.toString(), new TypeToken<List<Student>>(){}.getType());
    }

    private List<Grade> fetchGradesForStudent(String studentNumber) throws Exception {
        String apiUrl = API_BASE_URL + "scores/" + studentNumber.replace("/", "-");
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return new Gson().fromJson(response.toString(), new TypeToken<List<Grade>>(){}.getType());
    }

    private List<Exam> fetchExams() throws Exception {
        String apiUrl = API_BASE_URL + "exams";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return new Gson().fromJson(response.toString(), new TypeToken<List<Exam>>(){}.getType());
    }

    public static void main(String[] args) {
        try {
            List<Student> students = fetchStudents();
            new OverzichtStudentenGUI(students);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fout bij het ophalen van studentgegevens.", "Fout", JOptionPane.ERROR_MESSAGE);
        }
    }
}