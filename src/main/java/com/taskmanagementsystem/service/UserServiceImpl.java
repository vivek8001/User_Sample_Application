package com.taskmanagementsystem.service;

import com.taskmanagementsystem.exception.UserNotFoundException;
import com.taskmanagementsystem.repository.IUserRepository;
import com.taskmanagementsystem.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

     IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found"));
    }

    @Override
    public User updateUser(Long id, User user) {
        User userUpdate = getUserById(id);
        userUpdate.setUserName(user.getUserName());
        userUpdate.setRoles(user.getRoles());
        userUpdate.setFullName(user.getFullName());
        userUpdate.setPassword(user.getPassword());
        return userRepository.save(userUpdate);
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
