package models;

public class Exam {
    //gemaakte instance fields
    private int id;
    private int course_id;
    private String course_name;
    private int semester;
    private String type;
    private String date;

// Deze getters en setters zorgen ervoor dat de waarden van een Exam-object veilig kunnen worden ingesteld en opgehaald.
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCourse_id() { return course_id; }
    public void setCourse_id(int course_id) { this.course_id = course_id; }
    public String getCourse_name() { return course_name; }
    public void setCourse_name(String course_name) { this.course_name = course_name; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    @Override
    public String toString() {
        return "models.Exam{" +
                "id=" + id +
                ", course_id=" + course_id +
                ", course_name='" + course_name + '\'' +
                ", semester=" + semester +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}