import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// Voeg deze klasse toe aan je code
class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private JFrame parentFrame;

    public ButtonEditor(JCheckBox checkBox, JTable table) {
        super(checkBox);
        this.table = table;
        this.parentFrame = (JFrame) SwingUtilities.getWindowAncestor(table);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int row = table.getEditingRow();
            String studentNumber = (String) table.getValueAt(row, 3); // Studentnummer is in kolom 3
            deleteStudent(studentNumber, row);
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    private void deleteStudent(String studentNumber, int row) {
        int confirm = JOptionPane.showConfirmDialog(parentFrame,
                "Weet u zeker dat u student " + studentNumber + " wilt verwijderen?",
                "Bevestig verwijdering", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Maak de URL voor het verwijderen van een student
                String apiUrl = "https://trajectplannerapi.dulamari.com/students";

                // Maak een HTTP-verbinding
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Maak het JSON request body
                String jsonInputString = "{\"student_number\": \"" + studentNumber + "\"}";

                // Schrijf de JSON naar de request body
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Lees het antwoord van de API
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Verwijder de rij uit de tabel
                    ((DefaultTableModel) table.getModel()).removeRow(row);
                    JOptionPane.showMessageDialog(parentFrame,
                            "Student succesvol verwijderd.",
                            "Succes", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Fout bij verwijderen student: " + responseCode,
                            "Fout", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parentFrame,
                        "Fout bij verwijderen student: " + e.getMessage(),
                        "Fout", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

// Voeg deze klasse ook toe aan je code
class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}