package ua.org.zagoruiko.expenses.goalsservice.dto;

public class StartResponseDTO {
    private String userId;
    private BotStartState botStartState;

    public StartResponseDTO(String userId, BotStartState botStartState) {
        this.userId = userId;
        this.botStartState = botStartState;
    }

    public String getUserId() {
        return userId;
    }

    public BotStartState getBotStartState() {
        return botStartState;
    }
}
