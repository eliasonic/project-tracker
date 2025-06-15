package com.buildmaster.project_tracker.controller;

import com.buildmaster.project_tracker.dto.AuthResponse;
import com.buildmaster.project_tracker.dto.LoginRequest;
import com.buildmaster.project_tracker.dto.RegisterRequest;
import com.buildmaster.project_tracker.exception.ErrorResponse;
import com.buildmaster.project_tracker.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controller for handling authentication-related requests.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest)
            throws BadRequestException {
        return ResponseEntity.ok(authService.register(request, httpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(authService.login(request, httpRequest));
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<AuthResponse> oauthSuccess(@RequestParam String token) {
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/oauth2/failure")
    public ResponseEntity<ErrorResponse> oauthFailure(@RequestParam String error) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(
                        LocalDateTime.now(), "OAuth2 failed: " + error, null, "UNAUTHORIZED"));
    }
}