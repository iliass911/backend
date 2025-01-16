// src/main/java/com/example/backend/domain/user/entity/User.java
package com.example.backend.domain.user.entity;

import com.example.backend.domain.role.entity.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String matricule;

    @Column(nullable = false)
    private String role; // This field can still store "ADMIN" or "USER" strings

    // Many-to-One relationship with Role entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role roleEntity;
}
