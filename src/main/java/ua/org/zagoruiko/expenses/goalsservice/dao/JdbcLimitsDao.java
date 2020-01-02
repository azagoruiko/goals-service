package ua.org.zagoruiko.expenses.goalsservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.org.zagoruiko.expenses.goalsservice.model.Limit;
import ua.org.zagoruiko.expenses.goalsservice.model.LimitReportItem;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcLimitsDao implements LimitsDao {

    private static final String INSERT_LIMIT = "INSERT INTO goals.limits " +
            "(year, month, category, family, \"limit\", is_monthly) VALUES\n" +
            "(?, ?, ?, ?, ?, ?)\n" +
            "ON CONFLICT (year, month, category, family) DO\n" +
            "UPDATE SET \"limit\" = EXCLUDED.limit\n" +
            "WHERE goals.limits.year = EXCLUDED.year\n" +
            "    AND goals.limits.month = EXCLUDED.month\n" +
            "    AND goals.limits.family = EXCLUDED.family\n" +
            "    AND goals.limits.category = EXCLUDED.category";

    private static final String FIND_LIMIT = "SELECT year, month, category, family, \"limit\" FROM goals.limits\n" +
            "WHERE year = ?\n" +
            "    AND month = ?\n" +
            "    AND category = ?\n" +
            "    AND is_monthly = false\n" +
            "    AND family = ?";

    private static final String FIND_ALL_LIMITS = "SELECT year, month, category, family, \"limit\" FROM goals.limits\n" +
            "WHERE year = ?\n" +
            "    AND month = ?\n" +
            "    AND is_monthly = false\n" +
            "    AND family = ?";

    private static final String LIMITS_REPORT = "SELECT g.category, g.month, COALESCE(tr.am, 0) am, COALESCE(g.limit, 0) lim,\n" +
            "       ROUND(COALESCE(am/\"limit\", 0) * 100) percent\n" +
            "FROM (SELECT\n" +
            "    date_part('month', t.transaction_date) \"month\",\n" +
            "    date_part('year', t.transaction_date) \"year\",\n" +
            "    ROUND(SUM(amount)) * -1 am,\n" +
            "    category\n" +
            "FROM expenses.transactions t\n" +
            "    GROUP BY month, year, category) tr\n" +
            "RIGHT JOIN goals.limits g\n" +
            "    ON g.year = tr.year\n" +
            "    AND g.month = tr.month\n" +
            "    AND g.category = tr.category\n" +
            "WHERE g.year = ?\n" +
            "    AND g.month = ?\n" +
            "    AND g.family = ?\n" +
            "    AND g.is_monthly = false\n" +
            "ORDER BY percent DESC";

    private static final String MONTHLY_LIMIT_STATUS = "SELECT g.month, COALESCE(tr.am, 0) am, COALESCE(g.limit, 0) lim,\n" +
            "       ROUND(COALESCE(am/\"limit\", 0) * 100) percent\n" +
            "FROM (SELECT\n" +
            "          date_part('month', t.transaction_date) \"month\",\n" +
            "          date_part('year', t.transaction_date) \"year\",\n" +
            "          ROUND(SUM(amount)) * -1 am\n" +
            "      FROM expenses.transactions t WHERE category <> 'INCOME'\n" +
            "      GROUP BY month, year) tr\n" +
            "         JOIN goals.limits g\n" +
            "                    ON g.year = tr.year\n" +
            "                    AND g.month = tr.month\n" +
            "                    AND g.year = ?\n" +
            "                    AND g.month = ?\n" +
            "                    AND g.family = ?\n" +
            "WHERE g.is_monthly = true\n" +
            "ORDER BY percent DESC";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLimitsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Limit setLimit(Limit limit) {
        return this.setLimit(limit, false);
    }

    @Override
    public Limit setMonthlyLimit(Limit limit) {
        return this.setLimit(limit, true);
    }

    @Override
    public Limit setLimit(Limit limit, boolean isMonthly) {
        int updated = this.jdbcTemplate.update(INSERT_LIMIT, new Object[] {
                limit.getYear(),
                limit.getMonth(),
                limit.getCategory(),
                limit.getFamily(),
                limit.getLimit(),
                isMonthly
        });
        return updated == 1
                ? this.getLimit(limit.getYear(), limit.getMonth(), limit.getCategory(), limit.getFamily())
                : null;
    }

    @Override
    public Limit getLimit(int year, int month, String category, String family) {
        return this.jdbcTemplate.query(FIND_LIMIT, new Object[] {
                year,
                month,
                category,
                family
        }, (rs, rowNum) -> new Limit(
                rs.getInt(1),
                rs.getInt(2),
                rs.getString(3),
                rs.getString(4),
                rs.getInt(5)
        )).stream().findFirst().orElse(null);
    }

    @Override
    public List<Limit> getLimits(int year, int month, String family) {
        return this.jdbcTemplate.query(FIND_ALL_LIMITS, new Object[] {
                year,
                month,
                family
        }, (rs, rowNum) -> new Limit(
                rs.getInt(1),
                rs.getInt(2),
                rs.getString(3),
                rs.getString(4),
                rs.getInt(5)
        )).stream().collect(Collectors.toList());
    }

    @Override
    public List<LimitReportItem> getLimitReport(int year, int month, String family) {
        return this.jdbcTemplate.query(LIMITS_REPORT, new Object[] {
                year,
                month,
                family
        }, (rs, rowNum) -> new LimitReportItem(
                year,
                rs.getInt(2),
                rs.getString(1),
                rs.getInt(3),
                rs.getInt(4),
                rs.getInt(5)
        )).stream().collect(Collectors.toList());
    }

    @Override
    public LimitReportItem getMonthlyLimitStatus(int year, int month, String family) {
        return this.jdbcTemplate.query(MONTHLY_LIMIT_STATUS, new Object[] {
                year,
                month,
                family
        }, (rs, rowNum) -> new LimitReportItem(
                year,
                rs.getInt(1),
                "",
                rs.getInt(2),
                rs.getInt(3),
                rs.getInt(4)
        )).stream().findFirst().orElse(null);
    }
}
