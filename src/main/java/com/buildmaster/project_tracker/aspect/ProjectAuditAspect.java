package com.buildmaster.project_tracker.aspect;

import com.buildmaster.project_tracker.aspect.annotation.Auditable;
import com.buildmaster.project_tracker.audit.AuditActionType;
import com.buildmaster.project_tracker.audit.AuditLogger;
import com.buildmaster.project_tracker.dto.ProjectDTO;
import com.buildmaster.project_tracker.mapper.Mapper;
import com.buildmaster.project_tracker.model.jpa.Project;
import com.buildmaster.project_tracker.repository.jpa.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class ProjectAuditAspect {

    private final AuditLogger auditLogger;
    private final ProjectRepository projectRepository;
    private final Mapper<Project, ProjectDTO> projectMapper;

    @Around("@annotation(com.buildmaster.project_tracker.aspect.annotation.Auditable)")
    public Object aroundProjectAuditable(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Auditable auditable = method.getAnnotation(Auditable.class);

        Object[] args = pjp.getArgs();

        // Extract common inputs from method signature patterns:
        // create(ProjectDTO dto, String actor)
        // update(Long id, ProjectDTO dto, String actor)
        // delete(Long id, String actor)
        Long idArg = findFirstArgOfType(args, Long.class).orElse(null);
        ProjectDTO inputDto = findFirstArgOfType(args, ProjectDTO.class).orElse(null);
        String actor = findLastArgOfType(args, String.class);

        // For delete, capture a DTO snapshot before deletion (if available)
        ProjectDTO deleteSnapshot = null;
        if (auditable.action() == AuditActionType.DELETE && idArg != null) {
            projectRepository.findById(idArg).ifPresent(p -> {
                // local variable capture
            });
            Optional<Project> toDelete = projectRepository.findById(idArg);
            if (toDelete.isPresent()) {
                deleteSnapshot = projectMapper.mapToDTO(toDelete.get());
            }
        }

        Object result = pjp.proceed();

        // Determine resource id and payload for logging
        String resourceId = null;
        ProjectDTO payloadDto = inputDto;

        if (result instanceof ProjectDTO resDto) {
            // Prefer ID from the returned DTO after persistence (create/update)
            try {
                Method getId = resDto.getClass().getMethod("getId");
                Object idVal = getId.invoke(resDto);
                if (idVal != null) {
                    resourceId = String.valueOf(idVal);
                }
            } catch (NoSuchMethodException ignored) {
                // fallback below
            }
            payloadDto = resDto;
        }

        if (resourceId == null && idArg != null) {
            resourceId = String.valueOf(idArg);
        }

        // For delete, use the snapshot as payload
        if (auditable.action() == AuditActionType.DELETE && deleteSnapshot != null) {
            payloadDto = deleteSnapshot;
            if (resourceId == null && idArg != null) {
                resourceId = String.valueOf(idArg);
            }
        }

        // Final fallback for id
        if (resourceId == null) {
            resourceId = "unknown";
        }

        auditLogger.log(
                auditable.action(),
                auditable.entity(),
                resourceId,
                actor,
                payloadDto
        );

        return result;
    }

    private <T> Optional<T> findFirstArgOfType(Object[] args, Class<T> type) {
        for (Object arg : args) {
            if (type.isInstance(arg)) return Optional.of(type.cast(arg));
        }
        return Optional.empty();
    }

    private <T> String findLastArgOfType(Object[] args, Class<T> type) {
        for (int i = args.length - 1; i >= 0; i--) {
            if (type.isInstance(args[i])) return String.valueOf(args[i]);
        }
        return null;
    }
}