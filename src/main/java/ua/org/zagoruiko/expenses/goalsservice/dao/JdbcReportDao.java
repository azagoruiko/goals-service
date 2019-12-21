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
                "                 CASE\n" +
                "                     WHEN ARRAY['TRAVEL'] && tags THEN 'TRAVEL'\n" +
                "                     WHEN ARRAY['HOBBY'] && tags THEN 'HOBBY'\n" +
                "                     WHEN ARRAY['HEALTHCARE'] && tags THEN 'HEALTHCARE'\n" +
                "                     WHEN ARRAY['KIDS'] && tags THEN 'KIDS'\n" +
                "                     WHEN \n" +
                "                         ARRAY['SUPERMARKET'] && tags\n" +
                "                         AND NOT ARRAY['ROZETKA'] && tags\n" +
                "                         THEN 'SUPERMARKET'\n" +
                "                     WHEN ARRAY['ROZETKA'] && tags THEN 'ROZETKA'\n" +
                "                     WHEN ARRAY['HOUSEHOLD'] && tags THEN 'HOUSEHOLD'\n" +
                "                     WHEN ARRAY['TRANSPORT'] && tags THEN 'TRANSPORT'\n" +
                "                     WHEN ARRAY['EAT_OUT'] && tags THEN 'EAT_OUT'\n" +
                "                     WHEN ARRAY['PARTY'] && tags THEN 'PARTY'\n" +
                "                     WHEN ARRAY['VAPE'] && tags THEN 'VAPE'\n" +
                "                     WHEN ARRAY['INCOME'] && tags THEN 'INCOME'\n" +
                "                     ELSE 'OTHER'\n" +
                "                     END as tag\n" +
                "          FROM expenses.transactions\n" +
                "          WHERE date_part('month', transaction_date) = date_part('month', current_timestamp)\n" +
                "          AND   date_part('year', transaction_date) = date_part('year', current_timestamp)\n" +
                "          AND NOT ARRAY['INCOME'] && tags\n" +
                "          GROUP BY month, year, tag\n" +
                "          ORDER BY year, month DESC;", new Object[] {},
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
                "                 CASE\n" +
                "                     WHEN ARRAY['TRAVEL'] && tags THEN 'TRAVEL'\n" +
                "                     WHEN ARRAY['HOBBY'] && tags THEN 'HOBBY'\n" +
                "                     WHEN ARRAY['HEALTHCARE'] && tags THEN 'HEALTHCARE'\n" +
                "                     WHEN ARRAY['KIDS'] && tags THEN 'KIDS'\n" +
                "                     WHEN \n" +
                "                         ARRAY['SUPERMARKET'] && tags\n" +
                "                         AND NOT ARRAY['ROZETKA'] && tags\n" +
                "                         THEN 'SUPERMARKET'\n" +
                "                     WHEN ARRAY['ROZETKA'] && tags THEN 'ROZETKA'\n" +
                "                     WHEN ARRAY['HOUSEHOLD'] && tags THEN 'HOUSEHOLD'\n" +
                "                     WHEN ARRAY['TRANSPORT'] && tags THEN 'TRANSPORT'\n" +
                "                     WHEN ARRAY['EAT_OUT'] && tags THEN 'EAT_OUT'\n" +
                "                     WHEN ARRAY['PARTY'] && tags THEN 'PARTY'\n" +
                "                     WHEN ARRAY['INCOME'] && tags THEN 'INCOME'\n" +
                "                     ELSE 'OTHER'\n" +
                "                     END as tag\n" +
                "          FROM expenses.transactions\n" +
                "          WHERE date_part('year', transaction_date) = date_part('year', current_timestamp)\n" +
                "          AND NOT ARRAY['INCOME'] && tags\n" +
                "          GROUP BY month, year, tag\n" +
                "          ORDER BY year, month DESC;", new Object[] {},
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
