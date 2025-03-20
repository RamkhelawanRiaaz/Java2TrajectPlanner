import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OverzichtStudentenGUI {
    private JFrame frame;
    private JTable studentTable;
    private JTextField searchField;
    private static final String API_BASE_URL = "https://trajectplannerapi.dulamari.com/";

    public OverzichtStudentenGUI(List<Student> students) {
        System.out.println("OverzichtStudentenGUI wordt geopend met " + students.size() + " studenten.");

        // Maak het hoofdvenster
        frame = new JFrame("Overzicht Studenten");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);  // Iets groter voor een betere weergave
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));  // Donkere achtergrond

        // Zoekbalk
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setBackground(new Color(30, 30, 30));
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
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        frame.add(searchPanel, BorderLayout.NORTH);

        // Kolomnamen voor de tabel
        String[] columnNames = {"ID", "Voornaam", "Achternaam", "Studentnummer", "Geslacht", "Geboortedatum"};

        // Data voor de tabel
        Object[][] data = new Object[students.size()][7];
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            data[i][0] = student.getId();
            data[i][1] = student.getFirstname();
            data[i][2] = student.getLastname();
            data[i][3] = student.getStudentnumber();
            data[i][4] = student.getGender();
            data[i][5] = student.getBirthdate();

        }

        // Maak de tabel
        studentTable = new JTable(data, columnNames);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));  // Modern lettertype
        studentTable.setRowHeight(30);  // Iets grotere rijhoogte
        studentTable.setBackground(new Color(45, 45, 45));  // Donkere tabelachtergrond
        studentTable.setForeground(Color.WHITE);  // Witte tekst
        studentTable.setGridColor(new Color(80, 80, 80));  // Donkere rasterlijnen
        studentTable.setSelectionBackground(new Color(0, 120, 215));  // Blauwe selectiekleur
        studentTable.setSelectionForeground(Color.WHITE);  // Witte tekst bij selectie

        // Voeg een MouseListener toe aan de tabel
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

        // Pas de header van de tabel aan
        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));  // Vet lettertype voor de header
        header.setBackground(new Color(0, 120, 215));  // Blauwe headerachtergrond
        header.setForeground(Color.WHITE);  // Witte headertekst

        // Centreer de tekst in de cellen
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Voeg de tabel toe aan een JScrollPane
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());  // Verwijder de standaardrand
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));  // Donkere achtergrond voor de scrollpane
        frame.add(scrollPane, BorderLayout.CENTER);

        // Voeg wat padding toe rond de tabel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // 20px padding
        panel.setBackground(new Color(30, 30, 30));  // Donkere achtergrond
        panel.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.CENTER);

        // Toon het venster
        frame.setVisible(true);
    }

    private void searchStudent() {
        String searchText = searchField.getText().trim();
        for (int i = 0; i < studentTable.getRowCount(); i++) {
            String studentNumber = (String) studentTable.getValueAt(i, 3);
            if (studentNumber.equals(searchText)) {
                // Selecteer de rij en scroll naar de geselecteerde rij
                studentTable.setRowSelectionInterval(i, i);
                studentTable.scrollRectToVisible(studentTable.getCellRect(i, 0, true));
                return;
            }
        }
        // Als de student niet gevonden is, toon een foutmelding
        JOptionPane.showMessageDialog(frame, "Student niet gevonden.", "Fout", JOptionPane.ERROR_MESSAGE);
    }

    private void showStudentScores(String studentNumber) {
        try {
            // Haal de cijfers op via de API
            List<Grade> grades = fetchGradesForStudent(studentNumber);

            // Haal de examengegevens op
            List<Exam> exams = fetchExams();

            // Maak een JDialog in plaats van een JFrame
            JDialog scoresDialog = new JDialog(frame, "Cijfers voor " + studentNumber, true); // true = modal
            scoresDialog.setSize(800, 600); // Groter frame voor betere weergave
            scoresDialog.setLayout(new BorderLayout());
            scoresDialog.getContentPane().setBackground(new Color(30, 30, 30));

            // Kolomnamen voor de tabel
            String[] columnNames = {"Vak", "Cijfer", "Semester", "Type"};

            // Data voor de tabel
            Object[][] data = new Object[grades.size()][4];
            for (int i = 0; i < grades.size(); i++) {
                Grade grade = grades.get(i);

                // Zoek het bijbehorende examen op basis van exam_id
                Exam exam = exams.stream()
                        .filter(e -> e.getId() == Integer.parseInt(grade.getExam_id()))
                        .findFirst()
                        .orElse(null);

                // Vul de tabeldata in
                data[i][0] = grade.getCourse_name(); // Vaknaam
                data[i][1] = grade.getScore_value(); // Cijfer
                if (exam != null) {
                    data[i][2] = exam.getSemester(); // Semester
                    data[i][3] = exam.getType();    // Type (Regulier/Her)
                } else {
                    data[i][2] = "Onbekend"; // Als het examen niet gevonden is
                    data[i][3] = "Onbekend";
                }
            }

            // Maak de tabel
            JTable scoresTable = new JTable(data, columnNames);
            scoresTable.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Modern lettertype
            scoresTable.setRowHeight(30); // Grotere rijhoogte
            scoresTable.setBackground(new Color(45, 45, 45)); // Donkere tabelachtergrond
            scoresTable.setForeground(Color.WHITE); // Witte tekst
            scoresTable.setGridColor(new Color(80, 80, 80)); // Donkere rasterlijnen
            scoresTable.setSelectionBackground(new Color(0, 120, 215)); // Blauwe selectiekleur
            scoresTable.setSelectionForeground(Color.WHITE); // Witte tekst bij selectie

            // Centreer de tekst in de cellen
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < scoresTable.getColumnCount(); i++) {
                scoresTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // Pas de header van de tabel aan
            JTableHeader header = scoresTable.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Vet lettertype voor de header
            header.setBackground(new Color(0, 120, 215)); // Blauwe headerachtergrond
            header.setForeground(Color.WHITE); // Witte headertekst

            // Voeg de tabel toe aan een JScrollPane
            JScrollPane scrollPane = new JScrollPane(scoresTable);
            scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Verwijder de standaardrand
            scrollPane.getViewport().setBackground(new Color(30, 30, 30)); // Donkere achtergrond voor de scrollpane

            // Voeg wat padding toe rond de tabel
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 20px padding
            panel.setBackground(new Color(30, 30, 30)); // Donkere achtergrond
            panel.add(scrollPane, BorderLayout.CENTER);

            // Bereken het gemiddelde per vak
            Map<String, Double> averagePerCourse = calculateAveragePerCourse(studentNumber);

            // Maak een panel om het gemiddelde weer te geven
            JPanel averagePanel = new JPanel(new GridLayout(averagePerCourse.size(), 1, 10, 10));
            averagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            averagePanel.setBackground(new Color(30, 30, 30));

            for (Map.Entry<String, Double> entry : averagePerCourse.entrySet()) {
                String courseName = entry.getKey();
                double average = entry.getValue();
                JLabel averageLabel = new JLabel("Gemiddelde voor " + courseName + ": " + String.format("%.2f", average));
                averageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                averageLabel.setForeground(Color.WHITE);
                averageLabel.setHorizontalAlignment(JLabel.CENTER);
                averagePanel.add(averageLabel);
            }

            // Voeg een "Sluiten"-knop toe
            JButton closeButton = new JButton("Sluiten");
            closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            closeButton.setBackground(new Color(0, 120, 215));
            closeButton.setForeground(Color.WHITE);
            closeButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            closeButton.addActionListener(e -> scoresDialog.dispose()); // Sluit het dialoogvenster

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(new Color(30, 30, 30));
            buttonPanel.add(closeButton);

            // Voeg de componenten toe aan het dialoogvenster
            scoresDialog.add(panel, BorderLayout.CENTER); // Tabel met cijfers
            scoresDialog.add(averagePanel, BorderLayout.NORTH); // Gemiddelde per vak
            scoresDialog.add(buttonPanel, BorderLayout.SOUTH); // Sluiten-knop

            // Centreer het dialoogvenster ten opzichte van het hoofdvenster
            scoresDialog.setLocationRelativeTo(frame);

            // Toon het dialoogvenster
            scoresDialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Geen Cijfers", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private List<Grade> fetchGradesForStudent(String studentNumber) throws Exception {
        // Maak de URL voor het ophalen van de cijfers van een specifieke student
        String apiUrl = API_BASE_URL + "scores/" + studentNumber.replace("/", "-");

        // Maak een HTTP-verbinding
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Lees het antwoord van de API
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Converteer het JSON-antwoord naar een lijst van Grade-objecten
        Gson gson = new Gson();
        return gson.fromJson(response.toString(), new TypeToken<List<Grade>>() {}.getType());
    }

    private static List<Student> fetchStudents() throws Exception {
        // Maak de URL voor het ophalen van alle studenten
        String apiUrl = API_BASE_URL + "students";

        // Maak een HTTP-verbinding
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Lees het antwoord van de API
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Converteer het JSON-antwoord naar een lijst van Student-objecten
        Gson gson = new Gson();
        return gson.fromJson(response.toString(), new TypeToken<List<Student>>() {}.getType());
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

        Gson gson = new Gson();
        return gson.fromJson(response.toString(), new TypeToken<List<Exam>>() {}.getType());
    }

    private Map<String, Double> calculateAveragePerCourse(String studentNumber) {
        Map<String, Double> averagePerCourse = new HashMap<>(); // Map om het gemiddelde per vak op te slaan
        try {
            // Haal de cijfers op via de API
            List<Grade> grades = fetchGradesForStudent(studentNumber);

            // Loop door alle cijfers
            for (Grade grade : grades) {
                String courseName = grade.getCourse_name();
                double score = Double.parseDouble(grade.getScore_value());

                // Voeg het cijfer toe aan het vak
                if (averagePerCourse.containsKey(courseName)) {
                    // Als het vak al bestaat, tel het cijfer op en verhoog de teller
                    double currentTotal = averagePerCourse.get(courseName);
                    averagePerCourse.put(courseName, currentTotal + score);
                } else {
                    // Als het vak nog niet bestaat, voeg het toe aan de map
                    averagePerCourse.put(courseName, score);
                }
            }

            // Bereken het gemiddelde per vak
            for (Map.Entry<String, Double> entry : averagePerCourse.entrySet()) {
                String courseName = entry.getKey();
                double totalScore = entry.getValue();
                long count = grades.stream()
                        .filter(g -> g.getCourse_name().equals(courseName))
                        .count();
                double average = totalScore / count;
                averagePerCourse.put(courseName, average);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Fout bij het berekenen van het gemiddelde per vak.", "Fout", JOptionPane.ERROR_MESSAGE);
        }
        return averagePerCourse;
    }


    public static void main(String[] args) {
        try {
            // Haal de studentgegevens op via de API
            List<Student> students = fetchStudents();

            // Open de GUI met de opgehaalde studentgegevens
            new OverzichtStudentenGUI(students);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Fout bij het ophalen van de studentgegevens.", "Fout", JOptionPane.ERROR_MESSAGE);
        }
    }
}