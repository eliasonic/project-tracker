package com.buildmaster.project_tracker.repository.jpa;

import com.buildmaster.project_tracker.model.jpa.Task;
import com.buildmaster.project_tracker.model.jpa.Task.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAll(Pageable pageable);

    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    Page<Task> findByDevelopersId(Long developerId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.dueDate < CURRENT_DATE AND t.status <> 'DONE'")
    List<Task> findOverdueTasks();
}
