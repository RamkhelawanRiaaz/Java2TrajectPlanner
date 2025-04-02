package tests;

import GUI.OverzichtCijfersGUI;
import models.Grade;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class OverzichtCijfersGUIDeleteTest {

    @Test
    public void testDeleteViaGUIWithScoreId31() {
        //obejct van Grade maken
        Grade grade = new Grade();
        //ID zetten in die object
        grade.setId(31);

        OverzichtCijfersGUI gui = new OverzichtCijfersGUI(Collections.singletonList(grade));

        //obeject sturen naar gui.deleteGrade method en het te runnen met die ID dat verwacht word
        assertDoesNotThrow(() -> gui.deleteGrade(0));
    }
}
