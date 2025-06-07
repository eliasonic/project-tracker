package com.buildmaster.project_tracker.service.impl;

import com.buildmaster.project_tracker.audit.AuditActionType;
import com.buildmaster.project_tracker.audit.AuditLogger;
import com.buildmaster.project_tracker.dto.DeveloperDTO;
import com.buildmaster.project_tracker.exception.ResourceNotFoundException;
import com.buildmaster.project_tracker.mapper.Mapper;
import com.buildmaster.project_tracker.model.jpa.Developer;
import com.buildmaster.project_tracker.repository.jpa.DeveloperRepository;
import com.buildmaster.project_tracker.service.DeveloperService;
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
 * Service implementation for managing developers.
 */
@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {
    private final DeveloperRepository developerRepository;
    private final Mapper<Developer, DeveloperDTO> developerMapper;
    private final AuditLogger auditLogger;

    @Override
    @Cacheable(value = "developers", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    public Page<DeveloperDTO> getAllDevelopers(Pageable pageable) {
        return developerRepository.findAll(pageable)
                .map(developerMapper::mapToDTO);
    }

    @Override
    @Cacheable(value = "developer", key = "#id")
    public DeveloperDTO getDeveloperById(Long id) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer", "id", id));
        return developerMapper.mapToDTO(developer);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"developers", "developer", "developerByEmail"}, allEntries = true)
    public DeveloperDTO createDeveloper(DeveloperDTO developerDTO, String actorName) {
        if (developerRepository.findByEmail(developerDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + developerDTO.email());
        }

        Developer developer = developerMapper.mapToEntity(developerDTO);
        Developer savedDeveloper = developerRepository.save(developer);

        auditLogger.log(AuditActionType.CREATE, "Developer", savedDeveloper.getId().toString(), actorName, developerDTO);
        return developerMapper.mapToDTO(savedDeveloper);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"developers", "developer", "developerByEmail"}, key = "#id")
    public DeveloperDTO updateDeveloper(Long id, DeveloperDTO developerDTO, String actorName) {
        Developer existingDeveloper = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer", "id", id));

        if (!existingDeveloper.getEmail().equals(developerDTO.email())) {
            developerRepository.findByEmail(developerDTO.email())
                    .ifPresent(d -> {
                        throw new IllegalArgumentException("Email already exists: " + developerDTO.email());
                    });
        }

        developerMapper.update(developerDTO, existingDeveloper);
        Developer updatedDeveloper = developerRepository.save(existingDeveloper);

        auditLogger.log(AuditActionType.UPDATE, "Developer", updatedDeveloper.getId().toString(), actorName, developerDTO);
        return developerMapper.mapToDTO(updatedDeveloper);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"developers", "developer", "developerByEmail"}, allEntries = true)
    public void deleteDeveloper(Long id, String actorName) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer", "id", id));

        if (!developer.getTasks().isEmpty()) {
            throw new IllegalStateException("Cannot delete developer with assigned tasks");
        }

        DeveloperDTO developerDTO = developerMapper.mapToDTO(developer);
        auditLogger.log(AuditActionType.DELETE, "Developer", id.toString(), actorName, developerDTO);

        developerRepository.delete(developer);
    }

    @Override
    @Cacheable(value = "topDevelopers")
    public List<DeveloperDTO> getTopDevelopers() {
        return developerRepository.findTop5DevelopersByTaskCount().stream()
                .map(developerMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}
