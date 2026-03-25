package com.pugal.TaskWorkFlowManagementSystem.service;

import com.pugal.TaskWorkFlowManagementSystem.dto.TaskResponse;
import com.pugal.TaskWorkFlowManagementSystem.enums.TaskStatus;
import com.pugal.TaskWorkFlowManagementSystem.model.Task;
import com.pugal.TaskWorkFlowManagementSystem.repo.TaskHistoryRepository;
import com.pugal.TaskWorkFlowManagementSystem.repo.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AnalyticsService {

    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;

    public AnalyticsService(TaskRepository taskRepository,
                            TaskHistoryRepository taskHistoryRepository) {
        this.taskRepository = taskRepository;
        this.taskHistoryRepository = taskHistoryRepository;
    }

    public long getTaskCountByStatus(TaskStatus status) {
        return taskRepository.countByStatus(status);
    }

    public long getCompletedTasksOnDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay   = date.plusDays(1).atStartOfDay();
        return taskHistoryRepository.countCompletedTasksOnDate(startOfDay, endOfDay);
    }

    public List<TaskResponse> getCompletedTasksOnDateWithData(LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<UUID> taskIds =
                taskHistoryRepository.findCompletedTaskIdsOnDate(startOfDay, endOfDay);

        List<Task> tasks = taskRepository.findByIdIn(taskIds);

        return tasks.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<TaskResponse> getTasksByStatusWithData(TaskStatus status) {

        List<Task> tasks = taskRepository.findByStatus(status);

        return tasks.stream()
                .map(this::mapToResponse)
                .toList();
    }

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