package com.pugal.TaskWorkFlowManagementSystem.repo;

import com.pugal.TaskWorkFlowManagementSystem.model.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, UUID> {
}