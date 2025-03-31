package tests;

import API_calls.API;
import models.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateStudentTest {

    @Test
    public void testUpdateStudent1() {
        API api = new API();

        Student s = new Student();
        s.setId("107");
        s.setFirst_name("Rampie");
        s.setLast_name("Ramkhelawan");
        s.setMajor("SE");
        s.setCohort(1101);
        s.setTotal_ec(100);
        s.setGender("M");
        s.setPassword("test1234");
        s.setBirthdate("2003-10-05");

        assertDoesNotThrow(() -> api.updateStudent(s));
    }

    @Test
    public void testUpdateStudent2() {
        API api = new API();

        Student s = new Student();
        s.setId("108");
        s.setFirst_name("Rish");
        s.setLast_name("Sangham");
        s.setMajor("SE");
        s.setCohort(1101);
        s.setGender("F");
        s.setPassword("test1234");
        s.setBirthdate("2005-10-05");
        assertDoesNotThrow(() -> api.updateStudent(s));
    }

    @Test
    public void testUpdateStudent3() {
        API api = new API();

        Student s = new Student();
        s.setId("109");
        s.setFirst_name("Sherr");
        s.setLast_name("Sodipo");
        s.setMajor("SE");
        s.setCohort(1101);
        s.setGender("F");
        s.setPassword("test1234");
        s.setBirthdate("2005-10-05");

        assertDoesNotThrow(() -> api.updateStudent(s));
    }

    @Test
    public void testUpdateStudent4() {
        API api = new API();

        Student s = new Student();
        s.setId("110");
        s.setFirst_name("Sahkuntala");
        s.setLast_name("Ramdhiansing");
        s.setMajor("SE");
        s.setCohort(1011);
        s.setGender("M");
        s.setPassword("test1234");
        s.setBirthdate("2003-10-05");
        assertDoesNotThrow(() -> api.updateStudent(s));
    }
}
