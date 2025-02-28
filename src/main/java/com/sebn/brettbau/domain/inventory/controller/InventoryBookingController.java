package com.sebn.brettbau.domain.inventory.controller;

import com.sebn.brettbau.domain.inventory.dto.ActiveBookingDTO;
import com.sebn.brettbau.domain.inventory.dto.InventoryBookingDTO;
import com.sebn.brettbau.domain.inventory.entity.BookingStatus;
import com.sebn.brettbau.domain.inventory.repository.InventoryBookingRepository;
import com.sebn.brettbau.domain.inventory.service.InventoryBookingService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/inventory/bookings")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class InventoryBookingController {

    private final InventoryBookingService bookingService;
    private final RoleService roleService;
    private final UserService userService;
    private final InventoryBookingRepository bookingRepository;

    @PostMapping
    public ResponseEntity<InventoryBookingDTO> createBooking(
            @RequestBody @Valid BookingRequest request) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to book inventory items");
        }

        InventoryBookingDTO dto = bookingService.createBooking(
            request.getItemIds(),
            request.getReason()
        );
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> releaseBooking(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to release inventory bookings");
        }

        bookingService.releaseBooking(id);
        return ResponseEntity.noContent().build();
    }

    // Updated GET endpoint returning active bookings with reason
    @GetMapping("/active-items")
    public List<ActiveBookingDTO> getActiveBookedItems() {
        return bookingRepository.findAllActiveBookingItems(BookingStatus.ACTIVE);
    }

    // DTO for booking creation
    static class BookingRequest {
        private List<Long> itemIds;
        private String reason;
        
        public List<Long> getItemIds() { return itemIds; }
        public void setItemIds(List<Long> itemIds) { this.itemIds = itemIds; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
}
