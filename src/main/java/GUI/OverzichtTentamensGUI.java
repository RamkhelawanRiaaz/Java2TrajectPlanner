package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import API_calls.API;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.Tentamen;

public class OverzichtTentamensGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton refreshButton, updateButton, deleteButton;
    private HttpClient httpClient;
    private Gson gson;

    // Model class voor tentamens
    class Exam {
        int id;
        int course_id;
        String course_name;
        int semester;
        String type;
        String date;

        // Getters (nodig voor Gson)
        public int getId() { return id; }
        public int getCourse_id() { return course_id; }
        public String getCourse_name() { return course_name; }
        public int getSemester() { return semester; }
        public String getType() { return type; }
        public String getDate() { return date; }
    }

    public OverzichtTentamensGUI() {
        super("Overzicht Tentamens");
        httpClient = HttpClient.newHttpClient();
        gson = new Gson();

        setupUI();
        loadExamData();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupUI() {
        // Table model
        String[] columnNames = {"ID", "models.Course ID", "models.Course Name", "models.Semester", "Type", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Maak tabel niet bewerkbaar
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        refreshButton = new JButton("Refreshen");
        updateButton = new JButton("Bijwerken");
        deleteButton = new JButton("Verwijderen");

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event handlers
        refreshButton.addActionListener(e -> loadExamData());
        updateButton.addActionListener(e -> updateExam());
        deleteButton.addActionListener(e -> deleteExam());
    }

    private void loadExamData() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://trajectplannerapi.dulamari.com/exams"))
                    .GET()
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(this::parseExamData)
                    .exceptionally(e -> {
                        showError("Fout bij ophalen data: " + e.getMessage());
                        return null;
                    });
        } catch (Exception ex) {
            showError("Fout: " + ex.getMessage());
        }
    }

    private void parseExamData(String jsonData) {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            try {
                List<Exam> exams = gson.fromJson(jsonData, new TypeToken<List<Exam>>(){}.getType());
                for (Exam exam : exams) {
                    tableModel.addRow(new Object[]{
                            exam.id,
                            exam.course_id,
                            exam.course_name,
                            exam.semester,
                            exam.type,
                            exam.date
                    });
                }
            } catch (Exception e) {
                showError("Fout bij verwerken data: " + e.getMessage());
            }
        });
    }

    private void updateExam() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecteer eerst een tentamen");
            return;
        }

        // Haal huidige waarden op
        String examId = String.valueOf(tableModel.getValueAt(selectedRow, 0));
        String currentType = String.valueOf(tableModel.getValueAt(selectedRow, 4));
        String currentDate = String.valueOf(tableModel.getValueAt(selectedRow, 5));

        if (currentDate != null && currentDate.length() == 10) {
            currentDate += " 00:00:00";
        }

        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField typeField = new JTextField(currentType);
        JTextField dateField = new JTextField(currentDate);

        panel.add(new JLabel("Type (Regulier/Her):"));
        panel.add(typeField);
        panel.add(new JLabel("Datum (YYYY-MM-DD HH:MM:SS):"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(this, panel, "models.Tentamen bijwerken",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Tentamen t = new Tentamen();
                t.setId(examId);
                t.setExam_type(typeField.getText().trim());
                t.setExam_date(dateField.getText().trim());

                new API().updateTentamen(t);
                JOptionPane.showMessageDialog(this, "models.Tentamen succesvol bijgewerkt!");
                loadExamData();
            } catch (Exception e) {
                showError("Fout bij bijwerken: " + e.getMessage());
            }
        }
    }

    private void deleteExam() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecteer eerst een tentamen");
            return;
        }

        Object idValue = tableModel.getValueAt(selectedRow, 0);
        if (idValue == null) {
            showError("Geen ID gevonden voor geselecteerd tentamen.");
            return;
        }

        String examId = idValue.toString().trim();  // <-- heel belangrijk

        int confirm = JOptionPane.showConfirmDialog(this,
                "Weet u zeker dat u tentamen ID " + examId + " wilt verwijderen?",
                "Bevestig verwijderen", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                new API().deleteTentamen(examId);
                JOptionPane.showMessageDialog(this, "models.Tentamen succesvol verwijderd!");
                loadExamData();
            } catch (Exception e) {
                showError("Fout bij verwijderen: " + e.getMessage());
            }
        }
    }


    private void showError(String message) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, message, "Fout", JOptionPane.ERROR_MESSAGE));
    }

    private void showWarning(String message) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, message, "Waarschuwing", JOptionPane.WARNING_MESSAGE));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OverzichtTentamensGUI());
    }
}
//test