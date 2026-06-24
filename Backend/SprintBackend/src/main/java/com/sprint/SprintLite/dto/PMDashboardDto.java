package com.sprint.SprintLite.dto;
public class PMDashboardDto {

    private String sprintName;

    private Integer progress;

    private Long totalTasks;

    private Long completedTasks;

    private Long inProgressTasks;

    private Long blockedTasks;

    private Integer totalHours;

    public PMDashboardDto(){}

    public PMDashboardDto(
            String sprintName,
            Integer progress,
            Long totalTasks,
            Long completedTasks,
            Long inProgressTasks,
            Long blockedTasks,
            Integer totalHours
    ){
        this.sprintName=sprintName;
        this.progress=progress;
        this.totalTasks=totalTasks;
        this.completedTasks=completedTasks;
        this.inProgressTasks=inProgressTasks;
        this.blockedTasks=blockedTasks;
        this.totalHours=totalHours;
    }

}
