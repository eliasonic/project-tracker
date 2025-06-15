package com.buildmaster.project_tracker.service.impl;

import com.buildmaster.project_tracker.model.mongo.AuditLog;
import com.buildmaster.project_tracker.model.mongo.AuthAuditLog;
import com.buildmaster.project_tracker.repository.mongo.AuditLogRepository;
import com.buildmaster.project_tracker.repository.mongo.AuthAuditLogRepository;
import com.buildmaster.project_tracker.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service implementation for managing audit logs.
 */
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final AuthAuditLogRepository authAuditLogRepository;

    @Override
    public Page<AuditLog> getAllLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    public List<AuditLog> getFilteredLogs(String entityType, String actorName) {
        if (entityType != null) {
            return auditLogRepository.findByEntityType(entityType);
        } else if (actorName != null) {
            return auditLogRepository.findByActorName(actorName);
        }
        return List.of();
    }

    public List<AuthAuditLog> getAuthLogs() {
        return authAuditLogRepository.findAll();
    }
}