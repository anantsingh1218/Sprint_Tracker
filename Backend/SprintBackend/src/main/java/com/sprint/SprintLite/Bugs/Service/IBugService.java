package com.sprint.SprintLite.Bugs.Service;

import com.sprint.SprintLite.dto.BugDto;
import com.sprint.SprintLite.entity.Bug;

import com.sprint.SprintLite.dto.BugResponseDto;

import java.util.List;

public interface IBugService {
    BugResponseDto createBug(BugDto request);

    BugResponseDto getBugByiD(Integer id);

    List<BugResponseDto> getAllBugs();

    List<BugResponseDto> getBugsBySprintid(Integer sprintId);

    List<BugResponseDto> getBugsByStoryid(Integer storyId);

    BugResponseDto getBugsBySprintIdAndStoryId(Integer sprintId, Integer storyId);

    BugResponseDto updateBug(Integer id,BugDto request);

    void deleteBugByiD(Integer id);


}