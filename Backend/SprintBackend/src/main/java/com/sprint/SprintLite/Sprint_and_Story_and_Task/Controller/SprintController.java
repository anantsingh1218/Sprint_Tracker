package com.sprint.SprintLite.Sprint_and_Story_and_Task.Controller;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.ISprintService;
import com.sprint.SprintLite.dto.CreateSprintRequest;
import com.sprint.SprintLite.dto.GetAllResponseDto;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.enums.SprintStatus;
import com.sprint.SprintLite.repository.SprintRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sprint")
@RequiredArgsConstructor
public class SprintController {
    private final ISprintService sprintService;
    private final SprintRepository sprintRepository;

    @PostMapping("/add")
    public ResponseEntity<Sprint> addSprint(@RequestBody CreateSprintRequest request) {
        Sprint sprint=sprintService.createSprint(request);
        return ResponseEntity.ok(sprint);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sprint> getSprint(@PathVariable Long id) {
        Sprint sprint= sprintService.getSprintById(id);
        return ResponseEntity.ok(sprint);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Sprint>> getAllSprints() {
        List<Sprint>sprint=sprintService.getAllSprints();
        return ResponseEntity.ok(sprint);
    }

    @GetMapping("/getAllSprints")
    public ResponseEntity<List<GetAllResponseDto>> getAllProducts(){
        List<Sprint> sprintList = sprintRepository.findAll();
        if (sprintList.isEmpty()){
            throw new EntityNotFoundException("No Sprints registered");
        }
        List<GetAllResponseDto> getAllResponseDtoList = new ArrayList<GetAllResponseDto>();
        sprintList.forEach(sprint -> {
            GetAllResponseDto getAllResponseDto = new GetAllResponseDto(sprint.getId(), sprint.getSprintName());
            getAllResponseDtoList.add(getAllResponseDto);
        });
        return ResponseEntity.ok(getAllResponseDtoList);
    }

    // Update Sprint
    @PutMapping("/{id}")
    public ResponseEntity<Sprint> updateSprint(
            @PathVariable Long id,
            @RequestBody CreateSprintRequest request) {

        Sprint updatedSprint =
                sprintService.updateSprint(id, request);

        return ResponseEntity.ok(updatedSprint);
    }
    // Update Sprint Status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Sprint> updateSprintStatus(
            @PathVariable Long id,
            @RequestParam SprintStatus status) {

        Sprint updatedSprint =
                sprintService.updateSprintStatus(id, status);

        return ResponseEntity.ok(updatedSprint);
    }
    // Delete Sprint
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSprint(
            @PathVariable Long id) {

        sprintService.deleteSprint(id);

        return ResponseEntity.ok("Sprint deleted successfully");
    }
}

