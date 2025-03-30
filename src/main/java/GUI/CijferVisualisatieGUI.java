package GUI;

import API_calls.API;
import models.Grade;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CijferVisualisatieGUI {
    private JFrame frame;

    public CijferVisualisatieGUI(int studentId, String studentName) {
        frame = new JFrame("models.Cijfer Visualisatie - " + studentName);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        List<Grade> studentGrades = null;
        try {
            API api_request = new API();
            studentGrades = api_request.getScoresWithStudentNames();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Fout bij het ophalen van de cijfers: " + e.getMessage(), "Fout", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        boolean dataFound = false;
        for (Grade grade : studentGrades) {

            if (studentId == grade.getStudent_id()) {
                dataFound = true;
                try {

                    double score = grade.getScore_value();
                    dataset.addValue(score, "models.Cijfer", grade.getCourse_name());
                } catch (NumberFormatException e) {
                    System.err.println("Fout bij het omzetten van het cijfer naar een nummer: " + grade.getScore_value());
                }
            }
        }

        if (!dataFound) {
            JOptionPane.showMessageDialog(frame, "Geen cijfers gevonden voor deze student.", "Fout", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Cijfers van " + studentName,
                "Cursus",
                "models.Cijfer",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();

        plot.getRenderer().setSeriesPaint(0, Color.BLUE);

        ChartPanel chartPanel = new ChartPanel(chart);
        frame.add(chartPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}