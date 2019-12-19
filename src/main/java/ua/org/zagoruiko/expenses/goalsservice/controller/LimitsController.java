package ua.org.zagoruiko.expenses.goalsservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ua.org.zagoruiko.expenses.goalsservice.dto.*;
import ua.org.zagoruiko.expenses.goalsservice.service.LimitsService;
import ua.org.zagoruiko.expenses.goalsservice.service.ReportService;
import ua.org.zagoruiko.expenses.goalsservice.service.TheBot;
import ua.org.zagoruiko.expenses.goalsservice.service.UserService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RestController
public class LimitsController {
    @Autowired
    private TheBot bot;

    @Autowired
    private LimitsService limitsService;


    @RequestMapping(value = "/limits", method = RequestMethod.PUT)
    public LimitDTO put(@RequestBody LimitDTO limit) {
        Calendar date = Calendar.getInstance();
        limit.setYear(date.get(Calendar.YEAR));
        limit.setMonth(date.get(Calendar.MONTH));
        return this.limitsService.setLimit(limit);
    }

    @RequestMapping(value = "/limits/{family}/{category}", method = RequestMethod.GET)
    public LimitDTO get(@PathVariable String family,
                        @PathVariable String category) {
        Calendar date = Calendar.getInstance();
        return this.limitsService.getLimit(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                category, family);

    }

    @RequestMapping(value = "/limits/{family}", method = RequestMethod.GET)
    public List<LimitDTO> get(@PathVariable String family) {
        Calendar date = Calendar.getInstance();
        return this.limitsService.getLimits(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                family);

    }

}
