package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service;

import com.sprint.SprintLite.dto.CreateTaskRequest;
import com.sprint.SprintLite.entity.Task;

import java.util.List;

public interface ITaskService {
    Task createTask(CreateTaskRequest request);

    Task getTaskById(Long id);

    List<Task> getAllTasks();

    List<Task> getTasksBySprintId(Long sprintId);

    Task updateTask(Long id, CreateTaskRequest request);

    void deleteTask(Long id);
}
