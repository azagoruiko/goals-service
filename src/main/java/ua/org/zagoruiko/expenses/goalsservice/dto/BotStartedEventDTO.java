package ua.org.zagoruiko.expenses.goalsservice.dto;

import java.io.Serializable;

public class BotStartedEventDTO implements Serializable {
    private String chatId;
    private String userId;
    private String userName;

    public BotStartedEventDTO(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
