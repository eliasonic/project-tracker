package com.buildmaster.project_tracker.audit;

public interface AuditLogger {
    void log(AuditActionType actionType, String entityType, String entityId, String actorName, Object payload);
}
