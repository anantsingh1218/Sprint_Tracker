package com.sprint.SprintLite.DashBoard;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorklogDto {

    private Integer taskId;

    private String taskTitle;

    private String storyTitle;

    private BigDecimal hoursSpent;

    private LocalDate workDate;

    private String remarks;

}