package tests;

import API_calls.API;
import models.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteStudentTest {

    @Test
    public void testDeleteStudent_Riaaz() {
        //Object maken van API calls
        API api = new API();

        //Id dat gebruikt moet worden in de api.deleteStudent om de record te verwijderen.
        String studentId = "107";

        //ID sturen naar api.deleteStudent
        assertDoesNotThrow(() -> api.deleteStudent(studentId));
    }

    @Test
    public void testDeleteStudent_Rishika() {
        //Object maken van API calls
        API api = new API();

        //Id dat gebruikt moet worden in de api.deleteStudent om de record te verwijderen.
        String studentId = "108";

        //ID sturen naar api.deleteStudent
        assertDoesNotThrow(() -> api.deleteStudent(studentId));
    }

    @Test
    public void testDeleteStudent_Sherr() {
        //Object maken van API calls
        API api = new API();

        //Id dat gebruikt moet worden in de api.deleteStudent om de record te verwijderen.
        String studentId = "109";

        //ID sturen naar api.deleteStudent
        assertDoesNotThrow(() -> api.deleteStudent(studentId));
    }

    @Test
    public void testDeleteStudent_Shakeel() {
        //Object maken van API calls
        API api = new API();

        //Id dat gebruikt moet worden in de api.deleteStudent om de record te verwijderen.
        String studentId = "110";

        //ID sturen naar api.deleteStudent
        assertDoesNotThrow(() -> api.deleteStudent(studentId));
    }
}
