package com.buildmaster.project_tracker.security;

import com.buildmaster.project_tracker.audit.AuditActionType;
import com.buildmaster.project_tracker.audit.AuthAuditLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * This class handles the failure of OAuth2 authentication.
 */
@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    private final AuthAuditLogger authAuditLogger;

    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        authAuditLogger.logUnauthorizedAccess(AuditActionType.UNAUTHORIZED_ACCESS, exception, request);

        response.sendRedirect("/auth/oauth2/failure?error=" + URLEncoder.encode(exception.getMessage(), "UTF-8"));
    }
}
