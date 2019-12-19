package ua.org.zagoruiko.expenses.goalsservice.service;

import ua.org.zagoruiko.expenses.goalsservice.dto.LimitDTO;

import java.util.List;

public interface LimitsService {
    LimitDTO setLimit(LimitDTO limit);
    LimitDTO getLimit(int year, int month, String category, String family);
    List<LimitDTO> getLimits(int year, int month, String family);
}
