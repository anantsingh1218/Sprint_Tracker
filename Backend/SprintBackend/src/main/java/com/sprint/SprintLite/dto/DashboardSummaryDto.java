package com.sprint.SprintLite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDto {

    private Object release;

    private Object capacity;

    private Object velocity;

    private Object burndown;

}