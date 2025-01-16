// BomController.java  
package com.example.backend.domain.bom.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.bom.dto.BomDTO;
import com.example.backend.domain.bom.dto.BomLineDTO;
import com.example.backend.domain.bom.service.BomService;
import com.example.backend.domain.role.service.PermissionChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/boms")
public class BomController extends BaseController {
    private final BomService bomService;

    public BomController(PermissionChecker permissionChecker, BomService bomService) {
        super(permissionChecker);
        this.bomService = bomService;
    }

    @PostMapping("/family/{familyId}")
    public ResponseEntity<List<BomDTO>> createBomForFamily(@PathVariable Long familyId) {
        checkPermission("BOM", "CREATE");
        List<BomDTO> createdBoms = bomService.createBomForFamily(familyId);
        return ResponseEntity.ok(createdBoms);
    }

    @GetMapping("/family/{familyId}")
    public ResponseEntity<BomDTO> getBomByFamilyId(@PathVariable Long familyId) {
        checkPermission("BOM", "VIEW");
        BomDTO bomDTO = bomService.getBomByFamilyId(familyId);
        return ResponseEntity.ok(bomDTO);
    }

    @PostMapping("/family/{familyId}/lines") 
    public ResponseEntity<List<BomDTO>> addOrUpdateFamilyBomLines(
            @PathVariable Long familyId,
            @RequestBody Set<BomLineDTO> lineDTOs) {
        checkPermission("BOM", "MODIFY");  
        List<BomDTO> updatedBoms = bomService.addOrUpdateFamilyBomLines(familyId, lineDTOs);
        return ResponseEntity.ok(updatedBoms);
    }

    @PostMapping("/board/{boardId}")
    public ResponseEntity<BomDTO> createBomForBoard(@PathVariable Long boardId) {
        checkPermission("BOM", "CREATE");
        BomDTO createdBom = bomService.createBom(boardId);
        return ResponseEntity.ok(createdBom);
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<BomDTO> getBomByBoardId(@PathVariable Long boardId) {
        checkPermission("BOM", "VIEW");  
        BomDTO bomDTO = bomService.getBomByBoardId(boardId);
        return ResponseEntity.ok(bomDTO);
    }

    @GetMapping("/{bomId}")
    public ResponseEntity<BomDTO> getBom(@PathVariable Long bomId) {
        checkPermission("BOM", "VIEW");
        BomDTO bomDTO = bomService.getBom(bomId);
        return ResponseEntity.ok(bomDTO);
    }

    @PostMapping("/{bomId}/lines")
    public ResponseEntity<BomDTO> addOrUpdateBomLines(
            @PathVariable Long bomId,
            @RequestBody Set<BomLineDTO> lineDTOs) {
        checkPermission("BOM", "MODIFY");  
        BomDTO updatedBom = bomService.addOrUpdateBomLines(bomId, lineDTOs);
        return ResponseEntity.ok(updatedBom);
    }

    @DeleteMapping("/{bomId}")
    public ResponseEntity<Void> deleteBom(@PathVariable Long bomId) {
        checkPermission("BOM", "DELETE");
        bomService.deleteBom(bomId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/family/{familyId}")
    public ResponseEntity<Void> deleteFamilyBoms(@PathVariable Long familyId) {
        checkPermission("BOM", "DELETE");
        bomService.deleteFamilyBoms(familyId);
        return ResponseEntity.noContent().build();
    }
}