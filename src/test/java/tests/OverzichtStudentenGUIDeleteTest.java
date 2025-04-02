package tests;

import GUI.OverzichtStudentenGUI;
import models.Student;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class OverzichtStudentenGUIDeleteTest {

    @Test
    public void testDeleteViaGUIWithScoreId31() {
        //Student object aan maken
        Student student = new Student();
        //ID value zetten ib object
        student.setId("39");

        OverzichtStudentenGUI  gui = new OverzichtStudentenGUI(Collections.singletonList(student));

        //obeject sturen naar gui.deleteStudent method en het te runnen met die ID dat verwacht word
        assertDoesNotThrow(() -> gui.deleteStudent(0));
    }
}
