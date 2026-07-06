package com.sprint.SprintLite.DashBoard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportDashboardDto {

    private String reportName;

    private Object data;

}