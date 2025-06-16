package com.buildmaster.project_tracker.model.mongo;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Represents an audit log entry for security-related actions performed within the system.
 */
@Document(collection = "auth_audit_logs")
@Getter @Setter @NoArgsConstructor
public class AuthAuditLog {
    @Id
    private String id;
    private String actionType;
    private String username;
    private String ipAddress;
    private String userAgent;
    private String endpoint;
    private LocalDateTime timestamp = LocalDateTime.now();
    private boolean success;
    private String details;
}
