package com.buildmaster.project_tracker.service.impl;

import com.buildmaster.project_tracker.audit.AuditActionType;
import com.buildmaster.project_tracker.audit.AuditLogger;
import com.buildmaster.project_tracker.dto.TaskDTO;
import com.buildmaster.project_tracker.exception.ResourceNotFoundException;
import com.buildmaster.project_tracker.mapper.Mapper;
import com.buildmaster.project_tracker.model.jpa.Developer;
import com.buildmaster.project_tracker.model.jpa.Project;
import com.buildmaster.project_tracker.model.jpa.Task;
import com.buildmaster.project_tracker.repository.jpa.DeveloperRepository;
import com.buildmaster.project_tracker.repository.jpa.ProjectRepository;
import com.buildmaster.project_tracker.repository.jpa.TaskRepository;
import com.buildmaster.project_tracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for managing tasks.
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final DeveloperRepository developerRepository;
    private final Mapper<Task, TaskDTO> taskMapper;
    private final AuditLogger auditLogger;

    @Override
    @Cacheable(value = "tasks", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    public Page<TaskDTO> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(taskMapper::mapToDTO);
    }

    @Override
    @Cacheable(value = "task", key = "#id")
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        return taskMapper.mapToDTO(task);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public TaskDTO createTask(TaskDTO taskDTO, String actorName) {
        Project project = projectRepository.findById(taskDTO.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", taskDTO.projectId()));

        Task task = taskMapper.mapToEntity(taskDTO);
        task.setProject(project);

        if (taskDTO.developerIds() != null && !taskDTO.developerIds().isEmpty()) {
            Set<Developer> developers = new HashSet<>(developerRepository.findAllById(taskDTO.developerIds()));
            task.setDevelopers(developers);
        }

        Task savedTask = taskRepository.save(task);

        auditLogger.log(AuditActionType.CREATE, "Task", savedTask.getId().toString(), actorName, taskDTO);
        return taskMapper.mapToDTO(savedTask);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tasks", "task"}, key = "#id")
    public TaskDTO updateTask(Long id, TaskDTO taskDTO, String actorName) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        Project project = projectRepository.findById(taskDTO.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", taskDTO.projectId()));

        taskMapper.update(taskDTO, existingTask);
        existingTask.setProject(project);

        if (taskDTO.developerIds() != null) {
            Set<Developer> developers = new HashSet<>(developerRepository.findAllById(taskDTO.developerIds()));
            existingTask.setDevelopers(developers);
        }

        Task updatedTask = taskRepository.save(existingTask);

        auditLogger.log(AuditActionType.UPDATE, "Task", updatedTask.getId().toString(), actorName, taskDTO);
        return taskMapper.mapToDTO(updatedTask);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public void deleteTask(Long id, String actorName) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        TaskDTO taskDTO = taskMapper.mapToDTO(task);
        auditLogger.log(AuditActionType.DELETE, "Task", id.toString(), actorName, taskDTO);

        taskRepository.delete(task);
    }

    @Override
    @Cacheable(value = "projectTasks", key = "#projectId + '-' + #pageable.pageNumber")
    public Page<TaskDTO> getTasksByProject(Long projectId, Pageable pageable) {
        return taskRepository.findByProjectId(projectId, pageable)
                .map(taskMapper::mapToDTO);
    }

    @Override
    @Cacheable(value = "developerTasks", key = "#developerId  + '-' + #pageable.pageNumber")
    public Page<TaskDTO> getTasksByDeveloper(Long developerId, Pageable pageable) {
        return taskRepository.findByDevelopersId(developerId, pageable)
                .map(taskMapper::mapToDTO);
    }

    @Override
    @Cacheable(value = "overdueTasks")
    public List<TaskDTO> getOverdueTasks() {
        return taskRepository.findOverdueTasks().stream()
                .map(taskMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}
