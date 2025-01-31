DELIMITER //

CREATE TRIGGER trg_inventory_low_stock
AFTER UPDATE ON inventory_items
FOR EACH ROW
BEGIN
    IF NEW.quantity <= NEW.min_quantity THEN
        INSERT INTO notifications (
            title,
            message,
            module,
            type,
            severity,
            user_id,
            reference_id,
            reference_type,
            `read`,
            created_at
        )
        SELECT
            CONCAT('Low Stock Alert: ', NEW.ref_code),
            CONCAT('Item ', NEW.ref_code, ' is running low. Current quantity (', NEW.quantity, ') is at or below minimum level (', NEW.min_quantity, ').'),
            'INVENTORY',
            'WARNING',
            'HIGH',
            id,
            NEW.id,
            'INVENTORY_ITEM',
            0,
            NOW()
        FROM users
        WHERE role_id = (SELECT id FROM roles WHERE name = 'ADMIN');
    END IF;
END //

CREATE TRIGGER trg_inventory_over_stock
AFTER UPDATE ON inventory_items
FOR EACH ROW
BEGIN
    IF NEW.quantity >= NEW.max_quantity THEN
        INSERT INTO notifications (
            title,
            message,
            module,
            type,
            severity,
            user_id,
            reference_id,
            reference_type,
            `read`,
            created_at
        )
        SELECT
            CONCAT('Over Stock Alert: ', NEW.ref_code),
            CONCAT('Item ', NEW.ref_code, ' has exceeded maximum level. Current quantity (', NEW.quantity, ') is at or above maximum level (', NEW.max_quantity, ').'),
            'INVENTORY',
            'WARNING',
            'MEDIUM',
            id,
            NEW.id,
            'INVENTORY_ITEM',
            0,
            NOW()
        FROM users
        WHERE role_id = (SELECT id FROM roles WHERE name = 'ADMIN');
    END IF;
END //

DELIMITER ;