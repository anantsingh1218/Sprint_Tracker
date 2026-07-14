package com.sprint.SprintLite.DSU.controller;

import com.sprint.SprintLite.DSU.service.DsuService;
import com.sprint.SprintLite.dto.AutoDtoResponse;
import com.sprint.SprintLite.dto.DsuDto;
import com.sprint.SprintLite.entity.DSUNote;
import com.sprint.SprintLite.entity.Task;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.repository.DSUNoteRepository;
import com.sprint.SprintLite.util.CodeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/DSU")
@RequiredArgsConstructor
public class DSU_Controller {

    private final DsuService dsuService;



    @PostMapping("/{entityType}/{entityCode}")
    public ResponseEntity<DSUNote> createDSU(@PathVariable EntityType entityType, @PathVariable String entityCode, @RequestBody DsuDto request) {
        String prefix = getPrefix(entityType);
        Integer entityId = CodeUtils.decodeToInteger(prefix, entityCode);
        if (entityId == null) {
            throw new IllegalArgumentException("Invalid Entity Code for type " + entityType);
        }
        DSUNote d1 = dsuService.createDSUNote(entityType, entityId, request);
        return ResponseEntity.ok(d1);
    }

    @GetMapping("/{entityType}/{entityCode}")
    public ResponseEntity<List<DSUNote>> getDSUNotes(
            @PathVariable EntityType entityType,
            @PathVariable String entityCode
    ) {
        String prefix = getPrefix(entityType);
        Integer entityId = CodeUtils.decodeToInteger(prefix, entityCode);
        if (entityId == null) {
            throw new IllegalArgumentException("Invalid Entity Code for type " + entityType);
        }
        return ResponseEntity.ok(
                dsuService.getDSUNotes(entityType, entityId)
        );
    }

    private String getPrefix(EntityType entityType) {
        switch (entityType) {
            case PRODUCT: return "P";
            case FEATURE: return "F";
            case STORY: return "S";
            case TASK: return "T";
            case BUG: return "B";
            case SPRINT: return "SP";
            default: return "";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteDSU(@PathVariable Integer id) {
        dsuService.DeleteDsu(id);
        return "The DSU has been deleted";
    }

    @GetMapping("/date/{notesDate}")
    public ResponseEntity<Map<String,Object>> getDSUNotes(@PathVariable LocalDate notesDate) {
        return ResponseEntity.ok(dsuService.getDsuBuDate(notesDate));
    }

    @GetMapping("/auto")
    public ResponseEntity<AutoDtoResponse> generateAutoDSU(@RequestParam LocalDate startDate,
                                                           @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(
                dsuService.generateAutoDSU(startDate, endDate)
        );
    }

}