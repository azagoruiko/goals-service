package ua.org.zagoruiko.expenses.goalsservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TheBot extends TelegramLongPollingBot {
    private final String token;

    public TheBot(@Value("${BOT_TOKEN}") String token) {
        super();
        this.token = token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // TODO
    }

    @Override
    public String getBotUsername() {
        return "Шалений Бухгалтер";
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    public void send(SendMessage message) {
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
