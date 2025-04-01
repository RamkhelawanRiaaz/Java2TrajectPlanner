package tests;

import GUI.OverzichtStudentenGUI;
import models.Student;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class OverzichtStudentenGUIDeleteTest {

    @Test
    public void testDeleteViaGUIWithScoreId31() {
        Student student = new Student();
        student.setId("39");

        OverzichtStudentenGUI  gui = new OverzichtStudentenGUI(Collections.singletonList(student));

        assertDoesNotThrow(() -> gui.deleteStudent(0));
    }
}
