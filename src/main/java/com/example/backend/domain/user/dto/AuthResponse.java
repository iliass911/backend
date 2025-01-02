// src/main/java/com/example/backend/domain/user/dto/AuthResponse.java
package com.example.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String role;
    private Long userId; // Add this line
}
