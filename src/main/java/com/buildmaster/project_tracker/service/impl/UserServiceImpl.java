package com.buildmaster.project_tracker.service.impl;

import com.buildmaster.project_tracker.dto.UserDto;
import com.buildmaster.project_tracker.model.jpa.User;
import com.buildmaster.project_tracker.repository.jpa.UserRepository;
import com.buildmaster.project_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto getCurrentUser(User user) {
        return UserDto.mapToDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::mapToDto)
                .toList();
    }
}
