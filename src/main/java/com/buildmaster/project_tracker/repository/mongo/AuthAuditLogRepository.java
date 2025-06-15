package com.buildmaster.project_tracker.repository.mongo;

import com.buildmaster.project_tracker.model.mongo.AuthAuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuthAuditLogRepository extends MongoRepository<AuthAuditLog, String> {
}
