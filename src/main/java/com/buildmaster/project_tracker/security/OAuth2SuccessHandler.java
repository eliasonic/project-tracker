package com.buildmaster.project_tracker.security;

import com.buildmaster.project_tracker.audit.AuditActionType;
import com.buildmaster.project_tracker.audit.AuthAuditLogger;
import com.buildmaster.project_tracker.model.jpa.User;
import com.buildmaster.project_tracker.repository.jpa.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Set;
import java.util.UUID;

/**
 * This class handles the successful authentication of OAuth2 users.
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider tokenProvider;
    private final AuthAuditLogger authAuditLogger;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

            String providerId = oauthUser.getAttribute("SUB");
            String email = oauthUser.getAttribute("email");
            if (email == null) {
                throw new OAuth2AuthenticationException("Email not found in OAuth2 user");
            }

            User user = processOAuth2User(email, oauthToken.getAuthorizedClientRegistrationId(), providerId);
            UserDetails userDetails = new CustomUserDetails(user);
            String token = tokenProvider.generateToken(userDetails);

            authAuditLogger.logOAuthLogin(AuditActionType.OAUTH_LOGIN, email, "google", request);

            response.sendRedirect("/auth/oauth2/success?token=" + token);
        } catch (Exception e) {
            authAuditLogger.logUnauthorizedAccess(AuditActionType.UNAUTHORIZED_ACCESS,
                    new OAuth2AuthenticationException("OAuth2 processing failed"), request);

            response.sendRedirect("/auth/oauth2/failure?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    public User processOAuth2User(String email, String provider, String providerId) {
        return userRepository.findByEmail(email)
                .map(existingUser -> {
                    if (!existingUser.getProvider().equals(provider)) {
                        existingUser.setProvider(provider);
                        existingUser.setProviderId(providerId);
                        return userRepository.save(existingUser);
                    }
                    return existingUser;
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                            .roles(Set.of(User.Role.ROLE_CONTRACTOR))
                            .provider(provider)
                            .providerId(providerId)
                            .build();
                    return userRepository.save(newUser);
                });
    }
}