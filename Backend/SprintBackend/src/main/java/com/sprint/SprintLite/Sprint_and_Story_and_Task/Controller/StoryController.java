package com.sprint.SprintLite.Sprint_and_Story_and_Task.Controller;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl.StoryServiceImpl;
import com.sprint.SprintLite.dto.CreateStoryRequest;
import com.sprint.SprintLite.entity.Story;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<Story> updateStory(@PathVariable Integer id,@RequestBody CreateStoryRequest story) {
        Story st=storyService.updateStory(id,story);
        return ResponseEntity.ok().body(st);
    }
}
