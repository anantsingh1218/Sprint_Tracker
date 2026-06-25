package com.sprint.SprintLite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PMDashboardDto {

    private String sprintName;

    private Integer progress;

    private Long totalTasks;

    private Long completedTasks;

    private Long inProgressTasks;

    private Long blockedTasks;

    private Integer totalHours;

    private Long totalStories;

    private Integer sprintDuration;

    private Long completedStories;

    private Integer remainingHours;

    private Integer completionRate;

}