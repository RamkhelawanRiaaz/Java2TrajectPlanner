package models;

public class Course {
    //gemaakte instance fields
    private String id;
    private String name;
    private String ec;
    private String code;
    private String description;
    private String block;

// Deze getters en setters zorgen ervoor dat de waarden van een Course-object veilig kunnen worden ingesteld en opgehaald.
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
