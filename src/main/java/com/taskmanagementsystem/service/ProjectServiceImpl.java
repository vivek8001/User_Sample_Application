package com.taskmanagementsystem.service;

import com.taskmanagementsystem.entity.Project;
import com.taskmanagementsystem.exception.ProjectNotFoundException;
import com.taskmanagementsystem.repository.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements IProjectService{

    @Autowired
    private IProjectRepository projectRepository;

    @Override
    public List<Project> getAllproject() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(()-> new ProjectNotFoundException());
    }

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Long id, Project project) {
        Project availableProject=getProjectById(id);
        availableProject.setProjectDescription(project.getProjectDescription());
        availableProject.setProjectname(project.getProjectname());
        return availableProject;
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
