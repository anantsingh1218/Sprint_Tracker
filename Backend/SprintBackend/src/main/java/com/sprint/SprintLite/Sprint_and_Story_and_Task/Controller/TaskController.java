package com.sprint.SprintLite.Sprint_and_Story_and_Task.Controller;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.ITaskService;
import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl.TaskServiceImpl;
import com.sprint.SprintLite.dto.CreateTaskRequest;
import com.sprint.SprintLite.dto.GetAllResponseDto;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Task;
import com.sprint.SprintLite.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

    @RestController
    @RequestMapping("/task")

    @RequiredArgsConstructor
    public class TaskController {
        private final TaskServiceImpl taskService;
        private final TaskRepository taskRepository;

        @PostMapping("/add")
        public ResponseEntity<Task> addTask(
                @RequestBody CreateTaskRequest request) {

            Task task = taskService.createTask(request);
            return ResponseEntity.ok(task);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Task> getTask(
                @PathVariable Long id) {

            Task task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        }

        @GetMapping("/all")
        public ResponseEntity<List<Task>> getAllTasks() {

            List<Task> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(tasks);
        }

        @GetMapping("/sprint/{sprintId}")
        public ResponseEntity<List<Task>> getTasksBySprint(
                @PathVariable Long sprintId) {

            List<Task> tasks =
                    taskService.getTasksBySprintId(sprintId);

            return ResponseEntity.ok(tasks);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Task> updateTask(
                @PathVariable Integer id,
                @RequestBody CreateTaskRequest request) {

            Task updatedTask =
                    taskService.updateTask(id, request);

            return ResponseEntity.ok(updatedTask);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteTask(
                @PathVariable Long id) {

            taskService.deleteTask(id);

            return ResponseEntity.ok("Task deleted successfully");
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
