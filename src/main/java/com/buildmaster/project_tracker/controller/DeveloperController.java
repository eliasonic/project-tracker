package com.buildmaster.project_tracker.controller;

import com.buildmaster.project_tracker.dto.DeveloperDTO;
import com.buildmaster.project_tracker.service.DeveloperService;
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
@RequestMapping("/api/v1/developers")
@RequiredArgsConstructor
public class DeveloperController {
    private final DeveloperService developerService;

    @GetMapping
    @Operation(summary = "Get all developers with pagination")
    public ResponseEntity<Page<DeveloperDTO>> getAllDevelopers(Pageable pageable) {
        return ResponseEntity.ok(developerService.getAllDevelopers(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a developer by ID")
    public ResponseEntity<DeveloperDTO> getDeveloperById(@PathVariable Long id) {
        return ResponseEntity.ok(developerService.getDeveloperById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new developer")
    public ResponseEntity<DeveloperDTO> createDeveloper(@Valid @RequestBody DeveloperDTO developerDTO,
            @RequestHeader("X-Actor-Name") String actorName) {
        DeveloperDTO createdDeveloper = developerService.createDeveloper(developerDTO, actorName);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDeveloper);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing developer")
    public ResponseEntity<DeveloperDTO> updateDeveloper(@PathVariable Long id, @Valid @RequestBody DeveloperDTO developerDTO,
            @RequestHeader("X-Actor-Name") String actorName) {
        return ResponseEntity.ok(developerService.updateDeveloper(id, developerDTO, actorName));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a developer by ID")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id, @RequestHeader("X-Actor-Name") String actorName) {
        developerService.deleteDeveloper(id, actorName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/top")
    @Operation(summary = "Get top 5 developers")
    public ResponseEntity<List<DeveloperDTO>> getTopDevelopers() {
        return ResponseEntity.ok(developerService.getTopDevelopers());
    }
}
