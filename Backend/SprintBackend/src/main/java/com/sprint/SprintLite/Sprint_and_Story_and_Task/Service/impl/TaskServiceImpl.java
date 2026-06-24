package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.ITaskService;
import com.sprint.SprintLite.dto.CreateTaskRequest;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.Story;
import com.sprint.SprintLite.entity.Task;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.repository.SprintRepository;
import com.sprint.SprintLite.repository.StoryRepository;
import com.sprint.SprintLite.repository.TaskRepository;
import com.sprint.SprintLite.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements ITaskService {

    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;
    private final StoryRepository storyRepository;
    private final UsersRepository usersRepository;

    @Override
    public Task createTask(CreateTaskRequest request) {

        Users user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Sprint sprint = sprintRepository.findById(request.getSprintid())
                .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));

        Story story = storyRepository.findById(request.getStoryid())
                .orElseThrow(() -> new IllegalArgumentException("Story not found"));

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setBody(request.getBody());

        task.setUserid(user);
        task.setSprintid(sprint);
        task.setStoryid(story);

        task.setTaskstatus(request.getTaskstatus());
        task.setPriority(request.getPriority());

        task.setOriginalestimatehours(request.getOriginalestimatehours());
        task.setRemainingestimatehours(request.getRemainingestimatehours());

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
    public Task updateTask(Long id, CreateTaskRequest request) {

        Task task = getTaskById(id);

        Users user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Sprint sprint = sprintRepository.findById(request.getSprintid())
                .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));

        Story story = storyRepository.findById(request.getStoryid())
                .orElseThrow(() -> new IllegalArgumentException("Story not found"));

        task.setTitle(request.getTitle());
        task.setBody(request.getBody());

        task.setUserid(user);
        task.setSprintid(sprint);
        task.setStoryid(story);

        task.setTaskstatus(request.getTaskstatus());
        task.setPriority(request.getPriority());

        task.setOriginalestimatehours(request.getOriginalestimatehours());
        task.setRemainingestimatehours(request.getRemainingestimatehours());

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }
}