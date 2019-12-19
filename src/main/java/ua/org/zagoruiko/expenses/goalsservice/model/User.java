package ua.org.zagoruiko.expenses.goalsservice.model;

public class User {
    private Long id;
    private String family;
    private String name;

    public User(Long id, String family, String name) {
        this.id = id;
        this.family = family;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
