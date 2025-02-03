-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: sebn_ma_bb
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `boards`
--

DROP TABLE IF EXISTS `boards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `boards` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `area` varchar(255) DEFAULT NULL,
  `board_number` varchar(255) NOT NULL,
  `comment_1` varchar(255) DEFAULT NULL,
  `comment_2` varchar(255) DEFAULT NULL,
  `comment_3` varchar(255) DEFAULT NULL,
  `cost` double DEFAULT NULL,
  `creation_date` date DEFAULT NULL,
  `creation_reason` varchar(255) DEFAULT NULL,
  `current_tech_level` varchar(255) DEFAULT NULL,
  `derivate` varchar(255) DEFAULT NULL,
  `fb_id` varchar(255) DEFAULT NULL,
  `fb_name` varchar(255) DEFAULT NULL,
  `fb_size` varchar(255) DEFAULT NULL,
  `fb_type1` varchar(255) DEFAULT NULL,
  `fb_type2` varchar(255) DEFAULT NULL,
  `fb_type3` varchar(255) DEFAULT NULL,
  `first_green_release_date` date DEFAULT NULL,
  `first_orange_release_date` date DEFAULT NULL,
  `first_tech_level` varchar(255) DEFAULT NULL,
  `first_use_by_prod_date` date DEFAULT NULL,
  `first_yellow_release_date` date DEFAULT NULL,
  `in_use` varchar(255) DEFAULT NULL,
  `last_tech_change_imple_date` date DEFAULT NULL,
  `last_tech_change_implemented` varchar(255) DEFAULT NULL,
  `last_tech_change_release_date` date DEFAULT NULL,
  `next_tech_level` varchar(255) DEFAULT NULL,
  `plant` varchar(255) DEFAULT NULL,
  `projet` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `side` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `storage_place` varchar(255) DEFAULT NULL,
  `test_clip` bit(1) DEFAULT NULL,
  `assigned_user_id` bigint DEFAULT NULL,
  `family_id` bigint DEFAULT NULL,
  `pack_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKibpfm7pcl75relmu1ax4cnays` (`assigned_user_id`),
  KEY `FK34h2qw29t0wty3aso01g2y77q` (`family_id`),
  KEY `FKfo4yeju6hvhfds3v71d60g8as` (`pack_id`),
  CONSTRAINT `FK34h2qw29t0wty3aso01g2y77q` FOREIGN KEY (`family_id`) REFERENCES `board_families` (`id`),
  CONSTRAINT `FKfo4yeju6hvhfds3v71d60g8as` FOREIGN KEY (`pack_id`) REFERENCES `packs` (`id`),
  CONSTRAINT `FKibpfm7pcl75relmu1ax4cnays` FOREIGN KEY (`assigned_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB;

--
-- Dumping data for table `boards`
--

LOCK TABLES `boards` WRITE;
/*!40000 ALTER TABLE `boards` DISABLE KEYS */;
/*!40000 ALTER TABLE `boards` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-27 11:42:49
