package com.sprint.SprintLite.Sprint_and_Story_and_Task.Controller;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl.StoryServiceImpl;
import com.sprint.SprintLite.dto.CreateStoryRequest;
import com.sprint.SprintLite.dto.GetAllResponseDto;
import com.sprint.SprintLite.dto.RegisterResponseDto;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.dto.StoryResponseDto;
import com.sprint.SprintLite.entity.Story;
import com.sprint.SprintLite.repository.StoryRepository;
import com.sprint.SprintLite.util.CodeUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/story")
@RequiredArgsConstructor
public class StoryController {
    private final StoryServiceImpl storyService;
    private final StoryRepository storyRepository;

    @GetMapping("/all")
    public ResponseEntity<List<StoryResponseDto>> getAllStories() {
        return ResponseEntity.ok(storyService.getAllStories());
    }

    @PostMapping("/create")
    public ResponseEntity<StoryResponseDto> createStory(@RequestBody CreateStoryRequest story) {
        StoryResponseDto st=storyService.createStory(story);
        return ResponseEntity.ok().body(st);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoryResponseDto> updateStory(@PathVariable Integer id,@RequestBody CreateStoryRequest story) {
        StoryResponseDto st=storyService.updateStory(id,story);
        return ResponseEntity.ok().body(st);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RegisterResponseDto> deleteStory(@PathVariable String id) {
        storyService.deleteStory(CodeUtils.decodeToInteger("S",id));
        return ResponseEntity.ok(new RegisterResponseDto(("Story deleted successfully")));
    }

    @GetMapping("/getAllStories")
    public ResponseEntity<List<GetAllResponseDto>> getAllProducts(){
        List<Story> storyList = storyRepository.findAll();
        if (storyList.isEmpty()){
            throw new EntityNotFoundException("No Stories registered");
        }
        List<GetAllResponseDto> getAllResponseDtoList = new ArrayList<GetAllResponseDto>();
        storyList.forEach(story -> {
            GetAllResponseDto getAllResponseDto = new GetAllResponseDto(story.getId(), story.getTitle());
            getAllResponseDtoList.add(getAllResponseDto);
        });
        return ResponseEntity.ok(getAllResponseDtoList);
    }
}
