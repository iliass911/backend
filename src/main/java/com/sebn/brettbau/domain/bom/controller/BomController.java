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

 
    @PostMapping("/family/{familyId}")
    public ResponseEntity<List<BomDTO>> createBomForFamily(@PathVariable Long familyId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE BOM for family.");
        }
        List<BomDTO> createdBoms = bomService.createBomForFamily(familyId);
        return ResponseEntity.ok(createdBoms);
    }

 
    @GetMapping("/family/{familyId}")
    public ResponseEntity<BomDTO> getBomByFamilyId(@PathVariable Long familyId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ BOM by family ID.");
        }
        BomDTO bomDTO = bomService.getBomByFamilyId(familyId);
        return ResponseEntity.ok(bomDTO);
    }

 
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

 
    @PostMapping("/board/{boardId}")
    public ResponseEntity<BomDTO> createBomForBoard(@PathVariable Long boardId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE BOM for board.");
        }
        BomDTO createdBom = bomService.createBom(boardId);
        return ResponseEntity.ok(createdBom);
    }


    @GetMapping("/board/{boardId}")
    public ResponseEntity<BomDTO> getBomByBoardId(@PathVariable Long boardId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ BOM by board ID.");
        }
        BomDTO bomDTO = bomService.getBomByBoardId(boardId);
        return ResponseEntity.ok(bomDTO);
    }


    @GetMapping("/{bomId}")
    public ResponseEntity<BomDTO> getBom(@PathVariable Long bomId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ BOM by ID.");
        }
        BomDTO bomDTO = bomService.getBom(bomId);
        return ResponseEntity.ok(bomDTO);
    }


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

  
    @DeleteMapping("/{bomId}")
    public ResponseEntity<Void> deleteBom(@PathVariable Long bomId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOM, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE BOM.");
        }
        bomService.deleteBom(bomId);
        return ResponseEntity.noContent().build();
    }

 
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
