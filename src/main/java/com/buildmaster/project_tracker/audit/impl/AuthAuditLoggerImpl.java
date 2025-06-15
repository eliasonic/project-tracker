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

    public void logRegistrationAttempt(AuditActionType actionType, String email, boolean success, String error, HttpServletRequest request) {
        AuthAuditLog log = new AuthAuditLog();
        log.setActionType(actionType.name());
        log.setUsername(email);
        log.setSuccess(success);
        log.setErrorMessage(error);
        log.setIpAddress(request.getRemoteAddr());
        authAuditLogRepository.save(log);
    }

    public void logLoginAttempt(AuditActionType actionType, String email, boolean success, String error, HttpServletRequest request) {
        AuthAuditLog log = new AuthAuditLog();
        log.setActionType(actionType.name());
        log.setUsername(email);
        log.setSuccess(success);
        log.setErrorMessage(error);
        log.setIpAddress(request.getRemoteAddr());
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setEndpoint(request.getRequestURI());
        authAuditLogRepository.save(log);
    }

    public void logOAuthLogin(AuditActionType actionType, String email, String provider, HttpServletRequest request) {
        AuthAuditLog log = new AuthAuditLog();
        log.setActionType(actionType.name());
        log.setUsername(email);
        log.setSuccess(true);
        log.setIpAddress(request.getRemoteAddr());
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setEndpoint("/oauth2/callback/" + provider);
        authAuditLogRepository.save(log);
    }

    public void logUnauthorizedAccess(AuditActionType actionType, AuthenticationException ex, HttpServletRequest request) {
        AuthAuditLog log = new AuthAuditLog();
        log.setActionType(actionType.name());
        log.setSuccess(false);
        log.setErrorMessage(ex.getMessage());
        log.setIpAddress(request.getRemoteAddr());
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setEndpoint(request.getRequestURI());
        authAuditLogRepository.save(log);
    }
}
