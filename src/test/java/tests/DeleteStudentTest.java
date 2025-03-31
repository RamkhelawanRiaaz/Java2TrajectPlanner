package tests;

import API_calls.API;
import models.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteStudentTest {

    @Test
    public void testDeleteStudent_Riaaz() {
        API api = new API();
        String studentId = "107";

        assertDoesNotThrow(() -> api.deleteStudent(studentId));
    }

    @Test
    public void testDeleteStudent_Rishika() {
        API api = new API();
        String studentId = "108";

        assertDoesNotThrow(() -> api.deleteStudent(studentId));
    }

    @Test
    public void testDeleteStudent_Sherr() {
        API api = new API();
        String studentId = "109";

        assertDoesNotThrow(() -> api.deleteStudent(studentId));
    }

    @Test
    public void testDeleteStudent_Shakeel() {
        API api = new API();
        String studentId = "110";

        assertDoesNotThrow(() -> api.deleteStudent(studentId));
    }
}
