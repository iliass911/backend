package com.sebn.brettbau.domain.preventive_maintenance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "checklists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Checklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
    
    private String technicianName;    
    private String qualityAgentName;  
    private Integer completionPercentage; 
    private String comments;          
    private Boolean qualityValidated; 
    private LocalDateTime validationDate; 
    private LocalDateTime expiryDate;     
    private String workStatus;        
    private LocalDateTime createdAt;
    
    // New field for week number
    @Column(name = "week_number")
    private Integer weekNumber;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
