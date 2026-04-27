package com.example.ucitelnice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           LogoutSuccessHandler logoutSuccessHandler) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/api/payment/**",
                                "/api/cart/**"
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/products/all",
                                "/products/*",
                                "/api/products/all",
                                "/api/products/*",
                                "/cart",
                                "/api/cart",
                                "/api/cart/add/*",
                                "/api/cart/decrease/*",
                                "/access-denied",
                                "/error",
                                "/api/payment/stripe-test",
                                "/api/payment/checkout",
                                "/api/payment/webhook",
                                "/payment/success",
                                "/payment/cancelled",
                                "/api/orders/confirmation/**",
                                "/orders/confirmation/**",
                                "/api/order_items/**"
                        ).permitAll()
                        .requestMatchers(
                                "/products/new",
                                "/admin/**",
                                "/orders/**",
                                "/api/orders/**",
                                "/api/products/all-admin"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/products/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/edit/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/*/deactivate").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(login -> login
                        .successHandler(loginSuccessHandler))
                .logout(logout -> logout
                        .logoutSuccessHandler(logoutSuccessHandler)
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied"));

        return http.build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedLogoutSuccessHandler handler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        handler.setPostLogoutRedirectUri("{baseUrl}/");
        return handler;
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>(authorities);

            for (GrantedAuthority authority : authorities) {
                if (authority instanceof OidcUserAuthority oidcUserAuthority) {
                    mappedAuthorities.addAll(extractRealmRoles(oidcUserAuthority));
                }
            }

            return mappedAuthorities;
        };
    }

    private Set<GrantedAuthority> extractRealmRoles(OidcUserAuthority oidcUserAuthority) {
        Set<GrantedAuthority> roles = new HashSet<>();

        Map<String, Object> claims = oidcUserAuthority.getIdToken().getClaims();
        Object realmAccessObj = claims.get("realm_access");

        if (!(realmAccessObj instanceof Map<?, ?> realmAccess)) {
            return roles;
        }

        Object rolesObj = realmAccess.get("roles");

        if (!(rolesObj instanceof Collection<?> roleNames)) {
            return roles;
        }

        for (Object roleObj : roleNames) {
            String role = String.valueOf(roleObj);

            if (!role.startsWith("default-roles-")
                    && !role.equals("offline_access")
                    && !role.equals("uma_authorization")) {
                roles.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }

        return roles;
    }
}