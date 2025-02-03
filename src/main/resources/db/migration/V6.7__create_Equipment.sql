DROP TABLE IF EXISTS equipment;

CREATE TABLE `equipment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `equipment_id` VARCHAR(255) DEFAULT NULL,
    `equipment_name` VARCHAR(255) DEFAULT NULL,
    `type` VARCHAR(255) DEFAULT NULL,
    `projet` VARCHAR(255) DEFAULT NULL,
    `site` VARCHAR(255) DEFAULT NULL,
    `installation_date` DATE DEFAULT NULL,
    `status` VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB