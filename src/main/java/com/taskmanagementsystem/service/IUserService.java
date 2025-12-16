package com.taskmanagementsystem.service;

import com.taskmanagementsystem.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IUserService {

    List<User> getAllUser();
    User getUserById(Long id);
    User updateUser(Long id,User user);
    User addUser(User user);
    void deleteUser(Long id);

    User loadUserByUsername(String username);
}
