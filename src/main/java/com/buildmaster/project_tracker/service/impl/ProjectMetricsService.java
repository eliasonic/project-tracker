package com.buildmaster.project_tracker.service.impl;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
public class ProjectMetricsService {
    private final Counter projectCreateCounter;
    private final Timer projectCreateTimer;

    public ProjectMetricsService(MeterRegistry registry) {
        this.projectCreateCounter = Counter.builder("api.project.create.requests")
                .description("Total project creation requests")
                .tag("version", "v1")
                .register(registry);

        this.projectCreateTimer = Timer.builder("api.project.create.latency")
                .description("Project creation latency")
                .tag("version", "v1")
                .register(registry);
    }

    public void incrementCreateCounter() {
        projectCreateCounter.increment();
    }

    public Timer.Sample startTimer() {
        return Timer.start();
    }

    public void recordTimer(Timer.Sample sample) {
        sample.stop(projectCreateTimer);
    }
}