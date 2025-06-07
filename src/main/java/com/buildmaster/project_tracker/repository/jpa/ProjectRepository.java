package com.buildmaster.project_tracker.repository.jpa;

import com.buildmaster.project_tracker.model.jpa.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAll(Pageable pageable);

    @Query("SELECT p FROM Project p WHERE SIZE(p.tasks) = 0")
    List<Project> findProjectsWithoutTasks();
}
