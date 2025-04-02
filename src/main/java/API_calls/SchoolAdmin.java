package API_calls;
import models.*;

import java.util.List;
// interface: blueprint die de child classes moeten volgen
public interface SchoolAdmin {
// get methods: om data op te halen
    List<Student> getStudents() throws Exception;

    List<Semester> getSemesters() throws Exception;

    List<Grade> getGrades() throws Exception;

    List<Grade> getScoresWithStudentNames() throws Exception;

    List<Course> getCourses() throws Exception;

    List<Tentamen> getTentamens() throws Exception;

    // post, update, delete methods
    void postStudent(Student student);

    void updateStudent(Student student) throws Exception;

    void deleteStudent(String studentId) throws Exception;

    void postTentamen(Tentamen tentamen);

    void updateTentamen(Tentamen tentamen) throws Exception;

    void deleteTentamen(String examId) throws Exception;

    void postCijfer(Grade cijfer);

    void updateGrade(Grade grade) throws Exception;

    void deleteGrade(int scoreId) throws Exception;
}
