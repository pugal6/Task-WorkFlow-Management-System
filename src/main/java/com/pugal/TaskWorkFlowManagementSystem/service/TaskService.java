package com.pugal.TaskWorkFlowManagementSystem.service;

import com.pugal.TaskWorkFlowManagementSystem.dto.CreateTaskRequest;
import com.pugal.TaskWorkFlowManagementSystem.dto.TaskResponse;
import com.pugal.TaskWorkFlowManagementSystem.dto.UpdateTaskRequest;
import com.pugal.TaskWorkFlowManagementSystem.enums.TaskStatus;
import com.pugal.TaskWorkFlowManagementSystem.exception.InvalidTaskTransitionException;
import com.pugal.TaskWorkFlowManagementSystem.exception.TaskNotFoundException;
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
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;

    public TaskService(TaskRepository taskRepository, TaskHistoryRepository taskHistoryRepository) {
        this.taskRepository = taskRepository;
        this.taskHistoryRepository = taskHistoryRepository;
    }

    public TaskResponse createTask(CreateTaskRequest request) {

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());

        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);

        // Store initial values in history
        saveHistory(savedTask.getId(), "title", savedTask.getTitle());
        saveHistory(savedTask.getId(), "description", savedTask.getDescription());
        saveHistory(savedTask.getId(), "priority", savedTask.getPriority().name());
        saveHistory(savedTask.getId(), "status", savedTask.getStatus().name());

        return mapToResponse(savedTask);
    }

    private void saveHistory(UUID taskId, String field, String newValue) {
        TaskHistory history = new TaskHistory(taskId, field, null, newValue);
        taskHistoryRepository.save(history);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TaskResponse getTaskById(UUID id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        return mapToResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(UUID id, UpdateTaskRequest request) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        Task updatedTask = new Task();
        updatedTask.setTitle(request.getTitle());
        updatedTask.setDescription(request.getDescription());
        updatedTask.setPriority(request.getPriority());
        updatedTask.setStatus(request.getStatus());

        validateWorkflow(existingTask, updatedTask);
        trackChanges(existingTask, updatedTask);

        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setPriority(request.getPriority());
        existingTask.setStatus(request.getStatus());
        existingTask.setUpdatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(existingTask);

        return mapToResponse(savedTask);
    }

    public void deleteTask(UUID id) {

        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found");
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
                throw new InvalidTaskTransitionException(
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
                updated.getPriority() != null ? updated.getPriority().name() : null);

        trackField(existing.getId(), "STATUS",
                existing.getStatus() != null ? existing.getStatus().name() : null,
                updated.getStatus() != null ? updated.getStatus().name() : null);
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

    // ── DTO Mapping ──────────────────────────────────────────────────────────

    private TaskResponse mapToResponse(Task task) {

        TaskResponse response = new TaskResponse();

        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setPriority(task.getPriority());
        response.setStatus(task.getStatus());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());

        return response;
    }
}