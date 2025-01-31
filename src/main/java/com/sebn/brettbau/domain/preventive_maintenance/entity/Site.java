// src/main/java/com/example/backend/domain/preventive_maintenance/entity/Site.java

package com.sebn.brettbau.domain.preventive_maintenance.entity;

import javax.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "sites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Pack> packs;

    // Getters and Setters
    // ...
}

