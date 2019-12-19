package ua.org.zagoruiko.expenses.goalsservice.dto;

public class ReportItemDTO {
    Integer year;
    Integer month;
    Integer amount;
    String category;

    public ReportItemDTO(Integer year, Integer month, Integer amount, String category) {
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
