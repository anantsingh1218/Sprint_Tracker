package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service;

import com.sprint.SprintLite.dto.CreateStoryRequest;
import com.sprint.SprintLite.dto.StoryResponseDto;
import com.sprint.SprintLite.entity.Story;

import java.util.List;

public interface IStoryService {
    StoryResponseDto createStory(CreateStoryRequest story);
    StoryResponseDto updateStory(Integer id,CreateStoryRequest story);

    List<StoryResponseDto> getAllStories();
}
