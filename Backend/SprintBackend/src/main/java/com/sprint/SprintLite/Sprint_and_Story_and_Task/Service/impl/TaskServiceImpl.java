package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl;

import java.time.Instant;
import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.ITaskService;
import com.sprint.SprintLite.dto.CreateTaskRequest;
import com.sprint.SprintLite.dto.TaskResponseDto;
import com.sprint.SprintLite.entity.*;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.sprint.SprintLite.util.CodeUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements ITaskService {

    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;
    private final StoryRepository storyRepository;
    private final UsersRepository usersRepository;
    private final CommentRepository commentRepository;

    private TaskResponseDto mapToDto(Task task) {
        TaskResponseDto response = new TaskResponseDto();
        response.setTaskCode(CodeUtils.encode("T", task.getId()));
        response.setTitle(task.getTitle());
        response.setBody(task.getBody());
        response.setUserCode(task.getUserid() != null ? CodeUtils.encode("U", task.getUserid().getId()) : null);
        response.setSprintCode(task.getSprintid() != null ? CodeUtils.encode("SP", task.getSprintid().getId()) : null);
        response.setStoryCode(task.getStoryid() != null ? CodeUtils.encode("S", task.getStoryid().getId()) : null);
        response.setTaskstatus(task.getTaskstatus());
        response.setPriority(task.getPriority());
        response.setOriginalestimatehours(task.getOriginalestimatehours());
        response.setRemainingestimatehours(task.getRemainingestimatehours());

        java.util.List<com.sprint.SprintLite.dto.CommentDto> commentDtos = commentRepository
                .findByEntitytypeAndEntityid(EntityType.TASK, task.getId())
                .stream()
                .map(c -> {
                    com.sprint.SprintLite.dto.CommentDto cDto = new com.sprint.SprintLite.dto.CommentDto();
                    cDto.setUserCode(c.getCreatedBy());
                    cDto.setText(c.getComment());
                    cDto.setCreatedAt(c.getCreatedAt());
                    return cDto;
                }).toList();
        response.setCommentsList(commentDtos);

        return response;
    }

    @Override
    public TaskResponseDto createTask(CreateTaskRequest request) {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users userid = usersRepository.findById(CodeUtils.decodeToInteger("U", request.getUserCode()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        Sprint sprint = sprintRepository.findById(CodeUtils.decodeToInteger("SP", request.getSprintCode()))
                .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));

        Story story = storyRepository.findById(CodeUtils.decodeToInteger("S", request.getStoryCode()))
                .orElseThrow(() -> new IllegalArgumentException("Story not found"));

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setBody(request.getDescription());

        task.setUserid(userid);
        task.setSprintid(sprint);
        task.setStoryid(story);

        task.setTaskstatus(request.getStatus());
        task.setPriority(request.getPriority());

        task.setOriginalestimatehours(request.getEstimatedHours());
        task.setRemainingestimatehours(request.getRemainingHours());
        task.setCreatedAt(Instant.now());
        task.setCreatedBy(username);

        Task t1=taskRepository.save(task);

        if (request.getComments() != null && !request.getComments().isBlank()) {
            Comment comment = new Comment();
            comment.setComment(request.getComments());
            comment.setEntitytype(EntityType.TASK);
            comment.setEntityid(t1.getId());
            comment.setCreatedBy(username);
            comment.setCreatedAt(Instant.now());
            commentRepository.save(comment);
        }

        return mapToDto(t1);
    }

    @Override
    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return mapToDto(task);
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<TaskResponseDto> getTasksBySprintId(Long sprintId) {
        return taskRepository.findBySprintid_Id(sprintId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public TaskResponseDto updateTask(Integer id, CreateTaskRequest request) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        if (request.getTitle() != null) {
            existingTask.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            existingTask.setBody(request.getDescription());
        }

        if (request.getUserCode() != null) {
            Users assignedUser = usersRepository.findById(CodeUtils.decodeToInteger("U", request.getUserCode()))
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found"));

            existingTask.setUserid(assignedUser);
        }

        if (request.getSprintCode() != null) {
            Sprint sprint = sprintRepository.findById(CodeUtils.decodeToInteger("SP", request.getSprintCode()))
                    .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));

            existingTask.setSprintid(sprint);
        }

        if (request.getStoryCode() != null) {
            Story story = storyRepository.findById(CodeUtils.decodeToInteger("S", request.getStoryCode()))
                    .orElseThrow(() -> new IllegalArgumentException("Story not found"));

            existingTask.setStoryid(story);
        }

        if (request.getStatus() != null) {
            existingTask.setTaskstatus(request.getStatus());
        }

        if (request.getPriority() != null) {
            existingTask.setPriority(request.getPriority());
        }

        if (request.getEstimatedHours() != null) {
            existingTask.setOriginalestimatehours(request.getEstimatedHours());
        }

        if (request.getRemainingHours() != null) {
            existingTask.setRemainingestimatehours(request.getRemainingHours());
        }

        existingTask.setUpdatedBy(username);
        existingTask.setUpdatedAt(Instant.now());

        System.out.println("RECEIVED COMMENTS IN UPDATE TASK: " + request.getComments());

        if (request.getComments() != null && !request.getComments().isBlank()) {
            Comment comment = new Comment();
            comment.setComment(request.getComments());
            comment.setEntitytype(EntityType.TASK);
            comment.setEntityid(id);
            comment.setCreatedBy(username);
            comment.setCreatedAt(Instant.now());
            commentRepository.save(comment);
        }

        Task updatedTask = taskRepository.save(existingTask);
        return mapToDto(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }
}