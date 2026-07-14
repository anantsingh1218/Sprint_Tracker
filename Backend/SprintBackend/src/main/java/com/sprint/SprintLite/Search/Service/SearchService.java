package com.sprint.SprintLite.Search.Service;

import com.sprint.SprintLite.Search.Dto.SearchResultDto;
import com.sprint.SprintLite.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SearchService {

    private final TaskRepository taskRepository;
    private final StoryRepository storyRepository;
    private final FeatureRepository featureRepository;
    private final BugRepository bugRepository;
    private final UsersRepository usersRepository;
    private final SprintRepository sprintRepository;
    private final DSUNoteRepository dsuNoteRepository;

    public List<SearchResultDto> globalSearch(String keyword) {
        List<SearchResultDto> result = new ArrayList<>();

        taskRepository.findByTitleContainingIgnoreCase(keyword)
                .forEach(task ->
                        result.add(
                                new SearchResultDto(
                                        task.getId(),
                                        "Task",
                                        task.getTitle()
                                )
                        ));

        bugRepository.findByTitleContainingIgnoreCase(keyword)
                .forEach(bug ->
                        result.add(
                                new SearchResultDto(
                                        bug.getId(),
                                        "Bug",
                                        bug.getTitle()
                                )
                        ));

        storyRepository.findByTitleContainingIgnoreCase(keyword)
                .forEach(story ->
                        result.add(
                                new SearchResultDto(
                                        story.getId(),
                                        "Story",
                                        story.getTitle()
                                )
                        ));

        featureRepository.findByTitleContainingIgnoreCase(keyword)
                .forEach(feature ->
                        result.add(
                                new SearchResultDto(
                                        feature.getId(),
                                        "Feature",
                                        feature.getTitle()
                                )
                        ));

        usersRepository.findByUsernameContainingIgnoreCase(keyword)
                .forEach(user ->
                        result.add(
                                new SearchResultDto(
                                        user.getId(),
                                        "User",
                                        user.getUsername()
                                )
                        ));

        sprintRepository.findBySprintNameContainingIgnoreCase(keyword)
                .forEach(sprint ->
                        result.add(
                                new SearchResultDto(
                                        sprint.getId(),
                                        "Sprint",
                                        sprint.getSprintName()
                                )
                        ));

        dsuNoteRepository
                .findByBlockersContainingIgnoreCaseOrCompletedworkContainingIgnoreCaseOrNextplanContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword
                )
                .forEach(dsu ->
                        result.add(
                                new SearchResultDto(
                                        dsu.getId(),
                                        "DSU",
                                        dsu.getBlockers()
                                )
                        ));

        return result;
    }
}

    // so we are actually creating a list of SearchReturnDtos so its result will be giving back the response in the way:
    // so when we type : 'Log' so the ResultDto will be given in this format
    // [
    //  {
    //    "id": 1,
    //    "type": "Task",
    //    "name": "Login Fix"
    //  },
    //  {
    //    "id": 2,
    //    "type": "Bug",
    //    "name": "Login Page Crash"
    //  }
    //]

