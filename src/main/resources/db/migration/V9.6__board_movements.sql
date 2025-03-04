-- Table structure for table `board_movements`
--

DROP TABLE IF EXISTS `board_movements`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `board_movements` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `board_id` bigint NOT NULL,
  `from_location` varchar(255) DEFAULT NULL,
  `to_location` varchar(255) DEFAULT NULL,
  `moved_by` varchar(255) DEFAULT NULL,
  `move_date` datetime DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_board_id` (`board_id`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping initial data for table `board_movements`
--

LOCK TABLES `board_movements` WRITE;
/*!40000 ALTER TABLE `board_movements` DISABLE KEYS */;
-- Add initial data here if needed
/*!40000 ALTER TABLE `board_movements` ENABLE KEYS */;
UNLOCK TABLES;