package com.example.backend.domain.role.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private String matricule;
    private Long roleId;
}