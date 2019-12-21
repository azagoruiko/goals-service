package ua.org.zagoruiko.expenses.goalsservice.dto;

public class LimitReportItemDTO {
    private int year;
    private int month;
    private String category;
    private int amount;
    private int limit;
    private int percent;

    public LimitReportItemDTO(int year, int month, String category, int amount, int limit, int percent) {
        this.year = year;
        this.month = month;
        this.category = category;
        this.amount = amount;
        this.limit = limit;
        this.percent = percent;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
