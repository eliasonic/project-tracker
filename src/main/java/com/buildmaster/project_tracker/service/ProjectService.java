package com.buildmaster.project_tracker.service;

import com.buildmaster.project_tracker.dto.ProjectDTO;
import com.buildmaster.project_tracker.dto.ProjectListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    Page<ProjectListDTO> getAllProjects(Pageable pageable);

    ProjectDTO getProjectById(Long id);

    ProjectDTO createProject(ProjectDTO projectDTO, String actorName);

    ProjectDTO updateProject(Long id, ProjectDTO projectDTO, String actorName);

    void deleteProject(Long id, String actorName);

    List<ProjectDTO> getProjectsWithoutTasks();
}
