package com.buildmaster.project_tracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configuration class for MongoDB.
 */
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.buildmaster.project_tracker.repository.mongo")
public class MongoConfig {
}
