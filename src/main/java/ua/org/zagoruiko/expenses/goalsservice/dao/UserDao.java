package ua.org.zagoruiko.expenses.goalsservice.dao;

import ua.org.zagoruiko.expenses.goalsservice.dto.UserDTO;
import ua.org.zagoruiko.expenses.goalsservice.model.User;

import java.util.List;

public interface UserDao {
    User findUser(String id);
    User findUser(Long id);
    User registerUser(User user);
    User updateUser(User user);

    List<User> findFamilyUsers(String family);
}
