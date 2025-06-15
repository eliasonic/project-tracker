package com.buildmaster.project_tracker.controller;

import com.buildmaster.project_tracker.model.mongo.AuditLog;
import com.buildmaster.project_tracker.model.mongo.AuthAuditLog;
import com.buildmaster.project_tracker.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogService auditLogService;

    @GetMapping
    @Operation(summary = "Get all logs with pagination")
    public Page<AuditLog> getAllLogs(Pageable pageable) {
        return auditLogService.getAllLogs(pageable);
    }

    @GetMapping("/filter")
    @Operation(summary = "Get logs by entity type or actor name")
    public List<AuditLog> getFilteredLogs(
            @RequestParam(required = false) String entityType, @RequestParam(required = false) String actorName) {
        return auditLogService.getFilteredLogs(entityType, actorName);
    }

    @GetMapping("/auth")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get auth logs")
    public ResponseEntity<List<AuthAuditLog>> getAuthLogs() {
        return ResponseEntity.ok(auditLogService.getAuthLogs());
    }
}