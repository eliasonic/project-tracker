package com.buildmaster.project_tracker.service.impl;

import com.buildmaster.project_tracker.audit.AuditLogger;
import com.buildmaster.project_tracker.dto.ProjectDTO;
import com.buildmaster.project_tracker.exception.ResourceNotFoundException;
import com.buildmaster.project_tracker.mapper.Mapper;
import com.buildmaster.project_tracker.model.jpa.Project;
import com.buildmaster.project_tracker.repository.jpa.ProjectRepository;
import com.buildmaster.project_tracker.service.ProjectService;
import com.buildmaster.project_tracker.audit.AuditActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing projects.
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final Mapper<Project, ProjectDTO> projectMapper;
    private final AuditLogger auditLogger;

    @Override
    @Cacheable(value = "projects", key = "{#pageable.pageNumber,#pageable.pageSize,#pageable.sort}")
    public Page<ProjectDTO> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::mapToDTO);
    }

    @Override
    @Cacheable(value = "project", key = "#id")
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        return projectMapper.mapToDTO(project);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"projects", "project"}, allEntries = true)
    public ProjectDTO createProject(ProjectDTO projectDTO, String actorName) {
        Project project = projectMapper.mapToEntity(projectDTO);
        Project savedProject = projectRepository.save(project);

        auditLogger.log(AuditActionType.CREATE, "Project", savedProject.getId().toString(), actorName, projectDTO);
        return projectMapper.mapToDTO(savedProject);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"projects", "project"}, key = "#id")
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO, String actorName) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        projectMapper.update(projectDTO, existingProject);
        Project updatedProject = projectRepository.save(existingProject);

        auditLogger.log(AuditActionType.UPDATE, "Project", updatedProject.getId().toString(), actorName, projectDTO);
        return projectMapper.mapToDTO(updatedProject);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"projects", "project"}, allEntries = true)
    public void deleteProject(Long id, String actorName) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        ProjectDTO projectDTO = projectMapper.mapToDTO(project);
        auditLogger.log(AuditActionType.DELETE, "Project", id.toString(), actorName, projectDTO);
        projectRepository.delete(project);
    }

    @Override
    public List<ProjectDTO> getProjectsWithoutTasks() {
        return projectRepository.findProjectsWithoutTasks().stream()
                .map(projectMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}
