package com.buildmaster.project_tracker.service.impl;

import com.buildmaster.project_tracker.model.mongo.AuditLog;
import com.buildmaster.project_tracker.repository.mongo.AuditLogRepository;
import com.buildmaster.project_tracker.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service implementation for managing audit logs.
 */
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository auditLogRepository;

    @Override
    public Page<AuditLog> getAllLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    @Override
    public List<AuditLog> getLogsByEntityType(String entityType) {
        return auditLogRepository.findByEntityType(entityType);
    }

    @Override
    public List<AuditLog> getLogsByActorName(String actorName) {
        return auditLogRepository.findByActorName(actorName);
    }
}