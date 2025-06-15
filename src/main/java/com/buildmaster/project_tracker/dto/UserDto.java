package com.buildmaster.project_tracker.dto;

import com.buildmaster.project_tracker.model.jpa.User;
import com.buildmaster.project_tracker.model.jpa.User.Role;

import java.util.Set;

public record UserDto(
        Long id,
        String email,
        Set<Role> roles,
        String provider
) {
    public static UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getRoles(),
                user.getProvider()
        );
    }
}