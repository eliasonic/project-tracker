package com.buildmaster.project_tracker.dto;

import com.buildmaster.project_tracker.model.jpa.Project.ProjectStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ProjectDTO(
        Long id,

        @NotBlank @Size(max = 100)
        String name,

        @Size(max = 500)
        String description,

        @Future
        LocalDate deadline,

        ProjectStatus status
) {}

