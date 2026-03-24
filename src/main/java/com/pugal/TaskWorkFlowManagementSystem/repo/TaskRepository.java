package com.pugal.TaskWorkFlowManagementSystem.repo;

import com.pugal.TaskWorkFlowManagementSystem.enums.TaskStatus;
import com.pugal.TaskWorkFlowManagementSystem.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    long countByStatus(TaskStatus status);
}