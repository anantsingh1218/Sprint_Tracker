package com.sprint.SprintLite.DashBoard.service;

import com.sprint.SprintLite.DashBoard.*;
import org.springframework.data.domain.Page;

import java.util.List;

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


    Page<StoryListDto> getStories(
            Integer page,
            Integer size
    );

    FeatureProgressDto
    getFeatureProgress(
            Integer featureId
    );

    List<StoryCardDto>
    getStoriesByFeature(
            Integer featureId
    );

    StoryProgressDto
    getStoryProgress(
            Integer storyId
    );

    List<BoardColumnDto>
    getBoard(
            Integer sprintId
    );

    void moveTask(
            MoveTaskDto dto
    );

}