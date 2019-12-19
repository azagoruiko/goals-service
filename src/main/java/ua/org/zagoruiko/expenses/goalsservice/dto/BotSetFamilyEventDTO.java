package ua.org.zagoruiko.expenses.goalsservice.dto;

import java.io.Serializable;

public class BotSetFamilyEventDTO extends BotStartedEventDTO implements Serializable {
    private String family;

    public BotSetFamilyEventDTO(String userId, String userName, String family) {
        super(userId, userName);
        this.family = family;
    }

    public String getFamily() {
        return family;
    }
}
