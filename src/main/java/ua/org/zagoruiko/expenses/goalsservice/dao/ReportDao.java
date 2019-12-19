package ua.org.zagoruiko.expenses.goalsservice.dao;

import ua.org.zagoruiko.expenses.goalsservice.model.ReportItem;
import ua.org.zagoruiko.expenses.goalsservice.model.User;

import java.util.List;

public interface ReportDao {
    List<ReportItem> currentMonthReport();
    List<ReportItem> currentYearByMonthReport();
    List<ReportItem> currentYearTotalReport();
    List<ReportItem> currentMonthReport(List<String> categories);
    List<ReportItem> currentYearTotalReport(List<String> categories);
}
