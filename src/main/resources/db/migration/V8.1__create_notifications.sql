CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `message` varchar(1000) NOT NULL,
  `module` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `severity` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  `reference_id` bigint,
  `reference_type` varchar(255),
  `read` bit(1) NOT NULL DEFAULT 0,
  `created_at` datetime(6) NOT NULL,
  `read_at` datetime(6),
  PRIMARY KEY (`id`),
  INDEX `idx_user_read` (`user_id`, `read`),
  INDEX `idx_user_created_at` (`user_id`, `created_at`),
  CONSTRAINT `fk_notifications_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB