// src/main/java/com/sebn/brettbau/domain/user/dto/UserDTO.java
package com.sebn.brettbau.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String role;
    private String matricule;
    
    // Added employee information
 // Added employee information
    private String fullName;
    private String site;
    private String project;
    private String jobFunction;  // Changed from function
    private Date assignmentDate;
    private Double seniority;
    private String status;
}