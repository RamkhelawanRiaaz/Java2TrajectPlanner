package tests;

import API_calls.API;
import models.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//Met deze test worden de namen  van de groepsleden van dit project opgelsagen in de API (test voor presentatie voor mr. Pierpont)
public class AddStudentTest {

    @Test
    public void addStudentTest1() {
        //Object maken van API calls
        API api = new API();

        //Object maken van Student model
        Student s = new Student();
        //data zetten in parameters van Student model
        s.setFirst_name("Riaaz");
        s.setLast_name("Ramkhelawan");
        s.setMajor("SE");
        s.setCohort(1101);
        s.setGender("M");
        s.setPassword("test1234");
        s.setBirthdate("2003-10-05");

        //Student object sturen naar api.postStudent
        assertDoesNotThrow(() -> api.postStudent(s));
    }

    @Test
    public void addStudentTest2() {
        //Object maken van API calls
        API api = new API();

        //Object maken van Student model
        Student s = new Student();
        //data zetten in parameters van Student model
        s.setFirst_name("Rishika");
        s.setLast_name("Sangham");
        s.setMajor("SE");
        s.setCohort(1101);
        s.setGender("F");
        s.setPassword("test1234");
        s.setBirthdate("2005-10-05");

        //Student object sturen naar api.postStudent
        assertDoesNotThrow(() -> api.postStudent(s));
    }

    @Test
    public void addStudentTest3() {
        //Object maken van API calls
        API api = new API();

        //Object maken van Student model
        Student s = new Student();
        //data zetten in parameters van Student model
        s.setFirst_name("Sherreskly");
        s.setLast_name("Sodipo");
        s.setMajor("SE");
        s.setCohort(1101);
        s.setGender("F");
        s.setPassword("test1234");
        s.setBirthdate("2005-10-05");

        //Student object sturen naar api.postStudent
        assertDoesNotThrow(() -> api.postStudent(s));
    }

    @Test
    public void addStudentTest4() {
        //Object maken van API calls
        API api = new API();

        //Object maken van Student model
        Student s = new Student();
        //data zetten in parameters van Student model
        s.setFirst_name("Shakeel");
        s.setLast_name("Ramdhiansing");
        s.setMajor("SE");
        s.setCohort(1101);
        s.setGender("M");
        s.setPassword("test1234");
        s.setBirthdate("2003-10-05");

        //Student object sturen naar api.postStudent
        assertDoesNotThrow(() -> api.postStudent(s));
    }
}
