package com.sprint.SprintLite.Bugs.Service;

import com.sprint.SprintLite.dto.BugDto;
import com.sprint.SprintLite.entity.Bug;

import java.util.List;

public interface IBugService {
    Bug createBug(BugDto request);

    Bug getBugByiD(Integer id);

    List<Bug> getAllBugs();

    List<Bug> getBugsBySprintid(Integer sprintId);

    List<Bug> getBugsByStoryid(Integer storyId);

    Bug getBugsBySprintIdAndStoryId(Integer sprintId, Integer storyId);

    Bug updateBug(Integer id,BugDto request);

    void deleteBugByiD(Integer id);


}
