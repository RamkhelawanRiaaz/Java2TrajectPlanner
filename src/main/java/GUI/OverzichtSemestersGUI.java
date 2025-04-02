package GUI;

import models.Course;
import models.Semester;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class OverzichtSemestersGUI {
    private JFrame frame;
    private JTable semesterTable;

    public OverzichtSemestersGUI(List<Semester> semesters) {
        System.out.println("GUI.OverzichtSemestersGUI wordt geopend met " + semesters.size() + " semesters.");

        //Hoofdvenster
        frame = new JFrame("Overzicht Semesters");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 600);  // Iets groter voor een betere weergave
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));  // Donkere achtergrond

        // Kolomnamen voor de tabel
        String[] columnNames = {"models.Semester ID", "models.Semester Naam", "Cursussen"};

        // Data voor de tabel
        Object[][] data = new Object[semesters.size()][3];
        for (int i = 0; i < semesters.size(); i++) {
            Semester semester = semesters.get(i);
            data[i][0] = semester.getId();
            data[i][1] = semester.getSemester_name();

            StringBuilder coursesString = new StringBuilder();
            for (Course course : semester.getCourses()) {
                coursesString.append(course.getName()).append(" (").append(course.getCode()).append("), ");
            }
            data[i][2] = coursesString.toString();
        }

        // Maakt de tabel
        semesterTable = new JTable(data, columnNames);
        semesterTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));  // Modern lettertype
        semesterTable.setRowHeight(30);  // Iets grotere rijhoogte
        semesterTable.setBackground(new Color(45, 45, 45));  // Donkere tabelachtergrond
        semesterTable.setForeground(Color.WHITE);  // Witte tekst
        semesterTable.setGridColor(new Color(80, 80, 80));  // Donkere rasterlijnen
        semesterTable.setSelectionBackground(new Color(0, 120, 215));  // Blauwe selectiekleur
        semesterTable.setSelectionForeground(Color.WHITE);  // Witte tekst bij selectie

        // Pas de header van de tabel aan
        JTableHeader header = semesterTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));  // Vet lettertype voor de header
        header.setBackground(new Color(0, 120, 215));  // Blauwe headerachtergrond
        header.setForeground(Color.WHITE);  // Witte headertekst

        // Centreer de tekst in de cellen
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < semesterTable.getColumnCount(); i++) {
            semesterTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Voeg de tabel toe aan een JScrollPane
        JScrollPane scrollPane = new JScrollPane(semesterTable);
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