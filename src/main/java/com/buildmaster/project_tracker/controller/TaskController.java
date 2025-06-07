package com.buildmaster.project_tracker.controller;

import com.buildmaster.project_tracker.dto.TaskDTO;
import com.buildmaster.project_tracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Get all tasks with pagination")
    public ResponseEntity<Page<TaskDTO>> getAllTasks(Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasks(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO, @RequestHeader("X-Actor-Name") String actorName) {
        TaskDTO createdTask = taskService.createTask(taskDTO, actorName);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO,
            @RequestHeader("X-Actor-Name") String actorName) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO, actorName));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task by ID")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @RequestHeader("X-Actor-Name") String actorName) {
        taskService.deleteTask(id, actorName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get tasks by project ID with pagination")
    public ResponseEntity<Page<TaskDTO>> getTasksByProject(@PathVariable Long projectId, Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId, pageable));
    }

    @GetMapping("/developer/{developerId}")
    @Operation(summary = "Get tasks by developer ID with pagination")
    public ResponseEntity<Page<TaskDTO>> getTasksByDeveloper(@PathVariable Long developerId, Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByDeveloper(developerId, pageable));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue tasks")
    public ResponseEntity<List<TaskDTO>> getOverdueTasks() {
        return ResponseEntity.ok(taskService.getOverdueTasks());
    }
}