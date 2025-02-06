-- -----------------------------------------------------
-- File: V8.7__create_custom_tables.sql
-- Description: Creates the tables for the Custom Table module
-- -----------------------------------------------------

-- Ensure we start with a clean slate by disabling foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS;
SET FOREIGN_KEY_CHECKS=0;

-- -----------------------------------------------------
-- Drop existing tables (children first, then parents)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `custom_table_data`;
DROP TABLE IF EXISTS `custom_table_sessions`;
DROP TABLE IF EXISTS `custom_table_columns`;
DROP TABLE IF EXISTS `custom_tables`;

-- -----------------------------------------------------
-- Table: custom_tables
-- -----------------------------------------------------
CREATE TABLE `custom_tables` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) DEFAULT NULL,
    `description` TEXT DEFAULT NULL,
    `created_by` VARCHAR(255) DEFAULT NULL,
    `created_at` DATETIME DEFAULT NULL,
    `last_modified_at` DATETIME DEFAULT NULL,
    `last_modified_by` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------
-- Table: custom_table_columns
-- -----------------------------------------------------
CREATE TABLE custom_table_columns (
    id BIGINT NOT NULL AUTO_INCREMENT,
    table_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) DEFAULT NULL,
    order_index INT DEFAULT 0,
    required TINYINT(1) DEFAULT 0,
    default_value VARCHAR(255) DEFAULT NULL,
    numeric_precision INT DEFAULT NULL,
    numeric_scale INT DEFAULT NULL,
    max_length INT DEFAULT NULL,
    date_format VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id),
    KEY fk_table_id_idx (table_id),
    CONSTRAINT fk_custom_table_columns_table_id
        FOREIGN KEY (table_id)
        REFERENCES custom_tables (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------
-- Table: custom_table_data
-- -----------------------------------------------------
CREATE TABLE `custom_table_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `table_id` BIGINT NOT NULL,
    `column_id` BIGINT NOT NULL,
    `row_index` INT NOT NULL,
    `cell_value` TEXT DEFAULT NULL,
    `last_modified_at` DATETIME DEFAULT NULL,
    `last_modified_by` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_data_table_id_idx` (`table_id`),
    KEY `fk_data_column_id_idx` (`column_id`),
    CONSTRAINT `fk_custom_table_data_table_id`
        FOREIGN KEY (`table_id`)
        REFERENCES `custom_tables` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `fk_custom_table_data_column_id`
        FOREIGN KEY (`column_id`)
        REFERENCES `custom_table_columns` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------
-- Table: custom_table_sessions
-- -----------------------------------------------------
CREATE TABLE `custom_table_sessions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `table_id` BIGINT NOT NULL,
    `user_id` VARCHAR(255) NOT NULL,
    `joined_at` DATETIME DEFAULT NULL,
    `last_active_at` DATETIME DEFAULT NULL,
    `is_active` TINYINT(1) DEFAULT 1,
    PRIMARY KEY (`id`),
    KEY `fk_session_table_id_idx` (`table_id`),
    CONSTRAINT `fk_custom_table_sessions_table_id`
        FOREIGN KEY (`table_id`)
        REFERENCES `custom_tables` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;