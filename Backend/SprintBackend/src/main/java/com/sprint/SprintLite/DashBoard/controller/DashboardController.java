package com.sprint.SprintLite.DashBoard.controller;

import com.sprint.SprintLite.DashBoard.*;
import com.sprint.SprintLite.DashBoard.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashBoardService dashboardService;

    @GetMapping
    public DashboardResponseDto dashboard() {

        return dashboardService.getDashboard();

    }

    @GetMapping("/release")
    public ReleaseReadinessDto getReleaseReadiness(
            @RequestParam(required = false) Integer productId
    ) {

        return dashboardService.getReleaseReadiness(productId);

    }

    @GetMapping("/team-capacity")
    public TeamCapacityDto getTeamCapacity(
            @RequestParam(required = false) Integer productId
    ) {

        return dashboardService.getTeamCapacity(productId);

    }

    @GetMapping("/velocity")
    public VelocityDto getVelocity(
            @RequestParam(required = false) Integer productId
    ) {

        return dashboardService.getVelocity(productId);

    }

    @GetMapping("/burndown")
    public BurndownDto getBurndown(
            @RequestParam(required = false) Integer productId
    ) {

        return dashboardService.getBurndown(productId);

    }

    @GetMapping("/summary")
    public DashboardSummaryDto getSummary(
            @RequestParam(required = false) Integer productId
    ) {

        return dashboardService.getSummary(productId);

    }

    @GetMapping("/sprint-progress")
    public SprintProgressDto getSprintProgress(
            @RequestParam Integer productId
    ) {

        return dashboardService.getSprintProgress(productId);

    }


    @GetMapping("/export")
    public ExportDashboardDto export(
            @RequestParam(required = false) Integer productId
    ) {

        return dashboardService.exportDashboard(productId);

    }

    @GetMapping("/tasks")
    public Object getTasks(

            @RequestParam(defaultValue = "0")
            Integer page,

            @RequestParam(defaultValue = "10")
            Integer size

    ) {

        return dashboardService.getTasks(page, size);

    }

    @GetMapping("/stories")
    public Page<StoryListDto> stories(

            @RequestParam(defaultValue = "0")
            Integer page,

            @RequestParam(defaultValue = "10")
            Integer size

    ) {

        return dashboardService.getStories(page, size);

    }

    @GetMapping("/feature-progress/{featureId}")
    public FeatureProgressDto progress(
            @PathVariable Integer featureId
    ) {

        return dashboardService.getFeatureProgress(featureId);

    }

//    @GetMapping("/feature-stories/{featureId}")
//    public List<StoryCardDto> featureStories(
//            @PathVariable Integer featureId
//    ) {
//
//        return dashboardService.getStoriesByFeature(featureId);
//
//    }

    @GetMapping("/story-progress/{storyId}")
    public StoryProgressDto storyProgress(
            @PathVariable Integer storyId
    ) {

        return dashboardService.getStoryProgress(storyId);

    }

    @GetMapping("/board/{sprintId}")
    public List<BoardColumnDto> board(
            @PathVariable Integer sprintId
    ) {

        return dashboardService.getBoard(sprintId);

    }

    @PatchMapping("/task/move")
    public void moveTask(@RequestBody MoveTaskDto dto) {

        dashboardService.moveTask(dto);
    }

    @GetMapping("/products")
    public List<ProductDropdownDto> getProducts() {

        return dashboardService.getProducts();

    }

    @GetMapping("/focus-task")
    public ResponseEntity<TaskListDto> getFocusTask() {
        return ResponseEntity.ok(
                dashboardService.getFocusTask()
        );
    }

    @GetMapping("/recent-worklogs")
    public ResponseEntity<List<WorklogDto>> getRecentWork() {

        return ResponseEntity.ok(
                dashboardService.getRecentWorklogs()
        );

    }

    @GetMapping("/sprints")
    public List<SprintDropdownDto> getSprints() {

        return dashboardService.getSprintDropdown();

    }

}