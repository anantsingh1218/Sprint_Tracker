package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service;

import com.sprint.SprintLite.dto.CreateTaskRequest;
import com.sprint.SprintLite.dto.TaskResponseDto;

import java.util.List;

public interface ITaskService {
    TaskResponseDto createTask(CreateTaskRequest request);

    TaskResponseDto getTaskById(Long id);

    List<TaskResponseDto> getAllTasks();

    List<TaskResponseDto> getTasksBySprintId(Long sprintId);

    TaskResponseDto updateTask(Integer id, CreateTaskRequest request);

    void deleteTask(Long id);
}
