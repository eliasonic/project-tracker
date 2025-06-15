package com.buildmaster.project_tracker.audit;

/**
 * Enum representing different types of audit actions.
 */
public enum AuditActionType {
    CREATE,
    UPDATE,
    DELETE,
    ASSIGN,
    REGISTRATION_ATTEMPT,
    LOGIN_ATTEMPT,
    OAUTH_LOGIN,
    UNAUTHORIZED_ACCESS
}