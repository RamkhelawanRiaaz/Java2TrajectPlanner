package API_calls;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Student;

import java.nio.charset.StandardCharsets;
import java.util.*;
import models.*;

public class API implements SchoolAdmin {
    private  final String BASE_URL = "https://trajectplannerapi.dulamari.com";
    private  final String STUDENTS_ENDPOINT = "/students";
    private  final String SEMESTERS_ENDPOINT = "/semesters";
    private  final String SCORES_ENDPOINT = "/scores";
    private  final String EXAMS_ENDPOINT = "/exams";
    private  final String COURSES_ENDPOINT = "/courses";

    private  final HttpClient client = HttpClient.newHttpClient();

    // Methode om studenten op te halen
    public  List<Student> getStudents() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + STUDENTS_ENDPOINT))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            return gson.fromJson(response.body(), new TypeToken<List<Student>>() {}.getType());
        } else {
            throw new RuntimeException("Failed to fetch students: " + response.statusCode());
        }
    }

    // Methode om semesters op te halen
    public  List<Semester> getSemesters() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + SEMESTERS_ENDPOINT))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            return gson.fromJson(response.body(), new TypeToken<List<Semester>>() {}.getType());
        } else {
            throw new RuntimeException("Failed to fetch semesters: " + response.statusCode());
        }
    }

    // Methode om cijfers op te halen
    public  List<Grade> getGrades() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + SCORES_ENDPOINT))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            return gson.fromJson(response.body(), new TypeToken<List<Grade>>() {}.getType());
        } else {
            throw new RuntimeException("Failed to fetch grades: " + response.statusCode());
        }
    }

    public List<Course> getCourses() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + COURSES_ENDPOINT))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            return gson.fromJson(response.body(), new TypeToken<List<Course>>() {}.getType()); // ✅ fix hier
        } else {
            throw new RuntimeException("Failed to fetch courses: " + response.statusCode());
        }
    }

    //Methoden om cijfers op te halen
    public List<Tentamen> getTentamens() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + EXAMS_ENDPOINT))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            return gson.fromJson(response.body(), new TypeToken<List<Tentamen>>() {}.getType());
        } else {
            throw new RuntimeException("Failed to fetch tentamens: " + response.statusCode());
        }

    }

    // Methode om cijfers met studentnamen op te halen
    public List<Grade> getScoresWithStudentNames() throws Exception {
        // Haal de cijfers op
        List<Grade> scores = getGrades();

        // Haal de studenten op
        List<Student> students = getStudents();

        // Maak een map van student_id naar studentnaam voor snelle opzoekingen
        Map<String, String> studentIdToNameMap = new HashMap<>();
        for (Student student : students) {
            studentIdToNameMap.put(student.getId(), student.getFirst_name() + " " + student.getLast_name());
        }

        // Voeg de volledige naam toe aan elke score
        for (Grade grade : scores) {
            String fullName = studentIdToNameMap.get(grade.getStudent_id());
            grade.setStudent_full_name(fullName);
        }

        return scores;
    }

    public  void postStudent(Student student){
        try{
            Gson gson = new Gson();
            String json = gson.toJson(student);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + STUDENTS_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                System.out.println("models.Student successfully added.");
            } else {
                System.out.println("Failed to add student. Status code: " + response.statusCode());
                System.out.println("Response body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void postTentamen(Tentamen tentamen){
        try{
            Gson gson = new Gson();
            String json = gson.toJson(tentamen);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + EXAMS_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                System.out.println("models.Tentamen successfully aangemaakt..");
            } else {
                System.out.println("Failed to add models.Tentamen. Status code: " + response.statusCode());
                System.out.println("Response body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void postCijfer(Grade cijfer){
        try{
            Gson gson = new Gson();
            String json = gson.toJson(cijfer);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + SCORES_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                System.out.println("Score succesvol toegevoegd.");
            } else {
                System.out.println("Fout bij toevoegen score. Code: " + response.statusCode());
                System.out.println("Response: " + response.body());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void deleteStudent(String studentId) throws Exception {
        String jsonInputString = "{\"student_id\": \"" + studentId + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + STUDENTS_ENDPOINT))
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(jsonInputString, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Verwijderen van student mislukt: " + response.body());
        }
    }

    public void updateStudent(Student student) throws Exception {
        Gson gson = new Gson();

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("student_id", student.getId());
        requestData.put("student_number", student.getStudent_number());
        requestData.put("first_name", student.getFirst_name());
        requestData.put("last_name", student.getLast_name());
        requestData.put("password", student.getPassword());
        requestData.put("total_ec", student.getTotal_ec());
        requestData.put("gender", student.getGender());
        requestData.put("birthdate", student.getBirthdate());

        String json = gson.toJson(requestData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + STUDENTS_ENDPOINT))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Bijwerken van student mislukt: " + response.body());
        }
    }

    public void deleteGrade(int scoreId) throws Exception {
        String json = "{\"score_id\": " + scoreId + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + SCORES_ENDPOINT))
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Verwijderen van cijfer mislukt: " + response.body());
        }
    }

    public void updateGrade(Grade grade) throws Exception {
        Map<String, Object> requestData = new HashMap<>();

        if (grade.getStudent_id() != 0) {
            requestData.put("student_id", grade.getStudent_id());
        } else if (grade.getStudent_number() != null && !grade.getStudent_number().isEmpty()) {
            requestData.put("student_number", grade.getStudent_number());
        } else {
            throw new IllegalArgumentException("Student_id of student_number is verplicht.");
        }

        requestData.put("score_id", grade.getId());
        requestData.put("exam_id", grade.getExam_id());
        requestData.put("score_value", grade.getScore_value());

        if (grade.getScore_datetime() != null && !grade.getScore_datetime().isEmpty()) {
            requestData.put("score_datetime", grade.getScore_datetime());
        }

        Gson gson = new Gson();
        String json = gson.toJson(requestData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + SCORES_ENDPOINT))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Bijwerken van cijfer mislukt: " + response.body());
        }
    }

    public void deleteTentamen(String examId) throws Exception {
        if (examId == null || examId.trim().isEmpty()) {
            throw new IllegalArgumentException("models.Exam ID is leeg of null.");
        }

        String json = "{\"exam_id\": " + Integer.parseInt(examId.trim()) + "}";

        System.out.println("[DEBUG] DELETE tentamen JSON payload: " + json);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + EXAMS_ENDPOINT))
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("[DEBUG] Response status: " + response.statusCode());
        System.out.println("[DEBUG] Response body: " + response.body());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Verwijderen van tentamen mislukt: " + response.body());
        }
    }




    public void updateTentamen(Tentamen tentamen) throws Exception {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("exam_id", tentamen.getId());

        if (tentamen.getExam_type() != null && !tentamen.getExam_type().isEmpty()) {
            requestData.put("exam_type", tentamen.getExam_type());
        }

        if (tentamen.getExam_date() != null && !tentamen.getExam_date().isEmpty()) {
            requestData.put("EXM_DATETIME", tentamen.getExam_date());
        }

        Gson gson = new Gson();
        String json = gson.toJson(requestData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + EXAMS_ENDPOINT))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Bijwerken van tentamen mislukt: " + response.body());
        }
    }
}