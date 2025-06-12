package com.buildmaster.project_tracker.service;

import com.buildmaster.project_tracker.model.mongo.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AuditLogService {

    Page<AuditLog> getAllLogs(Pageable pageable);

    List<AuditLog> getFilteredLogs(String entityType, String actorName);
}