package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl;

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
        task.setBody(request.getBody());

        task.setUserid(userid);
        task.setSprintid(sprint);
        task.setStoryid(story);

        task.setTaskstatus(request.getTaskstatus());
        task.setPriority(request.getPriority());

        task.setOriginalestimatehours(request.getOriginalestimatehours());
        task.setRemainingestimatehours(request.getRemainingestimatehours());
        task.setCreatedBy(username);

        Task t1=taskRepository.save(task);

        TaskResponseDto response = new TaskResponseDto();
        response.setTitle(t1.getTitle());
        response.setBody(t1.getBody());
        response.setUserCode(CodeUtils.encode("U", t1.getId()));
        response.setSprintCode(CodeUtils.encode("SP", sprint.getId()));
        response.setStoryCode(CodeUtils.encode("S", story.getId()));
        response.setTaskstatus(t1.getTaskstatus());
        response.setOriginalestimatehours(t1.getOriginalestimatehours());
        response.setRemainingestimatehours(t1.getRemainingestimatehours());
        response.setTaskstatus(t1.getTaskstatus());
        response.setPriority(t1.getPriority());
        return response;
    }
    @Override
    public TaskResponseDto getTaskById(Long id) {

        Task task = taskRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Task not found"));

        TaskResponseDto response = new TaskResponseDto();

        response.setUserCode(CodeUtils.encode("U", task.getId()));
        response.setTitle(task.getTitle());
        response.setBody(task.getBody());
        response.setUserCode(CodeUtils.encode("U", task.getUserid().getId()));
        response.setSprintCode(CodeUtils.encode("SP", task.getSprintid().getId()));
        response.setStoryCode(CodeUtils.encode("S", task.getStoryid().getId()));
        response.setTaskstatus(task.getTaskstatus());
        response.setPriority(task.getPriority());
        response.setOriginalestimatehours(task.getOriginalestimatehours());
        response.setRemainingestimatehours(task.getRemainingestimatehours());

        return response;
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> {
                    TaskResponseDto response = new TaskResponseDto();

                    response.setUserCode(CodeUtils.encode("U", task.getId()));
                    response.setTitle(task.getTitle());
                    response.setBody(task.getBody());
                    response.setUserCode(CodeUtils.encode("U", task.getUserid().getId()));
                    response.setSprintCode(CodeUtils.encode("SP", task.getSprintid().getId()));
                    response.setStoryCode(CodeUtils.encode("S", task.getStoryid().getId()));
                    response.setTaskstatus(task.getTaskstatus());
                    response.setPriority(task.getPriority());
                    response.setOriginalestimatehours(task.getOriginalestimatehours());
                    response.setRemainingestimatehours(task.getRemainingestimatehours());

                    return response;
                })
                .toList();
    }
    @Override
    public List<TaskResponseDto> getTasksBySprintId(Long sprintId) {
        return taskRepository.findBySprintid_Id(sprintId)
                .stream()
                .map(task -> {
                    TaskResponseDto response = new TaskResponseDto();

                    response.setUserCode(CodeUtils.encode("U", task.getId()));
                    response.setTitle(task.getTitle());
                    response.setBody(task.getBody());
                    response.setUserCode(CodeUtils.encode("U", task.getUserid().getId()));
                    response.setSprintCode(CodeUtils.encode("SP", task.getSprintid().getId()));
                    response.setStoryCode(CodeUtils.encode("S", task.getStoryid().getId()));
                    response.setTaskstatus(task.getTaskstatus());
                    response.setPriority(task.getPriority());
                    response.setOriginalestimatehours(task.getOriginalestimatehours());
                    response.setRemainingestimatehours(task.getRemainingestimatehours());

                    return response;
                })
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

        if (request.getBody() != null) {
            existingTask.setBody(request.getBody());
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

        if (request.getTaskstatus() != null) {
            existingTask.setTaskstatus(request.getTaskstatus());
        }

        if (request.getPriority() != null) {
            existingTask.setPriority(request.getPriority());
        }

        if (request.getOriginalestimatehours() != null) {
            existingTask.setOriginalestimatehours(request.getOriginalestimatehours());
        }

        if (request.getRemainingestimatehours() != null) {
            existingTask.setRemainingestimatehours(request.getRemainingestimatehours());
        }

        existingTask.setUpdatedBy(username);


        Comment savedComment = null;
        if (request.getComments() != null && !request.getComments().isBlank()) {

            Comment comment = new Comment();
            comment.setComment(request.getComments());
            comment.setEntitytype(EntityType.TASK);
            comment.setEntityid(id);
            comment.setCreatedBy(username);
            savedComment = commentRepository.save(comment);
        }
        Task updatedTask = taskRepository.save(existingTask);

        TaskResponseDto response = new TaskResponseDto();

        response.setUserCode(CodeUtils.encode("U", updatedTask.getId()));
        response.setTitle(updatedTask.getTitle());
        response.setBody(updatedTask.getBody());

        response.setUserCode(
                CodeUtils.encode("U", updatedTask.getUserid() != null ? updatedTask.getUserid().getId() : null)
        );

        response.setSprintCode(
                CodeUtils.encode("SP", updatedTask.getSprintid() != null ? updatedTask.getSprintid().getId() : null)
        );

        response.setStoryCode(
                CodeUtils.encode("S", updatedTask.getStoryid() != null ? updatedTask.getStoryid().getId() : null)
        );

        response.setTaskstatus(updatedTask.getTaskstatus());
        response.setPriority(updatedTask.getPriority());

        response.setOriginalestimatehours(
                updatedTask.getOriginalestimatehours()
        );

        response.setRemainingestimatehours(
                updatedTask.getRemainingestimatehours()
        );
        if (request.getComments() != null) {
            response.setComments(savedComment.getComment());
        }

        return response;


    }
    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }
}