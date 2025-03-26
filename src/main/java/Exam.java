import com.google.gson.annotations.SerializedName;

public class Exam {
    private int id;

    @SerializedName("course_id")
    private int courseId;

    @SerializedName("course_name")
    private String courseName;

    private int semester;
    private String type;
    private String date;

    // Getters en setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}