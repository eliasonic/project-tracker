package com.buildmaster.project_tracker.service;

import com.buildmaster.project_tracker.dto.AuthResponse;
import com.buildmaster.project_tracker.dto.LoginRequest;
import com.buildmaster.project_tracker.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;

public interface AuthService {
    AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest) throws BadRequestException;

    AuthResponse login(LoginRequest request, HttpServletRequest httpRequest);
}
