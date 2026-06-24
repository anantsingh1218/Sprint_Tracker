package com.sprint.SprintLite.DashBoard.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import com.sprint.SprintLite.dto.DashboardResponseDto;
import com.sprint.SprintLite.DashBoard.service.DashBoardService;

@RestController

@RequestMapping("/dashboard")

@RequiredArgsConstructor

public class DashboardController {

    private final DashBoardService dashboardService;

    @GetMapping("/{userId}")
    public DashboardResponseDto dashboard(
            @PathVariable Integer userId
    ){

        return dashboardService
                .getDashboard(
                        userId
                );

    }

}