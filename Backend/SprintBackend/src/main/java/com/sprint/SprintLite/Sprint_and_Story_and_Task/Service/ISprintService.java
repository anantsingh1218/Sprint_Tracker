package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service;

import com.sprint.SprintLite.dto.CreateSprintRequest;
import com.sprint.SprintLite.dto.SprintResponseDto;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.enums.SprintStatus;

import java.util.List;

public interface ISprintService {

    SprintResponseDto createSprint(CreateSprintRequest request);

    SprintResponseDto getSprintById(Long id);

    List<SprintResponseDto> getAllSprints();

//    List<Sprint> getSprintsByProductId(Long productId);
     SprintResponseDto mapToDto(Sprint sprint);

    Sprint updateSprint(Long id, CreateSprintRequest request);

    SprintResponseDto updateSprintStatus(Long id, SprintStatus status);

    void deleteSprint(Long id);
}