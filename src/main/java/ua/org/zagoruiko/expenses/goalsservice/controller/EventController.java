package ua.org.zagoruiko.expenses.goalsservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.org.zagoruiko.expenses.goalsservice.dto.*;
import ua.org.zagoruiko.expenses.goalsservice.service.ReportService;
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
        UserDTO userDTO = this.userService.findUser(startEvent.getUserId());

        if (userDTO == null) {
            userDTO = this.userService.registerUser(
                    new UserDTO(startEvent.getUserId(), null, startEvent.getUserName()));
            if (userDTO != null) {
                return new StartResponseDTO(userDTO.getId(), BotStartState.FAMILY_REQUIRED);
            }
            throw new RuntimeException("For some reason user was not updated");
        }

        else if (userDTO.getFamily() == null) {
            return new StartResponseDTO(userDTO.getId(), BotStartState.FAMILY_REQUIRED);
        }
        return new StartResponseDTO(userDTO.getId(), BotStartState.ALREADY_REGISTERED);
    }

    @RequestMapping(value = "/event/bot/family", method = RequestMethod.POST)
    public StartResponseDTO setFamily(@RequestBody BotSetFamilyEventDTO setFamilyEvent) {
        UserDTO userDTO = this.userService.findUser(setFamilyEvent.getUserId());

        if (userDTO == null) {
            throw new RuntimeException("For some reason unregistered user tries to set family");
        }

        userDTO = this.userService.updateUser(new UserDTO(userDTO.getId(),
                setFamilyEvent.getFamily(),
                userDTO.getName()));

        if (userDTO != null && userDTO.getFamily() != null && !userDTO.getFamily().isEmpty()) {
            return new StartResponseDTO(userDTO.getId(), BotStartState.REGISTERED);
        }
        throw new RuntimeException("For some reason user was not updated");
    }

    @RequestMapping(value = "/event/bot/report", method = RequestMethod.POST)
    public List<ReportItemDTO> currentMonthReport(@RequestBody BotStartedEventDTO request) {
        List<ReportItemDTO> rep = this.reportService.currentMonthReport();
        return rep;
    }

    @RequestMapping(value = "/event/bot/report/family", method = RequestMethod.POST)
    public List<UserDTO> currentMonthFamilyReport(@RequestBody FamilyReportRequestEventDTO request) {
        List<UserDTO> users = this.userService.findFamilyUsers(request.getFamily());
        return users;
    }

}
