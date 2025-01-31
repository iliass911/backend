package com.sebn.brettbau.domain.debug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {
    @Autowired
    private DataSource dataSource;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Application is running!");
    }

    @GetMapping("/connection")
    public ResponseEntity<?> testDatabaseConnection() {
        Map<String, Object> response = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            response.put("status", "success");
            response.put("message", "Database connection established successfully!");
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            response.put("status", "error");
            response.put("message", "Database connection failed: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/system-info")
    public ResponseEntity<?> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("java.version", System.getProperty("java.version"));
        systemInfo.put("os.name", System.getProperty("os.name"));
        systemInfo.put("user.timezone", System.getProperty("user.timezone"));
        return ResponseEntity.ok(systemInfo);
    }
}
