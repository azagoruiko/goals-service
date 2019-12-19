package ua.org.zagoruiko.expenses.goalsservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.org.zagoruiko.expenses.goalsservice.model.User;

import java.util.List;

@Repository
public class JdbcUserDao implements UserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findUser(String id) {
        return this.findUser(Long.parseLong(id));
    }

    @Override
    public User findUser(Long id) {
        return this.jdbcTemplate
                .query("SELECT * FROM goals.users WHERE id = ?", new Object[] {id},
                        (rs, rowNum) -> new User(
                                rs.getLong(1),
                                rs.getString(2),
                                rs.getString(3)
                        )).stream().findAny().orElse(null);
    }

    @Override
    public User registerUser(User user) {
        int updated = this.jdbcTemplate.update("INSERT INTO goals.users (id, family, name) " +
                "VALUES (?, ?, ?)",
                new Object[] {user.getId(), user.getFamily(), user.getName()});
        return updated > 0 ? this.findUser(user.getId()) : null;
    }

    @Override
    public User updateUser(User user) {
        int updated = this.jdbcTemplate.update("UPDATE goals.users " +
                        "SET family = ?,  name = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?",
                new Object[] {user.getFamily(), user.getName(), user.getId()});
        return updated > 0 ? this.findUser(user.getId()) : user;
    }

    @Override
    public List<User> findFamilyUsers(String family) {
        return this.jdbcTemplate
            .query("SELECT * FROM goals.users WHERE family = ?", new Object[] {family},
                    (rs, rowNum) -> new User(
                            rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3)
                    ));
    }
}
