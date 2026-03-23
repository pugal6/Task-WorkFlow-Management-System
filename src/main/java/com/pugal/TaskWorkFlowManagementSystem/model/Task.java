package com.pugal.TaskWorkFlowManagementSystem.model;

import com.pugal.TaskWorkFlowManagementSystem.enums.Priority;
import com.pugal.TaskWorkFlowManagementSystem.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    @Getter
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Task() {
    }

    public Task(String title, String description, TaskStatus status, Priority priority) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}