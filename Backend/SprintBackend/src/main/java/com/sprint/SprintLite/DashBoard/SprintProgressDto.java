package com.sprint.SprintLite.DashBoard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintProgressDto {

    private Integer totalFeatures;

    private Integer totalStories;

    private Integer totalTasks;

    private Integer completedTasks;

    private Integer pendingTasks;

    private Integer blockedTasks;

    private Integer completion;
}