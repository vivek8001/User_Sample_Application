package com.taskmanagementsystem.exception;

public class ProjectNotFoundException extends RuntimeException{

    public String ProjectNotFound(String message){
        return message;
    }
}
