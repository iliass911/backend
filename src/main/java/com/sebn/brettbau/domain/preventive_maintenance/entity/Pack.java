// src/main/java/com/example/backend/domain/preventive_maintenance/entity/Pack.java
package com.sebn.brettbau.domain.preventive_maintenance.entity;

import javax.persistence.*;
import lombok.*;

//Pack.java
@Entity
@Table(name = "packs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pack {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String name;

 @ManyToOne(fetch = FetchType.LAZY, optional = false)
 @JoinColumn(name = "site_id", nullable = false)
 private Site site;

 @ManyToOne(fetch = FetchType.LAZY, optional = false)
 @JoinColumn(name = "project_id", nullable = false)
 private Project project;
}

