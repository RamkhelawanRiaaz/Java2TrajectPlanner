import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isClicked;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox, JTable table) {
        super(checkBox);
        this.table = table;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isClicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isClicked) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int studentId = (int) table.getValueAt(selectedRow, 0); // Pak het ID van de student
                removeStudent(studentId); // Verwijder student
            }
        }
        isClicked = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isClicked = false;
        return super.stopCellEditing();
    }

    private void removeStudent(int studentId) {
        int confirm = JOptionPane.showConfirmDialog(null, "Weet je zeker dat je deze student wilt verwijderen?", "Bevestiging", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("Student met ID " + studentId + " wordt verwijderd...");
            // Hier zou je een API-call kunnen doen om de student te verwijderen
        }
    }
}
