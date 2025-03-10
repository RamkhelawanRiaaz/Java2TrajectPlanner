import java.util.List;

public class Semester {
    private String id;
    private String semester_name;
    private List<Course> courses;

    // Getters en setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSemester_name() { return semester_name; }
    public void setSemester_name(String semester_name) { this.semester_name = semester_name; }
    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }

    @Override
    public String toString() {
        return "Semester{" +
                "id='" + id + '\'' +
                ", semester_name='" + semester_name + '\'' +
                ", courses=" + courses +
                '}';
    }
}