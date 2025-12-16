package com.taskmanagementsystem.service;

import com.taskmanagementsystem.entity.Task;
import com.taskmanagementsystem.exception.TaskNotFoundException;
import com.taskmanagementsystem.repository.ITaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements ITaskService{

    @Autowired
    ITaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException("Task not found"));
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, Task task) {
        Task currenttask= getTaskById(id);
        currenttask.setDetails(task.getDetails());
        currenttask.setStatus(task.getStatus());
        currenttask.setTitle(task.getTitle());
        return currenttask;
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
