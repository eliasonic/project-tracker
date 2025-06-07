package com.buildmaster.project_tracker.repository.mongo;

import com.buildmaster.project_tracker.model.mongo.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {

    List<AuditLog> findByEntityType(String entityType);

    List<AuditLog> findByActorName(String actorName);
}
