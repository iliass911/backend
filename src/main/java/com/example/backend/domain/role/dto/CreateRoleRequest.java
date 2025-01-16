package com.example.backend.domain.role.dto;

import lombok.Data;
import java.util.Set;

@Data
public class CreateRoleRequest {
    private String name;
    private String description;
    private Set<Long> permissionIds;
}
