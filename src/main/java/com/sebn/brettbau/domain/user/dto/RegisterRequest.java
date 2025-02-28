package com.sebn.brettbau.domain.user.dto;

import lombok.Data;
import java.util.Date;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String matricule;
    
    // Added employee information fields
 // Added employee information fields
    private String fullName;
    private String site;
    private String project;
    private String jobFunction;  // Changed from function
    private Date assignmentDate;
    private Double seniority;
    private String status;
}