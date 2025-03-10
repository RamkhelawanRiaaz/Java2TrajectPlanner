public class Student {
    private String id;  // Moet een String zijn
    private String first_name;
    private String last_name;
    private String student_number;
    private String gender;
    private String birthdate;
    private boolean admin;

    // Getters en setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFirstname() { return first_name; }
    public void setFirstname(String first_name) { this.first_name = first_name; }
    public String getLastname() { return last_name; }
    public void setLastname(String last_name) { this.last_name = last_name; }
    public String getStudentnumber() { return student_number; }
    public void setStudentnumber(String student_number) { this.student_number = student_number; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }
    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }


}