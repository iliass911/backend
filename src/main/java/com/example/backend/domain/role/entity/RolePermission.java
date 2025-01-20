package com.example.backend.domain.role.entity;

import com.example.backend.domain.security.Module;
import com.example.backend.domain.security.PermissionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We'll also ignore 'permissions' from Role if it tries to appear here
    // But it's enough to do the annotation in Role.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonIgnoreProperties({"permissions"}) 
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Module module;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionType permissionType;
}
