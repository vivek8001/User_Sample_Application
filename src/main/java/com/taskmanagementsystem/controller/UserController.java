package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.entity.User;
import com.taskmanagementsystem.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    IUserService userService;


    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(){
        return "Welcome Home";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user, @RequestParam Long id){
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    /*@PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest){
        return jwtService.generateToken(authRequest.getUsername());
    }*/

}
