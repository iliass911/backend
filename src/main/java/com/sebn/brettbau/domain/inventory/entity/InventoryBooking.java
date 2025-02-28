package com.sebn.brettbau.domain.inventory.entity;

import com.sebn.brettbau.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "inventory_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "booked_by", nullable = false)
    private User bookedBy;

    @ManyToMany
    @JoinTable(
        name = "inventory_booking_items",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Set<InventoryItem> items = new HashSet<>();
}
