package com.sebn.brettbau.domain.inventory.repository;

import com.sebn.brettbau.domain.inventory.dto.ActiveBookingDTO;
import com.sebn.brettbau.domain.inventory.entity.BookingStatus;
import com.sebn.brettbau.domain.inventory.entity.InventoryBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface InventoryBookingRepository extends JpaRepository<InventoryBooking, Long> {

    // Find any ACTIVE bookings that contain any of the given item IDs
    @Query("select b from InventoryBooking b join b.items i " +
           "where i.id in :itemIds and b.status = 'ACTIVE'")
    List<InventoryBooking> findActiveBookingsForItems(@Param("itemIds") List<Long> itemIds);

    // Return IDs of items that are currently in an ACTIVE booking
    @Query("select i.id from InventoryBooking b join b.items i " +
           "where b.status = 'ACTIVE'")
    List<Long> findAllItemIdsInActiveBookings();
    
    // Updated query: include b.reason in the DTO
    @Query("select new com.sebn.brettbau.domain.inventory.dto.ActiveBookingDTO(i.id, b.id, b.reason) " +
           "from InventoryBooking b join b.items i " +
           "where b.status = :activeStatus")
    List<ActiveBookingDTO> findAllActiveBookingItems(@Param("activeStatus") BookingStatus activeStatus);
}
