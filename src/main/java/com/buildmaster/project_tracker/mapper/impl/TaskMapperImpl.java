package com.buildmaster.project_tracker.mapper.impl;

import com.buildmaster.project_tracker.dto.TaskDTO;
import com.buildmaster.project_tracker.mapper.Mapper;
import com.buildmaster.project_tracker.model.jpa.Task;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements Mapper<Task, TaskDTO> {

    private ModelMapper modelMapper;

    public TaskMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TaskDTO mapToDTO(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }

    @Override
    public Task mapToEntity(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }

    @Override
    public void update(TaskDTO taskDTO, Task task) {
        modelMapper.map(taskDTO, task);
    }
}
