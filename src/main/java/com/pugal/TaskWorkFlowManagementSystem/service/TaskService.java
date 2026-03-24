package com.pugal.TaskWorkFlowManagementSystem.service;

import com.pugal.TaskWorkFlowManagementSystem.enums.TaskStatus;
import com.pugal.TaskWorkFlowManagementSystem.model.Task;
import com.pugal.TaskWorkFlowManagementSystem.model.TaskHistory;
import com.pugal.TaskWorkFlowManagementSystem.repo.TaskHistoryRepository;
import com.pugal.TaskWorkFlowManagementSystem.repo.TaskRepository;
import com.pugal.TaskWorkFlowManagementSystem.workflow.TaskWorkflowValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;

    public TaskService(TaskRepository taskRepository, TaskHistoryRepository taskHistoryRepository) {
        this.taskRepository = taskRepository;
        this.taskHistoryRepository = taskHistoryRepository;
    }

    public Task createTask(Task task) {

        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new RuntimeException("Title is required");
        }

        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);

        // Store initial values in history
        saveHistory(savedTask.getId(), "title", savedTask.getTitle());
        saveHistory(savedTask.getId(), "description", savedTask.getDescription());
        saveHistory(savedTask.getId(), "priority", savedTask.getPriority().name());
        saveHistory(savedTask.getId(), "status", savedTask.getStatus().name());

        return savedTask;
    }

    private void saveHistory(UUID taskId, String field, String newValue) {
        TaskHistory history = new TaskHistory(taskId, field, null, newValue);
        taskHistoryRepository.save(history);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(UUID id) {
        return taskRepository.findById(id);
    }

    @Transactional
    public Task updateTask(UUID id, Task updatedTask) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        validateWorkflow(existingTask, updatedTask);
        trackChanges(existingTask, updatedTask);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(existingTask);
    }

    public void deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    // ── Workflow Validation ───────────────────────────────────────────────────

    private void validateWorkflow(Task existingTask, Task updatedTask) {

        TaskStatus oldStatus = existingTask.getStatus();
        TaskStatus newStatus = updatedTask.getStatus();

        System.out.println("Old Status: " + oldStatus);
        System.out.println("New Status: " + newStatus);

        if (newStatus != null && oldStatus != newStatus) {

            boolean valid = TaskWorkflowValidator.isValidTransition(oldStatus, newStatus);

            System.out.println("Transition valid: " + valid);

            if (!valid) {
                throw new IllegalStateException(
                        "Invalid status transition from " + oldStatus + " to " + newStatus
                );
            }
        }
    }
    // ── History Tracking ─────────────────────────────────────────────────────

    private void trackChanges(Task existing, Task updated) {
        trackField(existing.getId(), "TITLE",
                existing.getTitle(), updated.getTitle());

        trackField(existing.getId(), "DESCRIPTION",
                existing.getDescription(), updated.getDescription());

        trackField(existing.getId(), "PRIORITY",
                existing.getPriority() != null ? existing.getPriority().name() : null,
                updated.getPriority()  != null ? updated.getPriority().name()  : null);

        trackField(existing.getId(), "STATUS",
                existing.getStatus() != null ? existing.getStatus().name() : null,
                updated.getStatus()  != null ? updated.getStatus().name()  : null);
    }

    private void trackField(UUID taskId, String field, String oldValue, String newValue) {
        if (!Objects.equals(oldValue, newValue)) {
            TaskHistory history = new TaskHistory();
            history.setTaskId(taskId);
            history.setFieldChanged(field);
            history.setOldValue(oldValue);
            history.setNewValue(newValue);
            history.setChangedAt(LocalDateTime.now());
            taskHistoryRepository.save(history);
        }
    }
}