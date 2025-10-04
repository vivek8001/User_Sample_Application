package com.userSample.service;

import com.userSample.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUser();
    User getUserById(Long id);
    User updateUser(Long id,User user);
    User addUser(User user);
    void deleteUser(Long id);
}
