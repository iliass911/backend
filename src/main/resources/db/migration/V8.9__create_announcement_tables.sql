-- src/main/resources/db/migration/V8.9__create_announcement_tables.sql
CREATE TABLE announcements (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    message VARCHAR(2000) NOT NULL,
    creator_id BIGINT NOT NULL,
    is_anonymous BIT(1) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (creator_id) REFERENCES users(id)
);

CREATE TABLE announcement_target_modules (
    announcement_id BIGINT NOT NULL,
    module VARCHAR(50) NOT NULL,
    PRIMARY KEY (announcement_id, module),
    FOREIGN KEY (announcement_id) REFERENCES announcements(id)
);

-- Add announcement to role_permissions module enum
ALTER TABLE role_permissions 
MODIFY COLUMN module ENUM('ACTION_MANAGEMENT','ANNOUNCEMENT','AUDIT_LOGS','BOARD','BOM',
'CHANGE_PROTOCOL','DASHBOARD','FB_FAMILY','INVENTORY','KPIS','MAINTENANCE',
'MAINTENANCE_SCHEDULE','PACK','PHASE','PROJECT','ROLE','SITE','USER','USERS_PREVENTIVE',
'WEEKLY_REPORT','Custom') NOT NULL;

-- Grant announcement permissions to admin role
INSERT INTO role_permissions (role_id, module, permission_type)
SELECT id, 'ANNOUNCEMENT', permission_type
FROM roles
CROSS JOIN (
    SELECT 'CREATE' AS permission_type UNION
    SELECT 'READ' UNION
    SELECT 'UPDATE' UNION
    SELECT 'DELETE'
) AS permissions
WHERE name = 'ADMIN';