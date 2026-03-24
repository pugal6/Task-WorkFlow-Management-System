package com.pugal.TaskWorkFlowManagementSystem.workflow;

import com.pugal.TaskWorkFlowManagementSystem.enums.TaskStatus;



public class TaskWorkflowValidator {

    public static boolean isValidTransition(TaskStatus from, TaskStatus to) {

        return switch (from) {
            case TODO -> to == TaskStatus.IN_PROGRESS || to == TaskStatus.CANCELLED;
            case IN_PROGRESS -> to == TaskStatus.DONE || to == TaskStatus.CANCELLED;
            case DONE -> to == TaskStatus.IN_PROGRESS; // reopen allowed
            default -> false;
        };
    }
}
