import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class Tentamen extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton refreshButton, updateButton, deleteButton;
    private HttpClient httpClient;
    private Gson gson;

    // Dark mode kleurenpalet
    private final Color PRIMARY_COLOR = new Color(100, 149, 237);  // Lichtblauw
    private final Color SECONDARY_COLOR = new Color(45, 45, 45);   // Donkergrijs
    private final Color ACCENT_COLOR = new Color(255, 87, 34);     // Oranje
    private final Color BACKGROUND_COLOR = new Color(30, 30, 30);  // Zeer donkergrijs
    private final Color TEXT_COLOR = new Color(220, 220, 220);     // Lichtgrijs
    private final Color TABLE_HEADER_COLOR = new Color(60, 60, 60); // Donkergrijs
    private final Color TABLE_SELECTION_COLOR = new Color(65, 105, 225); // Koningsblauw
    private final Color TABLE_GRID_COLOR = new Color(70, 70, 70);  // Donkergrijze gridlijnen

    // Model class voor tentamens
    class Exam {
        int id;
        int course_id;
        String course_name;
        int semester;
        String type;
        String date;

        public int getId() { return id; }
        public int getCourse_id() { return course_id; }
        public String getCourse_name() { return course_name; }
        public int getSemester() { return semester; }
        public String getType() { return type; }
        public String getDate() { return date; }
    }

    public Tentamen() {
        super("Overzicht Tentamens");
        httpClient = HttpClient.newHttpClient();
        gson = new Gson();

        setupUI();
        loadExamData();

        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ... [behoud alle bestaande imports en variabelen] ...

    private void setupUI() {
        // Main panel with dark styling
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel headerLabel = new JLabel("Overzicht Tentamen");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(TEXT_COLOR);
        headerLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Table model
        String[] columnNames = {"ID", "Course ID", "Course Name", "Semester", "Type", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Dark table styling with hover effects
        table = new JTable(tableModel) {
            private int hoverRowIndex = -1;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                if (row % 2 == 0) {
                    c.setBackground(BACKGROUND_COLOR);
                } else {
                    c.setBackground(new Color(40, 40, 40));
                }
                c.setForeground(TEXT_COLOR);

                if (row == hoverRowIndex) {
                    c.setBackground(new Color(80, 80, 80));
                }

                if (isRowSelected(row)) {
                    c.setBackground(TABLE_SELECTION_COLOR);
                    c.setForeground(Color.WHITE);
                }

                return c;
            }

            @Override
            protected void processMouseMotionEvent(java.awt.event.MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                if (row != hoverRowIndex) {
                    hoverRowIndex = row;
                    repaint();
                }
                super.processMouseMotionEvent(e);
            }

            @Override
            protected void processMouseEvent(java.awt.event.MouseEvent e) {
                if (e.getID() == java.awt.event.MouseEvent.MOUSE_EXITED) {
                    hoverRowIndex = -1;
                    repaint();
                }
                super.processMouseEvent(e);
            }
        };

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowGrid(true);
        table.setGridColor(TABLE_GRID_COLOR);
        table.setSelectionBackground(TABLE_SELECTION_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(SECONDARY_COLOR);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                table.repaint();
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(TEXT_COLOR);
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        refreshButton = createStyledButton("Vernieuwen", PRIMARY_COLOR);
        updateButton = createStyledButton("Bijwerken", new Color(76, 175, 80));
        deleteButton = createStyledButton("Verwijderen", ACCENT_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        refreshButton.addActionListener(e -> loadExamData());
        updateButton.addActionListener(e -> updateExam());
        deleteButton.addActionListener(e -> deleteExam());
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
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

        final int examId = (int) tableModel.getValueAt(selectedRow, 0);
        final String currentType = (String) tableModel.getValueAt(selectedRow, 4);
        final String currentDate = (String) tableModel.getValueAt(selectedRow, 5);

        final String formattedCurrentDate = currentDate != null && currentDate.length() == 10 ?
                currentDate + " 00:00:00" : currentDate;

        final JDialog dialog = new JDialog(this, "Tentamen bijwerken", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(800, 650);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel typeLabel = new JLabel("Type (Regulier/Her):");
        typeLabel.setForeground(TEXT_COLOR);
        contentPanel.add(typeLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        final JTextField typeField = new JTextField(currentType, 20);
        typeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeField.setBackground(SECONDARY_COLOR);
        typeField.setForeground(TEXT_COLOR);
        typeField.setCaretColor(TEXT_COLOR);
        contentPanel.add(typeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel dateLabel = new JLabel("Datum (YYYY-MM-DD HH:MM:SS):");
        dateLabel.setForeground(TEXT_COLOR);
        contentPanel.add(dateLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        final JFormattedTextField dateField;
        try {
            dateField = new JFormattedTextField(new javax.swing.text.MaskFormatter("####-##-## ##:##:##"));
            dateField.setValue(formattedCurrentDate);
        } catch (java.text.ParseException e) {
            throw new RuntimeException("Failed to create date formatter", e);
        }
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateField.setBackground(SECONDARY_COLOR);
        dateField.setForeground(TEXT_COLOR);
        dateField.setCaretColor(TEXT_COLOR);
        contentPanel.add(dateField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton cancelButton = createStyledButton("Annuleren", new Color(100, 100, 100));
        cancelButton.addActionListener(e -> dialog.dispose());

        JButton saveButton = createStyledButton("Opslaan", PRIMARY_COLOR);
        saveButton.addActionListener(e -> {
            try {
                JsonObject updateData = new JsonObject();
                updateData.addProperty("exam_id", examId);

                boolean hasChanges = false;

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

                String newDate = dateField.getText().trim();
                if (!newDate.equals(formattedCurrentDate)) {
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

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://trajectplannerapi.dulamari.com/exams"))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(updateData)))
                        .build();

                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenAccept(response -> {
                            if (response.statusCode() == 200) {
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(dialog, "Tentamen succesvol bijgewerkt!");
                                    loadExamData();
                                    dialog.dispose();
                                });
                            } else {
                                showError("Fout bij bijwerken: " + response.body());
                            }
                        });
            } catch (Exception ex) {
                showError("Fout: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteExam() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecteer eerst een tentamen");
            return;
        }

        int examId = (int) tableModel.getValueAt(selectedRow, 0);

        Object[] options = {"Verwijderen", "Annuleren"};
        int confirm = JOptionPane.showOptionDialog(this,
                "Weet u zeker dat u tentamen ID " + examId + " wilt verwijderen?",
                "Bevestig verwijderen",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[1]);

        if (confirm == 0) { // "Verwijderen" selected
            try {
                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("exam_id", examId);
                String jsonBody = gson.toJson(requestBody);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://trajectplannerapi.dulamari.com/exams"))
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .method("DELETE", HttpRequest.BodyPublishers.ofString(jsonBody))
                        .timeout(Duration.ofSeconds(15))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JOptionPane.showMessageDialog(this, "Tentamen succesvol verwijderd!");
                    loadExamData();
                } else {
                    handleDeleteError(response.statusCode(), response.body());
                }
            } catch (Exception ex) {
                showError("Er ging iets mis: " + ex.getMessage());
                ex.printStackTrace();
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
            errorMessage += "\nGeen details beschikbaar";
        }

        showError(errorMessage);
    }

    private void showError(String message) {
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        JOptionPane.showMessageDialog(this,
                "<html><font color='#ffffff'>" + message + "</font></html>",
                "Fout",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        JOptionPane.showMessageDialog(this,
                "<html><font color='#ffffff'>" + message + "</font></html>",
                "Waarschuwing",
                JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.arc", 20);
            UIManager.put("Component.arc", 20);
            UIManager.put("ProgressBar.arc", 20);
            UIManager.put("TextComponent.arc", 20);

            UIManager.put("OptionPane.background", new Color(30, 30, 30));
            UIManager.put("Panel.background", new Color(30, 30, 30));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Tentamen());
    }
}