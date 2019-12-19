package ua.org.zagoruiko.expenses.goalsservice.dto;

public class UserDTO {
    private final String id;
    private final String family;
    private final String name;

    public UserDTO(String id, String family, String name) {
        this.id = id;
        this.family = family;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getFamily() {
        return family;
    }

    public String getName() {
        return name;
    }
}
