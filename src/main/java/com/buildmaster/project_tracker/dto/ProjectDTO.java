package com.buildmaster.project_tracker.dto;

import com.buildmaster.project_tracker.model.jpa.Project.ProjectStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ProjectDTO(
        Long id,

        @NotBlank(message = "Project name is required")
        @Size(max = 100, message = "Project name must be less than 100 characters")
        String name,

        @Size(max = 500, message = "Description must be less than 500 characters")
        String description,

        @Future(message = "Deadline must be in the future")
        LocalDate deadline,

        ProjectStatus status
) {}

