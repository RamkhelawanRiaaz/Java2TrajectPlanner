package tests;

import API_calls.API;
import models.Exam;
import models.Tentamen;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddTentamenTest{
    @Test
    public void AddTentamenTest1() {
        API api = new API();

        Tentamen e = new Tentamen();
        e.setCourse_id(8);
        e.setExam_type("Regulier");
        e.setExam_date("2025-12-31");
        assertDoesNotThrow(() -> api.postTentamen(e));
    }

    @Test
    public void AddTentamenTest2() {
        API api = new API();

        Tentamen e = new Tentamen();
        e.setCourse_id(8);
        e.setExam_type("Her");
        e.setExam_date("2025-12-31");
        assertDoesNotThrow(() -> api.postTentamen(e));
    }
}