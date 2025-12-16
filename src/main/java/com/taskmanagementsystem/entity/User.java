package com.taskmanagementsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String roles;


    public User() {
    }

    public User(Long id, String username, String password, String fullName, String roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPassword() {
       return password;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    enum Role {
        USER,      // Regular user
        ADMIN,     // Administrator
        MODERATOR  // Moderator (optional)
    }


}
