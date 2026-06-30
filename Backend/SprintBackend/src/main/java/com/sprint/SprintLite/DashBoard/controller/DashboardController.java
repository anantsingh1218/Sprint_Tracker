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

    @GetMapping("/release/{userId}")
    public ReleaseReadinessDto getReleaseReadiness(
            @PathVariable Integer userId,
            @RequestParam(required = false) Integer productId
    ){
        return dashboardService.getReleaseReadiness(
                userId,
                productId
        );
    }

    @GetMapping("/team-capacity/{userId}")
    public TeamCapacityDto getTeamCapacity(
            @PathVariable Integer userId,
            @RequestParam(required = false) Integer productId
    ){
        return dashboardService.getTeamCapacity(
                userId,
                productId
        );
    }

    @GetMapping("/velocity/{userId}")
    public VelocityDto getVelocity(
            @PathVariable Integer userId,
            @RequestParam(required = false) Integer productId
    ) {
        return dashboardService.getVelocity(userId, productId);
    }

    @GetMapping("/burndown/{userId}")
    public BurndownDto getBurndown(
            @PathVariable Integer userId,
            @RequestParam(required = false) Integer productId
    ){
        return dashboardService.getBurndown(
                userId,
                productId
        );
    }

    @GetMapping("/summary/{userId}")
    public DashboardSummaryDto getSummary(
            @PathVariable Integer userId,
            @RequestParam(required = false) Integer productId
    ){
        return dashboardService.getSummary(
                userId,
                productId
        );
    }

    @GetMapping(
            "/export/{userId}"
    )

    public ExportDashboardDto
    export(
            @PathVariable
            Integer userId,
            @RequestParam(required = false) Integer productId
    ){

        return dashboardService
                .exportDashboard(
                        userId, productId
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

    @GetMapping("/products/{userId}")
    public List<ProductDropdownDto> getProducts(
            @PathVariable Integer userId
    ) {
        return dashboardService.getProducts(userId);
    }

}