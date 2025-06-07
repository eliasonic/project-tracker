package com.buildmaster.project_tracker.model.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents an audit log entry stored in MongoDB for significant actions performed within the system.
 */
@Document(collection = "audit_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AuditLog {
    @Id
    private String id;
    private String actionType;
    private String entityType;
    private String entityId;
    private LocalDateTime timestamp;
    private String actorName;
    private Map<String, Object> payload;
}
