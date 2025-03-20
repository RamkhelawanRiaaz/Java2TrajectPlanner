public class Grade {
    private String id;
    private String student_id;
    private String student_number;
    private String exam_id;
    private String course_name;
    private String score_value;
    private String score_datetime;
    private String student_full_name;
    private Semester semester; // Nieuw veld voor semesterinformatie

    // Getters en setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStudent_id() { return student_id; }
    public void setStudent_id(String student_id) { this.student_id = student_id; }
    public String getStudent_number() { return student_number; }
    public void setStudent_number(String student_number) { this.student_number = student_number; }
    public String getExam_id() { return exam_id; }
    public void setExam_id(String exam_id) { this.exam_id = exam_id; }
    public String getCourse_name() { return course_name; }
    public void setCourse_name(String course_name) { this.course_name = course_name; }
    public String getScore_value() { return score_value; }
    public void setScore_value(String score_value) { this.score_value = score_value; }
    public String getScore_datetime() { return score_datetime; }
    public void setScore_datetime(String score_datetime) { this.score_datetime = score_datetime; }
    public String getStudent_full_name() { return student_full_name; }
    public void setStudent_full_name(String student_full_name) { this.student_full_name = student_full_name; }
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }

    @Override
    public String toString() {
        return "Grade{" +
                "id='" + id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", student_number='" + student_number + '\'' +
                ", exam_id='" + exam_id + '\'' +
                ", course_name='" + course_name + '\'' +
                ", score_value='" + score_value + '\'' +
                ", score_datetime='" + score_datetime + '\'' +
                ", student_full_name='" + student_full_name + '\'' +
                ", semester=" + semester +
                '}';
    }
}