package com.example.ucitelnice.controller;

import com.example.ucitelnice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class CurrentUserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            response.put("isLoggedIn", false);
            response.put("isAdmin", false);
            response.put("username", null);
            response.put("email", null);
            return ResponseEntity.ok(response);
        }

        boolean isAdmin = false;
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                isAdmin = true;
                break;
            }
        }

        response.put("isLoggedIn", true);
        response.put("isAdmin", isAdmin);
        response.put("username", oidcUser.getPreferredUsername());
        response.put("email", oidcUser.getEmail());

        try {
            UUID keycloakId = UUID.fromString(oidcUser.getSubject());
            userRepository.findByKeycloakId(keycloakId).ifPresent(user -> {
                response.put("firstName", user.getFirstName());
                response.put("lastName", user.getLastName());
                response.put("userId", user.getId());
            });
        } catch (Exception ignored) {
        }

        return ResponseEntity.ok(response);
    }
}
