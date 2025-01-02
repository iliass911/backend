// src/main/java/com/example/backend/domain/user/dto/RegisterRequest.java
package com.example.backend.domain.user.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String matricule;
}
