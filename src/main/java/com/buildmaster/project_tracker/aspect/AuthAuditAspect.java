package com.buildmaster.project_tracker.aspect;

import com.buildmaster.project_tracker.aspect.annotation.AuthAuditable;
import com.buildmaster.project_tracker.audit.AuditActionType;
import com.buildmaster.project_tracker.audit.AuthAuditLogger;
import com.buildmaster.project_tracker.dto.LoginRequest;
import com.buildmaster.project_tracker.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthAuditAspect {

    private final AuthAuditLogger authAuditLogger;

    @Around("@annotation(com.buildmaster.project_tracker.aspect.annotation.AuthAuditable)")
    public Object aroundAuth(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Aspect triggered for method: {}", pjp.getSignature().getName());

        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        AuthAuditable annotation = method.getAnnotation(AuthAuditable.class);

        log.info("Found annotation with action: {}", annotation.action());

        Object[] args = pjp.getArgs();
        String email = resolveEmail(args).orElse("unknown");
        HttpServletRequest request = resolveRequest(args).orElse(null);

        try {
            Object result = pjp.proceed();
            if (annotation.action() == AuditActionType.REGISTRATION_ATTEMPT) {
                authAuditLogger.logRegistrationAttempt(annotation.action(), email, true, null, request);
            } else if (annotation.action() == AuditActionType.LOGIN_ATTEMPT) {
                authAuditLogger.logLoginAttempt(annotation.action(), email, true, null, request);
            }
            return result;
        } catch (BadCredentialsException ex) {
            if (annotation.action() == AuditActionType.LOGIN_ATTEMPT) {
                authAuditLogger.logLoginAttempt(annotation.action(), email, false, "Invalid credentials", request);
            } else {
                authAuditLogger.logRegistrationAttempt(annotation.action(), email, false, ex.getMessage(), request);
            }
            throw ex;
        } catch (Throwable ex) {
            if (annotation.action() == AuditActionType.REGISTRATION_ATTEMPT) {
                authAuditLogger.logRegistrationAttempt(annotation.action(), email, false, ex.getMessage(), request);
            } else {
                authAuditLogger.logLoginAttempt(annotation.action(), email, false, ex.getMessage(), request);
            }
            throw ex;
        }

    }

    private Optional<String> resolveEmail(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof RegisterRequest rr) {
                return Optional.ofNullable(rr.email());
            }
            if (arg instanceof LoginRequest lr) {
                return Optional.ofNullable(lr.email());
            }
        }
        return Optional.empty();
    }

    private Optional<HttpServletRequest> resolveRequest(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest req) {
                return Optional.of(req);
            }
        }
        return Optional.empty();
    }
}