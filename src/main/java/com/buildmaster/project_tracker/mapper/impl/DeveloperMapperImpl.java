package com.buildmaster.project_tracker.mapper.impl;

import com.buildmaster.project_tracker.dto.DeveloperDTO;
import com.buildmaster.project_tracker.mapper.Mapper;
import com.buildmaster.project_tracker.model.jpa.Developer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DeveloperMapperImpl implements Mapper<Developer, DeveloperDTO> {

    private ModelMapper modelMapper;

    public DeveloperMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public DeveloperDTO mapToDTO(Developer developer) {
        return modelMapper.map(developer, DeveloperDTO.class);
    }

    @Override
    public Developer mapToEntity(DeveloperDTO developerDTO) {
        return modelMapper.map(developerDTO, Developer.class);
    }

    @Override
    public void update(DeveloperDTO developerDTO, Developer developer) {
        modelMapper.map(developerDTO, developer);
    }
}
