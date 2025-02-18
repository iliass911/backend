-- V9.2__create_tool_management_tables.sql
-- Migration script for the Tool Management module

-- (Optional) Drop existing tables if needed
DROP TABLE IF EXISTS tool_maintenance;
DROP TABLE IF EXISTS tool_requests;
DROP TABLE IF EXISTS tool_transfers;
DROP TABLE IF EXISTS tools;

-- Create the "tools" table with snake_case columns
CREATE TABLE tools (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tool_id VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    type VARCHAR(255),
    location VARCHAR(255),
    `condition` VARCHAR(255),
    status VARCHAR(50),
    current_holder VARCHAR(255),
    last_maintained DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create the "tool_requests" table with snake_case columns
CREATE TABLE tool_requests (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tool_id BIGINT NOT NULL,
    requested_by VARCHAR(255) NOT NULL,
    approved_by VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    requested_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expected_return_date DATETIME,
    purpose TEXT,
    notes TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_tool_requests_tool
        FOREIGN KEY (tool_id) REFERENCES tools(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create the "tool_transfers" table with snake_case columns
CREATE TABLE tool_transfers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tool_id BIGINT NOT NULL,
    from_user VARCHAR(255) NOT NULL,
    to_user VARCHAR(255) NOT NULL,
    condition_before VARCHAR(255),
    condition_after VARCHAR(255),
    transferred_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    photo_urls TEXT,
    status VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_tool_transfers_tool
        FOREIGN KEY (tool_id) REFERENCES tools(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create the "tool_maintenance" table with snake_case columns
CREATE TABLE tool_maintenance (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tool_id BIGINT NOT NULL,
    type VARCHAR(255) NOT NULL,
    requested_by VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    requested_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME,
    notes TEXT,
    cost DECIMAL(10,2),
    PRIMARY KEY (id),
    CONSTRAINT fk_tool_maintenance_tool
        FOREIGN KEY (tool_id) REFERENCES tools(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- (Optional) Create indexes on foreign key columns for optimization
CREATE INDEX idx_tool_requests_tool_id ON tool_requests(tool_id);
CREATE INDEX idx_tool_transfers_tool_id ON tool_transfers(tool_id);
CREATE INDEX idx_tool_maintenance_tool_id ON tool_maintenance(tool_id);

-- Update the "role_permissions" table to include the new TOOL_MANAGEMENT module
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
    'TOOL_MANAGEMENT',
    'FB_FAMILY',
    'INVENTORY',
    'KPIS',
    'MAINTENANCE',
    'MAINTENANCE_SCHEDULE',
    'PACK',
    'PHASE',
    'PROJECT',
    'ROLE',
    'SITE',
    'USER',
    'USERS_PREVENTIVE',
    'WEEKLY_REPORT',
    'Custom'
) NOT NULL;

-- Grant all TOOL_MANAGEMENT permissions to the admin role
INSERT INTO role_permissions (role_id, module, permission_type)
SELECT id, 'TOOL_MANAGEMENT', permission_type
FROM roles
CROSS JOIN (
    SELECT 'CREATE' AS permission_type UNION
    SELECT 'READ' UNION
    SELECT 'UPDATE' UNION
    SELECT 'DELETE'
) AS permissions
WHERE name = 'ADMIN';
