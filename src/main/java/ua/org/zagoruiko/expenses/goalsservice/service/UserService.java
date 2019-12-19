package ua.org.zagoruiko.expenses.goalsservice.service;

import ua.org.zagoruiko.expenses.goalsservice.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO findUser(String id);
    List<UserDTO> findFamilyUsers(String id);
    UserDTO registerUser(UserDTO userDto);
    UserDTO updateUser(UserDTO userDto);
}
