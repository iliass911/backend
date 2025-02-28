package com.sebn.brettbau.domain.inventory.dto;

public class ActiveBookingDTO {
    private Long itemId;
    private Long bookingId;
    private String reason;

    public ActiveBookingDTO(Long itemId, Long bookingId, String reason) {
        this.itemId = itemId;
        this.bookingId = bookingId;
        this.reason = reason;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getReason() {
        return reason;
    }
}
