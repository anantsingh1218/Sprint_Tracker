package com.sprint.SprintLite.DashBoard.service;

import com.sprint.SprintLite.dto.*;

public interface DashBoardService {

    DashboardResponseDto
    getDashboard(
            Integer userId
    );

    ReleaseReadinessDto
    getReleaseReadiness(
            Integer userId);

    TeamCapacityDto
    getTeamCapacity(
            Integer userId
    );

    VelocityDto
    getVelocity(
            Integer userId
    );

    BurndownDto
    getBurndown(
            Integer userId
    );

    DashboardSummaryDto
    getSummary(
            Integer userId
    );

    ExportDashboardDto
    exportDashboard(
            Integer userId
    );

    Object getTasks(
            Integer page,
            Integer size
    );


}