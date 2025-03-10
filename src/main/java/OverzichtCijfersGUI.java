import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class OverzichtCijfersGUI {
    private JFrame frame;
    private JTable gradeTable;

    public OverzichtCijfersGUI(List<Grade> grades) {
        System.out.println("OverzichtCijfersGUI wordt geopend met " + grades.size() + " cijfers.");

        // Maak het hoofdvenster
        frame = new JFrame("Overzicht Cijfers");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 600);  // Iets groter voor een betere weergave
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));  // Donkere achtergrond

        // Kolomnamen voor de tabel
        String[] columnNames = {"ID", "Studentnummer", "Volledige Naam",  "Cursusnaam", "Cijfer", "Datum"};

        // Data voor de tabel
        Object[][] data = new Object[grades.size()][8];
        for (int i = 0; i < grades.size(); i++) {
            Grade grade = grades.get(i);
//            data[i][0] = grade.getId();
            data[i][0] = grade.getStudent_id();
            data[i][1] = grade.getStudent_number();
            data[i][2] = grade.getStudent_full_name();  // Volledige naam
//            data[i][4] = grade.getExam_id();
            data[i][3] = grade.getCourse_name();
            data[i][4] = grade.getScore_value();
            data[i][5] = grade.getScore_datetime();
        }

        // Maak de tabel
        gradeTable = new JTable(data, columnNames);
        gradeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));  // Modern lettertype
        gradeTable.setRowHeight(30);  // Iets grotere rijhoogte
        gradeTable.setBackground(new Color(45, 45, 45));  // Donkere tabelachtergrond
        gradeTable.setForeground(Color.WHITE);  // Witte tekst
        gradeTable.setGridColor(new Color(80, 80, 80));  // Donkere rasterlijnen
        gradeTable.setSelectionBackground(new Color(0, 120, 215));  // Blauwe selectiekleur
        gradeTable.setSelectionForeground(Color.WHITE);  // Witte tekst bij selectie

        // Pas de header van de tabel aan
        JTableHeader header = gradeTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));  // Vet lettertype voor de header
        header.setBackground(new Color(0, 120, 215));  // Blauwe headerachtergrond
        header.setForeground(Color.WHITE);  // Witte headertekst

        // Centreer de tekst in de cellen
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < gradeTable.getColumnCount(); i++) {
            gradeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Voeg de tabel toe aan een JScrollPane
        JScrollPane scrollPane = new JScrollPane(gradeTable);
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
}