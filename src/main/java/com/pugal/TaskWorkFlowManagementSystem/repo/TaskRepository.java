package com.pugal.TaskWorkFlowManagementSystem.repo;

import com.pugal.TaskWorkFlowManagementSystem.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}