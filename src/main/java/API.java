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
import com.google.gson.JsonObject;
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

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), new TypeToken<List<Exam>>(){}.getType());
        } else {
            throw new RuntimeException("Failed to fetch exams: " + response.statusCode());
        }
    }

    public static String updateExam(Exam exam) throws Exception {
        try {
            if (exam.getType() == null && exam.getDate() == null) {
                throw new IllegalArgumentException("Minimaal één veld (type of datum) kiezen voor update");
            }
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("exam_id", exam.getId());

            if (exam.getCourseId() > 0) {
                requestBody.put("code", String.valueOf(exam.getCourseId())); // Als string
            }

            if (exam.getType() != null) {
                if (!exam.getType().equals("Regulier") && !exam.getType().equals("Her")) {
                    throw new IllegalArgumentException("Type moet 'Regulier' of 'Her' zijn");
                }
                requestBody.put("exam_type", exam.getType());
            }

            if (exam.getDate() != null) {
                // Controleer datumformaat
                if (!exam.getDate().matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) {
                    throw new IllegalArgumentException("Ongeldig datumformaat. Gebruik YYYY-MM-DD HH:MM:SS");
                }
                requestBody.put("exam_datetime", exam.getDate());
            }
            String jsonBody = gson.toJson(requestBody);
            System.out.println("Sending JSON to API: " + jsonBody);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/exams")) // Endpoint volgens specificatie
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("API response status: " + response.statusCode());
            System.out.println("API response body: " + response.body());

            switch (response.statusCode()) {
                case 200:
                    return response.body();
                case 400:
                    throw new RuntimeException("Bad Request: " + parseApiError(response.body()));
                case 404:
                    throw new RuntimeException("Exam not found: " + parseApiError(response.body()));
                case 500:
                    throw new RuntimeException("Server Error: " + parseApiError(response.body()));
                default:
                    throw new RuntimeException("Unexpected response: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Error in updateExam: " + e.getMessage());
            throw e;
        }
    }



    public static String deleteExam(int examId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/exams/" + examId))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Delete failed with status: " + response.statusCode());
        }
        return response.body();
    }


    public static List<Grade> getScoresWithStudentNames() throws Exception {
        List<Grade> scores = getGrades();
        List<Student> students = getStudents();

        Map<String, String> studentIdToNameMap = new HashMap<>();
        for (Student student : students) {
            studentIdToNameMap.put(student.getId(), student.getFirstname() + " " + student.getLastname());
        }
        for (Grade grade : scores) {
            String fullName = studentIdToNameMap.get(grade.getStudent_id());
            grade.setStudent_full_name(fullName);
        }
        return scores;
    }


    private static String parseApiError(String responseBody) {
        try {
            JsonObject errorResponse = gson.fromJson(responseBody, JsonObject.class);
            if (errorResponse.has("error")) {
                return errorResponse.get("error").getAsString();
            }
            if (errorResponse.has("message")) {
                return errorResponse.get("message").getAsString();
            }
        } catch (Exception e) {
        }
        return responseBody;
    }

}