package models;

public class Student {
    private String id;
    private String first_name;
    private String last_name;
    private String student_number;
    private String password;
    private int total_ec;
    private String gender;
    private String birthdate;
    private String major;
    private int cohort;

    // Getters en setters
    public String getId() { return id; }
    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }

    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    public String getStudent_number() { return student_number; }
    public void setStudent_number(String student_number) { this.student_number = student_number; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getTotal_ec() { return total_ec; }
    public void setTotal_ec(int total_ec) { this.total_ec = total_ec; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public int getCohort() { return cohort; }
    public void setCohort(int cohort) { this.cohort = cohort; }
}