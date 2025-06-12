package com.buildmaster.project_tracker.dto;

import com.buildmaster.project_tracker.model.jpa.Task.TaskStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

public record TaskDTO(
        Long id,

        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must be less than 100 characters")
        String title,

        @Size(max = 500, message = "Description must be less than 500 characters")
        String description,

        TaskStatus status,

        @FutureOrPresent(message = "Due date must be in the present or future")
        LocalDate dueDate,

        Long projectId,

        Set<Long> developerIds
) {}