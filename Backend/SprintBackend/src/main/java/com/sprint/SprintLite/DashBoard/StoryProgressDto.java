package com.sprint.SprintLite.DashBoard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class StoryProgressDto {

    private Integer storyId;

    private String title;

    private Long totalTasks;

    private Long completedTasks;

    private Integer completion;

}