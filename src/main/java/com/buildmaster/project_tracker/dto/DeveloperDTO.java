package com.buildmaster.project_tracker.dto;

import jakarta.validation.constraints.*;

public record DeveloperDTO(
        Long id,

        @NotBlank @Size(max = 100)
        String name,

        @Email @NotBlank
        String email,

        @Size(max = 500)
        String skills
) {}
