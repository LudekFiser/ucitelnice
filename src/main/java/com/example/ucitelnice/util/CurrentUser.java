package com.example.ucitelnice.util;

import com.example.ucitelnice.entity.User;
import com.example.ucitelnice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrentUser {

    private final UserRepository userRepository;


    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("No authenticated user found");
        }
        var principal = authentication.getPrincipal();
        if (!(principal instanceof OidcUser oidcUser)) {
            throw new RuntimeException("Authenticated principal is not an OIDC user");
        }
        var keycloakId = UUID.fromString(oidcUser.getSubject());
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Current user was not found in database"));
    }
}
