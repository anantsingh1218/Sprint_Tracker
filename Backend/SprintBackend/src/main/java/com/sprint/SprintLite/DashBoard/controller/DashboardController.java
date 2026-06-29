package com.sprint.SprintLite.DashBoard.controller;

import com.sprint.SprintLite.DashBoard.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.sprint.SprintLite.DashBoard.service.DashBoardService;

import java.util.List;

@RestController

@RequestMapping("/dashboard")

@RequiredArgsConstructor

public class DashboardController {

    private final DashBoardService dashboardService;

    @GetMapping("/{userId}")
    public DashboardResponseDto dashboard(

            @PathVariable
            Integer userId,

            @RequestParam(
                    required=false
            )
            Integer productId,

            @RequestParam(
                    required=false
            )
            Integer sprintId

    ){

        return dashboardService
                .getDashboard(
                        userId
                );

    }

    @GetMapping(
            "/release/{userId}"
    )
    public ReleaseReadinessDto
    release(
            @PathVariable
            Integer userId
    ){

        return dashboardService
                .getReleaseReadiness(
                        userId
                );

    }

    @GetMapping(
            "/team-capacity/{userId}"
    )

    public TeamCapacityDto
    teamCapacity(
            @PathVariable
            Integer userId
    ){

        return dashboardService
                .getTeamCapacity(
                        userId
                );

    }

    @GetMapping(
            "/velocity/{userId}"
    )

    public VelocityDto
    velocity(
            @PathVariable
            Integer userId
    ){

        return dashboardService
                .getVelocity(
                        userId
                );

    }

    @GetMapping(
            "/burndown/{userId}"
    )

    public BurndownDto
    burndown(
            @PathVariable
            Integer userId
    ){

        return dashboardService
                .getBurndown(
                        userId
                );

    }

    @GetMapping(
            "/summary/{userId}"
    )

    public DashboardSummaryDto
    summary(
            @PathVariable
            Integer userId
    ){

        return dashboardService
                .getSummary(
                        userId
                );

    }

    @GetMapping(
            "/export/{userId}"
    )

    public ExportDashboardDto
    export(
            @PathVariable
            Integer userId
    ){

        return dashboardService
                .exportDashboard(
                        userId
                );

    }

    @GetMapping("/tasks")

    public Object getTasks(

            @RequestParam(
                    defaultValue="0"
            )
            Integer page,

            @RequestParam(
                    defaultValue="10"
            )
            Integer size

    ){

        return dashboardService
                .getTasks(
                        page,
                        size
                );

    }

    @GetMapping(
            "/stories"
    )
    public Page<StoryListDto>
    stories(

            @RequestParam(
                    defaultValue="0"
            )
            Integer page,

            @RequestParam(
                    defaultValue="10"
            )
            Integer size

    ){

        return dashboardService
                .getStories(
                        page,
                        size
                );

    }

    @GetMapping(
            "/feature-progress/{featureId}"
    )

    public FeatureProgressDto
    progress(

            @PathVariable
            Integer featureId

    ){

        return dashboardService
                .getFeatureProgress(
                        featureId
                );

    }

    @GetMapping(
            "/feature-stories/{featureId}"
    )

    public List<StoryCardDto>
    stories(

            @PathVariable
            Integer featureId

    ){

        return dashboardService
                .getStoriesByFeature(
                        featureId
                );

    }

    @GetMapping(
            "/story-progress/{storyId}"
    )

    public StoryProgressDto
    storyProgress(

            @PathVariable
            Integer storyId

    ){

        return dashboardService
                .getStoryProgress(
                        storyId
                );

    }

    @GetMapping(
            "/board/{sprintId}"
    )

    public List<BoardColumnDto>
    board(

            @PathVariable
            Integer sprintId

    ){

        return dashboardService
                .getBoard(
                        sprintId
                );

    }

    @PutMapping(
            "/move-task"
    )

    public void move(

            @RequestBody
            MoveTaskDto dto

    ){

        dashboardService
                .moveTask(
                        dto
                );

    }

}