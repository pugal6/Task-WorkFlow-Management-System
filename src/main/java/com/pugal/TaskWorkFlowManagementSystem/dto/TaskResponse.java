package com.pugal.TaskWorkFlowManagementSystem.dto;

import com.pugal.TaskWorkFlowManagementSystem.enums.Priority;
import com.pugal.TaskWorkFlowManagementSystem.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TaskResponse {

    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}