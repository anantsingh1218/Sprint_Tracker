package com.sprint.SprintLite.Bugs.Controller;

import com.sprint.SprintLite.Bugs.Service.IBugService;
import com.sprint.SprintLite.dto.BugDto;
import com.sprint.SprintLite.dto.BugResponseDto;
import com.sprint.SprintLite.dto.RegisterResponseDto;
import com.sprint.SprintLite.repository.BugRepository;
import com.sprint.SprintLite.util.CodeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Bug")
@RequiredArgsConstructor
public class BugController {
    private final IBugService bugService;
    private final BugRepository bugRepository;

    @PostMapping("/add")
    public ResponseEntity<BugResponseDto> addBug(@RequestBody BugDto request) {
        BugResponseDto bug = bugService.createBug(request);
        return ResponseEntity.ok(bug);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BugResponseDto> getBugById(@PathVariable Integer id) {
        return ResponseEntity.ok(
                bugService.getBugByiD(id)
        );
    }
    @GetMapping
    public ResponseEntity<List<BugResponseDto>> getAllBugs() {
        return ResponseEntity.ok(
                bugService.getAllBugs()
        );
    }

    @GetMapping("/sprint/{sprintCode}")
    public ResponseEntity<List<BugResponseDto>> getBugsBySprint(
            @PathVariable String sprintCode
    ) {
        Integer sprintId = CodeUtils.decodeToInteger("SP", sprintCode);
        return ResponseEntity.ok(
                bugService.getBugsBySprintid(sprintId)
        );
    }

    @GetMapping("/story/{storyCode}")
    public ResponseEntity<List<BugResponseDto>> getBugsByStory(
            @PathVariable String storyCode
    ) {
        Integer storyId = CodeUtils.decodeToInteger("S", storyCode);
        return ResponseEntity.ok(
                bugService.getBugsByStoryid(storyId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BugResponseDto> updateBug(
            @PathVariable Integer id,
            @RequestBody BugDto request
    ) {
        return ResponseEntity.ok(
                bugService.updateBug(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RegisterResponseDto> deleteBug(
            @PathVariable String id
    ) {
        bugService.deleteBugByiD(CodeUtils.decodeToInteger("B", id));
        return ResponseEntity.ok(new RegisterResponseDto("Bug deleted successfully"));
    }

}