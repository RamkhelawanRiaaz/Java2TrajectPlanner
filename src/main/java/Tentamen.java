import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import com.google.gson.*;

public class Tentamen extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton refreshButton, updateButton, deleteButton;
    private Gson gson;

    // Dark mode kleurenpalet
    private final Color PRIMARY_COLOR = new Color(100, 149, 237);
    private final Color SECONDARY_COLOR = new Color(45, 45, 45);
    private final Color ACCENT_COLOR = new Color(255, 87, 34);
    private final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color TABLE_HEADER_COLOR = new Color(60, 60, 60);
    private final Color TABLE_SELECTION_COLOR = new Color(65, 105, 225);
    private final Color TABLE_GRID_COLOR = new Color(70, 70, 70);

    public Tentamen() {
        super("Overzicht Tentamens");
        gson = new Gson();
        setupUI();
        loadExamData();
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupUI() {
        // Main panel
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

        // Table styling
        table = new JTable(tableModel) {
            private int hoverRowIndex = -1;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setBackground(row % 2 == 0 ? BACKGROUND_COLOR : new Color(40, 40, 40));
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

        // Table configuration
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowGrid(true);
        table.setGridColor(TABLE_GRID_COLOR);
        table.setSelectionBackground(TABLE_SELECTION_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(SECONDARY_COLOR);

        // Table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(TEXT_COLOR);
        header.setReorderingAllowed(false);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // Buttons
        refreshButton = createStyledButton("Vernieuwen", PRIMARY_COLOR);
        updateButton = createStyledButton("Bijwerken", new Color(76, 175, 80));
        deleteButton = createStyledButton("Verwijderen", ACCENT_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Add components to main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Button listeners
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
            List<Exam> exams = API.getExams();

            // Debug: toon ontvangen examens
            System.out.println("Aantal examens ontvangen: " + exams.size());
            for (Exam exam : exams) {
                System.out.println("Exam: " + exam.getId() +
                        ", Course: " + exam.getCourseId() +
                        ", Name: " + exam.getCourseName());
            }

            SwingUtilities.invokeLater(() -> {
                tableModel.setRowCount(0);
                for (Exam exam : exams) {
                    tableModel.addRow(new Object[]{
                            exam.getId(),
                            exam.getCourseId(),
                            exam.getCourseName(),
                            exam.getSemester(),
                            exam.getType(),
                            exam.getDate()
                    });
                }
            });
        } catch (Exception e) {
            showError("Fout bij ophalen data: " + e.getMessage());
            e.printStackTrace();
        }
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

        // Dialog setup
        final JDialog dialog = new JDialog(this, "Tentamen bijwerken", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(800, 650);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);

        // Content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Type field
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel typeLabel = new JLabel("Type (Regulier/Her):");
        typeLabel.setForeground(TEXT_COLOR);
        contentPanel.add(typeLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        final JTextField typeField = new JTextField(currentType, 20);
        styleTextField(typeField);
        contentPanel.add(typeField, gbc);

        // Date field
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel dateLabel = new JLabel("Datum (YYYY-MM-DD):");
        dateLabel.setForeground(TEXT_COLOR);
        contentPanel.add(dateLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        final JTextField dateField = new JTextField(currentDate, 20);
        styleTextField(dateField);
        contentPanel.add(dateField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton cancelButton = createStyledButton("Annuleren", new Color(100, 100, 100));
        cancelButton.addActionListener(e -> dialog.dispose());

        JButton saveButton = createStyledButton("Opslaan", PRIMARY_COLOR);
        saveButton.addActionListener(e -> {
            try {
                Exam exam = new Exam();
                exam.setId(examId);

                // Alleen type instellen als het is gewijzigd
                String newType = typeField.getText().trim();
                if (!newType.equals(currentType)) {
                    exam.setType(newType);
                }

                // Alleen datum instellen als het is gewijzigd
                String newDate = dateField.getText().trim();
                if (!newDate.equals(currentDate)) {
                    exam.setDate(newDate);
                }

                // Controleer of er iets is gewijzigd
                if (exam.getType() == null && exam.getDate() == null) {
                    showWarning("Geen wijzigingen gedetecteerd");
                    return;
                }

                API.updateExam(exam);
                JOptionPane.showMessageDialog(dialog, "Tentamen succesvol bijgewerkt!");
                loadExamData();
                dialog.dispose();
            } catch (Exception ex) {
                showError("Fout bij bijwerken: " + ex.getMessage());
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBackground(SECONDARY_COLOR);
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(TEXT_COLOR);
        textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void deleteExam() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecteer eerst een tentamen");
            return;
        }

        int examId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Weet u zeker dat u tentamen ID " + examId + " wilt verwijderen?",
                "Bevestig verwijderen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                API.deleteExam(examId);
                JOptionPane.showMessageDialog(this, "Tentamen succesvol verwijderd!");
                loadExamData();
            } catch (Exception ex) {
                showError("Fout bij verwijderen: " + ex.getMessage());
            }
        }
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
            UIManager.put("OptionPane.background", new Color(30, 30, 30));
            UIManager.put("Panel.background", new Color(30, 30, 30));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Tentamen());
    }
}