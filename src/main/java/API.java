import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.*;
import java.util.stream.Collectors;

public class API {
    private static final String BASE_URL = "https://trajectplannerapi.dulamari.com";
    private static final String STUDENTS_ENDPOINT = "/students";
    private static final String SEMESTERS_ENDPOINT = "/semesters";
    private static final String SCORES_ENDPOINT = "/scores";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    // Methode om studenten op te halen
    public static List<Student> getStudents() throws Exception {
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
    public static List<Semester> getSemesters() throws Exception {
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
    public static List<Grade> getGrades() throws Exception {
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


    public static List<Exam> getExams() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/exams"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Debug output
        System.out.println("API response: " + response.body());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), new TypeToken<List<Exam>>(){}.getType());
        } else {
            throw new RuntimeException("Failed to fetch exams: " + response.statusCode());
        }
    }
    public static String updateExam(Exam exam) throws Exception {
        try {
            // 1. Debug: Toon alle relevante informatie
            System.out.println("Exam being updated: " + exam.getId());
            System.out.println("Current course_id: " + exam.getCourseId());
            System.out.println("Current course_name: " + exam.getCourseName());

            // 2. Bereid request voor
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("exam_id", exam.getId());

            // Belangrijke aanpassing: probeer beide varianten
            if (exam.getCourseId() == 1) {
                // Speciale behandeling voor course 1
                requestBody.put("code", 1);  // als integer
                requestBody.put("course_id", 1); // dubbele velden
            } else {
                requestBody.put("code", exam.getCourseId());
            }

            requestBody.put("exam_type", exam.getType());
            requestBody.put("exam_datetime", exam.getDate().split(" ")[0]);

            // 3. Voeg optionele velden toe die mogelijk vereist zijn
            requestBody.put("semester", exam.getSemester());
            requestBody.put("course_name", exam.getCourseName());

            String jsonBody = gson.toJson(requestBody);
            System.out.println("Final JSON being sent:\n" + jsonBody);

            // 4. Verstuur request met extra headers
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/exams"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 5. Uitgebreide response logging
            System.out.println("HTTP Status: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() != 200) {
                throw new RuntimeException("API Error: " + response.body());
            }
            return response.body();
        } catch (Exception e) {
            System.err.println("Critical error during update:");
            e.printStackTrace();
            throw e;
        }
    }

    public static String deleteExam(int examId) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/exams/" + examId)) // Endpoint met exam ID
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Delete response: " + response.body());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Delete failed with status: " + response.statusCode());
            }
            return response.body();
        } catch (Exception e) {
            System.err.println("Error deleting exam: " + e.getMessage());
            throw e;
        }
    }

    public static List<Course> getCourses() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/courses")) // Pas aan naar juiste endpoint
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), new TypeToken<List<Course>>(){}.getType());
        } else {
            throw new RuntimeException("Failed to fetch courses: " + response.statusCode());
        }
    }

    // Methode om cijfers met studentnamen op te halen
    public static List<Grade> getScoresWithStudentNames() throws Exception {
        // Haal de cijfers op
        List<Grade> scores = getGrades();

        // Haal de studenten op
        List<Student> students = getStudents();

        // Maak een map van student_id naar studentnaam voor snelle opzoekingen
        Map<String, String> studentIdToNameMap = new HashMap<>();
        for (Student student : students) {
            studentIdToNameMap.put(student.getId(), student.getFirstname() + " " + student.getLastname());
        }

        // Voeg de volledige naam toe aan elke score
        for (Grade grade : scores) {
            String fullName = studentIdToNameMap.get(grade.getStudent_id());
            grade.setStudent_full_name(fullName);
        }

        return scores;
    }

}