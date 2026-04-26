package com.example.ucitelnice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CurrentUserResponse {

    private Long id;
    private UUID keycloakId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
