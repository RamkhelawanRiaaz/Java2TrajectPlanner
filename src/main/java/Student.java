public class Student {
    private String id;  // Moet een String zijn
    private String firstname;
    private String lastname;
    private String studentnumber;
    private String gender;
    private String birthdate;
    private boolean admin;

    // Getters en setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getStudentnumber() { return studentnumber; }
    public void setStudentnumber(String studentnumber) { this.studentnumber = studentnumber; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }
    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", studentnumber='" + studentnumber + '\'' +
                ", gender='" + gender + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", admin=" + admin +
                '}';
    }
}