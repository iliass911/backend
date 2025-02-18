-- src/main/resources/db/migration/V9.0__create_chat_tables.sql

-- Drop existing tables if they exist
DROP TABLE IF EXISTS chat_mentions;
DROP TABLE IF EXISTS chat_messages;

-- Create messages table with file attachment support
CREATE TABLE chat_messages (
    id BIGINT NOT NULL AUTO_INCREMENT,
    content VARCHAR(2000) NOT NULL,
    sender_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    is_everyone_mention BIT(1) NOT NULL DEFAULT 0,
    has_attachment BIT(1) NOT NULL DEFAULT 0,
    attachment_url VARCHAR(1000),
    attachment_type VARCHAR(50),
    attachment_name VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (sender_id) REFERENCES users(id),
    INDEX idx_has_attachment (has_attachment)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create mentions table with indexes for better performance
CREATE TABLE chat_mentions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    message_id BIGINT NOT NULL,
    mentioned_user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (message_id) REFERENCES chat_messages(id) ON DELETE CASCADE,
    FOREIGN KEY (mentioned_user_id) REFERENCES users(id),
    INDEX idx_message_mentions (message_id, mentioned_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Modify role_permissions to include CHAT module if not exists
ALTER TABLE role_permissions 
MODIFY COLUMN module ENUM(
    'ACTION_MANAGEMENT','ANNOUNCEMENT','AUDIT_LOGS','BOARD','BOM',
    'CHANGE_PROTOCOL','CHAT','DASHBOARD','FB_FAMILY','INVENTORY','KPIS','MAINTENANCE',
    'MAINTENANCE_SCHEDULE','PACK','PHASE','PROJECT','ROLE','SITE','USER','USERS_PREVENTIVE',
    'WEEKLY_REPORT','Custom'
) NOT NULL;

-- Grant all chat permissions to admin role
INSERT INTO role_permissions (role_id, module, permission_type)
SELECT id, 'CHAT', permission_type
FROM roles
CROSS JOIN (
    SELECT 'CREATE' AS permission_type UNION
    SELECT 'READ' UNION
    SELECT 'UPDATE' UNION
    SELECT 'DELETE'
) AS permissions
WHERE name = 'ADMIN';

-- Create event to clean old messages
DROP EVENT IF EXISTS clean_old_chat_messages;
CREATE EVENT clean_old_chat_messages
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_TIMESTAMP + INTERVAL 1 MINUTE
DO
    DELETE FROM chat_messages 
    WHERE created_at < DATE_SUB(NOW(), INTERVAL 2 DAY);