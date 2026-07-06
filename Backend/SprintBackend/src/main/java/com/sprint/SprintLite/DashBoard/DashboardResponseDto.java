package com.sprint.SprintLite.DashBoard;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashboardResponseDto {

    private String role;

    private Object dashboard;

    public DashboardResponseDto(
            String role,
            Object dashboard
    ){
        this.role=role;
        this.dashboard=dashboard;
    }

}