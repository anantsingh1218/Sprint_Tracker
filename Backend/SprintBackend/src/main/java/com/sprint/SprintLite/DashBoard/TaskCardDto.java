package com.sprint.SprintLite.DashBoard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCardDto {

    private Integer id;

    private String taskCode;

    private String title;

    private String priority;

    private String story;

    private Integer remainingHours;

    private String assignee;


}