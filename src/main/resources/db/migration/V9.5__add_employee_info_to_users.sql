ALTER TABLE `users` 
ADD COLUMN `full_name` VARCHAR(255) COMMENT 'Employee full name (N&P from data)',
ADD COLUMN `site` VARCHAR(255) COMMENT 'Work site location (SEBN Ma1, SEBN Ma2, Sat1, etc.)',
ADD COLUMN `project` VARCHAR(255) COMMENT 'Project assignment (T_roc, HV, Q7, Golf, etc.)',
ADD COLUMN `job_function` VARCHAR(255) COMMENT 'Job function/title',  -- Changed from function
ADD COLUMN `assignment_date` DATE COMMENT 'Date of assignment to BrettBau',
ADD COLUMN `seniority` DOUBLE COMMENT 'Seniority in years',
ADD COLUMN `status` VARCHAR(100) COMMENT 'Status like Formation, Intégration, etc.';

-- Update the admin user with dummy data
UPDATE `users` 
SET 
    `full_name` = 'Administrator',
    `site` = 'SEBN HQ',
    `project` = 'All Projects',
    `job_function` = 'System Administrator',  -- Changed from function
    `assignment_date` = '2020-01-01',
    `seniority` = 5.0,
    `status` = 'Active'
WHERE `id` = 1;

-- Create index on frequently queried fields
CREATE INDEX `idx_user_site_project` ON `users` (`site`, `project`);
CREATE INDEX `idx_user_job_function` ON `users` (`job_function`);  -- Changed from function