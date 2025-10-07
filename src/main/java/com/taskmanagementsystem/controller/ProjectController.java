package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.entity.Project;
import com.taskmanagementsystem.service.IProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private IProjectService projectService;

    @GetMapping
    List<Project> getAllProject(){
        return projectService.getAllproject();
    }

    @GetMapping("/{id}")
    Project getProjectById(@PathVariable Long id){
        return projectService.getProjectById(id);
    }

    @PostMapping
    Project createProject(@RequestBody Project project){
        return projectService.createProject(project);
    }

    @PutMapping
    Project updateProject(@PathVariable Long id,@RequestBody Project project){
       return projectService.updateProject(id,project);
    }

    @DeleteMapping("/{id}")
    void deleteProjectById(Long id){
        projectService.deleteProject(id);
    }
}
