package com.taskmanagementsystem.service;

import com.taskmanagementsystem.entity.Project;

import java.util.List;

public interface IProjectService {

    List<Project> getAllproject();
    Project getProjectById(Long id);
    Project createProject(Project project);
    Project updateProject(Long id,Project project);
    void  deleteProject(Long id);
}
