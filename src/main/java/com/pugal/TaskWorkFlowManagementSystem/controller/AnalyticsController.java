package com.pugal.TaskWorkFlowManagementSystem.controller;
import com.pugal.TaskWorkFlowManagementSystem.dto.TaskResponse;
import com.pugal.TaskWorkFlowManagementSystem.enums.TaskStatus;
import com.pugal.TaskWorkFlowManagementSystem.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/tasks/count-by-status/{status}")
    public ResponseEntity<Long> getTaskCountByStatus(@PathVariable TaskStatus status) {

        long count = analyticsService.getTaskCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/tasks/completed-on/{date}")
    public ResponseEntity<Long> getCompletedTasksOnDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        long count = analyticsService.getCompletedTasksOnDate(date);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/tasks/completed-on/{date}/tasks")
    public ResponseEntity<List<TaskResponse>> getCompletedTasksOnDateWithData(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        List<TaskResponse> tasks = analyticsService.getCompletedTasksOnDateWithData(date);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/status/{status}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasksByStatusWithData(
            @PathVariable TaskStatus status) {

        List<TaskResponse> tasks = analyticsService.getTasksByStatusWithData(status);
        return ResponseEntity.ok(tasks);
    }
}
