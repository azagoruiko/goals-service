package ua.org.zagoruiko.expenses.goalsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.zagoruiko.expenses.goalsservice.dao.UserDao;
import ua.org.zagoruiko.expenses.goalsservice.dto.UserDTO;
import ua.org.zagoruiko.expenses.goalsservice.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDTO findUser(String id) {
        User user = this.userDao.findUser(id);
        return user == null ? null :
                new UserDTO(user.getId().toString(), user.getFamily(), user.getName());
    }

    @Override
    public List<UserDTO> findFamilyUsers(String family) {
        return this.userDao.findFamilyUsers(family).stream()
                .map(u -> new UserDTO(u.getId().toString(), u.getFamily(), u.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO registerUser(UserDTO userDto) {
        User user = this.userDao.registerUser(
                new User(Long.parseLong(userDto.getId()), userDto.getFamily(), userDto.getName()));
        return user == null ? null :
                new UserDTO(user.getId().toString(), user.getFamily(), user.getName());
    }

    @Override
    public UserDTO updateUser(UserDTO userDto) {
        User user = this.userDao.updateUser(
                new User(Long.parseLong(userDto.getId()), userDto.getFamily(), userDto.getName()));
        return user == null ? null :
                new UserDTO(user.getId().toString(), user.getFamily(), user.getName());
    }
}
