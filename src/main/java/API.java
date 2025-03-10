import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.*;

public class API {
    private static final String BASE_URL = "https://trajectplannerapi.dulamari.com";
    private static final String STUDENTS_ENDPOINT = "/students";
    private static final String SEMESTERS_ENDPOINT = "/semesters";
    private static final String SCORES_ENDPOINT = "/scores";

    private static final HttpClient client = HttpClient.newHttpClient();

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