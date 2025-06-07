package com.buildmaster.project_tracker.dto;

import com.buildmaster.project_tracker.model.jpa.Task.TaskStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

public record TaskDTO(
        Long id,

        @NotBlank @Size(max = 100)
        String title,

        @Size(max = 500)
        String description,

        TaskStatus status,

        @FutureOrPresent
        LocalDate dueDate,

        Long projectId,

        Set<Long> developerIds
) {}