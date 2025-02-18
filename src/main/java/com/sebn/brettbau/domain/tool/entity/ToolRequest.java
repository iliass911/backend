package com.sebn.brettbau.domain.tool.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tool_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToolRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tool_id")
    private Tool tool;

    @Column(name = "requested_by")
    private String requestedBy;
    
    @Column(name = "approved_by")
    private String approvedBy;
    
    private String status;
    
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;
    
    @Column(name = "expected_return_date")
    private LocalDateTime expectedReturnDate;
    
    private String purpose;
    private String notes;
}
