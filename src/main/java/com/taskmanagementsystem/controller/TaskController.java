package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.entity.Task;
import com.taskmanagementsystem.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @GetMapping
    public List<Task> getallTasks(){
        return taskService.getAllTasks();
    }

    @GetMapping("/{Id}")
    public Task getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @PutMapping
    public Task updateTask(@PathVariable Long id, @RequestBody Task task){
        return  taskService.updateTask(id,task);
    }
    @PostMapping
    public Task createTask(@RequestBody Task task){
        return taskService.createTask(task);
    }
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }
}
