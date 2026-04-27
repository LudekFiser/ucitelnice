package com.example.ucitelnice.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           LogoutSuccessHandler logoutSuccessHandler) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/api/payment/**",
                                "/api/cart/**",
                                "/api/products/**",
                                "/api/orders/**",
                                "/api/order_items/**"
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
                                "/api/order_items/**",
                                "/api/auth/me"
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
                        .authenticationEntryPoint((request, response, authException) -> {
                            String path = request.getRequestURI();
                            if (path.startsWith("/api/")) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setContentType("application/json");
                                response.getWriter().write("{\"error\":\"Unauthorized\"}");
                            } else {
                                response.sendRedirect("/oauth2/authorization/keycloak");
                            }
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            String path = request.getRequestURI();
                            if (path.startsWith("/api/")) {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                response.setContentType("application/json");
                                response.getWriter().write("{\"error\":\"Forbidden\"}");
                            } else {
                                response.sendRedirect("/access-denied");
                            }
                        })
                );

        return http.build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedLogoutSuccessHandler handler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        handler.setPostLogoutRedirectUri("http://localhost:5173/");
        return handler;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // KRITICKÉ - posílá session cookies (cart)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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