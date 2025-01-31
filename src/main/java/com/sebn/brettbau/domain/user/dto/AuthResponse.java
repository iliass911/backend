// AuthResponse.java
package com.sebn.brettbau.domain.user.dto;

import com.sebn.brettbau.domain.security.Module;
import lombok.Data;
import java.util.Map;
import java.util.Set;

@Data
public class AuthResponse {
    private String token;
    private String role;
    private Long userId;
    private Map<Module, Set<String>> permissions;

    public AuthResponse(String token, String role, Long userId, Map<Module, Set<String>> permissions) {
        this.token = token;
        this.role = role;
        this.userId = userId;
        this.permissions = permissions;
        
    }
}
