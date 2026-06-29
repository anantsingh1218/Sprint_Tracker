package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.IStoryService;
import com.sprint.SprintLite.dto.CreateStoryRequest;
import com.sprint.SprintLite.entity.*;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements IStoryService {

    private final StoryRepository storyRepository;
    private final FeatureRepository featureRepository;
    private final UsersRepository usersRepository;
    private final SprintRepository sprintRepository;
    private final CommentRepository commentRepository;

    @Override
    public Story createStory(CreateStoryRequest request) {

        // Get logged-in user from JWT/Security Context
        String currentUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        //  Fetch Feature
        Feature feature = featureRepository.findById(request.getFeatureId())
                .orElseThrow(() ->
                        new RuntimeException("Feature not found"));

        //  Fetch Assigned User
        Users assignedUser = usersRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        //  Fetch Sprint
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


        return storyRepository.save(story);
    }

    @Override
    public Story updateStory(Integer id, CreateStoryRequest story) {

        Story existingStory = storyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Story not found"));

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

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

        if (story.getComments() != null && !story.getComments().isEmpty()) {
            Comment comment = new Comment();
            comment.setComment(story.getComments());
            comment.setCreatedBy(username);
            comment.setEntitytype(EntityType.STORY);
            comment.setEntityid(id);
            comment.setCreatedAt(Instant.now());
            commentRepository.save(comment);
        }

        return storyRepository.save(existingStory);

    }
}

