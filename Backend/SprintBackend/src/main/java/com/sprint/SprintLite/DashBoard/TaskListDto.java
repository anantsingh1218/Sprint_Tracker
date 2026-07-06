package com.sprint.SprintLite.DashBoard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskListDto {

    private String taskCode;

    private String title;

    private String storyName;

    private String sprintName;

    private String status;

    private String priority;

    private Integer originalEstimateHours;

    private Integer remainingEstimateHours;

}