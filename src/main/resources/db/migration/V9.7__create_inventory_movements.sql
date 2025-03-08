-- Create table for inventory movements to track stock changes
CREATE TABLE `inventory_movements` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `item_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `timestamp` DATETIME NOT NULL,
  `previous_quantity` INT NOT NULL,
  `new_quantity` INT NOT NULL,
  `quantity_changed` INT NOT NULL,
  `movement_type` VARCHAR(50) NOT NULL,
  `reason` VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_movement_item` (`item_id`),
  KEY `idx_movement_user` (`user_id`),
  KEY `idx_movement_timestamp` (`timestamp`),
  KEY `idx_movement_type` (`movement_type`),
  CONSTRAINT `fk_movement_item` FOREIGN KEY (`item_id`) REFERENCES `inventory_items` (`id`),
  CONSTRAINT `fk_movement_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

-- Create view for inventory movements with item details
CREATE OR REPLACE VIEW `inventory_movement_details` AS
SELECT 
  m.id,
  m.item_id,
  i.ref_code AS item_ref_code,
  i.site AS item_site,
  i.type AS item_type,
  i.place AS item_place,
  m.user_id,
  u.username,
  m.timestamp,
  m.previous_quantity,
  m.new_quantity,
  m.quantity_changed,
  m.movement_type,
  m.reason
FROM 
  inventory_movements m
JOIN 
  inventory_items i ON m.item_id = i.id
JOIN 
  users u ON m.user_id = u.id;