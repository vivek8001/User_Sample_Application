package com.taskmanagementsystem.service;

import com.taskmanagementsystem.entity.User;

import java.util.List;

public interface IUserService {

    List<User> getAllUser();
    User getUserById(Long id);
    User updateUser(Long id,User user);
    User addUser(User user);
    void deleteUser(Long id);
}
