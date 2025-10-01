package com.buildmaster.project_tracker.aspect.annotation;

import com.buildmaster.project_tracker.audit.AuditActionType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AuthAuditable {
    AuditActionType action();
}
