package ua.org.zagoruiko.expenses.goalsservice.dao;

import ua.org.zagoruiko.expenses.goalsservice.model.Limit;
import ua.org.zagoruiko.expenses.goalsservice.model.LimitReportItem;

import java.util.List;

public interface LimitsDao {
    Limit setLimit(Limit limit);
    Limit getLimit(int year, int month, String category, String family);
    List<Limit> getLimits(int year, int month, String family);
    List<LimitReportItem> getLimitReport(int year, int month, String family);
}
