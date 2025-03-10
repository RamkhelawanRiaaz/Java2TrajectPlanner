public class Course {
    private String course_id;
    private String course_name;
    private String ec;
    private String course_code;
    private String course_description;
    private String block;

    // Getters en setters
    public String getCourse_id() { return course_id; }
    public void setCourse_id(String course_id) { this.course_id = course_id; }
    public String getCourse_name() { return course_name; }
    public void setCourse_name(String course_name) { this.course_name = course_name; }
    public String getEc() { return ec; }
    public void setEc(String ec) { this.ec = ec; }
    public String getCourse_code() { return course_code; }
    public void setCourse_code(String course_code) { this.course_code = course_code; }
    public String getCourse_description() { return course_description; }
    public void setCourse_description(String course_description) { this.course_description = course_description; }
    public String getBlock() { return block; }
    public void setBlock(String block) { this.block = block; }

    @Override
    public String toString() {
        return "Course{" +
                "course_id='" + course_id + '\'' +
                ", course_name='" + course_name + '\'' +
                ", ec='" + ec + '\'' +
                ", course_code='" + course_code + '\'' +
                ", course_description='" + course_description + '\'' +
                ", block='" + block + '\'' +
                '}';
    }
}