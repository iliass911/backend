package com.sebn.brettbau.domain.tool.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tools")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String toolId;
    private String name;
    private String brand;
    private String type;
    private String location;
    
    @Column(name = "`condition`")  // Escape the reserved keyword with backticks
    private String condition;
    
    private String status;
    private String currentHolder;

    @Column(name = "last_maintained")
    private LocalDateTime lastMaintained;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}