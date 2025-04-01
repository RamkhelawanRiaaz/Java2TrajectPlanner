package tests;

import GUI.OverzichtCijfersGUI;
import models.Grade;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class OverzichtCijfersGUIDeleteTest {

    @Test
    public void testDeleteViaGUIWithScoreId31() {
        Grade grade = new Grade();
        grade.setId(31);

        OverzichtCijfersGUI gui = new OverzichtCijfersGUI(Collections.singletonList(grade));

        assertDoesNotThrow(() -> gui.deleteGrade(0));
    }
}
