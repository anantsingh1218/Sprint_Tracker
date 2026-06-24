package com.sprint.SprintLite.DashBoard.service;

import com.sprint.SprintLite.dto.DashboardResponseDto;

public interface DashBoardService {

    DashboardResponseDto getDashboard(
            Integer userId
    );

}