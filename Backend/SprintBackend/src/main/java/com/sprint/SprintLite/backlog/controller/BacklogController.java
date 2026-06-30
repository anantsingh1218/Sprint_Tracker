package com.sprint.SprintLite.backlog.controller;

import com.sprint.SprintLite.backlog.service.IBacklogService;
import com.sprint.SprintLite.dto.FeatureResponseDto;
import com.sprint.SprintLite.dto.StoryResponseDto;
import com.sprint.SprintLite.dto.TaskResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backlog")
@RequiredArgsConstructor
public class BacklogController {
    private final IBacklogService backlogService;

    @GetMapping("/getAllFeatures")
    public ResponseEntity<List<FeatureResponseDto>> getAllFeaturesForBacklog(){
        List<FeatureResponseDto> featureResponseDtoList = backlogService.getAllFeatures();
        if (featureResponseDtoList.isEmpty()){
            throw new EntityNotFoundException("Features not found");
        }
        else{
            return ResponseEntity.ok(featureResponseDtoList);
        }
    }

    @GetMapping("/getAllStories")
    public ResponseEntity<List<StoryResponseDto>> getAllStoriesForBacklog(){
        List<StoryResponseDto> storyResponseDtoList = backlogService.getAllStories();
        if (storyResponseDtoList.isEmpty()){
            throw new EntityNotFoundException("Stories not found");
        }
        else{
            return ResponseEntity.ok(storyResponseDtoList);
        }
    }

    @GetMapping("/getAllTasks")
    public ResponseEntity<List<TaskResponseDto>> getAllTasksForBacklog(){
        List<TaskResponseDto> taskResponseDtoList = backlogService.getAllTasks();
        if (taskResponseDtoList.isEmpty()){
            throw new EntityNotFoundException("Tasks not found");
        }
        else{
            return ResponseEntity.ok(taskResponseDtoList);
        }
    }
}
