package com.buildmaster.project_tracker.service.impl;

import com.buildmaster.project_tracker.audit.AuditActionType;
import com.buildmaster.project_tracker.audit.AuthAuditLogger;
import com.buildmaster.project_tracker.dto.AuthResponse;
import com.buildmaster.project_tracker.dto.LoginRequest;
import com.buildmaster.project_tracker.dto.RegisterRequest;
import com.buildmaster.project_tracker.model.jpa.User;
import com.buildmaster.project_tracker.model.jpa.User.Role;
import com.buildmaster.project_tracker.repository.jpa.UserRepository;
import com.buildmaster.project_tracker.security.CustomUserDetailsService;
import com.buildmaster.project_tracker.security.JwtTokenProvider;
import com.buildmaster.project_tracker.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service implementation of {@link AuthService} for handling local user registration and authentication.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final AuthAuditLogger authAuditLogger;
    private final CustomUserDetailsService userDetailsService;

    public AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest) throws BadRequestException {
        if (userRepository.existsByEmail(request.email())) {
            authAuditLogger.logRegistrationAttempt(
                    AuditActionType.REGISTRATION_ATTEMPT, request.email(), false, "Email already exists", httpRequest);
            throw new BadRequestException("Email already in use");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(Role.ROLE_CONTRACTOR))
                .provider("local")
                .build();

        User savedUser = userRepository.save(user);
        authAuditLogger.logRegistrationAttempt(
                AuditActionType.REGISTRATION_ATTEMPT, request.email(), true, null, httpRequest);

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = tokenProvider.generateToken(userDetails);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = tokenProvider.generateToken(userDetails);

            authAuditLogger.logLoginAttempt(
                    AuditActionType.LOGIN_ATTEMPT, request.email(), true, null, httpRequest);
            return new AuthResponse(token);

        } catch (BadCredentialsException ex) {
            authAuditLogger.logLoginAttempt(
                    AuditActionType.LOGIN_ATTEMPT, request.email(), false, "Invalid credentials", httpRequest);
            throw new BadCredentialsException("Invalid email/password");
        }
    }
}