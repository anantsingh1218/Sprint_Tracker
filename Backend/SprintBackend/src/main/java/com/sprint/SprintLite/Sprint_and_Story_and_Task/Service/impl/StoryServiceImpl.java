package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.IStoryService;
import com.sprint.SprintLite.dto.CreateStoryRequest;
import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.Story;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.repository.FeatureRepository;
import com.sprint.SprintLite.repository.SprintRepository;
import com.sprint.SprintLite.repository.StoryRepository;
import com.sprint.SprintLite.repository.UsersRepository;
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
}