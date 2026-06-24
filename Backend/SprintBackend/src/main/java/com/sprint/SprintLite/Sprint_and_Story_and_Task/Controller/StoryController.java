package com.sprint.SprintLite.Sprint_and_Story_and_Task.Controller;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl.StoryServiceImpl;
import com.sprint.SprintLite.dto.CreateStoryRequest;
import com.sprint.SprintLite.entity.Story;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/story")
@RequiredArgsConstructor
public class StoryController {
    private final StoryServiceImpl storyService;

    @PostMapping("/create")
    public ResponseEntity<Story> createStory(@RequestBody CreateStoryRequest story) {
        Story st=storyService.createStory(story);
        return ResponseEntity.ok().body(st);
    }
}
