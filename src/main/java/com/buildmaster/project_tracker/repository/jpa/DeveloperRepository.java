package com.buildmaster.project_tracker.repository.jpa;

import com.buildmaster.project_tracker.model.jpa.Developer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Page<Developer> findAll(Pageable pageable);

    Optional<Developer> findByEmail(String email);

    @Query("SELECT d FROM Developer d JOIN d.tasks t GROUP BY d.id ORDER BY COUNT(t) DESC LIMIT 5")
    List<Developer> findTop5DevelopersByTaskCount();
}