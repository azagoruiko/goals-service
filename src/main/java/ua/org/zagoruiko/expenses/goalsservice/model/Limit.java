package ua.org.zagoruiko.expenses.goalsservice.model;

public class Limit {
    private int year;
    private int month;
    private String category;
    private String family;
    private int limit;

    public Limit(int year, int month, String category, String family, int limit) {
        this.year = year;
        this.month = month;
        this.category = category;
        this.family = family;
        this.limit = limit;
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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }
}
