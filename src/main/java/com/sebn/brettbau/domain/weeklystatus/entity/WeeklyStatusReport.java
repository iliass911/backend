package com.sebn.brettbau.domain.weeklystatus.entity;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "weekly_status_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyStatusReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String weekNumber;         // e.g., KW50, KW51...
    private String coordinator;        // Coord
    private String responsiblePerson;  // Responsible person
    private String projectCode;        // Project code
    @Column(columnDefinition = "TEXT")
    private String taskDescription;    // Task description
    private String workType;           // Type of work
    private String priority;           // Priority (Critique, High, Medium, Low)
    private Integer progressPercentage;// Progress percentage
    private String project;            // Project filter field (e.g., BATTERIE MB21, Golf A8)
    private String month;              // Month filter field (e.g., janvier, décembre)
}

