package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service;

import com.sprint.SprintLite.dto.CreateSprintRequest;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.enums.SprintStatus;

import java.util.List;

public interface ISprintService {

    Sprint createSprint(CreateSprintRequest request);

    Sprint getSprintById(Long id);

    List<Sprint> getAllSprints();

//    List<Sprint> getSprintsByProductId(Long productId);

    Sprint updateSprint(Long id, CreateSprintRequest request);

    Sprint updateSprintStatus(Long id, SprintStatus status);

    void deleteSprint(Long id);
}