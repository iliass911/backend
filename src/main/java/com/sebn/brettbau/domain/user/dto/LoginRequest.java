package com.sebn.brettbau.domain.user.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
