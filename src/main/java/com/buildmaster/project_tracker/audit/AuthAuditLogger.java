package com.buildmaster.project_tracker.audit;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;

public interface AuthAuditLogger {

    void logRegistrationAttempt(AuditActionType actionType, String email, boolean success, String error, HttpServletRequest request);

    void logLoginAttempt(AuditActionType actionType, String email, boolean success, String error, HttpServletRequest request);

    void logOAuthLogin(AuditActionType actionType, String email, String provider, HttpServletRequest request);

    void logUnauthorizedAccess(AuditActionType actionType, AuthenticationException ex, HttpServletRequest request);
}
