package ua.org.zagoruiko.expenses.goalsservice.model;

public class ReportItem {
    Integer year;
    Integer month;
    Integer amount;
    String category;

    public ReportItem(Integer year, Integer month, Integer amount, String category) {
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.category = category;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }
}
