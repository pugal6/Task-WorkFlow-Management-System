package com.pugal.TaskWorkFlowManagementSystem.repo;

import com.pugal.TaskWorkFlowManagementSystem.model.Task;
import com.pugal.TaskWorkFlowManagementSystem.model.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, UUID> {

    @Query("""
SELECT COUNT(DISTINCT th.taskId)
FROM TaskHistory th
WHERE th.fieldChanged = 'STATUS'
AND th.newValue = 'DONE'
AND th.changedAt >= :startOfDay
AND th.changedAt < :endOfDay
""")
    long countCompletedTasksOnDate(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("""
       SELECT th.taskId
       FROM TaskHistory th
       WHERE th.newValue = 'DONE'
       AND th.changedAt >= :start
       AND th.changedAt < :end
       """)
    List<UUID> findCompletedTaskIdsOnDate(LocalDateTime start, LocalDateTime end);
}