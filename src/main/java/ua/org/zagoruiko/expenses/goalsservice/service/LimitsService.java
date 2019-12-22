package ua.org.zagoruiko.expenses.goalsservice.service;

import org.springframework.transaction.annotation.Transactional;
import ua.org.zagoruiko.expenses.goalsservice.dto.LimitDTO;
import ua.org.zagoruiko.expenses.goalsservice.dto.LimitReportItemDTO;

import java.util.List;

public interface LimitsService {
    LimitDTO setLimit(LimitDTO limit);

    LimitDTO setMonthlyLimit(LimitDTO limit);

    LimitDTO getLimit(int year, int month, String category, String family);
    List<LimitDTO> getLimits(int year, int month, String family);
    List<LimitReportItemDTO> getLimitReport(int year, int month, String family);
    void renewLimits(List<LimitReportItemDTO> oldLimits, String family);

    @Transactional
    LimitReportItemDTO getMonthlyLimitStatus(int year, int month, String family);

    void renewMonthlyLimit(LimitReportItemDTO oldLimit, String family);
}
