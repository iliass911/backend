// src/main/java/com/sebn/brettbau/domain/user/entity/User.java
package com.sebn.brettbau.domain.user.entity;

import com.sebn.brettbau.domain.role.entity.Role;
import javax.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; 

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String matricule;

    // Link to the real Role entity
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    
    // New employee information fields
    @Column(name = "full_name")
    private String fullName; // N&P from the data
    
    @Column(name = "site")
    private String site; // SEBN Ma1, SEBN Ma2, Sat1, etc.
    
    @Column(name = "project")
    private String project; // T_roc, HV, Q7, Golf, etc.
    
    @Column(name = "job_function")
    private String jobFunction; // Job function
    
    @Column(name = "assignment_date")
    @Temporal(TemporalType.DATE)
    private Date assignmentDate; // DATE D'AFFECTATION BB
    
    @Column(name = "seniority")
    private Double seniority; // Anciennetée
    
    @Column(name = "status")
    private String status; // Formation, Intégration, etc.
}