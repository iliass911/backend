package com.sebn.brettbau.domain.user.controller;

import com.sebn.brettbau.config.JwtTokenUtil;
import com.sebn.brettbau.domain.user.dto.AuthResponse;
import com.sebn.brettbau.domain.user.dto.LoginRequest;
import com.sebn.brettbau.domain.user.dto.RegisterRequest;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest registerRequest) throws Exception {
        User user = userService.registerUser(registerRequest);
        // Insert the *role name* into the token
        String token = jwtTokenUtil.generateToken(user.getUsername(), user.getRole().getName());
        return new AuthResponse(token, user.getRole().getName(), user.getId());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        User user = userService.authenticateUser(loginRequest);
        String token = jwtTokenUtil.generateToken(user.getUsername(), user.getRole().getName());
        return new AuthResponse(token, user.getRole().getName(), user.getId());
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return jwtTokenUtil.validateToken(token.substring(7));
        }
        return false;
    }
}
