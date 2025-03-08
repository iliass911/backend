SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS;
SET FOREIGN_KEY_CHECKS=0;

-- -----------------------------------------------------
-- Drop existing tables if they exist
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bom_items`;
DROP TABLE IF EXISTS `board_families`;

-- -----------------------------------------------------
-- Table: board_families
-- -----------------------------------------------------
CREATE TABLE `board_families` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`family_name` VARCHAR(255) NOT NULL,
`project` VARCHAR(255) NOT NULL,
`side` VARCHAR(50) NOT NULL,
`derivate` VARCHAR(255) NOT NULL,
`number_of_boards` INT NOT NULL,
`phase` VARCHAR(50) NOT NULL,
`created_at` DATETIME NOT NULL,
`updated_at` DATETIME NOT NULL,
PRIMARY KEY (`id`),
INDEX `idx_project_phase` (`project`, `phase`),
INDEX `idx_family_name` (`family_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------
-- Table: bom_items
-- -----------------------------------------------------
CREATE TABLE `bom_items` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`board_family_id` BIGINT NOT NULL,
`segment` VARCHAR(255) NOT NULL,
`kurzname` VARCHAR(255) NOT NULL,
`ident_matchcode` VARCHAR(255) NOT NULL,
`model_type` VARCHAR(50),
`sesam_number` VARCHAR(100),
`missing_on_board` BIT(1) DEFAULT 0,
`quantity_on_board` INT NOT NULL,
`observation` TEXT,
`price` DECIMAL(10,2),
`created_at` DATETIME NOT NULL,
`updated_at` DATETIME NOT NULL,
-- New column for motif
`motif` VARCHAR(500),
PRIMARY KEY (`id`),
INDEX `idx_board_family` (`board_family_id`),
INDEX `idx_ident_matchcode` (`ident_matchcode`),
CONSTRAINT `fk_bom_board_family`
FOREIGN KEY (`board_family_id`)
REFERENCES `board_families` (`id`)
ON DELETE CASCADE
ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------
-- Add module to role_permissions enum
-- -----------------------------------------------------
ALTER TABLE role_permissions
MODIFY COLUMN module ENUM(
'ACTION_MANAGEMENT','ANNOUNCEMENT','AUDIT_LOGS','BOARD','BOM',
'CHANGE_PROTOCOL','CHAT','DASHBOARD','FB_FAMILY','INVENTORY',
'KPIS','MAINTENANCE','MAINTENANCE_SCHEDULE','PACK','PHASE',
'PROJECT','ROLE','SITE','USER','USERS_PREVENTIVE','WEEKLY_REPORT',
'Custom'
) NOT NULL;

-- -----------------------------------------------------
-- Grant permissions to admin role
-- -----------------------------------------------------
INSERT INTO role_permissions (role_id, module, permission_type)
SELECT id, 'FB_FAMILY', permission_type
FROM roles
CROSS JOIN (
SELECT 'CREATE' AS permission_type UNION
SELECT 'READ' UNION
SELECT 'UPDATE' UNION
SELECT 'DELETE'
) AS permissions
WHERE name = 'ADMIN';

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;