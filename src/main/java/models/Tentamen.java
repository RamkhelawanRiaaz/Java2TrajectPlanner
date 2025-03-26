package models;

public class Tentamen {
    private String id;
    private int course_id;
    private String code;
    private String exam_type;
    private String exam_date;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getCourse_id() { return course_id; }
    public void setCourse_id(int course_id) { this.course_id = course_id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getExam_type() { return exam_type; }
    public void setExam_type(String exam_type) { this.exam_type = exam_type; }

    public String getExam_date() { return exam_date; }
    public void setExam_date(String exam_date) { this.exam_date = exam_date; }
}



