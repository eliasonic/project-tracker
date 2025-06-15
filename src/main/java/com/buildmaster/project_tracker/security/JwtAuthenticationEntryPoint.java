package com.buildmaster.project_tracker.security;

import com.buildmaster.project_tracker.audit.AuditActionType;
import com.buildmaster.project_tracker.audit.AuthAuditLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthAuditLogger authAuditLogger;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        authAuditLogger.logUnauthorizedAccess(AuditActionType.UNAUTHORIZED_ACCESS, authException, request);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(
                String.format("""
                {
                    "status": 401,
                    "error": "Unauthorized",
                    "message": "%s",
                    "path": "%s"
                }
                """, authException.getMessage(), request.getRequestURI())
        );
    }
}
