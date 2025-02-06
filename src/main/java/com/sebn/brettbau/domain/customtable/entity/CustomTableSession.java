package com.sebn.brettbau.domain.customtable.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "custom_table_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomTableSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private CustomTable table;

    private String userId;
    private LocalDateTime joinedAt;
    private LocalDateTime lastActiveAt;
    private Boolean isActive;
}
