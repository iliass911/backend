package com.sebn.brettbau.domain.tool.controller;

import com.sebn.brettbau.domain.tool.dto.*;
import com.sebn.brettbau.domain.tool.service.ToolService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tools")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ToolController {
    private final ToolService service;

    // Tool endpoints
    @GetMapping
    public ResponseEntity<List<ToolDTO>> getAllTools(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.READ, requestingModule, Module.TOOL_MANAGEMENT);
            List<ToolDTO> tools = service.getAll();
            return ResponseEntity.ok(tools);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping
    public ResponseEntity<ToolDTO> createTool(
            @RequestBody ToolDTO dto,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.CREATE, requestingModule, Module.TOOL_MANAGEMENT);
            ToolDTO createdTool = service.createTool(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTool);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // PUT endpoint for updating a tool
    @PutMapping("/{id}")
    public ResponseEntity<ToolDTO> updateTool(
            @PathVariable Long id,
            @RequestBody ToolDTO dto,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.UPDATE, requestingModule, Module.TOOL_MANAGEMENT);
            ToolDTO updatedTool = service.updateTool(id, dto);
            return ResponseEntity.ok(updatedTool);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // Request endpoints
    @GetMapping("/request")
    public ResponseEntity<List<ToolRequestDTO>> getAllRequests(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.READ, requestingModule, Module.TOOL_MANAGEMENT);
            List<ToolRequestDTO> requests = service.getAllRequests();
            return ResponseEntity.ok(requests);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/request")
    public ResponseEntity<ToolRequestDTO> createRequest(
            @RequestBody ToolRequestDTO dto,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.CREATE, requestingModule, Module.TOOL_MANAGEMENT);
            ToolRequestDTO request = service.createToolRequest(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(request);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // Transfer endpoints
    @GetMapping("/transfer")
    public ResponseEntity<List<ToolTransferDTO>> getAllTransfers(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.READ, requestingModule, Module.TOOL_MANAGEMENT);
            List<ToolTransferDTO> transfers = service.getAllTransfers();
            return ResponseEntity.ok(transfers);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<ToolTransferDTO> createTransfer(
            @RequestBody ToolTransferDTO dto,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.CREATE, requestingModule, Module.TOOL_MANAGEMENT);
            ToolTransferDTO transfer = service.createToolTransfer(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(transfer);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // Maintenance endpoints
    @GetMapping("/maintenance")
    public ResponseEntity<List<ToolMaintenanceDTO>> getAllMaintenance(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.READ, requestingModule, Module.TOOL_MANAGEMENT);
            List<ToolMaintenanceDTO> maintenance = service.getAllMaintenance();
            return ResponseEntity.ok(maintenance);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/maintenance")
    public ResponseEntity<ToolMaintenanceDTO> createMaintenance(
            @RequestBody ToolMaintenanceDTO dto,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.CREATE, requestingModule, Module.TOOL_MANAGEMENT);
            ToolMaintenanceDTO maintenance = service.createMaintenanceRequest(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(maintenance);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // Delete endpoints
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTool(
            @PathVariable Long id,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.DELETE, requestingModule, Module.TOOL_MANAGEMENT);
            service.deleteTool(id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/request/{id}")
    public ResponseEntity<Void> deleteRequest(
            @PathVariable Long id,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.DELETE, requestingModule, Module.TOOL_MANAGEMENT);
            service.deleteRequest(id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/transfer/{id}")
    public ResponseEntity<Void> deleteTransfer(
            @PathVariable Long id,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.DELETE, requestingModule, Module.TOOL_MANAGEMENT);
            service.deleteTransfer(id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/maintenance/{id}")
    public ResponseEntity<Void> deleteMaintenance(
            @PathVariable Long id,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.DELETE, requestingModule, Module.TOOL_MANAGEMENT);
            service.deleteMaintenance(id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private void verifyPermission(PermissionType permissionType, String requestingModule, Module targetModule) {
        // Your permission verification logic here
    }
}
