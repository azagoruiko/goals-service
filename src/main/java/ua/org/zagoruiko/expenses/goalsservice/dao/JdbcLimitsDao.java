package ua.org.zagoruiko.expenses.goalsservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.org.zagoruiko.expenses.goalsservice.model.Limit;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcLimitsDao implements LimitsDao {
    private static final String INSERT_LIMIT = "INSERT INTO goals.limits " +
            "(year, month, category, family, \"limit\") VALUES\n" +
            "(?, ?, ?, ?, ?)\n" +
            "ON CONFLICT (year, month, category, family) DO\n" +
            "UPDATE SET \"limit\" = EXCLUDED.limit\n" +
            "WHERE goals.limits.year = EXCLUDED.year\n" +
            "    AND goals.limits.month = EXCLUDED.month\n" +
            "    AND goals.limits.family = EXCLUDED.family\n" +
            "    AND goals.limits.category = EXCLUDED.category";

    private static final String FIND_LIMIT = "SELECT year, month, category, \"limit\" FROM goals.limits\n" +
            "WHERE year = ?\n" +
            "    AND month = ?\n" +
            "    AND family = ?\n" +
            "    AND category = ?";

    private static final String FIND_ALL_LIMITS = "SELECT year, month, category, \"limit\" FROM goals.limits\n" +
            "WHERE year = ?\n" +
            "    AND month = ?\n" +
            "    AND family = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLimitsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Limit setLimit(Limit limit) {
        int updated = this.jdbcTemplate.update(INSERT_LIMIT, new Object[] {
                limit.getYear(),
                limit.getMonth(),
                limit.getCategory(),
                limit.getFamily(),
                limit.getLimit()
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
}
