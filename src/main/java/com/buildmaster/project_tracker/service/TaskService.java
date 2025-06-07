package com.buildmaster.project_tracker.service;

import com.buildmaster.project_tracker.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    Page<TaskDTO> getAllTasks(Pageable pageable);

    TaskDTO getTaskById(Long id);

    TaskDTO createTask(TaskDTO taskDTO, String actorName);

    TaskDTO updateTask(Long id, TaskDTO taskDTO, String actorName);

    void deleteTask(Long id, String actorName);

    Page<TaskDTO> getTasksByProject(Long projectId, Pageable pageable);

    Page<TaskDTO> getTasksByDeveloper(Long developerId, Pageable pageable);

    List<TaskDTO> getOverdueTasks();
}