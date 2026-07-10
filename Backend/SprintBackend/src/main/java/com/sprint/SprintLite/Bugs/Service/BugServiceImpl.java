package com.sprint.SprintLite.Bugs.Service;

import com.sprint.SprintLite.dto.BugDto;
import com.sprint.SprintLite.dto.BugResponseDto;
import com.sprint.SprintLite.entity.*;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.sprint.SprintLite.util.CodeUtils;

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
    public BugResponseDto createBug(BugDto request) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Users assignedUser = null;
        if (request.getAssignedUserCode() != null) {
            assignedUser = usersRepository.findById(CodeUtils.decodeToInteger("U", request.getAssignedUserCode()))
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        Story story = null;
        if (request.getStoryCode() != null) {
            story = storyRepository.findById(CodeUtils.decodeToInteger("S", request.getStoryCode()))
                    .orElseThrow(() -> new RuntimeException("Story not found"));
        }

        Sprint sprint = null;
        if (request.getSprintCode() != null) {
            sprint = sprintRepository.findById(CodeUtils.decodeToInteger("SP", request.getSprintCode()))
                    .orElseThrow(() -> new RuntimeException("Sprint not found"));
        }

        Bug bug = new Bug();
        bug.setDescription(request.getDescription());
        bug.setBugstatus(request.getBugstatus());
        bug.setPriority(request.getPriority());
        bug.setTitle(request.getTitle());
        bug.setStoryid(story);
        bug.setSprintid(sprint);
        bug.setAssignedto(assignedUser);
        bug.setOriginalestimatehours(request.getOriginalestimatehours());
        bug.setRemainingestimatehours(request.getRemainingestimatehours());
        bug.setReopencount(0);
        bug.setCreatedBy(username);
        bug.setCreatedAt(Instant.now());

        Bug savedBug = bugRepository.save(bug);

        return mapToDto(savedBug);
    }

    @Override
    public BugResponseDto getBugByiD(Integer id) {
        return mapToDto(bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found")));
    }

    @Override
    public List<BugResponseDto> getAllBugs() {
        return bugRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public List<BugResponseDto> getBugsBySprintid(Integer sprintId) {
        return bugRepository.findBySprintid(sprintId).stream().map(this::mapToDto).toList();
    }

    @Override
    public List<BugResponseDto> getBugsByStoryid(Integer storyId) {
        return bugRepository.findByStoryid(storyId).stream().map(this::mapToDto).toList();
    }

    @Override
    public BugResponseDto getBugsBySprintIdAndStoryId(Integer sprintId, Integer storyId) {
        return mapToDto(bugRepository.findBySprintidAndStoryid(
                sprintId,
                storyId
        ));
    }

    @Override
    public BugResponseDto updateBug(Integer id, BugDto request) {

        Bug existingBug = bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));

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

        if (request.getAssignedUserCode() != null) {
            Users assignedUser = usersRepository.findById(CodeUtils.decodeToInteger("U", request.getAssignedUserCode()))
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingBug.setAssignedto(assignedUser);
        }

        if (request.getStoryCode() != null) {
            Story story = storyRepository.findById(CodeUtils.decodeToInteger("S", request.getStoryCode()))
                    .orElseThrow(() -> new RuntimeException("Story not found"));
            existingBug.setStoryid(story);
        }

        if (request.getSprintCode() != null) {
            Sprint sprint = sprintRepository.findById(CodeUtils.decodeToInteger("SP", request.getSprintCode()))
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

        return mapToDto(savedBug);
    }

    @Override
    public void deleteBugByiD(Integer id) {
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));
        bugRepository.delete(bug);
    }

    private BugResponseDto mapToDto(Bug bug) {
        BugResponseDto dto = new BugResponseDto();
        dto.setId(bug.getId());
        dto.setBugCode(bug.getBugCode());
        dto.setTitle(bug.getTitle());
        dto.setDescription(bug.getDescription());
        dto.setSprintCode(bug.getSprintid() != null ? CodeUtils.encode("SP", bug.getSprintid().getId()) : null);
        dto.setStoryCode(bug.getStoryid() != null ? CodeUtils.encode("S", bug.getStoryid().getId()) : null);
        dto.setAssignedUserCode(bug.getAssignedto() != null ? CodeUtils.encode("U", bug.getAssignedto().getId()) : null);
        dto.setBugstatus(bug.getBugstatus());
        dto.setPriority(bug.getPriority());
        dto.setOriginalestimatehours(bug.getOriginalestimatehours());
        dto.setRemainingestimatehours(bug.getRemainingestimatehours());
        dto.setReopencount(bug.getReopencount());

        java.util.List<com.sprint.SprintLite.dto.CommentDto> commentDtos = commentRepository
                .findByEntitytypeAndEntityid(com.sprint.SprintLite.entity.enums.EntityType.BUG, bug.getId())
                .stream()
                .map(c -> {
                    com.sprint.SprintLite.dto.CommentDto cDto = new com.sprint.SprintLite.dto.CommentDto();
                    cDto.setUserCode(c.getCreatedBy());
                    cDto.setText(c.getComment());
                    cDto.setCreatedAt(c.getCreatedAt());
                    return cDto;
                }).toList();
        dto.setComments(commentDtos);

        return dto;
    }
}