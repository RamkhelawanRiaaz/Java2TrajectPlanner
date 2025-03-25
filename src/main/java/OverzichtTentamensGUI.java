import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupUI() {
        // Table model
        String[] columnNames = {"ID", "Course ID", "Course Name", "Semester", "Type", "Date"};
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
        int examId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentType = (String) tableModel.getValueAt(selectedRow, 4);
        String currentDate = (String) tableModel.getValueAt(selectedRow, 5);

        // Converteer bestaande datum naar juist formaat indien nodig
        if (currentDate != null && currentDate.length() == 10) { // Als YYYY-MM-DD
            currentDate += " 00:00:00"; // Voeg standaard tijd toe
        }

        // Maak formulier met datumveld
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField typeField = new JTextField(currentType);

        // Gebruik JFormattedTextField voor datum
        JFormattedTextField dateField;
        try {
            dateField = new JFormattedTextField(new javax.swing.text.MaskFormatter("####-##-## ##:##:##"));
            dateField.setValue(currentDate);
        } catch (java.text.ParseException e) {
            dateField = new JFormattedTextField();
            dateField.setValue(currentDate);
        }
        dateField.setColumns(20);

        panel.add(new JLabel("Type (Regulier/Her):"));
        panel.add(typeField);
        panel.add(new JLabel("Datum (YYYY-MM-DD HH:MM:SS):"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Tentamen bijwerken",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                JsonObject updateData = new JsonObject();
                updateData.addProperty("exam_id", examId);

                boolean hasChanges = false;

                // Valideer en verwerk type
                if (!typeField.getText().equals(currentType)) {
                    String newType = typeField.getText().trim();
                    if (newType.equals("Regulier") || newType.equals("Her")) {
                        updateData.addProperty("exam_type", newType);
                        hasChanges = true;
                    } else {
                        showError("Type moet 'Regulier' of 'Her' zijn");
                        return;
                    }
                }

                // Valideer en verwerk datum
                String newDate = dateField.getText().trim();
                if (!newDate.equals(currentDate)) {
                    if (newDate.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) {
                        updateData.addProperty("EXM_DATETIME", newDate);
                        hasChanges = true;
                    } else {
                        showError("Ongeldig datumformaat. Gebruik YYYY-MM-DD HH:MM:SS");
                        return;
                    }
                }

                if (!hasChanges) {
                    showWarning("Geen wijzigingen gedetecteerd");
                    return;
                }

                // Debug output
                System.out.println("Verzendende update: " + gson.toJson(updateData));

                // Verstuur request
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://trajectplannerapi.dulamari.com/exams"))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(updateData)))
                        .build();

                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenAccept(response -> {
                            if (response.statusCode() == 200) {
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(this, "Tentamen succesvol bijgewerkt!");
                                    loadExamData();
                                });
                            } else {
                                showError("Fout bij bijwerken: " + response.body());
                            }
                        });
            } catch (Exception ex) {
                showError("Fout: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }


    private void deleteExam() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecteer eerst een tentamen");
            return;
        }

        int examId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Weet u zeker dat u tentamen ID " + examId + " wilt verwijderen?",
                "Bevestig verwijderen", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("exam_id", examId);
                String jsonBody = gson.toJson(requestBody);

                System.out.println("[DEBUG] Verzendende DELETE request:");
                System.out.println("URL: https://trajectplannerapi.dulamari.com/exams");
                System.out.println("Body: " + jsonBody);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://trajectplannerapi.dulamari.com/exams"))
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .method("DELETE", HttpRequest.BodyPublishers.ofString(jsonBody))
                        .timeout(Duration.ofSeconds(15))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("[DEBUG] Ontvangen response:");
                System.out.println("Status: " + response.statusCode());
                System.out.println("Body: " + response.body());

                if (response.statusCode() == 200) {
                    JOptionPane.showMessageDialog(this, "Tentamen succesvol verwijderd!");
                    loadExamData();
                } else {
                    handleDeleteError(response.statusCode(), response.body());
                }
            } catch (Exception ex) {
                System.err.println("[ERROR] Uitzondering tijdens verwijderen:");
                ex.printStackTrace();
                showError("Er ging iets mis: " + ex.getMessage());
            }
        }
    }

    private void handleDeleteError(int statusCode, String responseBody) {
        String errorMessage = "Serverfout bij verwijderen (Code: " + statusCode + ")";

        if (responseBody != null && !responseBody.trim().isEmpty()) {
            try {
                JsonElement jsonElement = JsonParser.parseString(responseBody);
                if (jsonElement.isJsonObject()) {
                    JsonObject errorResponse = jsonElement.getAsJsonObject();
                    if (errorResponse.has("message")) {
                        errorMessage += "\n" + errorResponse.get("message").getAsString();
                    }
                } else {
                    errorMessage += "\n" + responseBody;
                }
            } catch (JsonSyntaxException e) {
                errorMessage += "\nResponse: " + responseBody;
            }
        } else {
            errorMessage += "\nGeen details beschikbaar - mogelijk een serverconfiguratieprobleem";
        }

        showError(errorMessage);
    }

    private String parseErrorMessage(String responseBody) {
        try {
            JsonObject json = gson.fromJson(responseBody, JsonObject.class);
            if (json.has("error")) {
                return json.get("error").getAsString();
            }
            if (json.has("message")) {
                return json.get("message").getAsString();
            }
        } catch (Exception e) {
            // Geen JSON response
        }
        return responseBody;
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