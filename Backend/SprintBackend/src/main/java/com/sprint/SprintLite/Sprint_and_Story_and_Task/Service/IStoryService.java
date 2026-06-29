package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service;

import com.sprint.SprintLite.dto.CreateStoryRequest;
import com.sprint.SprintLite.entity.Story;

public interface IStoryService {
    Story createStory(CreateStoryRequest story);
    Story updateStory(Integer id,CreateStoryRequest story);
}
