package com.buildmaster.project_tracker.mapper.impl;

import com.buildmaster.project_tracker.dto.ProjectDTO;
import com.buildmaster.project_tracker.mapper.Mapper;
import com.buildmaster.project_tracker.model.jpa.Project;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapperImpl implements Mapper<Project, ProjectDTO> {

    private ModelMapper modelMapper;

    public ProjectMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ProjectDTO mapToDTO(Project project) {
        return modelMapper.map(project, ProjectDTO.class);
    }

    @Override
    public Project mapToEntity(ProjectDTO projectDTO) {
        return modelMapper.map(projectDTO, Project.class);
    }

    @Override
    public void update(ProjectDTO projectDTO, Project project) {
        modelMapper.map(projectDTO, project);
    }
}
