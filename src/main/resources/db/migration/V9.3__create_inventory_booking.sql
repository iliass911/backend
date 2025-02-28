-- Create table for inventory bookings
CREATE TABLE `inventory_bookings` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `booking_date` DATETIME NOT NULL,
  `reason` VARCHAR(500) NOT NULL,
  `status` VARCHAR(20) NOT NULL,
  `booked_by` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`booked_by`) REFERENCES `users` (`id`)
);

-- Create junction table for booking-items relationship
CREATE TABLE `inventory_booking_items` (
  `booking_id` BIGINT NOT NULL,
  `item_id` BIGINT NOT NULL,
  PRIMARY KEY (`booking_id`, `item_id`),
  FOREIGN KEY (`booking_id`) REFERENCES `inventory_bookings` (`id`),
  FOREIGN KEY (`item_id`) REFERENCES `inventory_items` (`id`)
);
