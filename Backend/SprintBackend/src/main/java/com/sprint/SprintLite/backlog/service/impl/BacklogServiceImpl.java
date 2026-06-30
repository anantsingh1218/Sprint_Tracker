package com.sprint.SprintLite.backlog.service.impl;

import com.sprint.SprintLite.backlog.service.IBacklogService;
import com.sprint.SprintLite.dto.FeatureResponseDto;
import com.sprint.SprintLite.dto.StoryResponseDto;
import com.sprint.SprintLite.dto.TaskResponseDto;
import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.Story;
import com.sprint.SprintLite.entity.Task;
import com.sprint.SprintLite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BacklogServiceImpl implements IBacklogService {
    private final FeatureRepository featureRepository;
    private final StoryRepository storyRepository;
    private final TaskRepository taskRepository;
    @Override
    public List<FeatureResponseDto> getAllFeatures() {
        List<FeatureResponseDto> featureResponseDtosList = new ArrayList<FeatureResponseDto>();
        List<Feature> featureList = featureRepository.findAll();
        featureList.forEach(feature -> {
            FeatureResponseDto featureResponseDto = new FeatureResponseDto(
                    feature.getId(),
                    feature.getTitle(),
                    feature.getDescription(),
                    feature.getFeatureStatus(),
                    feature.getPriority().toString(),
                    feature.getSprintId().getSprintName(),
                    feature.getRemainingStoryPoints(),
                    feature.getEstimatedStoryPoints(),
                    feature.getProductId().getProductname(),
                    feature.getUpdatedBy()
            );
            featureResponseDtosList.add(featureResponseDto);
        });
        return featureResponseDtosList;
    }

    @Override
    public List<StoryResponseDto> getAllStories() {
        List<StoryResponseDto> storyResponseDtoList = new ArrayList<StoryResponseDto>();
        List<Story> storyList = storyRepository.findAll();
        storyList.forEach(story -> {
            StoryResponseDto storyResponseDto = new StoryResponseDto(
                    story.getId(),
                    story.getTitle(),
                    story.getBody(),
                    story.getFeatureid().getTitle(),
                    story.getFeatureid().getId(),
                    story.getUserid().getUsername(),
                    story.getStorystatus(),
                    story.getPriority(),
                    story.getSprintid().getSprintName(),
                    story.getStorypoints(),
                    story.getStorypoints()
            );
            storyResponseDtoList.add(storyResponseDto);
        });
        return storyResponseDtoList;
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        List<TaskResponseDto> taskResponseDtoList = new ArrayList<TaskResponseDto>();
        List<Task> taskList = taskRepository.findAll();
        taskList.forEach(task -> {
            TaskResponseDto taskResponseDto = new TaskResponseDto(
                    task.getId(),
                    task.getTitle(),
                    task.getBody(),
                    task.getStoryid().getTitle(),
                    task.getStoryid().getId(),
                    task.getUserid().getUsername(),
                    task.getTaskstatus(),
                    task.getPriority(),
                    task.getSprintid().getSprintName(),
                    task.getOriginalestimatehours(),
                    task.getRemainingestimatehours()
            );
            taskResponseDtoList.add(taskResponseDto);
        });
        return taskResponseDtoList;
    }
}
