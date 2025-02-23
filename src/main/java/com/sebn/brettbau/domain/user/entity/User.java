// src/main/java/com/example/backend/domain/user/entity/User.java
package com.sebn.brettbau.domain.user.entity;

import com.sebn.brettbau.domain.role.entity.Role;
import javax.persistence.*;
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

    // Link to the real Role entity
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}

