package com.buildmaster.project_tracker.audit.impl;

import com.buildmaster.project_tracker.audit.AuditActionType;
import com.buildmaster.project_tracker.audit.AuditLogger;
import com.buildmaster.project_tracker.model.mongo.AuditLog;
import com.buildmaster.project_tracker.repository.mongo.AuditLogRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Class for recording audit logs in the system.
 *
 * @see AuditLog
 * @see AuditActionType
 */
@Component
public class AuditLoggerImpl implements AuditLogger {
    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public AuditLoggerImpl(AuditLogRepository auditLogRepository, ObjectMapper objectMapper) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = objectMapper;
    }

    public void log(AuditActionType actionType, String entityType, String entityId, String actorName, Object payload) {
        AuditLog auditLog = new AuditLog();
        auditLog.setActionType(actionType.name());
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setActorName(actorName);

        Map<String, Object> payloadMap = objectMapper.convertValue(payload, new TypeReference<Map<String, Object>>() {});
        auditLog.setPayload(payloadMap);

        auditLogRepository.save(auditLog);
    }
}
