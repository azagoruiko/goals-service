package ua.org.zagoruiko.expenses.goalsservice.dto;

import java.io.Serializable;

public class FamilyReportRequestEventDTO implements Serializable {
    private String family;
    private String message;

    public FamilyReportRequestEventDTO(String family, String message) {
        this.family = family;
        this.message = message;
    }

    public String getFamily() {
        return family;
    }

    public String getMessage() {
        return message;
    }
}
