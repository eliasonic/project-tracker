package com.buildmaster.project_tracker.audit.impl;

import com.buildmaster.project_tracker.audit.AuditActionType;
import com.buildmaster.project_tracker.audit.AuthAuditLogger;
import com.buildmaster.project_tracker.model.mongo.AuthAuditLog;
import com.buildmaster.project_tracker.repository.mongo.AuthAuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthAuditLoggerImpl implements AuthAuditLogger {
    private final AuthAuditLogRepository authAuditLogRepository;

    /**
     * Unified logging method for all auth events
     *
     * @param actionType Type of audit action (required)
     * @param username User identifier (nullable for system events)
     * @param success Whether the action succeeded
     * @param details Contextual details (error message, provider name, etc.)
     * @param request HTTP request (for extracting metadata)
     */
    public void log(
            AuditActionType actionType,
            String username,
            Boolean success,
            String details,
            HttpServletRequest request
    ) {
        AuthAuditLog log = new AuthAuditLog();
        log.setActionType(actionType.name());
        log.setUsername(username);
        log.setSuccess(success);
        log.setDetails(details);
        log.setIpAddress(request.getRemoteAddr());
        log.setUserAgent(request.getHeader("User-Agent"));

        if (actionType == AuditActionType.OAUTH_LOGIN) {
            log.setEndpoint("/oauth2/callback/" + details);
        } else {
            log.setEndpoint(request.getRequestURI());
        }

        authAuditLogRepository.save(log);
    }

    // Helper methods for backward compatibility
    public void logRegistrationAttempt(AuditActionType actionType, String email, boolean success, String error, HttpServletRequest request) {
        log(actionType, email, success, error, request);
    }

    public void logLoginAttempt(AuditActionType actionType, String email, boolean success, String error, HttpServletRequest request) {
        log(actionType, email, success, error, request);
    }

    public void logOAuthLogin(AuditActionType actionType, String email, String provider, HttpServletRequest request) {
        log(actionType, email, true, provider, request);
    }

    public void logUnauthorizedAccess(AuditActionType actionType, AuthenticationException ex, HttpServletRequest request) {
        log(actionType, null, false, ex.getMessage(), request);
    }
}
