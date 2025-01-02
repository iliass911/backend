// src/main/java/com/example/backend/domain/preventive_maintenance/entity/Pack.java

package com.example.backend.domain.preventive_maintenance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "packs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Establishing a Many-to-One relationship with Site
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    // If a Pack can be associated with multiple Projects via Boards, ensure that Boards handle this relationship
}
