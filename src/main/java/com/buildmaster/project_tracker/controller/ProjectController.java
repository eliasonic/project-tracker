package com.buildmaster.project_tracker.controller;

import com.buildmaster.project_tracker.dto.ProjectDTO;
import com.buildmaster.project_tracker.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    @Operation(summary = "Get all projects with pagination")
    public ResponseEntity<Page<ProjectDTO>> getAllProjects(Pageable pageable) {
        return ResponseEntity.ok(projectService.getAllProjects(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a project by ID")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new project")
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO,
            @RequestHeader("X-Actor-Name") String actorName) {
        ProjectDTO createdProject = projectService.createProject(projectDTO, actorName);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDTO projectDTO,
            @RequestHeader("X-Actor-Name") String actorName) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDTO, actorName));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project by ID")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, @RequestHeader("X-Actor-Name") String actorName) {
        projectService.deleteProject(id, actorName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/without-tasks")
    @Operation(summary = "Get projects without tasks")
    public ResponseEntity<List<ProjectDTO>> getProjectsWithoutTasks() {
        return ResponseEntity.ok(projectService.getProjectsWithoutTasks());
    }
}