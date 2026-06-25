package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.ITaskService;
import com.sprint.SprintLite.dto.CreateTaskRequest;
import com.sprint.SprintLite.entity.*;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    public Task createTask(CreateTaskRequest request) {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users userid = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        Sprint sprint = sprintRepository.findById(request.getSprintid())
                .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));

        Story story = storyRepository.findById(request.getStoryid())
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

        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksBySprintId(Long sprintId) {
        return taskRepository.findBySprintid_Id(sprintId);
    }

    @Override
    public Task updateTask(Integer id, CreateTaskRequest request) {

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

        if (request.getUserId() != null) {
            Users assignedUser = usersRepository.findById(request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found"));

            existingTask.setUserid(assignedUser);
        }

        if (request.getSprintid() != null) {
            Sprint sprint = sprintRepository.findById(request.getSprintid())
                    .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));

            existingTask.setSprintid(sprint);
        }

        if (request.getStoryid() != null) {
            Story story = storyRepository.findById(request.getStoryid())
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

        if (request.getComments() != null && !request.getComments().isBlank()) {
            Comment comment = new Comment();
            comment.setComment(request.getComments());
            comment.setEntitytype(EntityType.TASK);
            comment.setEntityid(id);
            comment.setCreatedBy(username);

            commentRepository.save(comment);
        }

        return taskRepository.save(existingTask);
    }
    @Override
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }
}