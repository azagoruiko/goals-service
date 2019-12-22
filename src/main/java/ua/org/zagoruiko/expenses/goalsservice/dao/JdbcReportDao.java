package ua.org.zagoruiko.expenses.goalsservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.org.zagoruiko.expenses.goalsservice.model.ReportItem;

import java.util.List;

@Repository
public class JdbcReportDao implements ReportDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcReportDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ReportItem> currentMonthReport() {
        return this.jdbcTemplate.query("SELECT (date_part('month', transaction_date)) AS \"month\",\n" +
                "       (date_part('year', transaction_date)) AS \"year\",\n" +
                "                 round(sum(amount)) AS \"amount\",\n" +
                "                 category\n" +
                "          FROM expenses.transactions\n" +
                "          WHERE date_part('month', transaction_date) = date_part('month', current_timestamp)\n" +
                "          AND   date_part('year', transaction_date) = date_part('year', current_timestamp)\n" +
                "          AND category <> 'INCOME'\n" +
                "          GROUP BY month, year, category\n" +
                "          ORDER BY amount, year, month DESC;", new Object[] {},
                (rs, rowNum) -> new ReportItem(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4)
                ));
    }

    @Override
    public List<ReportItem> currentYearByMonthReport() {
        return this.jdbcTemplate.query("SELECT (date_part('month', transaction_date)) AS \"month\",\n" +
                "       (date_part('year', transaction_date)) AS \"year\",\n" +
                "                 round(sum(amount)) AS \"amount\",\n" +
                "                 category\n" +
                "          FROM expenses.transactions\n" +
                "          WHERE date_part('year', transaction_date) = date_part('year', current_timestamp)\n" +
                "          AND category <> 'INCOME'\n" +
                "          GROUP BY month, year, category\n" +
                "          ORDER BY amount,year, month DESC;", new Object[] {},
                (rs, rowNum) -> new ReportItem(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4)
                ));
    }

    @Override
    public List<ReportItem> currentYearTotalReport() {
        return null;
    }

    @Override
    public List<ReportItem> currentMonthReport(List<String> categories) {
        return null;
    }

    @Override
    public List<ReportItem> currentYearTotalReport(List<String> categories) {
        return null;
    }
}
