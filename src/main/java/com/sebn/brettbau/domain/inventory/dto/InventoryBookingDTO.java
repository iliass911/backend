package com.sebn.brettbau.domain.inventory.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InventoryBookingDTO {
    private Long id;
    private LocalDateTime bookingDate;
    private String reason;
    private String status;
    private Long bookedById;
    private String bookedByName;
    private List<Long> itemIds;
    private List<String> itemRefCodes;
}
