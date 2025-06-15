package com.buildmaster.project_tracker.service;

import com.buildmaster.project_tracker.dto.UserDto;
import com.buildmaster.project_tracker.model.jpa.User;

import java.util.List;

public interface UserService {
    public UserDto getCurrentUser(User user);

    public List<UserDto> getAllUsers();
}
