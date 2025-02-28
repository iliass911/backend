package com.sebn.brettbau.domain.inventory.service;

import com.sebn.brettbau.domain.audit.service.AuditLogService;
import com.sebn.brettbau.domain.inventory.dto.InventoryBookingDTO;
import com.sebn.brettbau.domain.inventory.entity.BookingStatus;
import com.sebn.brettbau.domain.inventory.entity.InventoryBooking;
import com.sebn.brettbau.domain.inventory.entity.InventoryItem;
import com.sebn.brettbau.domain.inventory.repository.InventoryBookingRepository;
import com.sebn.brettbau.domain.inventory.repository.InventoryItemRepository;
import com.sebn.brettbau.domain.notification.service.NotificationService;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.repository.UserRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InventoryBookingService {

    private final InventoryBookingRepository bookingRepository;
    private final InventoryItemRepository itemRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;

    @Transactional
    public InventoryBookingDTO createBooking(List<Long> itemIds, String reason) {
        if (itemIds == null || itemIds.isEmpty()) {
            throw new IllegalArgumentException("Item IDs cannot be null or empty");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking reason cannot be null or empty");
        }

        // Get username from security context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Creating booking for user: {}", username);

        // Find user
        User bookedByUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalStateException("User not found: " + username));
        
        log.debug("Found user with ID: {}", bookedByUser.getId());

        // Verify all items exist
        List<InventoryItem> items = itemRepository.findAllById(itemIds);
        if (items.size() != itemIds.size()) {
            List<Long> foundIds = items.stream()
                .map(InventoryItem::getId)
                .collect(Collectors.toList());
            List<Long> missingIds = itemIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toList());
            throw new ResourceNotFoundException("Items not found with IDs: " + missingIds);
        }

        // Check for existing active bookings
        List<InventoryBooking> existingActiveBookings = 
            bookingRepository.findActiveBookingsForItems(itemIds);

        if (!existingActiveBookings.isEmpty()) {
            List<String> bookedItems = existingActiveBookings.stream()
                .flatMap(booking -> booking.getItems().stream())
                .map(item -> String.format("%s (ID: %d)", item.getRefCode(), item.getId()))
                .collect(Collectors.toList());
            throw new IllegalStateException(
                "Some items are already in active bookings: " + String.join(", ", bookedItems));
        }

        try {
            // Create and save the booking
            InventoryBooking booking = InventoryBooking.builder()
                .bookingDate(LocalDateTime.now())
                .reason(reason.trim())
                .status(BookingStatus.ACTIVE)
                .bookedBy(bookedByUser)
                .items(new HashSet<>(items))
                .build();

            log.debug("Saving booking with user ID: {}", bookedByUser.getId());
            booking = bookingRepository.save(booking);
            log.debug("Created booking with ID: {}", booking.getId());

            // Create notification
            notificationService.createNotification(
                "Items Booked",
                String.format("%d items booked by %s: %s",
                    items.size(),
                    bookedByUser.getUsername(),
                    reason
                ),
                "INVENTORY",
                "INFO",
                "MEDIUM",
                bookedByUser.getId(),
                booking.getId(),
                "INVENTORY_BOOKING"
            );

            // Audit log
            auditLogService.logEvent(
                bookedByUser,
                "INVENTORY_BOOKED",
                String.format("Booked %d items. Reason: %s",
                    items.size(), reason
                ),
                "INVENTORY",
                booking.getId()
            );

            return mapToDTO(booking);

        } catch (Exception e) {
            log.error("Error creating booking for user ID {}: {}", bookedByUser.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to create booking: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void releaseBooking(Long bookingId) {
        InventoryBooking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new IllegalStateException("Cannot release a booking that is not active");
        }

        // Get username from security context for auditing
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User releasedByUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalStateException("User not found: " + username));

        // Update booking status
        booking.setStatus(BookingStatus.RELEASED);
        bookingRepository.save(booking);

        // Create notification
        notificationService.createNotification(
            "Booking Released",
            String.format("Booking #%d released by %s", booking.getId(), releasedByUser.getUsername()),
            "INVENTORY",
            "INFO",
            "MEDIUM",
            releasedByUser.getId(),
            booking.getId(),
            "INVENTORY_BOOKING"
        );

        // Audit log
        auditLogService.logEvent(
            releasedByUser,
            "INVENTORY_BOOKING_RELEASED",
            String.format("Released booking #%d containing %d items", 
                booking.getId(), 
                booking.getItems().size()),
            "INVENTORY",
            booking.getId()
        );

        log.info("Booking {} released by user {}", bookingId, releasedByUser.getUsername());
    }

    private InventoryBookingDTO mapToDTO(InventoryBooking booking) {
        InventoryBookingDTO dto = new InventoryBookingDTO();
        dto.setId(booking.getId());
        dto.setBookingDate(booking.getBookingDate());
        dto.setReason(booking.getReason());
        dto.setStatus(booking.getStatus().name());
        dto.setBookedById(booking.getBookedBy().getId());
        dto.setBookedByName(booking.getBookedBy().getUsername());
        
        dto.setItemIds(
            booking.getItems().stream()
                .map(InventoryItem::getId)
                .collect(Collectors.toList())
        );
        dto.setItemRefCodes(
            booking.getItems().stream()
                .map(InventoryItem::getRefCode)
                .collect(Collectors.toList())
        );
        
        return dto;
    }
}