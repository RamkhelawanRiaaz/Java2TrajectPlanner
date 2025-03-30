package tests;

import API_calls.API;
import models.Grade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddCijferTest {

    @Test
    public void testAddCijfer1() {
        API api = new API();

        Grade g = new Grade();
        //g.setStudent_number("SE/1123/110");
         g.setStudent_id(107);
        g.setExam_id(1);
        g.setScore_value(8.5);
        g.setScore_datetime("2025-12-31 13:04:23");

        assertDoesNotThrow(() -> api.postCijfer(g));
    }

    @Test
    public void testAddCijfer2() {
        API api = new API();

        Grade g = new Grade();
        //g.setStudent_number("SE/1123/104");
        g.setStudent_id(108);
        g.setExam_id(1);
        g.setScore_value(8);
        g.setScore_datetime("2025-12-31 13:04:23");

        assertDoesNotThrow(() -> api.postCijfer(g));
    }

    @Test
    public void testAddCijfer3() {
        API api = new API();

        Grade g = new Grade();
        //g.setStudent_number("SE/1123/108");
        g.setStudent_id(109);
        g.setExam_id(1);
        g.setScore_value(9);
        g.setScore_datetime("2025-12-31 13:04:23");

        assertDoesNotThrow(() -> api.postCijfer(g));
    }

    @Test
    public void testAddCijfer4() {
        API api = new API();

        Grade g = new Grade();
        //g.setStudent_number("SE/1123/109");
        g.setStudent_id(110);
        g.setExam_id(1);
        g.setScore_value(6.7);
        g.setScore_datetime("2025-12-31 13:04:23");

        assertDoesNotThrow(() -> api.postCijfer(g));
    }


    @Test
    public void testAddCijfer5() {
        API api = new API();

        Grade g = new Grade();
        //g.setStudent_number("SE/1123/110");
        g.setStudent_id(107);
        g.setExam_id(45);
        g.setScore_value(9.5);
        g.setScore_datetime("2025-12-31 13:04:23");

        assertDoesNotThrow(() -> api.postCijfer(g));
    }

    @Test
    public void testAddCijfer6() {
        API api = new API();

        Grade g = new Grade();
        //g.setStudent_number("SE/1123/104");
        g.setStudent_id(108);
        g.setExam_id(45);
        g.setScore_value(10);
        g.setScore_datetime("2025-12-31 13:04:23");

        assertDoesNotThrow(() -> api.postCijfer(g));
    }

    @Test
    public void testAddCijfer7() {
        API api = new API();

        Grade g = new Grade();
        //g.setStudent_number("SE/1123/108");
        g.setStudent_id(109);
        g.setExam_id(45);
        g.setScore_value(7.5);
        g.setScore_datetime("2025-12-31 13:04:23");

        assertDoesNotThrow(() -> api.postCijfer(g));
    }

    @Test
    public void testAddCijfer8() {
        API api = new API();

        Grade g = new Grade();
        //g.setStudent_number("SE/1123/109");
        g.setStudent_id(110);
        g.setExam_id(45);
        g.setScore_value(7.7);
        g.setScore_datetime("2025-12-31 13:04:23");

        assertDoesNotThrow(() -> api.postCijfer(g));
    }


}
