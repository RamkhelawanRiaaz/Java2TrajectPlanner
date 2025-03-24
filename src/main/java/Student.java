public class Student {
    private String id;
    private String first_name;  // Overeenkomstig met API
    private String last_name;   // Overeenkomstig met API
    private String student_number; // Overeenkomstig met API
    private String password;
    private int total_ec;       // Overeenkomstig met API
    private String gender;
    private String birthdate;

    // Getters en setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstname() { return first_name; }
    public void setFirstname(String first_name) { this.first_name = first_name; }

    public String getLastname() { return last_name; }
    public void setLastname(String last_name) { this.last_name = last_name; }

    public String getStudentnumber() { return student_number; }
    public void setStudentnumber(String student_number) { this.student_number = student_number; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getTotalEc() { return total_ec; }
    public void setTotalEc(int total_ec) { this.total_ec = total_ec; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }
}