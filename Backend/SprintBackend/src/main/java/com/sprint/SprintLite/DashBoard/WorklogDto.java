package com.sprint.SprintLite.DashBoard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class WorklogDto {

    private Integer taskId;

    private Integer hours;

    private String comment;

}