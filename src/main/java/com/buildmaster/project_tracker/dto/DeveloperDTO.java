package com.buildmaster.project_tracker.dto;

import jakarta.validation.constraints.*;

public record DeveloperDTO(
        Long id,

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be less than 100 characters")
        String name,

        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required")
        String email,

        @Size(max = 500, message = "Skills must be less than 500 characters")
        String skills
) {}
