package com.sprint.SprintLite.Bugs.Controller;

import com.sprint.SprintLite.Bugs.Service.IBugService;
import com.sprint.SprintLite.dto.BugDto;
import com.sprint.SprintLite.entity.Bug;
import com.sprint.SprintLite.repository.BugRepository;
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
    public ResponseEntity<Bug> addBug(@RequestBody BugDto request) {
        Bug bug = bugService.createBug(request);
        return ResponseEntity.ok(bug);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bug> getBugById(@PathVariable Integer id) {
        return ResponseEntity.ok(
                bugService.getBugByiD(id)
        );
    }
    @GetMapping
    public ResponseEntity<List<Bug>> getAllBugs() {
        return ResponseEntity.ok(
                bugService.getAllBugs()
        );
    }

    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<List<Bug>> getBugsBySprint(
            @PathVariable Integer sprintId
    ) {
        return ResponseEntity.ok(
                bugService.getBugsBySprintid(sprintId)
        );
    }

    @GetMapping("/story/{storyId}")
    public ResponseEntity<List<Bug>> getBugsByStory(
            @PathVariable Integer storyId
    ) {
        return ResponseEntity.ok(
                bugService.getBugsByStoryid(storyId)
        );

        }
    @PutMapping("/{id}")
    public ResponseEntity<Bug> updateBug(
            @PathVariable Integer id,
            @RequestBody BugDto request
    ) {
        return ResponseEntity.ok(
                bugService.updateBug(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBug(
            @PathVariable Integer id
    ) {
        bugService.deleteBugByiD(id);
        return ResponseEntity.ok("Bug deleted successfully");
    }

}
