package ua.org.zagoruiko.expenses.goalsservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ua.org.zagoruiko.expenses.goalsservice.dto.*;
import ua.org.zagoruiko.expenses.goalsservice.service.ReportService;
import ua.org.zagoruiko.expenses.goalsservice.service.TheBot;
import ua.org.zagoruiko.expenses.goalsservice.service.UserService;

import java.util.List;


@RestController
public class EventController {
    private final class Accumulator {
        private int total = 0;

        public void add(int what) {
            this.total += what;
        }

        public int getTotal() {
            return total;
        }
    }

    @Autowired
    private TheBot bot;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;



    private StringBuilder buildReportMessage(List<ReportItemDTO> rep, String greetings) {
        StringBuilder sb = new StringBuilder(greetings + "\n```\n");
        Accumulator total = new Accumulator();
        int max = rep.stream().mapToInt(c -> c.getCategory().length()).max().getAsInt();
        rep.stream().forEach(r -> {
            total.add(r.getAmount());
            StringBuilder spaces = new StringBuilder(r.getCategory());
            while (spaces.length() < max) {
                spaces.append(' ');
            }
            sb.append(spaces.toString() + "\t: " + r.getAmount() + "\n");
        });
        sb.append("```\n**Заголом: " + total.getTotal() + "**\n");
        return sb;
    }

    @RequestMapping(value = "/event/bot/start", method = RequestMethod.POST)
    public StartResponseDTO start(@RequestBody BotStartedEventDTO startEvent) {
        ApiContextInitializer.init();
        SendMessage message = new SendMessage()
                .setChatId(startEvent.getChatId());

        UserDTO userDTO = this.userService.findUser(startEvent.getUserId());

        if (userDTO == null) {
            userDTO = this.userService.registerUser(
                    new UserDTO(startEvent.getUserId(), null, startEvent.getUserName()));
            if (userDTO != null) {
                this.bot.send(
                        message.setText("Service said: Enter your family to join"));
                return new StartResponseDTO(userDTO.getId(), BotStartState.FAMILY_REQUIRED);
            }
            this.bot.send(
                    message.setText("Service said: Tried to register user but it wasn't saved for some reason"));
            throw new RuntimeException("For some reason user was not updated");
        }

        else if (userDTO.getFamily() == null) {
            this.bot.send(
                    message.setText("Service said: Enter your family to join"));
            return new StartResponseDTO(userDTO.getId(), BotStartState.FAMILY_REQUIRED);
        }
        this.bot.send(
                message.setText("Service said: Already registered"));
        return new StartResponseDTO(userDTO.getId(), BotStartState.ALREADY_REGISTERED);
    }

    @RequestMapping(value = "/event/bot/family", method = RequestMethod.POST)
    public StartResponseDTO setFamily(@RequestBody BotSetFamilyEventDTO setFamilyEvent) {
        ApiContextInitializer.init();
        SendMessage message = new SendMessage()
                .setChatId(setFamilyEvent.getChatId());

        UserDTO userDTO = this.userService.findUser(setFamilyEvent.getUserId());

        if (userDTO == null) {
            this.bot.send(
                    message.setText("Service said: For some reason unregistered user tries to set family"));
            throw new RuntimeException("For some reason unregistered user tries to set family");
        }

        userDTO = this.userService.updateUser(new UserDTO(userDTO.getId(),
                setFamilyEvent.getFamily(),
                userDTO.getName()));

        if (userDTO != null && userDTO.getFamily() != null && !userDTO.getFamily().isEmpty()) {
            return new StartResponseDTO(userDTO.getId(), BotStartState.REGISTERED);
        }
        this.bot.send(
                message.setText("Service said: Tried to set a family for the user but it wasn't saved for some reason"));
        throw new RuntimeException("For some reason user was not updated");
    }

    @RequestMapping(value = "/event/bot/report", method = RequestMethod.POST)
    public List<ReportItemDTO> currentMonthReport(@RequestBody BotStartedEventDTO request) {
        ApiContextInitializer.init();
        UserDTO user = this.userService.findUser(request.getUserId());
        SendMessage message = new SendMessage()
                .setChatId(user.getId());
        List<ReportItemDTO> rep = this.reportService.currentMonthReport();
        StringBuilder sb = buildReportMessage(rep, "Шалений репорт на поточний мiсяць:");
        this.bot.send(message.setText(sb.toString()).setParseMode(ParseMode.MARKDOWN));
        return rep;
    }

    @RequestMapping(value = "/event/bot/report/family", method = RequestMethod.POST)
    public List<UserDTO> currentMonthFamilyReport(@RequestBody FamilyReportRequestEventDTO request) {
        ApiContextInitializer.init();
        List<UserDTO> users = this.userService.findFamilyUsers(request.getFamily());
        List<ReportItemDTO> rep = this.reportService.currentMonthReport();
        users.forEach(user -> {
            SendMessage message = new SendMessage()
                    .setChatId(user.getId());
            StringBuilder sb = buildReportMessage(rep,
                    "Шановний/Шановна " + user.getName() + "!\n" +  request.getMessage());
            this.bot.send(message.setText(sb.toString()).setParseMode(ParseMode.MARKDOWN));
        });

        return users;
    }

}
