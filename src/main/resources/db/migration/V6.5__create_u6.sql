DROP TABLE IF EXISTS u6;

CREATE TABLE `u6` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `date` DATE DEFAULT NULL,
    `mois` VARCHAR(255) DEFAULT NULL,
    `semaine` VARCHAR(255) DEFAULT NULL,
    `equipment` VARCHAR(255) DEFAULT NULL,
    `equipment_name` VARCHAR(255) DEFAULT NULL,
    `site` VARCHAR(255) DEFAULT NULL,
    `projet` VARCHAR(255) DEFAULT NULL,
    `shift` VARCHAR(255) DEFAULT NULL,
    `duree_arret` INT DEFAULT NULL,
    `accepte` VARCHAR(50) DEFAULT NULL,
    `nbr_operateurs` INT DEFAULT NULL,
    `description_defaillance` VARCHAR(255) DEFAULT NULL,
    `description_action_corrective` VARCHAR(255) DEFAULT NULL,
    `intervenant` VARCHAR(255) DEFAULT NULL,
    `type_equipement` VARCHAR(255) DEFAULT NULL,
    `nature_intervention` VARCHAR(255) DEFAULT NULL,
    `lien_scan_fiche_bde` VARCHAR(255) DEFAULT NULL,
    `installation_date` DATE DEFAULT NULL,
    `equipment_status` VARCHAR(100) DEFAULT NULL,
    `mtbf` DOUBLE DEFAULT NULL,
    `mttr` DOUBLE DEFAULT NULL,
    `record_type` VARCHAR(50) DEFAULT 'downtime',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
