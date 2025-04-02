package tests;

import API_calls.API;
import models.Exam;
import models.Tentamen;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddTentamenTest{
    @Test
    public void AddTentamenTest1() {
        //Object maken van API calls
        API api = new API();

        //Object maken van Tentamen model
        Tentamen e = new Tentamen();
        //data zetten in parameters van Tentamen model
        e.setCourse_id(8);
        e.setExam_type("Regulier");
        e.setExam_date("2025-12-31");

        //tentamen object sturen naar api.postTentamen
        assertDoesNotThrow(() -> api.postTentamen(e));
    }

    @Test
    public void AddTentamenTest2() {
        //Object maken van API calls
        API api = new API();

        //Object maken van Tentamen model
        Tentamen e = new Tentamen();
        //data zetten in parameters van Tentamen model
        e.setCourse_id(8);
        e.setExam_type("Her");
        e.setExam_date("2025-12-31");

        //tentamen object sturen naar api.postTentamen
        assertDoesNotThrow(() -> api.postTentamen(e));
    }
}