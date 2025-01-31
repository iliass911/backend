ALTER TABLE `inventory_items`
ADD COLUMN `min_quantity` int NOT NULL DEFAULT 0,
ADD COLUMN `max_quantity` int NOT NULL DEFAULT 999999,
ADD INDEX `idx_quantity_check` (`quantity`, `min_quantity`, `max_quantity`);

CREATE OR REPLACE VIEW `low_stock_items` AS
SELECT id, ref_code, site, quantity, min_quantity
FROM inventory_items
WHERE quantity <= min_quantity;

CREATE OR REPLACE VIEW `over_stock_items` AS
SELECT id, ref_code, site, quantity, max_quantity
FROM inventory_items
WHERE quantity >= max_quantity;