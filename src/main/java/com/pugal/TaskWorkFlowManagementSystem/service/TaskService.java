package com.pugal.TaskWorkFlowManagementSystem.service;


import com.pugal.TaskWorkFlowManagementSystem.enums.TaskStatus;
import com.pugal.TaskWorkFlowManagementSystem.model.Task;
import com.pugal.TaskWorkFlowManagementSystem.repo.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {

        if(task.getTitle() == null || task.getTitle().isBlank()){
            throw new RuntimeException("Title is required");
        }

        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(UUID id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(UUID id, Task updatedTask) {

        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isEmpty()) {
            throw new RuntimeException("Task not found");
        }

        Task task = optionalTask.get();

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setPriority(updatedTask.getPriority());
        task.setStatus(updatedTask.getStatus());
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public void deleteTask(UUID id) {

        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }

        taskRepository.deleteById(id);
    }
}