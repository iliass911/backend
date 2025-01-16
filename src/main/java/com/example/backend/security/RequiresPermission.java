package com.example.backend.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@permissionChecker.hasPermission(#resource, #action)")
public @interface RequiresPermission {
    String resource();
    String action();
}
