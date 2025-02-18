package com.sebn.brettbau.domain.tool.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tool_maintenance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToolMaintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tool_id")
    private Tool tool;

    private String type;
    
    @Column(name = "requested_by")
    private String requestedBy;
    
    private String description;
    private String status;
    
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    private String notes;
    private Double cost;
}
