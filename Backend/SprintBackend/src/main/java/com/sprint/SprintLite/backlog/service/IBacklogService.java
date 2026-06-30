package com.sprint.SprintLite.backlog.service;

import com.sprint.SprintLite.backlog.dto.BugResponseDto;
import com.sprint.SprintLite.backlog.dto.FeatureResponseDto;
import com.sprint.SprintLite.backlog.dto.StoryResponseDto;
import com.sprint.SprintLite.backlog.dto.TaskResponseDto;

import java.util.List;

public interface IBacklogService {
    List<FeatureResponseDto> getAllFeatures();
    List<StoryResponseDto> getAllStories();
    List<TaskResponseDto> getAllTasks();
    List<BugResponseDto> getAllBugs();
}
