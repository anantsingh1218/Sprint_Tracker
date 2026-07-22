package com.sprint.SprintLite.DashBoard.service;

import com.sprint.SprintLite.entity.Sprint;

public interface DashboardMetricsService {

    Integer
    calculateProgress(
            Long total,
            Long completed
    );

    Integer
    calculateCompletionRate(
            Long completed,
            Long total
    );

    Integer
    calculateRemainingHours(
            Sprint sprint
    );

}