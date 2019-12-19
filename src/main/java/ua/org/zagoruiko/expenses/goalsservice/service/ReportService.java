package ua.org.zagoruiko.expenses.goalsservice.service;

import ua.org.zagoruiko.expenses.goalsservice.dto.ReportItemDTO;

import java.util.List;

public interface ReportService {
    List<ReportItemDTO> currentMonthReport();
    List<ReportItemDTO> currentYearByMonthReport();
    List<ReportItemDTO> currentYearTotalReport();
    List<ReportItemDTO> currentMonthReport(List<String> categories);
    List<ReportItemDTO> currentYearTotalReport(List<String> categories);
}
