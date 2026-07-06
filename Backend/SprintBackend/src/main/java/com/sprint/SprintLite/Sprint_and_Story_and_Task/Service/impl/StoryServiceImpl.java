package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.IStoryService;
import com.sprint.SprintLite.dto.CreateStoryRequest;
import com.sprint.SprintLite.dto.StoryResponseDto;
import com.sprint.SprintLite.entity.*;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements IStoryService {

    private final StoryRepository storyRepository;
    private final FeatureRepository featureRepository;
    private final UsersRepository usersRepository;
    private final SprintRepository sprintRepository;
    private final CommentRepository commentRepository;

    @Override
    public StoryResponseDto createStory(CreateStoryRequest request) {


        String currentUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();


        Feature feature = featureRepository.findById(request.getFeatureId())
                .orElseThrow(() ->
                        new RuntimeException("Feature not found"));


        Users assignedUser = usersRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));



        Sprint sprint = sprintRepository.findById(request.getSprintId())
                .orElseThrow(() ->
                        new RuntimeException("Sprint not found"));

        // Create Story object
        Story story = new Story();
        story.setTitle(request.getTitle());
        story.setBody(request.getBody());
        story.setFeatureid(feature);
        story.setUserid(assignedUser);
        story.setSprintid(sprint);
        story.setStorystatus(request.getStatus());
        story.setPriority(request.getPriority());
        story.setStorypoints(request.getStoryPoints());
        story.setCreatedBy(currentUsername);
        story.setCreatedAt(Instant.now());
        Story savedStory = storyRepository.save(story);

        StoryResponseDto response = new StoryResponseDto();
        response.setTitle(story.getTitle());
        response.setBody(story.getBody());
        response.setFeatureId(feature.getId());
        response.setSprintId(sprint.getId());
        response.setUserId(assignedUser.getId());
        response.setStoryStatus(story.getStorystatus());
        response.setPriority(story.getPriority());
        response.setStoryPoints(story.getStorypoints());
        return response;
    }

    @Override
    public StoryResponseDto updateStory(Integer id, CreateStoryRequest story) {

        Story existingStory = storyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Story not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null || "anonymousUser".equalsIgnoreCase(username)) {
            username = "System";
        }

        if (story.getTitle() != null) {
            existingStory.setTitle(story.getTitle());
        }

        if (story.getBody() != null) {
            existingStory.setBody(story.getBody());
        }

        if (story.getPriority() != null) {
            existingStory.setPriority(story.getPriority());
        }

        if (story.getStatus() != null) {
            existingStory.setStorystatus(story.getStatus());
        }


        if (story.getStoryPoints() != null) {
            existingStory.setStorypoints(story.getStoryPoints());
        }

        // 3. Map relations carefully
        if (story.getUserId() != null) {
            Users user = usersRepository.findById(story.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            existingStory.setUserid(user);
        }

        if (story.getSprintId() != null) {
            Sprint sprint = sprintRepository.findById(story.getSprintId())
                    .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));
            existingStory.setSprintid(sprint);
        }

        if (story.getFeatureId() != null) {
            Feature feature = featureRepository.findById(story.getFeatureId())
                    .orElseThrow(() -> new IllegalArgumentException("Feature not found"));
            existingStory.setFeatureid(feature);
        }

        existingStory.setUpdatedBy(username);

        // 4. Handle Comments
        Comment savedComment = null;
        if (story.getComments() != null && !story.getComments().trim().isEmpty()) {
            Comment comment = new Comment();
            comment.setComment(story.getComments());
            comment.setCreatedBy(username);
            comment.setEntitytype(EntityType.STORY);
            comment.setEntityid(id);
            comment.setCreatedAt(Instant.now());

            savedComment = commentRepository.save(comment);
        }

        Story updatedStory = storyRepository.save(existingStory);

        // 5. Safe Response Mapping (Checks for Null to prevent App Crashes)
        StoryResponseDto response = new StoryResponseDto();
        response.setTitle(updatedStory.getTitle());
        response.setBody(updatedStory.getBody());
        response.setStoryStatus(updatedStory.getStorystatus());
        response.setPriority(updatedStory.getPriority());
        response.setStoryPoints(updatedStory.getStorypoints());

        if (updatedStory.getFeatureid() != null) {
            response.setFeatureId(updatedStory.getFeatureid().getId());
        }
        if (updatedStory.getSprintid() != null) {
            response.setSprintId(updatedStory.getSprintid().getId());
        }
        if (updatedStory.getUserid() != null) {
            response.setUserId(updatedStory.getUserid().getId());
        }

        if (savedComment != null) {
            // Formats the timestamp to look clean (e.g., 2026-07-04 13:14)
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm")
                    .withZone(java.time.ZoneId.systemDefault());

            String formattedTime = formatter.format(savedComment.getCreatedAt());

            // Combines everything: "Vedansh (2026-07-04 13:14): HI"
            response.setComments(savedComment.getCreatedBy() + " (" + formattedTime + "): " + savedComment.getComment());
        }

        return response;
    }

    // Add this method inside your StoryServiceImpl class

    @Override
    public List<StoryResponseDto> getAllStories() {
        return storyRepository.findAll().stream().map(story -> {
            StoryResponseDto dto = new StoryResponseDto();
            dto.setId(story.getId());
            dto.setTitle(story.getTitle());
            dto.setBody(story.getBody());
            dto.setStoryStatus(story.getStorystatus());
            dto.setPriority(story.getPriority());
            dto.setStoryPoints(story.getStorypoints());

            // Handle potential null checks safely for relational mappings
            if (story.getFeatureid() != null) dto.setFeatureId(story.getFeatureid().getId());
            if (story.getSprintid() != null) dto.setSprintId(story.getSprintid().getId());
            if (story.getUserid() != null) dto.setUserId(story.getUserid().getId());

            return dto;
        }).collect(Collectors.toList());
    }
}

