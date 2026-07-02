package com.sprint.SprintLite.DashBoard.service;

import com.sprint.SprintLite.DashBoard.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DashBoardService {

    DashboardResponseDto getDashboard();

    ReleaseReadinessDto getReleaseReadiness(
            Integer productId
    );

    TeamCapacityDto getTeamCapacity(
            Integer productId
    );

    VelocityDto getVelocity(
            Integer productId
    );

    BurndownDto getBurndown(
            Integer productId
    );

    DashboardSummaryDto getSummary(
            Integer productId
    );

    ExportDashboardDto exportDashboard(
            Integer productId
    );

    Object getTasks(
            Integer page,
            Integer size
    );

    Page<StoryListDto> getStories(
            Integer page,
            Integer size
    );

    FeatureProgressDto getFeatureProgress(
            Integer featureId
    );

    List<StoryCardDto> getStoriesByFeature(
            Integer featureId
    );

    StoryProgressDto getStoryProgress(
            Integer storyId
    );

    List<BoardColumnDto> getBoard(
            Integer sprintId
    );

    void moveTask(
            MoveTaskDto dto
    );

    List<ProductDropdownDto> getProducts();

    TaskListDto getFocusTask();

    List<WorklogDto> getRecentWorklogs();
}