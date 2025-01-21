package com.sebn.brettbau.domain.role.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * e.g. "ADMIN", "USER", "MANAGER"
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * A Role can have many RolePermissions. 
     * 
     * We tell Jackson to ignore the 'role' property inside each RolePermission
     * so we don't recursively serialize back-and-forth forever.
     */
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"role"})  // Tells Jackson not to serialize the 'role' field of RolePermission
    private Set<RolePermission> permissions = new HashSet<>();
}
