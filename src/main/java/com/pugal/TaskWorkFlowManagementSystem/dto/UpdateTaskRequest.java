package com.pugal.TaskWorkFlowManagementSystem.dto;

import com.pugal.TaskWorkFlowManagementSystem.enums.Priority;
import com.pugal.TaskWorkFlowManagementSystem.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Priority must be provided")
    private Priority priority;

    @NotNull(message = "Status must be provided")
    private TaskStatus status;
}