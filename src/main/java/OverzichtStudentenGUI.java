import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class OverzichtStudentenGUI {
    private JFrame frame;
    private JTable studentTable;

    public OverzichtStudentenGUI(List<Student> students) {
        System.out.println("OverzichtStudentenGUI wordt geopend met " + students.size() + " studenten.");

        // Maak het hoofdvenster
        frame = new JFrame("Overzicht Studenten");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);  // Iets groter voor een betere weergave
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));  // Donkere achtergrond

        // Kolomnamen voor de tabel
        String[] columnNames = {"ID", "Voornaam", "Achternaam", "Studentnummer", "Geslacht", "Geboortedatum", "Admin"};

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
            data[i][6] = student.isAdmin() ? "Ja" : "Nee";
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
}