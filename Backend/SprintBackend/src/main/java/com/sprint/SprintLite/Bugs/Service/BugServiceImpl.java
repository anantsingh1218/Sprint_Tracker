package com.sprint.SprintLite.Bugs.Service;

import com.sprint.SprintLite.dto.BugDto;
import com.sprint.SprintLite.entity.*;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BugServiceImpl implements IBugService {

    private final BugRepository bugRepository;
    private final UsersRepository usersRepository;
    private final StoryRepository storyRepository;
    private final SprintRepository sprintRepository;
    private final CommentRepository commentRepository;

    @Override
    public Bug createBug(BugDto request) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Users assignedUser = usersRepository.findById(request.getAssignedto())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Story story = storyRepository.findById(request.getStoryid())
                .orElseThrow(() -> new RuntimeException("Story not found"));

        Sprint sprint = sprintRepository.findById(request.getSprintid())
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        Bug bug = new Bug();
        bug.setDescription(request.getDescription());
        bug.setBugstatus(request.getBugstatus());
        bug.setPriority(request.getPriority());
        bug.setStoryid(story);
        bug.setSprintid(sprint);
        bug.setAssignedto(assignedUser);
        bug.setOriginalestimatehours(request.getOriginalestimatehours());
        bug.setRemainingestimatehours(request.getRemainingestimatehours());
        bug.setReopencount(0);
        bug.setCreatedBy(username);
        bug.setCreatedAt(Instant.now());

        Bug savedBug = bugRepository.save(bug);

        // Save comment during create
        if (request.getComments() != null &&
                !request.getComments().isBlank()) {

            Comment comment = new Comment();
            comment.setComment(request.getComments());
            comment.setEntitytype(EntityType.BUG);
            comment.setEntityid(savedBug.getId());
            comment.setCreatedBy(username);
            comment.setCreatedAt(Instant.now());

            commentRepository.save(comment);
        }

        return savedBug;
    }

    @Override
    public Bug getBugByiD(Integer id) {
        return bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));
    }

    @Override
    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    @Override
    public List<Bug> getBugsBySprintid(Integer sprintId) {
        return bugRepository.findBySprintid(sprintId);
    }

    @Override
    public List<Bug> getBugsByStoryid(Integer storyId) {
        return bugRepository.findByStoryid(storyId);
    }

    @Override
    public Bug getBugsBySprintIdAndStoryId(Integer sprintId, Integer storyId) {
        return bugRepository.findBySprintidAndStoryid(
                sprintId,
                storyId
        );
    }

    @Override
    public Bug updateBug(Integer id, BugDto request) {

        Bug existingBug = getBugByiD(id);

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        if (request.getDescription() != null) {
            existingBug.setDescription(request.getDescription());
        }

        if (request.getBugstatus() != null) {
            existingBug.setBugstatus(request.getBugstatus());
        }

        if (request.getPriority() != null) {
            existingBug.setPriority(request.getPriority());
        }

        if (request.getAssignedto() != null) {
            Users assignedUser = usersRepository.findById(request.getAssignedto())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingBug.setAssignedto(assignedUser);
        }

        if (request.getStoryid() != null) {
            Story story = storyRepository.findById(request.getStoryid())
                    .orElseThrow(() -> new RuntimeException("Story not found"));
            existingBug.setStoryid(story);
        }

        if (request.getSprintid() != null) {
            Sprint sprint = sprintRepository.findById(request.getSprintid())
                    .orElseThrow(() -> new RuntimeException("Sprint not found"));
            existingBug.setSprintid(sprint);
        }

        if (request.getOriginalestimatehours() != null) {
            existingBug.setOriginalestimatehours(request.getOriginalestimatehours());
        }

        if (request.getRemainingestimatehours() != null) {
            existingBug.setRemainingestimatehours(request.getRemainingestimatehours());
        }

        existingBug.setUpdatedBy(username);
        existingBug.setUpdatedAt(Instant.now());

        Bug savedBug = bugRepository.save(existingBug);

        // Save comment during update
        if (request.getComments() != null &&
                !request.getComments().isBlank()) {

            Comment comment = new Comment();
            comment.setComment(request.getComments());
            comment.setEntitytype(EntityType.BUG);
            comment.setEntityid(savedBug.getId());
            comment.setCreatedBy(username);
            comment.setCreatedAt(Instant.now());

            commentRepository.save(comment);
        }

        return savedBug;
    }

    @Override
    public void deleteBugByiD(Integer id) {
        Bug bug = getBugByiD(id);
        bugRepository.delete(bug);
    }
}