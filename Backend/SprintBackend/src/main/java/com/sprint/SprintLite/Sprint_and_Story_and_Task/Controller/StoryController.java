package com.sprint.SprintLite.Sprint_and_Story_and_Task.Controller;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl.StoryServiceImpl;
import com.sprint.SprintLite.dto.CreateStoryRequest;
import com.sprint.SprintLite.dto.GetAllResponseDto;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Story;
import com.sprint.SprintLite.repository.StoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/story")
@RequiredArgsConstructor
public class StoryController {
    private final StoryServiceImpl storyService;
    private final StoryRepository storyRepository;

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
