package com.sprint.SprintLite.DashBoard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryListDto {

    private Integer id;

    private String title;

    private String featureName;

    private String sprintName;

    private String status;

    private Integer totalTasks;

    private Integer completedTasks;

    private Integer progress;

}