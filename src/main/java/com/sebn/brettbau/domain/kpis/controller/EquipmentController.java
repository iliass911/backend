	package com.sebn.brettbau.domain.kpis.controller;
	
	import com.sebn.brettbau.domain.kpis.entity.Equipment;
	import com.sebn.brettbau.domain.kpis.service.EquipmentService;
	import com.sebn.brettbau.domain.role.service.RoleService;
	import com.sebn.brettbau.domain.security.Module;
	import com.sebn.brettbau.domain.security.PermissionType;
	import com.sebn.brettbau.domain.user.entity.User;
	import com.sebn.brettbau.domain.user.service.UserService;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.security.access.AccessDeniedException;
	import org.springframework.validation.annotation.Validated;
	import org.springframework.web.bind.annotation.*;
	
	import java.util.List;
	import java.util.Optional;
	import java.util.stream.Collectors;
	
	@RestController
	@RequestMapping("/api/equipment")
	@CrossOrigin(origins = "http://10.150.2.201:3000")
	@Validated
	public class EquipmentController {
	
	    private final EquipmentService equipmentService;
	    private final RoleService roleService;
	    private final UserService userService;
	
	    @Autowired
	    public EquipmentController(EquipmentService equipmentService, RoleService roleService, UserService userService) {
	        this.equipmentService = equipmentService;
	        this.roleService = roleService;
	        this.userService = userService;
	    }
	
	    @PostMapping
	    public ResponseEntity<Equipment> create(@RequestBody @Validated Equipment equipment) {
	        User currentUser = userService.getCurrentUser();
	        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.CREATE)) {
	            throw new AccessDeniedException("No permission to create Equipment");
	        }
	        Equipment savedEquipment = equipmentService.save(equipment);
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedEquipment);
	    }
	
	    @GetMapping
	    public ResponseEntity<List<Equipment>> getAll() {
	        User currentUser = userService.getCurrentUser();
	        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.READ)) {
	            throw new AccessDeniedException("No permission to read Equipment");
	        }
	        List<Equipment> equipments = equipmentService.findAll();
	        return ResponseEntity.ok(equipments);
	    }
	
	    @GetMapping("/{id}")
	    public ResponseEntity<Equipment> getById(@PathVariable Long id) {
	        User currentUser = userService.getCurrentUser();
	        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.READ)) {
	            throw new AccessDeniedException("No permission to read Equipment");
	        }
	        Optional<Equipment> equipment = equipmentService.findById(id);
	        return equipment.map(ResponseEntity::ok)
	                        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
	    }
	
	    @PutMapping("/{id}")
	    public ResponseEntity<Equipment> update(@PathVariable Long id, @RequestBody @Validated Equipment equipment) {
	        User currentUser = userService.getCurrentUser();
	        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.UPDATE)) {
	            throw new AccessDeniedException("No permission to update Equipment");
	        }
	        Optional<Equipment> existing = equipmentService.findById(id);
	        if (existing.isPresent()) {
	            equipment.setId(id);
	            Equipment updatedEquipment = equipmentService.save(equipment);
	            return ResponseEntity.ok(updatedEquipment);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
	    }
	
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> delete(@PathVariable Long id) {
	        User currentUser = userService.getCurrentUser();
	        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.DELETE)) {
	            throw new AccessDeniedException("No permission to delete Equipment");
	        }
	        Optional<Equipment> existing = equipmentService.findById(id);
	        if (existing.isPresent()) {
	            equipmentService.deleteById(id);
	            return ResponseEntity.noContent().build();
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
	    }
	
	    // Bulk creation endpoint
	    @PostMapping("/bulk")
	    public ResponseEntity<List<Equipment>> createBulk(@RequestBody @Validated List<Equipment> equipments) {
	        User currentUser = userService.getCurrentUser();
	        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.CREATE)) {
	            throw new AccessDeniedException("No permission to create Equipment");
	        }
	        
	        // Option 1: If you have a saveAll method in your service:
	        // List<Equipment> savedEquipments = equipmentService.saveAll(equipments);
	
	        // Option 2: Otherwise, save each equipment individually
	        List<Equipment> savedEquipments = equipments.stream()
	                .map(equipmentService::save)
	                .collect(Collectors.toList());
	                
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedEquipments);
	    }
	}
