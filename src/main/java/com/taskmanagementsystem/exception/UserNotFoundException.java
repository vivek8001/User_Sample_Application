package com.taskmanagementsystem.exception;

public class UserNotFoundException extends RuntimeException{

    String message;

    public UserNotFoundException(String message){
        super(String.format("%s User not found %s : %s",message));
        this.message=message;
    }

}
