package models;

public class Course {
    private String id;                // Vroeger: course_id
    private String name;              // Vroeger: course_name
    private String ec;
    private String code;              // Vroeger: course_code
    private String description;       // Vroeger: course_description
    private String block;

    // Getters en setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEc() { return ec; }
    public void setEc(String ec) { this.ec = ec; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBlock() { return block; }
    public void setBlock(String block) { this.block = block; }
}
