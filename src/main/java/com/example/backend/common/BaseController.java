// src/main/java/com/example/backend/common/BaseController.java
package com.example.backend.common;

import com.example.backend.domain.role.service.PermissionChecker;
import org.springframework.security.access.AccessDeniedException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseController {
    protected final PermissionChecker permissionChecker;
    
    protected void checkPermission(String resource, String action) {
        if (!permissionChecker.hasPermission(resource, action)) {
            throw new AccessDeniedException("Access denied");
        }
    }
}