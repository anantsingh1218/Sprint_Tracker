package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.IStoryService;
import com.sprint.SprintLite.dto.CreateStoryRequest;
import com.sprint.SprintLite.dto.StoryResponseDto;
import com.sprint.SprintLite.entity.*;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.repository.*;
import com.sprint.SprintLite.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.sprint.SprintLite.util.CodeUtils;

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

        Feature feature = featureRepository.findFeatureByFeatureCode(request.getFeatureCode())
                .orElseThrow(() -> new RuntimeException("Feature not found"));

        Integer userId = CodeUtils.decodeToInteger("U", request.getUserCode());

        Users assignedUser = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Sprint sprint = sprintRepository.findSprintBySprintCode(request.getSprintCode());

        // Create Story object
        Story story = new Story();
        story.setTitle(request.getTitle());
        story.setBody(request.getBody());
        story.setFeatureid(feature);
        story.setUserid(assignedUser);
        story.setSprintid(sprint);
        story.setStorystatus(request.getStatus());
        story.setPriority(request.getPriority());
        story.setStorypoints(request.getRemainingStoryPoint());
        story.setCreatedBy(currentUsername);
        story.setCreatedAt(Instant.now());
        Story savedStory = storyRepository.save(story);

        StoryResponseDto response = new StoryResponseDto();
        response.setTitle(story.getTitle());
        response.setBody(story.getBody());
        response.setFeatureCode(CodeUtils.encode("F", feature.getId()));
        response.setSprintCode(CodeUtils.encode("SP", sprint.getId()));
        response.setUserCode(CodeUtils.encode("U", assignedUser.getId()));
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


        if (story.getRemainingStoryPoint() != null) {
            existingStory.setStorypoints(story.getRemainingStoryPoint());
        }

        // 3. Map relations carefully
        if (story.getUserCode() != null) {
            Integer userId = CodeUtils.decodeToInteger("U", story.getUserCode());
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            existingStory.setUserid(user);
        }

        if (story.getSprintCode() != null) {
            Sprint sprint = sprintRepository.findSprintBySprintCode(story.getSprintCode());
        }

        if (story.getFeatureCode() != null) {
            Feature feature = featureRepository.findFeatureByFeatureCode(story.getFeatureCode())
                    .orElseThrow(() -> new IllegalArgumentException("Feature not found"));
            existingStory.setFeatureid(feature);
        }

        existingStory.setUpdatedBy(username);
        existingStory.setUpdatedAt(Instant.now());

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
            response.setFeatureCode(CodeUtils.encode("F", updatedStory.getFeatureid().getId()));
        }
        if (updatedStory.getSprintid() != null) {
            response.setSprintCode(CodeUtils.encode("SP", updatedStory.getSprintid().getId()));
        }
        if (updatedStory.getUserid() != null) {
            response.setUserCode(CodeUtils.encode("U", updatedStory.getUserid().getId()));
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

    @Override
    public void deleteStory(Integer id) {
        Story story =  storyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Story not found"));
        storyRepository.delete(story);
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
            if (story.getFeatureid() != null) dto.setFeatureCode(CodeUtils.encode("F", story.getFeatureid().getId()));
            if (story.getSprintid() != null) dto.setSprintCode(CodeUtils.encode("SP", story.getSprintid().getId()));
            if (story.getUserid() != null) dto.setUserCode(CodeUtils.encode("U", story.getUserid().getId()));

            List<Comment> storyComments = commentRepository.findByEntitytypeAndEntityid(EntityType.STORY, story.getId());

            if (!storyComments.isEmpty()) {
                // Safely grab the text of the last comment if the list isn't empty
                dto.setComments(storyComments.getLast().getComment());
            } else {
                // Fallback if there are no comments yet
                dto.setComments(null);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public StoryResponseDto getStoryById(Integer id) {
        // 1. Fetch the story or throw a 404/runtime exception if it doesn't exist
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Story not found with ID: " + id));

        // 2. Initialize the response DTO
        StoryResponseDto dto = new StoryResponseDto();
        dto.setId(story.getId());
        dto.setTitle(story.getTitle());
        dto.setBody(story.getBody());
        dto.setStoryStatus(story.getStorystatus());
        dto.setPriority(story.getPriority());
        dto.setStoryPoints(story.getStorypoints());

        // 3. Handle relational mappings safely
        if (story.getFeatureid() != null) {
            dto.setFeatureCode(CodeUtils.encode("F", story.getFeatureid().getId()));
        }
        if (story.getSprintid() != null) {
            dto.setSprintCode(CodeUtils.encode("SP", story.getSprintid().getId()));
        }
        if (story.getUserid() != null) {
            dto.setUserCode(CodeUtils.encode("U", story.getUserid().getId()));
        }

        List<Comment> storyComments = commentRepository.findByEntitytypeAndEntityid(EntityType.STORY, story.getId());

        if (!storyComments.isEmpty()) {
            // Safely grab the text of the last comment if the list isn't empty
            dto.setComments(storyComments.getLast().getComment());
        } else {
            // Fallback if there are no comments yet
            dto.setComments(null);
        }

        return dto;
    }
}