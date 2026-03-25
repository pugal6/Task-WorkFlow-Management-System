package com.pugal.TaskWorkFlowManagementSystem.repo;

import com.pugal.TaskWorkFlowManagementSystem.enums.TaskStatus;
import com.pugal.TaskWorkFlowManagementSystem.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    long countByStatus(TaskStatus status);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByIdIn(List<UUID> ids);
}