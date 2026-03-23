package com.pugal.TaskWorkFlowManagementSystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "task_history")
@Data
public class TaskHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID taskId;

    private String fieldChanged;

    private String oldValue;

    private String newValue;

    private LocalDateTime changedAt;

    public TaskHistory() {
    }

    public TaskHistory(UUID taskId, String fieldChanged, String oldValue, String newValue) {
        this.taskId = taskId;
        this.fieldChanged = fieldChanged;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedAt = LocalDateTime.now();
    }


}