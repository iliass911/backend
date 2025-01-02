// src/main/java/com/example/backend/domain/user/dto/UserDTO.java
package com.example.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String role;
}
