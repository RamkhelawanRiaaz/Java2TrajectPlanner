import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OverzichtCijfersGUI {
    private JFrame frame;
    private JTable gradeTable;
    private JButton visualizeButton;

    public OverzichtCijfersGUI(List<Grade> grades) {
        System.out.println("OverzichtCijfersGUI wordt geopend met " + grades.size() + " cijfers.");

        // Maak het hoofdvenster
        frame = new JFrame("Overzicht Cijfers");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        // Kolomnamen voor de tabel
        String[] columnNames = {"ID", "Studentnummer", "Volledige Naam", "Cursusnaam", "Cijfer", "Datum"};

        // Data voor de tabel
        Object[][] data = new Object[grades.size()][6];
        for (int i = 0; i < grades.size(); i++) {
            Grade grade = grades.get(i);
            data[i][0] = grade.getStudent_id();
            data[i][1] = grade.getStudent_number();
            data[i][2] = grade.getStudent_full_name();  // Volledige naam
            data[i][3] = grade.getCourse_name();
            data[i][4] = grade.getScore_value();
            data[i][5] = grade.getScore_datetime();
        }

        gradeTable = new JTable(data, columnNames);
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
                    String studentId = (String) gradeTable.getValueAt(selectedRow, 0);  // Zorg ervoor dat het een String is
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

        frame.setVisible(true);
    }

    private void openCijferVisualisatie(String studentId, String studentName) {
        new CijferVisualisatieGUI(studentId, studentName);
    }

    public static void main(String[] args) {
        try {

            List<Grade> grades = API.getScoresWithStudentNames();

            SwingUtilities.invokeLater(() -> new OverzichtCijfersGUI(grades));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Er is een fout opgetreden bij het ophalen van de gegevens.", "Fout", JOptionPane.ERROR_MESSAGE);
        }
    }
}