package com.buildmaster.project_tracker.service;

import com.buildmaster.project_tracker.dto.DeveloperDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeveloperService {
    Page<DeveloperDTO> getAllDevelopers(Pageable pageable);

    DeveloperDTO getDeveloperById(Long id);

    DeveloperDTO createDeveloper(DeveloperDTO developerDTO, String actorName);

    DeveloperDTO updateDeveloper(Long id, DeveloperDTO developerDTO, String actorName);

    void deleteDeveloper(Long id, String actorName);

    List<DeveloperDTO> getTopDevelopers();
}