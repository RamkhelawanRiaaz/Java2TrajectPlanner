package GUI;

import API_calls.API;
import models.Course;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.util.List;

public class OverzichtCoursesGUI {
    private JFrame frame;
    private JTable courseTable;

    public OverzichtCoursesGUI(List<Course> courses) {
        System.out.println("GUI.OverzichtCoursesGUI wordt geopend met " + courses.size() + " cursussen.");

        frame = new JFrame("Overzicht StudieOnderdelen");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        // Kolomnamen
        String[] columnNames = {"ID", "Cursuscode", "Cursusnaam", "EC", "Beschrijving", "Blok"};

        // Data voor de tabel
        Object[][] data = new Object[courses.size()][6];
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            data[i][0] = course.getId();
            data[i][1] = course.getCode();
            data[i][2] = course.getName();
            data[i][3] = course.getEc();
            data[i][4] = course.getDescription();
            data[i][5] = course.getBlock();
        }
        //Tabel wordt gemaakt
        courseTable = new JTable(data, columnNames);
        courseTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseTable.setRowHeight(30);
        courseTable.setBackground(new Color(45, 45, 45));
        courseTable.setForeground(Color.WHITE);
        courseTable.setGridColor(new Color(80, 80, 80));
        courseTable.setSelectionBackground(new Color(0, 120, 215));
        courseTable.setSelectionForeground(Color.WHITE);

        JTableHeader header = courseTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.WHITE);
        //Positie van tabel
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < courseTable.getColumnCount(); i++) {
            courseTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        //Scroll functie
        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(30, 30, 30));
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            List<Course> courses = new API().getCourses();
            SwingUtilities.invokeLater(() -> new OverzichtCoursesGUI(courses));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Er is een fout opgetreden bij het ophalen van de gegevens.", "Fout", JOptionPane.ERROR_MESSAGE);
        }
    }
}
