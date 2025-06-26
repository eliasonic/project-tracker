package com.buildmaster.project_tracker.dto;

import com.buildmaster.project_tracker.model.jpa.Project;
import com.buildmaster.project_tracker.model.jpa.Project.ProjectStatus;

public record ProjectListDTO(
        Long id,
        String name,
        ProjectStatus status
) {
    public static ProjectListDTO mapToDto(Project project) {
        return new ProjectListDTO(
                project.getId(),
                project.getName(),
                project.getStatus()
        );
    }
}