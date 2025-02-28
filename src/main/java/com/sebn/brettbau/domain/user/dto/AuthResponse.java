// AuthResponse.java
package com.sebn.brettbau.domain.user.dto;

import com.sebn.brettbau.domain.security.Module;
import lombok.Data;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Data
public class AuthResponse {
    private String token;
    private String role;
    private Long userId;
    private Map<Module, Set<String>> permissions;
    
    // Added employee info fields for the response
 // Added employee info fields for the response
    private String matricule;
    private String fullName;
    private String site;
    private String project;
    private String jobFunction;  // Changed from function
    private Date assignmentDate;
    private Double seniority;
    private String status;

    public AuthResponse(String token, String role, Long userId, Map<Module, Set<String>> permissions) {
        this.token = token;
        this.role = role;
        this.userId = userId;
        this.permissions = permissions;
    }
    
    // Extended constructor to include employee data
 // Extended constructor to include employee data
    public AuthResponse(String token, String role, Long userId, Map<Module, Set<String>> permissions,
                       String matricule, String fullName, String site, String project, 
                       String jobFunction, Date assignmentDate, Double seniority, String status) {  // Changed from function
        this.token = token;
        this.role = role;
        this.userId = userId;
        this.permissions = permissions;
        this.matricule = matricule;
        this.fullName = fullName;
        this.site = site;
        this.project = project;
        this.jobFunction = jobFunction;  // Changed from function
        this.assignmentDate = assignmentDate;
        this.seniority = seniority;
        this.status = status;
    }
}