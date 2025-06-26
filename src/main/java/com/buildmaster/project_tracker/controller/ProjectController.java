package com.buildmaster.project_tracker.controller;

import com.buildmaster.project_tracker.dto.ProjectDTO;
import com.buildmaster.project_tracker.dto.ProjectListDTO;
import com.buildmaster.project_tracker.security.CustomUserDetails;
import com.buildmaster.project_tracker.service.ProjectService;
import com.buildmaster.project_tracker.service.impl.ProjectMetricsService;
import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMetricsService metricsService;

    @GetMapping
    @Operation(summary = "Get all projects with pagination")
    public ResponseEntity<Page<ProjectListDTO>> getAllProjects(Pageable pageable) {
        return ResponseEntity.ok(projectService.getAllProjects(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a project by ID")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new project")
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Timer.Sample timer = metricsService.startTimer();
        try {
            ProjectDTO createdProject = projectService.createProject(projectDTO, userDetails.getUsername());
            metricsService.incrementCreateCounter();

            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } finally {
            metricsService.recordTimer(timer);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDTO projectDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDTO, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project by ID")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        projectService.deleteProject(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/without-tasks")
    @Operation(summary = "Get projects without tasks")
    public ResponseEntity<List<ProjectDTO>> getProjectsWithoutTasks() {
        return ResponseEntity.ok(projectService.getProjectsWithoutTasks());
    }
}