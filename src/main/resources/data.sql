-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: busify
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audit_logs`
--

DROP TABLE IF EXISTS `audit_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `details` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `target_entity` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `target_id` bigint DEFAULT NULL,
  `timestamp` datetime(6) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjs4iimve3y0xssbtve5ysyef0` (`user_id`),
  CONSTRAINT `FKjs4iimve3y0xssbtve5ysyef0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `audit_logs_chk_1` CHECK (json_valid(`details`))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_logs`
--

LOCK TABLES `audit_logs` WRITE;
/*!40000 ALTER TABLE `audit_logs` DISABLE KEYS */;
INSERT INTO `audit_logs` VALUES (1,'CREATE_BOOKING','{\"booking_code\": \"BOOK123\"}','BOOKING',1,'2025-07-24 12:00:00.000000',1),(2,'CREATE_BOOKING','{\"booking_code\": \"BOOK124\"}','BOOKING',2,'2025-07-24 13:00:00.000000',5),(3,'CREATE_BOOKING','{\"booking_code\": \"BOOK125\"}','BOOKING',3,'2025-07-24 14:00:00.000000',6),(4,'CREATE_BOOKING','{\"booking_code\": \"BOOK126\"}','BOOKING',4,'2025-07-24 15:00:00.000000',8),(5,'CREATE_BOOKING','{\"booking_code\": \"BOOK127\"}','BOOKING',5,'2025-07-24 16:00:00.000000',5);
/*!40000 ALTER TABLE `audit_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_promotions`
--

DROP TABLE IF EXISTS `booking_promotions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_promotions` (
  `booking_id` bigint NOT NULL,
  `promotion_id` bigint NOT NULL,
  PRIMARY KEY (`booking_id`,`promotion_id`),
  KEY `FK6ad7p0xekh937qe72cfb7dg5w` (`promotion_id`),
  CONSTRAINT `FK6ad7p0xekh937qe72cfb7dg5w` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`promotion_id`),
  CONSTRAINT `FKkbk18851fr0cvuyjhxamxrwtu` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_promotions`
--

LOCK TABLES `booking_promotions` WRITE;
/*!40000 ALTER TABLE `booking_promotions` DISABLE KEYS */;
INSERT INTO `booking_promotions` VALUES (1,1);
/*!40000 ALTER TABLE `booking_promotions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_code` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `guest_address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `guest_email` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `guest_full_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `guest_phone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `seat_number` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` enum('canceled_by_operator','canceled_by_user','completed','confirmed','pending') COLLATE utf8mb4_general_ci NOT NULL,
  `total_amount` decimal(38,2) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `agent_accept_booking_id` bigint DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `trip_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKq97166k18hklq6ls46osbrftx` (`booking_code`),
  KEY `idx_booking_customerID_createdAt` (`customer_id`,`created_at`),
  KEY `FKegnh9ksa308rvreolxjjgxfbn` (`agent_accept_booking_id`),
  KEY `FK76g5jpvf8bcqejvp5d2vgrnjb` (`trip_id`),
  CONSTRAINT `FK76g5jpvf8bcqejvp5d2vgrnjb` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`trip_id`),
  CONSTRAINT `FKegnh9ksa308rvreolxjjgxfbn` FOREIGN KEY (`agent_accept_booking_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKib6gjgj2e9binkktxmm175bmm` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (1,'BOOK123','2025-07-24 12:00:00.000000','456 Nguyễn Trãi, TP.HCM','customer1@busify.com','Trần Thị Khách','0987654321','A1','confirmed',500000.00,'2025-07-24 12:00:00.000000',NULL,1,1),(2,'BOOK124','2025-07-24 13:00:00.000000','34 Hai Bà Trưng, Huế','customer2@busify.com','Lê Văn Khách','0976543210','C1','confirmed',150000.00,'2025-07-24 13:00:00.000000',NULL,6,2),(3,'BOOK125','2025-07-24 14:00:00.000000','56 Nguyễn Huệ, Hà Nội','customer3@busify.com','Phạm Thị Hành Khách','0965432109','E1','pending',250000.00,'2025-07-24 14:00:00.000000',NULL,7,3),(4,'BOOK126','2025-07-24 15:00:00.000000','90 Lý Thường Kiệt, Cần Thơ','customer4@busify.com','Hoàng Văn Khách','0954321098','C1','confirmed',200000.00,'2025-07-24 15:00:00.000000',NULL,8,4),(5,'BOOK127','2025-07-24 16:00:00.000000','34 Hai Bà Trưng, Huế','customer2@busify.com','Lê Văn Khách','0976543210','E1','confirmed',220000.00,'2025-07-24 16:00:00.000000',NULL,6,5),(6,'BOOKING-1c96d426-e2ce-445a-b21e-6ee83fb3fdce','2025-08-06 08:05:36.502850','34 Hai Bà Trưng, Huế','customer2@busify.com','Lê Văn Khách','0976543210','A10, B13, A06','confirmed',120000.00,'2025-08-06 08:05:36.502850',NULL,18,1),(12,'BOOKING-16dd4dca-4aea-45cb-8cf2-30a532792614','2025-08-08 03:47:48.767281',NULL,NULL,NULL,NULL,'A01','confirmed',120000.00,'2025-08-17 15:57:18.438429',NULL,18,1),(13,'BOOKING-04382de0-3e47-4283-9dda-f5fd9e3d49bc','2025-08-08 08:03:03.723501',NULL,NULL,NULL,NULL,'B10','confirmed',120000.00,'2025-08-18 05:43:27.230371',NULL,18,1),(14,'BOOKING-442dc346-2608-490e-8a91-4aefa187bf7a','2025-08-18 04:38:45.121033',NULL,NULL,NULL,NULL,'A.1.1','confirmed',330000.00,'2025-08-18 04:39:34.320875',NULL,18,10),(15,'BOOKING-3a70f601-bcd7-4500-8630-0a2a0d7e4f27','2025-08-18 05:21:14.161277',NULL,NULL,NULL,NULL,'A.1.1','confirmed',330000.00,'2025-08-18 05:22:08.586131',NULL,18,10),(16,'BOOKING-cc8e9321-20bc-4fa2-b912-099875efcc6c','2025-08-18 05:25:03.865563',NULL,NULL,NULL,NULL,'A.1.1','confirmed',330000.00,'2025-08-18 05:26:06.621016',NULL,18,10),(17,'BOOKING-c8b6cd9e-6102-406b-851b-692ef37884e8','2025-08-18 05:31:14.584756',NULL,NULL,NULL,NULL,'A.1.1,B.1.1','confirmed',660000.00,'2025-08-18 05:31:50.847952',NULL,18,10),(18,'BOOKING-10c6f28f-f940-4f8e-81a5-efadf2bd6cf4','2025-08-18 05:37:04.580158',NULL,NULL,NULL,NULL,'A.1.1,B.1.1','confirmed',660000.00,'2025-08-18 05:37:30.028585',NULL,18,10),(19,'BOOKING-c722752f-fe81-4d74-952d-800044b1d71b','2025-08-18 05:47:26.915259',NULL,NULL,NULL,NULL,'A.1.1,B.1.1','confirmed',660000.00,'2025-08-18 05:47:57.818202',NULL,18,10),(20,'BOOKING-57164a37-70a0-4313-8c13-18544a948e30','2025-08-18 05:55:48.486000',NULL,NULL,NULL,NULL,'A.1.1,B.1.1','confirmed',660000.00,'2025-08-18 05:56:17.624102',NULL,18,10),(21,'BOOKING-5ef24ffb-fa30-4a1b-b923-28e2b41d98e8','2025-08-18 06:02:59.355842',NULL,NULL,NULL,NULL,'A.1.1,B.1.1','confirmed',660000.00,'2025-08-18 06:03:27.902155',NULL,18,10),(22,'BOOKING-e96e0c7f-3fba-4b86-8a11-b8c8c1a3f65a','2025-08-18 07:00:02.953999',NULL,NULL,NULL,NULL,'A.1.1,B.1.1','confirmed',660000.00,'2025-08-18 07:01:10.256390',NULL,18,10),(23,'BA458E','2025-08-20 15:03:03.747601',NULL,NULL,NULL,NULL,'B10','pending',120000.00,'2025-08-20 15:03:03.747601',NULL,18,1);
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bus_models`
--

DROP TABLE IF EXISTS `bus_models`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bus_models` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8y17oiagnmlkr9c3xnv61c7yj` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus_models`
--

LOCK TABLES `bus_models` WRITE;
/*!40000 ALTER TABLE `bus_models` DISABLE KEYS */;
INSERT INTO `bus_models` VALUES (3,'Hyundai Aero'),(1,'Hyundai Universe'),(4,'Isuzu Samco'),(5,'Mercedes Sprinter'),(2,'Thaco Mobihome'),(6,'Volvo B11R');
/*!40000 ALTER TABLE `bus_models` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bus_operators`
--

DROP TABLE IF EXISTS `bus_operators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bus_operators` (
  `operator_id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `hotline` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `license_path` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` enum('active','inactive','pending_approval','suspended') COLLATE utf8mb4_general_ci NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `owner_id` bigint DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`operator_id`),
  KEY `FKhdysn8wxp8hnchty3yuo3nkii` (`owner_id`),
  CONSTRAINT `FK1s2plmqc02rou11x8g1k4eawh` FOREIGN KEY (`owner_id`) REFERENCES `profiles` (`id`) ON DELETE SET NULL,
  CONSTRAINT `FKhdysn8wxp8hnchty3yuo3nkii` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus_operators`
--

LOCK TABLES `bus_operators` WRITE;
/*!40000 ALTER TABLE `bus_operators` DISABLE KEYS */;
INSERT INTO `bus_operators` VALUES (1,'789 Lê Lợi, Đà Nẵng','2025-07-24 10:00:00.000000','Nhà xe uy tín','operator@busify.com','0901234567','/licenses/operator1.pdf','Nhà Xe ABC','active','2025-07-24 10:00:00.000000',3,0),(2,'12 Phạm Ngọc Thạch, Đà Lạt','2025-07-24 11:00:00.000000','Nhà xe chất lượng cao','operator2@busify.com','0931234567','/licenses/operator2.pdf','Nhà Xe XYZ','active','2025-07-24 11:00:00.000000',5,0),(3,'45 Nguyễn Văn Cừ, Huế','2025-07-24 11:00:00.000000','Dịch vụ thân thiện','operator3@busify.com','0932234567','/licenses/operator3.pdf','Nhà Xe Huế','pending_approval','2025-07-24 11:00:00.000000',5,0),(4,'67 Trần Hưng Đạo, Cần Thơ','2025-07-24 11:00:00.000000','Nhà xe uy tín miền Tây','operator4@busify.com','0933234567','/licenses/operator4.pdf','Nhà Xe Miền Tây','active','2025-07-24 11:00:00.000000',5,0),(5,'89 Võ Thị Sáu, Vũng Tàu','2025-07-24 11:00:00.000000','Chuyến xe an toàn','operator5@busify.com','0934234567','/licenses/operator5.pdf','Nhà Xe Biển Xanh','active','2025-07-24 11:00:00.000000',5,0),(6,'101 Lê Lai, Nha Trang','2025-07-24 11:00:00.000000','Dịch vụ cao cấp','operator6@busify.com','0935234567','/licenses/operator6.pdf','Nhà Xe Nha Trang','active','2025-07-24 11:00:00.000000',5,0);
/*!40000 ALTER TABLE `bus_operators` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `buses`
--

DROP TABLE IF EXISTS `buses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `buses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amenities` json DEFAULT NULL,
  `license_plate` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `status` enum('active','out_of_service','under_maintenance') COLLATE utf8mb4_general_ci NOT NULL,
  `total_seats` int NOT NULL,
  `operator_id` bigint DEFAULT NULL,
  `seat_layout_id` int DEFAULT NULL,
  `model_id` bigint DEFAULT NULL,
  `model` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_buses_licensePlate` (`license_plate`),
  KEY `FKwmt4p1sd2pys4x800ha06ste` (`operator_id`),
  KEY `FK71mdanlbyn55bepxk8j27n3g6` (`seat_layout_id`),
  KEY `FKnyhvro1aecuy0rfu8da4a4tls` (`model_id`),
  CONSTRAINT `FK71mdanlbyn55bepxk8j27n3g6` FOREIGN KEY (`seat_layout_id`) REFERENCES `seat_layouts` (`id`) ON DELETE SET NULL,
  CONSTRAINT `FKnyhvro1aecuy0rfu8da4a4tls` FOREIGN KEY (`model_id`) REFERENCES `bus_models` (`id`) ON DELETE SET NULL,
  CONSTRAINT `FKwmt4p1sd2pys4x800ha06ste` FOREIGN KEY (`operator_id`) REFERENCES `bus_operators` (`operator_id`) ON DELETE SET NULL,
  CONSTRAINT `buses_chk_1` CHECK (json_valid(`amenities`))
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buses`
--

LOCK TABLES `buses` WRITE;
/*!40000 ALTER TABLE `buses` DISABLE KEYS */;
INSERT INTO `buses` VALUES (2,'{\"tv\": false, \"wifi\": false, \"toilet\": true, \"charging\": false, \"air_conditioner\": false}','51B-678.90','active',40,1,1,1,NULL),(3,'{\"tv\": false, \"wifi\": true, \"toilet\": false, \"charging\": false, \"air_conditioner\": true}','51B-54321','active',48,3,3,3,NULL),(4,'{\"tv\": true, \"wifi\": false, \"toilet\": true, \"charging\": false, \"air_conditioner\": false}','51B-98765','under_maintenance',40,4,1,4,NULL),(5,'{\"tv\": false, \"wifi\": true, \"toilet\": true, \"charging\": true, \"air_conditioner\": true}','51B-11223','active',32,5,2,5,NULL),(8,'{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": false, \"air_conditioner\": false}','51B-123.45','active',40,1,1,1,NULL),(9,'{\"tv\": false, \"wifi\": true, \"toilet\": false, \"charging\": false, \"air_conditioner\": false}','36B-123.31','active',40,1,1,2,NULL),(14,'{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": false, \"air_conditioner\": false}','36B-123.31','out_of_service',40,1,1,1,NULL),(16,'{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": false, \"air_conditioner\": false}','36B-123.31','under_maintenance',40,1,1,1,NULL),(17,'{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": false, \"air_conditioner\": false}','23S-223.21','active',40,1,1,1,NULL),(19,'{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": true, \"air_conditioner\": false}','23S-223.21','active',40,2,1,1,NULL),(22,'{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": true, \"air_conditioner\": false}','51B-678.90','active',80,1,2,1,NULL),(24,'{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": true, \"air_conditioner\": false}','51B-678.90','active',80,1,2,1,NULL),(25,'{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": false, \"air_conditioner\": true}','51B-678.90','active',80,2,2,1,NULL);
/*!40000 ALTER TABLE `buses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `complaints`
--

DROP TABLE IF EXISTS `complaints`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `complaints` (
  `complaints_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` enum('New','in_progress','pending','rejected','resolved') COLLATE utf8mb4_general_ci NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `assigned_agent_id` bigint DEFAULT NULL,
  `booking_id` bigint DEFAULT NULL,
  `customer_id` bigint NOT NULL,
  PRIMARY KEY (`complaints_id`),
  KEY `FKdjr8v1k0sie3eioouxacnfjsx` (`assigned_agent_id`),
  KEY `FK9iay3myxtp9bh1vf9iuj2rich` (`booking_id`),
  KEY `FKhtvrekom2uyukk0u8e95np1se` (`customer_id`),
  CONSTRAINT `FK9iay3myxtp9bh1vf9iuj2rich` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`),
  CONSTRAINT `FKdjr8v1k0sie3eioouxacnfjsx` FOREIGN KEY (`assigned_agent_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKhtvrekom2uyukk0u8e95np1se` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `complaints`
--

LOCK TABLES `complaints` WRITE;
/*!40000 ALTER TABLE `complaints` DISABLE KEYS */;
INSERT INTO `complaints` VALUES (1,'2025-07-25 22:00:00.000000','Ghế không thoải mái','New','Khiếu nại về ghế','2025-07-25 22:00:00.000000',NULL,1,2),(2,'2025-07-25 14:00:00.000000','Xe đến muộn 30 phút','pending','Khiếu nại về thời gian','2025-07-25 14:00:00.000000',NULL,2,6),(3,'2025-07-25 15:00:00.000000','Wifi không hoạt động','New','Khiếu nại về wifi','2025-07-25 15:00:00.000000',NULL,3,7);
/*!40000 ALTER TABLE `complaints` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contracts`
--

DROP TABLE IF EXISTS `contracts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contracts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `vatcode` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `admin_note` varchar(255) DEFAULT NULL,
  `approved_date` datetime(6) DEFAULT NULL,
  `created_date` datetime(6) NOT NULL,
  `email` varchar(255) NOT NULL,
  `end_date` datetime(6) NOT NULL,
  `last_modified` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `license_url` varchar(255) NOT NULL,
  `operation_area` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `start_date` datetime(6) NOT NULL,
  `status` enum('ACCEPTED','ACTIVE','EXPIRED','NEED_REVISION','PENDING','REJECTED') NOT NULL,
  `updated_date` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKcxaca0d8qugontspm79o8mfk5` (`vatcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contracts`
--

LOCK TABLES `contracts` WRITE;
/*!40000 ALTER TABLE `contracts` DISABLE KEYS */;
/*!40000 ALTER TABLE `contracts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `driver_license_number` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id` bigint NOT NULL,
  `operator_id` bigint DEFAULT NULL,
  `employee_type` enum('DRIVER','TICKET_OFFICER') COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8yuomapwk6ker3oiqr5uo8axa` (`operator_id`),
  CONSTRAINT `FK3c8y10sx8hu44w2qvr8gbqxj9` FOREIGN KEY (`id`) REFERENCES `profiles` (`id`),
  CONSTRAINT `FK8yuomapwk6ker3oiqr5uo8axa` FOREIGN KEY (`operator_id`) REFERENCES `bus_operators` (`operator_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES ('DL678901',8,2,'DRIVER'),('DL27292372',17,1,'DRIVER'),('DL0823282',18,2,'DRIVER'),('DL2398232',19,1,'DRIVER'),(NULL,20,NULL,'DRIVER'),('DL092322',21,1,'DRIVER'),('DL2938232',22,1,'DRIVER');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locations`
--

DROP TABLE IF EXISTS `locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `locations` (
  `location_id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `city` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locations`
--

LOCK TABLES `locations` WRITE;
/*!40000 ALTER TABLE `locations` DISABLE KEYS */;
INSERT INTO `locations` VALUES (1,'Bến xe Miền Đông','TP.HCM',10.782,106.693,'Bến xe Miền Đông'),(2,'Bến xe Giáp Bát','Hà Nội',20.987,105.841,'Bến xe Giáp Bát'),(3,'Bến xe Đà Lạt','Đà Lạt',11.94,108.437,'Bến xe Đà Lạt'),(4,'Bến xe Huế','Huế',16.467,107.595,'Bến xe Huế'),(5,'Bến xe Cần Thơ','Cần Thơ',10.045,105.746,'Bến xe Cần Thơ'),(6,'Bến xe Vũng Tàu','Vũng Tàu',10.346,107.084,'Bến xe Vũng Tàu'),(7,'Bến xe Nha Trang','Nha Trang',12.25,109.194,'Bến xe Nha Trang');
/*!40000 ALTER TABLE `locations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `payment_id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) NOT NULL,
  `paid_at` datetime(6) DEFAULT NULL,
  `status` enum('completed','failed','pending','refunded') COLLATE utf8mb4_general_ci NOT NULL,
  `transaction_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `booking_id` bigint NOT NULL,
  `payment_gateway_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `payment_method` enum('BANK_TRANSFER','CREDIT_CARD','PAYPAL','PAY_LATER','VNPAY') COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`payment_id`),
  UNIQUE KEY `UK8inpv30544qjykcwa6ck7pusy` (`transaction_code`),
  KEY `FKc52o2b1jkxttngufqp3t7jr3h` (`booking_id`),
  CONSTRAINT `FKc52o2b1jkxttngufqp3t7jr3h` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,500000.00,'2025-07-24 12:05:00.000000','completed','TX123456',6,NULL,'BANK_TRANSFER'),(2,150000.00,'2025-07-24 13:05:00.000000','completed','TX123457',2,NULL,'BANK_TRANSFER'),(3,250000.00,NULL,'pending','TX123458',3,NULL,'BANK_TRANSFER'),(4,200000.00,'2025-07-24 15:05:00.000000','completed','TX123459',4,NULL,'BANK_TRANSFER'),(5,220000.00,'2025-07-24 16:05:00.000000','completed','TX123460',5,NULL,'BANK_TRANSFER'),(25,120000.00,'2025-08-17 15:57:18.391431','completed','TXN431FD8BB4024',12,NULL,'VNPAY'),(26,330000.00,'2025-08-18 04:39:34.278877','completed','TXN9590B3E8DF64',14,NULL,'VNPAY'),(27,330000.00,'2025-08-18 05:22:08.571127','completed','TXN5376F19330E6',15,NULL,'VNPAY'),(28,330000.00,'2025-08-18 05:26:06.605016','completed','TXN6476C35277D3',16,NULL,'VNPAY'),(29,660000.00,'2025-08-18 05:31:50.831625','completed','TXN5CBE26FEE2DA',17,NULL,'VNPAY'),(30,660000.00,'2025-08-18 05:37:29.980577','completed','TXNF2B556C256C5',18,NULL,'VNPAY'),(31,120000.00,'2025-08-18 05:43:27.214380','completed','TXN2EC63DEE0D0B',13,NULL,'VNPAY'),(32,660000.00,'2025-08-18 05:47:57.777204','completed','TXN7A2B20D9BD19',19,NULL,'VNPAY'),(33,660000.00,'2025-08-18 05:56:17.583103','completed','TXNE3DFFA0A1FFE',20,NULL,'VNPAY'),(34,660000.00,'2025-08-18 06:03:27.862159','completed','TXN4C99A1C9933C',21,NULL,'VNPAY'),(35,660000.00,'2025-08-18 07:01:10.240391','completed','TXNED357273D4AA',22,NULL,'VNPAY');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profiles`
--

DROP TABLE IF EXISTS `profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profiles` (
  `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `full_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `phone_number` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` enum('active','inactive','suspended') COLLATE utf8mb4_general_ci NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK55e5d3sfwkob62wtprm633alk` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profiles`
--

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
INSERT INTO `profiles` VALUES ('456 Nguyễn Trãi, TP.HCM','2025-07-24 10:00:00.000000','Trần Thị Khách','0987654321','active','2025-07-24 10:00:00.000000',1),('789 Lê Lợi, Đà Nẵng','2025-07-24 10:00:00.000000','Lê Văn Nhà Xe','0901234567','active','2025-07-24 10:00:00.000000',3),('12 Phạm Ngọc Thạch, Đà Lạt','2025-07-24 11:00:00.000000','Nguyễn Thị Nhà Xe','0931234567','active','2025-07-24 11:00:00.000000',5),('34 Hai Bà Trưng, Huế','2025-07-24 11:00:00.000000','Lê Văn Khách','0976543210','active','2025-07-24 11:00:00.000000',6),('56 Nguyễn Huệ, Hà Nội','2025-07-24 11:00:00.000000','Phạm Thị Hành Khách','0965432109','active','2025-07-24 11:00:00.000000',7),('78 Lê Duẩn, Đà Nẵng','2025-07-24 11:00:00.000000','Trần Văn Tài Xế','0943210987','suspended','2025-08-21 02:39:03.160107',8),(NULL,'2025-08-09 17:49:19.343369','Antoin','0901020304','active','2025-08-09 17:51:26.861114',11),('ĐA','2025-08-16 20:11:37.040784','Nguyễn Văn A','0967070842','suspended','2025-08-20 03:10:45.294043',17),('Đà Nẵng','2025-08-17 14:00:30.527919','Antoin','0123456789','active','2025-08-21 02:33:14.422257',18),('Đà Nẵng','2025-08-20 02:53:11.094487','Trần Văn Hùng','0129328232','active','2025-08-20 03:06:46.815384',19),(NULL,'2025-08-20 03:30:36.317877','Đinh Tiến Đạt',NULL,'active','2025-08-20 03:30:36.317877',20),('Đà Nẵng','2025-08-20 03:32:28.946466','Trần Hùng Anh','0906030013','inactive','2025-08-20 03:34:47.504264',21),('Đà Nẵng','2025-08-20 14:26:27.393212','Hoàng Văn Hoài','0129328232','active','2025-08-20 14:28:00.778924',22);
/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotions`
--

DROP TABLE IF EXISTS `promotions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotions` (
  `promotion_id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `discount_type` enum('FIXED_AMOUNT','PERCENTAGE') COLLATE utf8mb4_general_ci NOT NULL,
  `discount_value` decimal(38,2) NOT NULL,
  `end_date` date NOT NULL,
  `start_date` date NOT NULL,
  `status` enum('active','expired','inactive') COLLATE utf8mb4_general_ci NOT NULL,
  `usage_limit` int DEFAULT NULL,
  PRIMARY KEY (`promotion_id`),
  UNIQUE KEY `UKjdho73ymbyu46p2hh562dk4kk` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotions`
--

LOCK TABLES `promotions` WRITE;
/*!40000 ALTER TABLE `promotions` DISABLE KEYS */;
INSERT INTO `promotions` VALUES (1,'DISCOUNT10','PERCENTAGE',10.00,'2025-08-01','2025-07-24','active',100);
/*!40000 ALTER TABLE `promotions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `review_id` bigint NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `rating` int NOT NULL,
  `customer_id` bigint NOT NULL,
  `trip_id` bigint NOT NULL,
  PRIMARY KEY (`review_id`),
  KEY `FKkquncb1glvrldaui8v52xfd5q` (`customer_id`),
  KEY `FKe70h9t86py3fbswj0gw16v0by` (`trip_id`),
  CONSTRAINT `FKe70h9t86py3fbswj0gw16v0by` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`trip_id`),
  CONSTRAINT `FKkquncb1glvrldaui8v52xfd5q` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,'Chuyến đi tuyệt vời!','2025-07-25 21:00:00.000000',5,2,1),(2,'Chuyến đi thoải mái, tài xế thân thiện','2025-07-25 13:00:00.000000',4,6,2),(3,'Xe sạch sẽ nhưng đến muộn 15 phút','2025-07-25 17:00:00.000000',3,7,3),(5,'Điều hòa lúc có lúc không','2025-07-25 18:00:00.000000',3,6,5),(8,'Thái độ tài xế lồi lõm','2025-08-04 14:02:28.672474',2,6,2),(9,'Thái độ tài xế lồi lõm','2025-08-04 14:09:49.583860',2,6,2);
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN'),(2,'CUSTOMER'),(9,'OPERATOR'),(10,'STAFF');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `route_stops`
--

DROP TABLE IF EXISTS `route_stops`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `route_stops` (
  `stop_order` int DEFAULT NULL,
  `time_offset_from_start` int DEFAULT NULL,
  `location_id` bigint NOT NULL,
  `route_id` bigint NOT NULL,
  PRIMARY KEY (`location_id`,`route_id`),
  KEY `FK63y33daxb1qs5nbnkuicbpkej` (`route_id`),
  CONSTRAINT `FK63y33daxb1qs5nbnkuicbpkej` FOREIGN KEY (`route_id`) REFERENCES `routes` (`route_id`) ON DELETE CASCADE,
  CONSTRAINT `FKp1fka8xfu6pf91algbjueupey` FOREIGN KEY (`location_id`) REFERENCES `locations` (`location_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `route_stops`
--

LOCK TABLES `route_stops` WRITE;
/*!40000 ALTER TABLE `route_stops` DISABLE KEYS */;
INSERT INTO `route_stops` VALUES (1,0,1,1),(1,0,1,2),(2,360,1,3),(2,300,1,5),(2,720,2,1),(1,0,2,6),(2,180,3,2),(1,0,4,3),(2,480,4,6),(1,0,5,4),(2,240,6,4),(1,0,7,5);
/*!40000 ALTER TABLE `route_stops` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `routes`
--

DROP TABLE IF EXISTS `routes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `routes` (
  `route_id` bigint NOT NULL AUTO_INCREMENT,
  `default_duration_minutes` int DEFAULT NULL,
  `default_price` decimal(10,2) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `end_location_id` bigint DEFAULT NULL,
  `start_location_id` bigint DEFAULT NULL,
  PRIMARY KEY (`route_id`),
  KEY `FKtqxldvq6vfex3mna2wxpuw9an` (`end_location_id`),
  KEY `FKja5uqif3mnj3ff3idvepoifar` (`start_location_id`),
  CONSTRAINT `FKja5uqif3mnj3ff3idvepoifar` FOREIGN KEY (`start_location_id`) REFERENCES `locations` (`location_id`) ON DELETE SET NULL,
  CONSTRAINT `FKtqxldvq6vfex3mna2wxpuw9an` FOREIGN KEY (`end_location_id`) REFERENCES `locations` (`location_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `routes`
--

LOCK TABLES `routes` WRITE;
/*!40000 ALTER TABLE `routes` DISABLE KEYS */;
INSERT INTO `routes` VALUES (1,720,400000.00,'TP.HCM - Hà Nội',2,1),(2,180,150000.00,'TP.HCM - Đà Lạt',3,1),(3,360,250000.00,'Huế - Đà Nẵng',1,4),(4,240,200000.00,'Cần Thơ - Vũng Tàu',6,5),(5,300,220000.00,'Nha Trang - TP.HCM',1,7),(6,480,300000.00,'Hà Nội - Huế',4,2),(12,720,123232.00,'TPHCM - Hà Nội',2,1);
/*!40000 ALTER TABLE `routes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seat_layouts`
--

DROP TABLE IF EXISTS `seat_layouts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seat_layouts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `layout_data` json NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `seat_layouts_chk_1` CHECK (json_valid(`layout_data`))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat_layouts`
--

LOCK TABLES `seat_layouts` WRITE;
/*!40000 ALTER TABLE `seat_layouts` DISABLE KEYS */;
INSERT INTO `seat_layouts` VALUES (1,'{\"cols\": 4, \"rows\": 10, \"floors\": 1}','Standard 40 seats'),(2,'{\"cols\": 4, \"rows\": 10, \"floors\": 2}','Standard 32 seats'),(3,'{\"cols\": 4, \"rows\": 10, \"floors\": 2}','Luxury 48 seats');
/*!40000 ALTER TABLE `seat_layouts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spring_session`
--

DROP TABLE IF EXISTS `spring_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spring_session` (
  `PRIMARY_ID` char(36) COLLATE utf8mb4_general_ci NOT NULL,
  `SESSION_ID` char(36) COLLATE utf8mb4_general_ci NOT NULL,
  `CREATION_TIME` bigint NOT NULL,
  `LAST_ACCESS_TIME` bigint NOT NULL,
  `MAX_INACTIVE_INTERVAL` int NOT NULL,
  `EXPIRY_TIME` bigint NOT NULL,
  `PRINCIPAL_NAME` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spring_session`
--

LOCK TABLES `spring_session` WRITE;
/*!40000 ALTER TABLE `spring_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `spring_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spring_session_attributes`
--

DROP TABLE IF EXISTS `spring_session_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) COLLATE utf8mb4_general_ci NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spring_session_attributes`
--

LOCK TABLES `spring_session_attributes` WRITE;
/*!40000 ALTER TABLE `spring_session_attributes` DISABLE KEYS */;
INSERT INTO `spring_session_attributes` VALUES ('f829b138-41e6-48ba-8b7c-3ab8c541c09b','org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository.AUTHORIZATION_REQUEST',_binary '�\�\0sr\0Lorg.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\0\0\0\0\0\0l\0\nL\0additionalParameterst\0Ljava/util/Map;L\0\nattributesq\0~\0L\0authorizationGrantTypet\0ALorg/springframework/security/oauth2/core/AuthorizationGrantType;L\0authorizationRequestUrit\0Ljava/lang/String;L\0authorizationUriq\0~\0L\0clientIdq\0~\0L\0redirectUriq\0~\0L\0responseTypet\0SLorg/springframework/security/oauth2/core/endpoint/OAuth2AuthorizationResponseType;L\0scopest\0Ljava/util/Set;L\0stateq\0~\0xpsr\0%java.util.Collections$UnmodifiableMap��t�B\0L\0mq\0~\0xpsr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0noncet\0+w_LgaDRBcMIvSki0da2H_JcFxqvNKj-PP861o7jSK1ox\0sq\0~\0sq\0~\0	?@\0\0\0\0\0w\0\0\0\0\0\0t\0registration_idt\0googleq\0~\0t\0�uiNlRoru5eIfS4DRJirCgkTrq3IUdi85KhFPwfZHZKIEUBb1NvlQIwdpttgVIrJrzSXp8wP6rC5SS_p_IOZar_CXtsOWPotIZAR69rh2meRgLNCqGSFswAACCMbjLN3Dx\0sr\0?org.springframework.security.oauth2.core.AuthorizationGrantType\0\0\0\0\0\0l\0L\0valueq\0~\0xpt\0authorization_codetRhttps://accounts.google.com/o/oauth2/auth?response_type=code&client_id=1048195747015-mf73fonbfa82re2s680jdbhgmkd930dh.apps.googleusercontent.com&scope=openid%20profile%20email&state=BcVpZ2hbgu_yEhG7wd_fRbcV2qjUfNkSBYStgmejrrI%3D&redirect_uri=http://localhost:8080/login/oauth2/code/google&nonce=w_LgaDRBcMIvSki0da2H_JcFxqvNKj-PP861o7jSK1ot\0)https://accounts.google.com/o/oauth2/autht\0I1048195747015-mf73fonbfa82re2s680jdbhgmkd930dh.apps.googleusercontent.comt\0.http://localhost:8080/login/oauth2/code/googlesr\0Qorg.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType\0\0\0\0\0\0l\0L\0valueq\0~\0xpt\0codesr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^�\0L\0ct\0Ljava/util/Collection;xpsr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0openidt\0profilet\0emailxt\0,BcVpZ2hbgu_yEhG7wd_fRbcV2qjUfNkSBYStgmejrrI=');
/*!40000 ALTER TABLE `spring_session_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tickets` (
  `ticket_id` bigint NOT NULL AUTO_INCREMENT,
  `passenger_name` varchar(255) NOT NULL,
  `passenger_phone` varchar(255) DEFAULT NULL,
  `price` decimal(38,2) NOT NULL,
  `seat_number` varchar(255) NOT NULL,
  `status` enum('cancelled','used','valid') NOT NULL,
  `ticket_code` varchar(255) NOT NULL,
  `booking_id` bigint NOT NULL,
  PRIMARY KEY (`ticket_id`),
  UNIQUE KEY `UKcvl4jbu5fln08ltem9rrmtp8w` (`ticket_code`),
  KEY `FKefja4avuu7g29t78mxifrsynb` (`booking_id`),
  CONSTRAINT `FKefja4avuu7g29t78mxifrsynb` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
--

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
INSERT INTO `tickets` VALUES (1,'Trần Thị Khách','0987654321',500000.00,'B01','valid','TICKET123',1),(2,'Lê Văn Khách','0976543210',150000.00,'A05','valid','TICKET124',2),(3,'Phạm Thị Hành Khách','0965432109',250000.00,'B05','valid','TICKET125',3),(4,'Hoàng Văn Khách','0954321098',200000.00,'B10','valid','TICKET126',4),(5,'Lê Văn Khách','0976543210',220000.00,'A17','valid','TICKET127',5),(7,'Trần Thị Khách','0987654321',500000.00,'B12','valid','TICKET128',1),(39,'Antoin','0901020304',500000.00,'A01','valid','02A328',12),(40,'Antoin','0901020304',500000.00,'A01','valid','5AADC3',12),(41,'Antoin','0123456789',500000.00,'A01','valid','4FA6EC',12),(42,'Antoin','0123456789',500000.00,'A01','valid','5665D5',12),(43,'Antoin','0123456789',330000.00,'A.1.1','valid','F332F0',14),(44,'Antoin','0123456789',330000.00,'A.1.1','valid','A1CEE3',15),(45,'Antoin','0123456789',330000.00,'A.1.1','valid','8B225C',16),(46,'Antoin','0123456789',330000.00,'A.1.1','valid','478079',17),(47,'Antoin','0123456789',330000.00,'B.1.1','valid','5B1860',17),(48,'Antoin','0123456789',330000.00,'A.1.1','valid','A03E8D',18),(49,'Antoin','0123456789',330000.00,'B.1.1','valid','00E0B9',18),(50,'Antoin','0123456789',500000.00,'B10','valid','C97C97',13),(51,'Antoin','0123456789',330000.00,'A.1.1','valid','ECFDF3',19),(52,'Antoin','0123456789',330000.00,'B.1.1','valid','109D87',19),(53,'Antoin','0123456789',330000.00,'A.1.1','valid','A7389D',20),(54,'Antoin','0123456789',330000.00,'B.1.1','valid','474E2D',20),(55,'Antoin','0123456789',330000.00,'A.1.1','valid','0C1805',21),(56,'Antoin','0123456789',330000.00,'B.1.1','valid','ED93C0',21),(57,'Antoin','0123456789',330000.00,'A.1.1','valid','BB6A80',22),(58,'Antoin','0123456789',330000.00,'B.1.1','valid','1A429B',22);
/*!40000 ALTER TABLE `tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trip_seats`
--

DROP TABLE IF EXISTS `trip_seats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trip_seats` (
  `seat_number` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `trip_id` bigint NOT NULL,
  `locked_at` datetime(6) DEFAULT NULL,
  `status` enum('available','booked','locked') COLLATE utf8mb4_general_ci NOT NULL,
  `locking_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`seat_number`,`trip_id`),
  KEY `FKiv2sj369u16d4iyoi6wkvnn11` (`locking_user_id`),
  CONSTRAINT `FKiv2sj369u16d4iyoi6wkvnn11` FOREIGN KEY (`locking_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trip_seats`
--

LOCK TABLES `trip_seats` WRITE;
/*!40000 ALTER TABLE `trip_seats` DISABLE KEYS */;
INSERT INTO `trip_seats` VALUES ('A.1.1',10,NULL,'booked',NULL),('B.1.1',10,NULL,'booked',NULL),('B10',1,NULL,'booked',NULL);
/*!40000 ALTER TABLE `trip_seats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trips`
--

DROP TABLE IF EXISTS `trips`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trips` (
  `trip_id` bigint NOT NULL AUTO_INCREMENT,
  `departure_time` datetime(6) NOT NULL,
  `estimated_arrival_time` datetime(6) DEFAULT NULL,
  `price_per_seat` decimal(10,2) NOT NULL,
  `status` enum('arrived','cancelled','delayed','departed','on_time','scheduled') COLLATE utf8mb4_general_ci NOT NULL,
  `bus_id` bigint DEFAULT NULL,
  `driver_id` bigint DEFAULT NULL,
  `route_id` bigint DEFAULT NULL,
  PRIMARY KEY (`trip_id`),
  KEY `idx_trips_departureTime_routeId` (`departure_time`,`route_id`),
  KEY `FK2vg7b2xayoq4ogt2kbsot4juq` (`bus_id`),
  KEY `FKevvmqtfyigychrwa7y8c14sht` (`driver_id`),
  KEY `FKm7ci3blm9wj2k0d94chu18y7s` (`route_id`),
  CONSTRAINT `FK2vg7b2xayoq4ogt2kbsot4juq` FOREIGN KEY (`bus_id`) REFERENCES `buses` (`id`) ON DELETE SET NULL,
  CONSTRAINT `FKevvmqtfyigychrwa7y8c14sht` FOREIGN KEY (`driver_id`) REFERENCES `employees` (`id`) ON DELETE SET NULL,
  CONSTRAINT `FKm7ci3blm9wj2k0d94chu18y7s` FOREIGN KEY (`route_id`) REFERENCES `routes` (`route_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trips`
--

LOCK TABLES `trips` WRITE;
/*!40000 ALTER TABLE `trips` DISABLE KEYS */;
INSERT INTO `trips` VALUES (1,'2025-08-20 08:00:00.000000','2025-08-20 13:00:00.000000',500000.00,'cancelled',2,8,5),(2,'2025-08-21 09:00:00.000000','2025-08-21 12:00:00.000000',150000.00,'delayed',2,8,2),(3,'2025-08-09 10:00:00.000000','2025-08-09 16:00:00.000000',250000.00,'scheduled',3,8,3),(4,'2025-08-09 11:00:00.000000','2025-08-09 15:00:00.000000',200000.00,'scheduled',5,8,4),(5,'2025-08-09 12:00:00.000000','2025-08-09 17:00:00.000000',220000.00,'scheduled',NULL,8,5),(6,'2025-08-20 08:00:00.000000','2025-08-20 16:00:00.000000',300000.00,'scheduled',2,8,6),(10,'2025-08-19 12:00:00.000000','2025-08-20 00:00:00.000000',330000.00,'scheduled',2,8,1),(12,'2025-08-19 12:00:00.000000','2025-08-20 00:00:00.000000',200000.00,'scheduled',2,8,1),(16,'2025-08-19 09:20:00.000000','2025-08-19 21:20:00.000000',123455.00,'scheduled',2,8,1),(17,'2025-08-20 22:00:00.000000','2025-08-21 10:00:00.000000',1223121.00,'scheduled',2,8,1);
/*!40000 ALTER TABLE `trips` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `refresh_token` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  `auth_provider` enum('FACEBOOK','GOOGLE','LOCAL') COLLATE utf8mb4_general_ci NOT NULL,
  `email_verified` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_users_email` (`email`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'customer1@busify.com','$2a$10$exampleHash',NULL,NULL,'LOCAL',_binary '\0'),(2,'operator@busify.com','$2a$10$exampleHash',NULL,NULL,'LOCAL',_binary '\0'),(3,'driver@busify.com','$2a$10$exampleHash',NULL,NULL,'LOCAL',_binary '\0'),(5,'customer2@busify.com','$2a$10$exampleHash',NULL,NULL,'LOCAL',_binary '\0'),(6,'customer3@busify.com','$2a$10$exampleHash',NULL,NULL,'LOCAL',_binary '\0'),(7,'driver2@busify.com','$2a$10$exampleHash',NULL,NULL,'LOCAL',_binary '\0'),(8,'customer4@busify.com','$2a$10$exampleHash',NULL,NULL,'LOCAL',_binary '\0'),(10,'admin@gmail.com','$2a$10$NkxbpFM873h8cygyP.EVxeynAbyCKSNHbyvuGb5SwbscSVvos1H7m','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3NTU2NTQ1ODMsImV4cCI6MTc1NjI1OTM4M30.t_J6XddP7B6fAOQOvhH1zpMh-B11wD9eb4oiV1tQY9U',1,'LOCAL',_binary ''),(11,'antoin2901@gmail.com','$2a$10$/NL9PmrixMqsU9M7E7DGKO62mpPCgswnaJAJfnMzXfr9dN2LVmASe','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvaW4yOTAxQGdtYWlsLmNvbSIsImlhdCI6MTc1NDc2MTg4NiwiZXhwIjoxNzU1MzY2Njg2fQ.kLl64OIb6cnAfgcOZkQq-NvatTz6MXTCK5QQNd-NCdc',9,'LOCAL',_binary ''),(17,'nguyenvana@gmail.com','$2a$10$26XLKib7eI1EbLOn6WXQruCaOmSyKTXkRNQYfYcacxYsPZQdrmpDO',NULL,10,'LOCAL',_binary '\0'),(18,'antoinnguyen95@gmail.com','$2a$10$PfTudr3FNRwpjglJUuSYCOOgNanrCyV81J6yZx1hq/bPaDUsb.hoC','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvaW5uZ3V5ZW45NUBnbWFpbC5jb20iLCJpYXQiOjE3NTU3NDEyNTAsImV4cCI6MTc1NjM0NjA1MH0.G1lMbbQPJpSNTNj1TMB7PVJrlJDFjWSZj5ZNGerxR2Q',9,'LOCAL',_binary ''),(19,'hungtv@gmail.com','$2a$10$FYNWrq1wh2h0md..gl.EoeaREiNOPYmLgj3JSidd4zsmUeqG/VWle','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJodW5ndHZAZ21haWwuY29tIiwiaWF0IjoxNzU1NjU4NDgzLCJleHAiOjE3NTYyNjMyODN9.qYO_NELK9r5zzDl886tDi_GKyzAiuWSRdRz5-yZFSk8',10,'LOCAL',_binary ''),(20,'datdt@gmail.com','$2a$10$yJN7z2bAJCv5Fcz9qvHGqOjDXUnyT4/OprpUkQYguTHMN/utcLVEu',NULL,10,'LOCAL',_binary ''),(21,'anhth@gmail.com','$2a$10$0vwubdCMNzv/S.3N/no8gOWk3QdP2KvfZ9Li/zij11EeWzEGV84GG',NULL,10,'LOCAL',_binary ''),(22,'hoaihv@gmail.com','$2a$10$dAUxu.eWuYTR/gZkx.YKMegmWZFest6G0SO18Nw3taeo165oVa54W','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob2FpaHZAZ21haWwuY29tIiwiaWF0IjoxNzU1NzAwMDEyLCJleHAiOjE3NTYzMDQ4MTJ9.28-zM_MT-tRzRsE44u8s9bZFhZ_heEjZ6LoyUXbpReU',10,'LOCAL',_binary '');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verification_tokens`
--

DROP TABLE IF EXISTS `verification_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verification_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) NOT NULL,
  `expiry_date` datetime(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  `token_type` enum('ACCOUNT_ACTIVATION','EMAIL_VERIFICATION','PASSWORD_RESET') DEFAULT NULL,
  `used` bit(1) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6q9nsb665s9f8qajm3j07kd1e` (`token`),
  UNIQUE KEY `UKdqp95ggn6gvm865km5muba2o5` (`user_id`),
  CONSTRAINT `FK54y8mqsnq1rtyf581sfmrbp4f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verification_tokens`
--

LOCK TABLES `verification_tokens` WRITE;
/*!40000 ALTER TABLE `verification_tokens` DISABLE KEYS */;
INSERT INTO `verification_tokens` VALUES (1,'2025-08-10 00:49:19.370371','2025-08-11 00:49:19.370371','e99c0f10-2b5a-4d75-a888-06f3da4fe762-1754761759368','EMAIL_VERIFICATION',_binary '\0',11),(2,'2025-08-17 21:00:30.592951','2025-08-18 21:00:30.592951','dd1e3f1b-5b3e-4409-980f-47fb4918df51-1755439230592','EMAIL_VERIFICATION',_binary '',18);
/*!40000 ALTER TABLE `verification_tokens` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-21  9:47:53
