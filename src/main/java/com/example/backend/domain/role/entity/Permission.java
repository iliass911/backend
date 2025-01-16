package com.example.backend.domain.role.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Map the permissionName property to the column named "name"
    @Column(name = "name", nullable = false, unique = true)
    private String permissionName;

    @Column(nullable = false)
    private String resource; // e.g., "INVENTORY", "MAINTENANCE", etc.

    @Column(nullable = false)
    private String action;   // e.g., "VIEW", "CREATE", "UPDATE", "DELETE"

    @Column
    private String description;

    // New constructor including permissionName
    public Permission(String permissionName, String resource, String action, String description) {
        this.permissionName = permissionName;
        this.resource = resource;
        this.action = action;
        this.description = description;
    }
    
    // Existing constructor for backward compatibility (optional)
    public Permission(String resource, String action, String description) {
        this.resource = resource;
        this.action = action;
        this.description = description;
    }
}
