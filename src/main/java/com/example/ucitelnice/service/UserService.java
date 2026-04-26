package com.example.ucitelnice.service;

import com.example.ucitelnice.dto.CurrentUserResponse;
import com.example.ucitelnice.entity.User;
import com.example.ucitelnice.mapper.UserMapper;
import com.example.ucitelnice.repository.UserRepository;
import com.example.ucitelnice.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final CurrentUser currentUser;
    private final UserMapper userMapper;

    @Transactional
    public User syncUserFromKeycloak(OidcUser oidcUser) {
        var keycloakId = UUID.fromString(oidcUser.getSubject());
        var user = userRepository.findByKeycloakId(keycloakId).orElseGet(User::new);

        user.setKeycloakId(keycloakId);
        user.setEmail(oidcUser.getEmail());
        user.setFirstName(oidcUser.getGivenName());
        user.setLastName(oidcUser.getFamilyName());
        return userRepository.save(user);
    }
}
