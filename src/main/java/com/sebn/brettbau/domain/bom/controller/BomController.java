package com.sebn.brettbau.domain.bom.controller;

import com.sebn.brettbau.domain.bom.dto.BomDTO;
import com.sebn.brettbau.domain.bom.dto.BomLineDTO;
import com.sebn.brettbau.domain.bom.service.BomService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/boms")
@RequiredArgsConstructor
public class BomController {
    private final BomService bomService;
    private final UserService userService;
    private final RoleService roleService;

    /**
     * Create a new BOM for a given Board Family.
     * This will create the same BOM for all boards in the family.
     * Requires CREATE permission on BOM module.
     */
    @PostMapping("/family/{familyId}")
    public ResponseEntity<List<BomDTO>> createBomForFamily(@PathVariable Long familyId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE BOM for family.");
        }
        List<BomDTO> createdBoms = bomService.createBomForFamily(familyId);
        return ResponseEntity.ok(createdBoms);
    }

    /**
     * Get existing BOMs by Family ID.
     * Returns the BOM template for the family.
     * Requires READ permission on BOM module.
     */
    @GetMapping("/family/{familyId}")
    public ResponseEntity<BomDTO> getBomByFamilyId(@PathVariable Long familyId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ BOM by family ID.");
        }
        BomDTO bomDTO = bomService.getBomByFamilyId(familyId);
        return ResponseEntity.ok(bomDTO);
    }

    /**
     * Add or update BOM lines for a family.
     * This will update the BOM for all boards in the family.
     * Requires UPDATE permission on BOM module.
     */
    @PostMapping("/family/{familyId}/lines")
    public ResponseEntity<List<BomDTO>> addOrUpdateFamilyBomLines(
            @PathVariable Long familyId,
            @RequestBody Set<BomLineDTO> lineDTOs) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE BOM lines for family.");
        }
        List<BomDTO> updatedBoms = bomService.addOrUpdateFamilyBomLines(familyId, lineDTOs);
        return ResponseEntity.ok(updatedBoms);
    }

    /**
     * Create a new BOM for a given Board.
     * Kept for backward compatibility and individual board cases.
     * Requires CREATE permission on BOM module.
     */
    @PostMapping("/board/{boardId}")
    public ResponseEntity<BomDTO> createBomForBoard(@PathVariable Long boardId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE BOM for board.");
        }
        BomDTO createdBom = bomService.createBom(boardId);
        return ResponseEntity.ok(createdBom);
    }

    /**
     * Get an existing BOM by Board ID.
     * Kept for backward compatibility and individual board cases.
     * Requires READ permission on BOM module.
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<BomDTO> getBomByBoardId(@PathVariable Long boardId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ BOM by board ID.");
        }
        BomDTO bomDTO = bomService.getBomByBoardId(boardId);
        return ResponseEntity.ok(bomDTO);
    }

    /**
     * Get an existing BOM by ID.
     * Kept for backward compatibility.
     * Requires READ permission on BOM module.
     */
    @GetMapping("/{bomId}")
    public ResponseEntity<BomDTO> getBom(@PathVariable Long bomId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ BOM by ID.");
        }
        BomDTO bomDTO = bomService.getBom(bomId);
        return ResponseEntity.ok(bomDTO);
    }

    /**
     * Add or update BOM lines for a specific BOM.
     * Kept for backward compatibility and individual board cases.
     * Requires UPDATE permission on BOM module.
     */
    @PostMapping("/{bomId}/lines")
    public ResponseEntity<BomDTO> addOrUpdateBomLines(
            @PathVariable Long bomId,
            @RequestBody Set<BomLineDTO> lineDTOs) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE BOM lines.");
        }
        BomDTO updatedBom = bomService.addOrUpdateBomLines(bomId, lineDTOs);
        return ResponseEntity.ok(updatedBom);
    }

    /**
     * Delete a BOM entirely.
     * Kept for backward compatibility.
     * Requires DELETE permission on BOM module.
     */
    @DeleteMapping("/{bomId}")
    public ResponseEntity<Void> deleteBom(@PathVariable Long bomId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE BOM.");
        }
        bomService.deleteBom(bomId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all BOMs for a family.
     * Requires DELETE permission on BOM module.
     */
    @DeleteMapping("/family/{familyId}")
    public ResponseEntity<Void> deleteFamilyBoms(@PathVariable Long familyId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE family BOMs.");
        }
        bomService.deleteFamilyBoms(familyId);
        return ResponseEntity.noContent().build();
    }
}
