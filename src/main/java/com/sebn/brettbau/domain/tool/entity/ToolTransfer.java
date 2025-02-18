package com.sebn.brettbau.domain.tool.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tool_transfers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToolTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tool_id")
    private Tool tool;

    @Column(name = "from_user")
    private String fromUser;
    
    @Column(name = "to_user")
    private String toUser;
    
    @Column(name = "condition_before")
    private String conditionBefore;
    
    @Column(name = "condition_after")
    private String conditionAfter;
    
    @Column(name = "transferred_at")
    private LocalDateTime transferredAt;
    
    private String notes;
    
    @Column(name = "photo_urls")
    private String photoUrls;
    
    private String status;
}