-- Create implementation_plan table
CREATE TABLE `implementation_plan` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `side` VARCHAR(255) DEFAULT NULL,
    `fb_type` VARCHAR(255) DEFAULT NULL,
    `phase` VARCHAR(255) DEFAULT NULL,
    `comment` TEXT DEFAULT NULL,
    `fb_title` VARCHAR(255) DEFAULT NULL,
    `statut_release_pqm` VARCHAR(255) DEFAULT NULL,
    `cad_designer_name` VARCHAR(255) DEFAULT NULL,
    `visualisation_status` VARCHAR(255) DEFAULT NULL,
    `visualisation_sent_to_pqm_date` DATE DEFAULT NULL,
    `pqm_release_visualisation` VARCHAR(255) DEFAULT NULL,
    `implementation_on_fb` VARCHAR(255) DEFAULT NULL,
    `implementation_start_date` DATE DEFAULT NULL,
    `implementation_end_date` DATE DEFAULT NULL,
    `implementation_statut` VARCHAR(255) DEFAULT NULL,
    `delivered_to_pqm_date` DATE DEFAULT NULL,
    `release_start_date` DATE DEFAULT NULL,
    `release_end_date` DATE DEFAULT NULL,
    `first_check_pqm` VARCHAR(255) DEFAULT NULL,
    `reparation_bb` VARCHAR(255) DEFAULT NULL,
    `second_check_pqm` VARCHAR(255) DEFAULT NULL,
    `yellow_release_date` DATE DEFAULT NULL,
    `wh_sample_status` VARCHAR(255) DEFAULT NULL,
    `orange_release_date` DATE DEFAULT NULL,
    `green_release_date` DATE DEFAULT NULL,
    `cablage_simulation_tc` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add IMPLEMENTATION_PLAN to role_permissions module enum
ALTER TABLE role_permissions 
MODIFY COLUMN module ENUM(
    'ACTION_MANAGEMENT',
    'ANNOUNCEMENT',
    'AUDIT_LOGS',
    'BOARD',
    'BOM',
    'CHANGE_PROTOCOL',
    'CHAT',
    'DASHBOARD',
    'FB_FAMILY',
    'IMPLEMENTATION_PLAN',
    'INVENTORY',
    'KPIS',
    'MAINTENANCE',
    'MAINTENANCE_SCHEDULE',
    'PACK',
    'PHASE',
    'PROJECT',
    'ROLE',
    'SITE',
    'TOOL_MANAGEMENT',
    'USER',
    'USERS_PREVENTIVE',
    'WEEKLY_REPORT',
    'Custom'
) NOT NULL;

-- Grant IMPLEMENTATION_PLAN permissions to admin role
INSERT INTO role_permissions (role_id, module, permission_type)
SELECT id, 'IMPLEMENTATION_PLAN', permission_type
FROM roles
CROSS JOIN (
    SELECT 'CREATE' AS permission_type UNION
    SELECT 'READ' UNION
    SELECT 'UPDATE' UNION
    SELECT 'DELETE'
) AS permissions
WHERE name = 'ADMIN';