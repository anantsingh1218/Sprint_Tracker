package com.sprint.SprintLite.Sprint_and_Story_and_Task.Controller;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.ITaskService;
import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl.TaskServiceImpl;
import com.sprint.SprintLite.dto.CreateTaskRequest;
import com.sprint.SprintLite.dto.RegisterResponseDto;
import com.sprint.SprintLite.dto.TaskResponseDto;
import com.sprint.SprintLite.dto.GetAllResponseDto;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Task;
import com.sprint.SprintLite.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sprint.SprintLite.util.CodeUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/task")

@RequiredArgsConstructor
public class TaskController {
    private final TaskServiceImpl taskService;
    private final TaskRepository taskRepository;

    @PostMapping("/add")
    public ResponseEntity<TaskResponseDto> addTask(
            @RequestBody CreateTaskRequest request) {

        TaskResponseDto task = taskService.createTask(request);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(
            @PathVariable String id) {

        TaskResponseDto task = taskService.getTaskById(CodeUtils.decodeToLong("T", id));
        return ResponseEntity.ok(task);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {

        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksBySprint(
            @PathVariable String sprintId) {

        List<TaskResponseDto> tasks =
                taskService.getTasksBySprintId(CodeUtils.decodeToLong("SP", sprintId));

        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable String id,
            @RequestBody CreateTaskRequest request) {

        TaskResponseDto updatedTask =
                taskService.updateTask(CodeUtils.decodeToInteger("T", id), request);

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RegisterResponseDto> deleteTask(
            @PathVariable String id) {

        taskService.deleteTask(CodeUtils.decodeToLong("T", id));

        return ResponseEntity.ok(new RegisterResponseDto("Task deleted successfully"));
    }

    @GetMapping("/getAllTasks")
    public ResponseEntity<List<GetAllResponseDto>> getAllProducts(){
        List<Task> taskList = taskRepository.findAll();
        if (taskList.isEmpty()){
            throw new EntityNotFoundException("No Tasks registered");
        }
        List<GetAllResponseDto> getAllResponseDtoList = new ArrayList<GetAllResponseDto>();
        taskList.forEach(task -> {
            GetAllResponseDto getAllResponseDto = new GetAllResponseDto(task.getId(), task.getTitle());
            getAllResponseDtoList.add(getAllResponseDto);
        });
        return ResponseEntity.ok(getAllResponseDtoList);
    }

}