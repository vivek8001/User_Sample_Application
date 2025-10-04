package com.userSample.service;

import com.userSample.Repository.UserRepository;
import com.userSample.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

     UserRepository userRepository;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("No user found"));
    }

    @Override
    public User updateUser(Long id, User user) {
        User exists= getUserById(id);
        exists.setUserName(user.getUserName());
        exists.setRoles(user.getRoles());
        exists.setFullName(user.getFullName());
        exists.setPassword(user.getPassword());
        return userRepository.save(exists);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById( id);
    }
}
