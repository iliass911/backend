package com.sebn.brettbau.domain.customtable.controller;

import com.sebn.brettbau.domain.customtable.dto.CustomColumnDTO;
import com.sebn.brettbau.domain.customtable.dto.CustomTableDTO;
import com.sebn.brettbau.domain.customtable.dto.TableUpdateMessage;
import com.sebn.brettbau.domain.customtable.service.CustomTableService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/custom-tables")
@RequiredArgsConstructor
public class CustomTableController {

    private static final Logger logger = LoggerFactory.getLogger(CustomTableController.class);
    private final CustomTableService tableService;

    /**
     * Create a new custom table.
     */
    @PostMapping
    public ResponseEntity<?> createTable(
            @RequestBody CustomTableDTO tableDTO,
            Authentication authentication) {
        try {
            CustomTableDTO created = tableService.createTable(tableDTO, authentication.getName());
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request for validation errors.
            logger.warn("Validation error while creating table: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error in createTable endpoint: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error creating table: " + e.getMessage()));
        }
    }

    /**
     * Get all tables for the authenticated user.
     *
     * Note: This endpoint now returns all tables in the system, regardless of creator.
     */
    @GetMapping
    public ResponseEntity<List<CustomTableDTO>> getTables(Authentication authentication) {
        List<CustomTableDTO> tables = tableService.getTablesByUser(authentication.getName());
        return ResponseEntity.ok(tables);
    }

    /**
     * Get a specific table by ID.
     */
    @GetMapping("/{tableId}")
    public ResponseEntity<CustomTableDTO> getTable(@PathVariable Long tableId) {
        CustomTableDTO table = tableService.getTableById(tableId);
        return ResponseEntity.ok(table);
    }

    /**
     * Update an existing table.
     *
     * This endpoint receives the full state of the table (including columns and data)
     * and persists it in the database.
     */
    @PutMapping("/{tableId}")
    public ResponseEntity<CustomTableDTO> updateTable(
            @PathVariable Long tableId,
            @RequestBody CustomTableDTO tableDTO) {
        CustomTableDTO updated = tableService.updateTable(tableId, tableDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a table.
     */
    @DeleteMapping("/{tableId}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long tableId) {
        tableService.deleteTable(tableId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Add a new column to a table.
     */
    @PostMapping("/{tableId}/columns")
    public ResponseEntity<CustomColumnDTO> addColumn(
            @PathVariable Long tableId,
            @RequestBody CustomColumnDTO columnDTO) {
        CustomColumnDTO added = tableService.addColumn(tableId, columnDTO);
        return ResponseEntity.ok(added);
    }

    /**
     * Get active users for a table.
     */
    @GetMapping("/{tableId}/users")
    public ResponseEntity<List<String>> getActiveUsers(@PathVariable Long tableId) {
        List<String> users = tableService.getActiveUsers(tableId);
        return ResponseEntity.ok(users);
    }

    /**
     * WebSocket endpoint for table updates.
     */
    @MessageMapping("/table/{tableId}/update")
    public void handleTableUpdate(
            @DestinationVariable Long tableId,
            TableUpdateMessage update,
            Authentication authentication) {
        update.setUpdatedBy(authentication.getName());
        switch (update.getUpdateType()) {
            case CELL_UPDATE:
                tableService.updateCell(tableId, update);
                break;
            case ROW_INSERT:
                tableService.insertRow(tableId, update);
                break;
            case ROW_DELETE:
                tableService.deleteRow(tableId, update);
                break;
            case COLUMN_UPDATE:
                tableService.updateColumn(tableId, update);
                break;
        }
    }

    /**
     * WebSocket endpoint for joining a table session.
     */
    @MessageMapping("/table/{tableId}/join")
    public void joinSession(
            @DestinationVariable Long tableId,
            Authentication authentication) {
        tableService.joinSession(tableId, authentication.getName());
    }

    /**
     * WebSocket endpoint for leaving a table session.
     */
    @MessageMapping("/table/{tableId}/leave")
    public void leaveSession(
            @DestinationVariable Long tableId,
            Authentication authentication) {
        tableService.leaveSession(tableId, authentication.getName());
    }
}
