-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Sep 24, 2025 at 08:24 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `busify`
--

-- --------------------------------------------------------

--
-- Table structure for table `audit_logs`
--

CREATE TABLE `audit_logs` (
  `id` bigint(20) NOT NULL,
  `action` varchar(255) NOT NULL,
  `details` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `target_entity` varchar(255) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `timestamp` datetime(6) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ;

--
-- Dumping data for table `audit_logs`
--

INSERT INTO `audit_logs` (`id`, `action`, `details`, `target_entity`, `target_id`, `timestamp`, `user_id`) VALUES
(1, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK123\"}', 'BOOKING', 1, '2025-07-24 12:00:00.000000', 1),
(2, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK124\"}', 'BOOKING', 2, '2025-07-24 13:00:00.000000', 5),
(3, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK125\"}', 'BOOKING', 3, '2025-07-24 14:00:00.000000', 6),
(4, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK126\"}', 'BOOKING', 4, '2025-07-24 15:00:00.000000', 8),
(5, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK127\"}', 'BOOKING', 5, '2025-07-24 16:00:00.000000', 5),
(7, 'DELETE', '{\"ticket_code\":\"TICKET124\"}', 'TICKET', 2, '2025-08-24 10:20:39.441348', 25),
(8, 'DELETE', '{\"booking_code\":\"BOOK125\"}', 'BOOKING', 3, '2025-08-24 10:24:17.685735', 25),
(9, 'UPDATE', '{\"booking_code\":\"BOOK123\"}', 'BOOKING', 1, '2025-08-24 10:27:21.590302', 25),
(10, 'DELETE', '{\"ticket_code\":\"TICKET125\"}', 'TICKET', 3, '2025-08-24 11:11:41.066146', 25),
(11, 'DELETE', '{\"booking_code\":\"BOOK126\"}', 'BOOKING', 4, '2025-08-25 13:57:50.725041', 25),
(12, 'UPDATE_STATUS', '{\"ticket_code\":\"169399\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 110, '2025-08-30 17:13:25.282978', 22),
(13, 'UPDATE_STATUS', '{\"ticket_code\":\"05FA30\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 111, '2025-08-30 17:15:50.863976', 22),
(14, 'UPDATE_STATUS', '{\"ticket_code\":\"169399\",\"previous_status\":\"valid\",\"new_status\":\"cancelled\",\"reason\":\"Vé đã bị hủy\"}', 'TICKET', 110, '2025-08-30 17:33:44.617341', 22),
(15, 'UPDATE_STATUS', '{\"ticket_code\":\"05FA30\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 111, '2025-08-30 17:33:53.246985', 22),
(19, 'DELETE', '{\"booking_code\":\"A18503\"}', 'BOOKING', 30, '2025-08-31 11:49:00.264786', 34),
(20, 'DELETE', '{\"booking_code\":\"09A5F0\"}', 'BOOKING', 31, '2025-08-31 11:50:18.382607', 34),
(21, 'DELETE', '{\"booking_code\":\"EC692B\"}', 'BOOKING', 137, '2025-09-05 01:52:19.499454', 28),
(22, 'DELETE', '{\"booking_code\":\"4DF78A\", \"refund_percentage\": 0.70, \"refund_reason\": \"Hủy trước chuyến đi khoảng 1 ngày\"}', 'BOOKING', 32, '2025-09-05 11:48:37.145185', 34),
(23, 'AUTO_CANCEL_TICKETS', '{\"trip_id\":3,\"cancelled_tickets_count\":3,\"reason\":\"Trip status changed to departed\"}', 'TRIP', 3, '2025-09-05 14:10:13.771788', 22),
(24, 'AUTO_CANCEL_TICKETS', '{\"trip_id\":3,\"cancelled_tickets_count\":3,\"reason\":\"Trip status changed to departed\"}', 'TRIP', 3, '2025-09-05 14:15:53.765522', 22),
(25, 'AUTO_CANCEL_TICKETS', '{\"trip_id\":3,\"cancelled_tickets_count\":2,\"reason\":\"Trip status changed to departed\"}', 'TRIP', 3, '2025-09-05 14:17:49.432328', 22),
(26, 'UPDATE_STATUS', '{\"ticket_code\":\"E81183\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 117, '2025-09-05 14:52:55.555855', 22),
(27, 'AUTO_CANCEL_TICKETS', '{\"trip_id\":3,\"cancelled_tickets_count\":2,\"reason\":\"Trip status changed to departed\"}', 'TRIP', 3, '2025-09-05 15:24:34.080985', 22),
(28, 'UPDATE_STATUS', '{\"ticket_code\":\"169399\",\"previous_status\":\"valid\",\"new_status\":\"cancelled\",\"reason\":\"Hành khách đã hủy vé\"}', 'TICKET', 110, '2025-09-05 16:34:17.919885', 22),
(29, 'UPDATE_STATUS', '{\"ticket_code\":\"05FA30\",\"previous_status\":\"valid\",\"new_status\":\"cancelled\",\"reason\":\"Hành khách đã hủy vé\"}', 'TICKET', 111, '2025-09-05 16:34:18.269425', 22),
(30, 'UPDATE_STATUS', '{\"ticket_code\":\"169399\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 110, '2025-09-05 16:35:45.178814', 22),
(31, 'UPDATE_STATUS', '{\"ticket_code\":\"05FA30\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 111, '2025-09-05 16:35:45.382091', 22),
(32, 'UPDATE_STATUS', '{\"ticket_code\":\"169399\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 110, '2025-09-05 16:41:32.165536', 22),
(33, 'UPDATE_STATUS', '{\"ticket_code\":\"05FA30\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 111, '2025-09-05 16:41:32.276064', 22),
(34, 'UPDATE_STATUS', '{\"ticket_code\":\"90CEA3\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 118, '2025-09-05 21:50:49.124336', 40),
(35, 'UPDATE_STATUS', '{\"ticket_code\":\"F3B118\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 119, '2025-09-05 21:50:49.334794', 40),
(36, 'UPDATE_STATUS', '{\"ticket_code\":\"169399\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 110, '2025-09-05 22:12:21.178687', 22),
(37, 'UPDATE_STATUS', '{\"ticket_code\":\"05FA30\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 111, '2025-09-05 22:12:23.319886', 22),
(38, 'UPDATE_STATUS', '{\"ticket_code\":\"169399\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 110, '2025-09-05 22:13:02.607003', 22),
(39, 'UPDATE_STATUS', '{\"ticket_code\":\"05FA30\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 111, '2025-09-05 22:13:02.846743', 22),
(40, 'AUTO_COMPLETE_BOOKINGS', '{\"trip_id\":24,\"completed_bookings_count\":7,\"reason\":\"Trip status changed to arrived\"}', 'TRIP', 24, '2025-09-05 22:35:28.827230', 40),
(41, 'UPDATE_STATUS', '{\"ticket_code\":\"169399\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 110, '2025-09-05 23:03:48.029245', 22),
(42, 'UPDATE_STATUS', '{\"ticket_code\":\"05FA30\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 111, '2025-09-05 23:04:06.242511', 22),
(43, 'AUTO_COMPLETE_BOOKINGS', '{\"trip_id\":3,\"completed_bookings_count\":2,\"reason\":\"Trip status changed to arrived\"}', 'TRIP', 3, '2025-09-05 23:05:21.353196', 22),
(44, 'AUTO_COMPLETE_BOOKINGS', '{\"trip_id\":24,\"completed_bookings_count\":1,\"reason\":\"Trip status changed to arrived\"}', 'TRIP', 24, '2025-09-05 23:11:53.551960', 40),
(45, 'DELETE', '{\"ticket_code\":\"TICKET123\"}', 'TICKET', 1, '2025-09-09 14:32:43.226140', 10),
(46, 'DELETE', '{\"booking_code\":\"BOOKING-1c96d426-e2ce-445a-b21e-6ee83fb3fdce\"}', 'BOOKING', 6, '2025-09-09 14:34:32.059987', 10),
(47, 'DELETE', '{\"ticket_code\":\"TICKET123\"}', 'TICKET', 1, '2025-09-09 14:50:34.542529', 10),
(48, 'DELETE', '{\"ticket_code\":\"TICKET125\"}', 'TICKET', 3, '2025-09-09 14:50:45.382794', 10),
(49, 'DELETE', '{\"ticket_code\":\"83CB2E\"}', 'TICKET', 75, '2025-09-09 15:02:26.384351', 10),
(50, 'DELETE', '{\"ticket_code\":\"TICKET126\"}', 'TICKET', 4, '2025-09-09 15:02:47.075777', 10),
(51, 'UPDATE', '{\"licensePlate\":\"36B-123.33\",\"modelName\":\"Hyundai Universe\",\"status\":\"active\",\"totalSeats\":40}', 'BUS', 16, '2025-09-10 09:10:29.851635', 18),
(52, 'UPDATE', '{\"licensePlate\":\"36B-123.33\",\"modelName\":\"Hyundai Universe\",\"status\":\"active\",\"totalSeats\":40}', 'BUS', 16, '2025-09-10 09:58:56.561580', 18),
(53, 'UPDATE', '{\"licensePlate\":\"51B-123.46\",\"modelName\":\"Hyundai Universe\",\"status\":\"active\",\"totalSeats\":40}', 'BUS', 8, '2025-09-10 10:34:47.964484', 18),
(54, 'UPDATE', '{\"licensePlate\":\"36B-123.32\",\"modelName\":\"Hyundai Universe\",\"status\":\"under_maintenance\",\"totalSeats\":40}', 'BUS', 14, '2025-09-10 10:35:32.750416', 18),
(55, 'UPDATE', '{\"licensePlate\":\"36B-123.32\",\"modelName\":\"Hyundai Universe\",\"status\":\"active\",\"totalSeats\":40}', 'BUS', 14, '2025-09-10 10:36:53.595017', 18),
(56, 'UPDATE', '{\"licensePlate\":\"23S-223.24\",\"modelName\":\"Isuzu Samco\",\"status\":\"active\",\"totalSeats\":40}', 'BUS', 17, '2025-09-10 10:44:41.276466', 18),
(57, 'UPDATE', '{\"licensePlate\":\"23S-223.24\",\"modelName\":\"Isuzu Samco\",\"status\":\"active\",\"totalSeats\":40}', 'BUS', 17, '2025-09-10 10:47:15.565688', 18),
(58, 'UPDATE', '{\"licensePlate\":\"51B-678.80\",\"modelName\":\"Mercedes Sprinter\",\"status\":\"active\",\"totalSeats\":80}', 'BUS', 22, '2025-09-10 10:51:55.083022', 18),
(59, 'UPDATE', '{\"licensePlate\":\"51B-678.91\",\"modelName\":\"Thaco Mobihome\",\"status\":\"active\",\"totalSeats\":80}', 'BUS', 24, '2025-09-10 11:03:31.166614', 18),
(60, 'UPDATE', '{\"licensePlate\":\"36B-123.31\",\"modelName\":\"Thaco Mobihome\",\"status\":\"active\",\"totalSeats\":80}', 'BUS', 9, '2025-09-10 11:04:29.250702', 18),
(61, 'UPDATE', '{\"route_id\":12,\"route_name\":\"Bến xe Miền Đông - Cổng 3 ⟶ Bến xe Giáp Bát\",\"start_location_id\":1,\"end_location_id\":2,\"default_price\":123232.00,\"default_duration\":720,\"action\":\"update\"}', 'ROUTE', 12, '2025-09-10 11:23:37.272727', 18),
(62, 'DELETE', '{\"booking_code\":\"920F8A\"}', 'BOOKING', 163, '2025-09-10 13:28:48.763296', 28),
(63, 'CREATE', '{\"payment_id\":117,\"booking_id\":168,\"amount\":307000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNFD92B6454F7D\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 117, '2025-09-10 15:32:22.205456', 26),
(64, 'CREATE', '{\"payment_id\":118,\"booking_id\":169,\"amount\":350000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNECCB186942B3\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 118, '2025-09-10 15:33:37.519129', 26),
(65, 'CREATE', '{\"payment_id\":119,\"booking_id\":170,\"amount\":234000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN752E6E4C84AD\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 119, '2025-09-10 15:55:42.479582', 31),
(66, 'CREATE', '{\"payment_id\":120,\"booking_id\":171,\"amount\":2698000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN87B35607971D\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 120, '2025-09-10 21:28:48.899964', 26),
(67, 'DELETE', '{\"booking_code\":\"2B635C\"}', 'BOOKING', 159, '2025-09-10 22:05:51.251737', 28),
(68, 'UPDATE', '{\"employee_id\":40,\"full_name\":\"Nguyễn Hoàng Anh\",\"email\":\"anhnh@gmail.com\",\"status\":\"active\",\"operator_id\":1,\"action\":\"update\"}', 'EMPLOYEE', 40, '2025-09-10 22:11:46.696464', 18),
(69, 'CREATE', '{\"payment_id\":121,\"booking_id\":172,\"amount\":300000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNDEE918E81D84\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 121, '2025-09-10 22:13:53.077708', 28),
(70, 'UPDATE_STATUS', '{\"ticket_code\":\"56B1E4\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 128, '2025-09-10 22:19:54.677123', 40),
(71, 'UPDATE_STATUS', '{\"ticket_code\":\"B1DA17\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 129, '2025-09-10 22:19:54.895548', 40),
(72, 'UPDATE_STATUS', '{\"ticket_code\":\"AB1026\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 130, '2025-09-10 22:19:55.110878', 40),
(73, 'CREATE', '{\"payment_id\":122,\"booking_id\":173,\"amount\":200000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN3F55A11FCD8A\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 122, '2025-09-10 22:30:45.653960', 31),
(74, 'UPDATE_STATUS', '{\"ticket_code\":\"35F6DA\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 131, '2025-09-10 22:32:04.814717', 40),
(75, 'UPDATE_STATUS', '{\"ticket_code\":\"8D41A0\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 132, '2025-09-10 22:32:05.017967', 40),
(76, 'UPDATE', '{\"oldStatus\":\"departed\",\"newStatus\":\"arrived\",\"reason\":\"null\"}', 'TRIP_STATUS', 38, '2025-09-10 22:47:03.945412', 40),
(77, 'AUTO_COMPLETE_BOOKINGS', '{\"trip_id\":38,\"completed_bookings_count\":1,\"reason\":\"Trip status changed to arrived\"}', 'TRIP', 38, '2025-09-10 22:47:06.879691', 40),
(78, 'UPDATE', '{\"oldStatus\":\"scheduled\",\"newStatus\":\"delayed\",\"reason\":\"null\"}', 'TRIP_STATUS', 38, '2025-09-10 22:48:46.474006', 40),
(79, 'UPDATE', '{\"oldStatus\":\"delayed\",\"newStatus\":\"departed\",\"reason\":\"null\"}', 'TRIP_STATUS', 38, '2025-09-10 22:51:22.506545', 40),
(80, 'UPDATE', '{\"oldStatus\":\"departed\",\"newStatus\":\"arrived\",\"reason\":\"null\"}', 'TRIP_STATUS', 38, '2025-09-10 22:51:32.336017', 40),
(81, 'AUTO_COMPLETE_BOOKINGS', '{\"trip_id\":38,\"completed_bookings_count\":1,\"reason\":\"Trip status changed to arrived\"}', 'TRIP', 38, '2025-09-10 22:51:35.121579', 40),
(82, 'UPDATE', '{\"oldStatus\":\"scheduled\",\"newStatus\":\"arrived\",\"reason\":\"null\"}', 'TRIP_STATUS', 38, '2025-09-10 22:58:47.627332', 40),
(83, 'AUTO_COMPLETE_BOOKINGS', '{\"trip_id\":38,\"completed_bookings_count\":1,\"reason\":\"Trip status changed to arrived\"}', 'TRIP', 38, '2025-09-10 22:58:50.186463', 40),
(84, 'DELETE', '{\"booking_code\":\"83B677\", \"refund_percentage\": 1.00, \"refund_reason\": \"Hủy trong vòng 24 giờ sau khi đặt\"}', 'BOOKING', 173, '2025-09-10 23:32:17.334522', 31),
(85, 'UPDATE', '{\"oldStatus\":\"scheduled\",\"newStatus\":\"arrived\",\"reason\":\"null\"}', 'TRIP_STATUS', 32, '2025-09-10 23:57:07.273924', 22),
(86, 'CREATE', '{\"payment_id\":123,\"booking_id\":174,\"amount\":2000000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNAE64B8573928\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 123, '2025-09-10 23:58:51.863947', 31),
(87, 'UPDATE_STATUS', '{\"ticket_code\":\"4E2C78\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 133, '2025-09-11 00:01:00.545843', 22),
(88, 'UPDATE_STATUS', '{\"ticket_code\":\"6D29D8\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 134, '2025-09-11 00:01:00.768343', 22),
(89, 'UPDATE_STATUS', '{\"ticket_code\":\"299893\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 135, '2025-09-11 00:01:00.992681', 22),
(90, 'UPDATE_STATUS', '{\"ticket_code\":\"A28E84\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 136, '2025-09-11 00:01:01.206229', 22),
(91, 'UPDATE', '{\"oldStatus\":\"scheduled\",\"newStatus\":\"arrived\",\"reason\":\"null\"}', 'TRIP_STATUS', 39, '2025-09-11 00:01:24.260011', 22),
(92, 'AUTO_COMPLETE_BOOKINGS', '{\"trip_id\":39,\"completed_bookings_count\":1,\"reason\":\"Trip status changed to arrived\"}', 'TRIP', 39, '2025-09-11 00:01:27.104131', 22),
(93, 'CREATE', '{\"payment_id\":124,\"booking_id\":175,\"amount\":4500000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN718F2E2109A0\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 124, '2025-09-11 10:09:52.970167', 28),
(94, 'CREATE', '{\"payment_id\":125,\"booking_id\":176,\"amount\":6000000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNBFBA8830EF37\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 125, '2025-09-11 10:41:20.163512', 28),
(95, 'CREATE', '{\"payment_id\":126,\"booking_id\":177,\"amount\":6000000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN78AADB8F4D1A\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 126, '2025-09-11 10:43:29.924625', 28),
(96, 'CREATE', '{\"payment_id\":127,\"booking_id\":178,\"amount\":6000000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXND552BF00F368\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 127, '2025-09-11 13:04:56.493161', 28),
(97, 'DELETE', '{\"booking_code\":\"1B32B6\"}', 'BOOKING', 178, '2025-09-11 13:06:32.786342', 28),
(98, 'DELETE', '{\"booking_code\":\"B9A382\"}', 'BOOKING', 176, '2025-09-11 13:16:55.210282', 28),
(99, 'CREATE', '{\"payment_id\":128,\"booking_id\":179,\"amount\":7500000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXND374C4728E0A\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 128, '2025-09-11 13:49:27.436130', 28),
(100, 'DELETE', '{\"booking_code\":\"D09232\"}', 'BOOKING', 179, '2025-09-11 13:50:53.050597', 28),
(101, 'CREATE', '{\"payment_id\":129,\"booking_id\":180,\"amount\":300000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNB61812F03D8C\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 129, '2025-09-11 13:56:24.314703', 28),
(102, 'DELETE', '{\"booking_code\":\"174858\"}', 'BOOKING', 175, '2025-09-11 13:58:20.955191', 28),
(103, 'CREATE', '{\"payment_id\":130,\"booking_id\":181,\"amount\":900000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN7703F7091942\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 130, '2025-09-11 14:14:19.027476', 28),
(104, 'CREATE', '{\"payment_id\":131,\"booking_id\":182,\"amount\":400000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN8B4C49D85E1D\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 131, '2025-09-11 14:17:05.039261', 28),
(105, 'CREATE', '{\"payment_id\":132,\"booking_id\":183,\"amount\":400000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNEC922A59CF58\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 132, '2025-09-11 14:18:28.612016', 28),
(106, 'DELETE', '{\"booking_code\":\"54E432\"}', 'BOOKING', 181, '2025-09-11 14:26:20.539104', 28),
(107, 'CREATE', '{\"payment_id\":133,\"booking_id\":184,\"amount\":300000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN94DEC96A70DB\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 133, '2025-09-11 14:27:05.566761', 26),
(108, 'DELETE', '{\"booking_code\":\"7E4405\"}', 'BOOKING', 169, '2025-09-11 14:28:21.576960', 26),
(109, 'USE', '{\"user_id\":26,\"promotion_code\":\"RUVDTU\",\"promotion_id\":15,\"discount_value\":20.00,\"action\":\"mark_as_used\"}', 'USER_PROMOTION', 15, '2025-09-11 21:32:52.421464', 26),
(110, 'CREATE', '{\"payment_id\":134,\"booking_id\":185,\"amount\":697600.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN5E5752B8A315\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 134, '2025-09-11 21:32:52.661234', 26),
(111, 'UPDATE_STATUS', '{\"ticket_code\":\"5D0306\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 180, '2025-09-11 21:37:12.679376', 19),
(112, 'UPDATE_STATUS', '{\"ticket_code\":\"A30D05\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 181, '2025-09-11 21:37:12.929397', 19),
(113, 'UPDATE', '{\"oldStatus\":\"scheduled\",\"newStatus\":\"arrived\",\"reason\":\"null\"}', 'TRIP_STATUS', 25, '2025-09-11 21:40:21.619333', 19),
(114, 'AUTO_COMPLETE_BOOKINGS', '{\"trip_id\":25,\"completed_bookings_count\":3,\"reason\":\"Trip status changed to arrived\"}', 'TRIP', 25, '2025-09-11 21:40:24.638795', 19),
(115, 'CREATE', '{\"payment_id\":135,\"booking_id\":186,\"amount\":1780000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN43BEC937E035\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 135, '2025-09-11 21:44:04.489127', 26),
(116, 'DELETE', '{\"booking_code\":\"3314EB\", \"refund_percentage\": 1.00, \"refund_reason\": \"Hủy trong vòng 24 giờ sau khi đặt\"}', 'BOOKING', 186, '2025-09-11 21:45:28.620755', 26),
(117, 'CREATE', '{\"payment_id\":136,\"booking_id\":187,\"amount\":200000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN6EDCA2D97397\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 136, '2025-09-11 21:54:05.621153', 26),
(118, 'CREATE', '{\"licensePlate\":\"88A-888.81\",\"modelName\":\"Hyundai Universe\",\"operatorId\":1,\"totalSeats\":80}', 'BUS', 45, '2025-09-11 22:40:49.317187', 18),
(119, 'CREATE', '{\"licensePlate\":\"51H-554.36\",\"modelName\":\"Thaco Mobihome\",\"operatorId\":1,\"totalSeats\":40}', 'BUS', 46, '2025-09-11 22:41:20.777739', 18),
(120, 'UPDATE', '{\"licensePlate\":\"88A-888.81\",\"modelName\":\"Hyundai Universe\",\"status\":\"active\",\"totalSeats\":80}', 'BUS', 45, '2025-09-11 22:41:37.506869', 18),
(121, 'CREATE', '{\"payment_id\":137,\"booking_id\":189,\"amount\":623000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNDCCB74F16521\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 137, '2025-09-11 22:56:35.377761', 31),
(122, 'UPDATE_STATUS', '{\"ticket_code\":\"7A39EA\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 189, '2025-09-11 22:59:42.066090', 32),
(123, 'UPDATE_STATUS', '{\"ticket_code\":\"7A39EA\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 189, '2025-09-11 23:00:21.723395', 32),
(124, 'UPDATE', '{\"oldStatus\":\"scheduled\",\"newStatus\":\"arrived\",\"reason\":\"null\"}', 'TRIP_STATUS', 33, '2025-09-11 23:00:42.141968', 32),
(125, 'AUTO_COMPLETE_BOOKINGS', '{\"trip_id\":33,\"completed_bookings_count\":1,\"reason\":\"Trip status changed to arrived\"}', 'TRIP', 33, '2025-09-11 23:00:45.562257', 32),
(126, 'CREATE', '{\"payment_id\":138,\"booking_id\":190,\"amount\":100000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN3F61CFA611EC\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 138, '2025-09-11 23:12:51.311569', 26),
(127, 'CREATE', '{\"payment_id\":139,\"booking_id\":191,\"amount\":200000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNE8B10E83F13F\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 139, '2025-09-11 23:24:35.606626', 26),
(128, 'CREATE', '{\"payment_id\":140,\"booking_id\":192,\"amount\":872000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN89F3E417D645\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 140, '2025-09-11 23:29:14.660625', 26),
(129, 'DELETE', '{\"booking_code\":\"C99A1E\", \"refund_percentage\": 1.00, \"refund_reason\": \"Hủy trong vòng 24 giờ sau khi đặt\"}', 'BOOKING', 192, '2025-09-11 23:30:26.734258', 26),
(130, 'CREATE', '{\"payment_id\":141,\"booking_id\":193,\"amount\":872000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN03C581C1E801\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 141, '2025-09-11 23:35:23.589948', 26),
(131, 'DELETE', '{\"booking_code\":\"D6D064\", \"refund_percentage\": 1.00, \"refund_reason\": \"Hủy trong vòng 24 giờ sau khi đặt\"}', 'BOOKING', 193, '2025-09-11 23:36:19.956292', 26),
(132, 'UPDATE', '{\"booking_code\":\"BOOKING-442dc346-2608-490e-8a91-4aefa187bf7a\"}', 'BOOKING', 14, '2025-09-12 00:00:15.741930', 21),
(133, 'DELETE', '{\"email\":\"vc@gmail.com\",\"action\":\"soft_delete\"}', 'USER', 42, '2025-09-12 00:06:44.259302', 10),
(134, 'DELETE', '{\"booking_code\":\"127E6C\", \"refund_percentage\": 1.00, \"refund_reason\": \"Hủy trong vòng 24 giờ sau khi đặt\"}', 'BOOKING', 189, '2025-09-12 00:10:18.555809', 31),
(135, 'DELETE', '{\"ticket_code\":\"5CB04B\", \"refund_percentage\": 1.00, \"refund_reason\": \"Hủy trong vòng 24 giờ sau khi đặt\"}', 'TICKET', 194, '2025-09-12 00:16:14.826733', 10),
(136, 'DELETE', '{\"booking_code\":\"127E6C\", \"refund_percentage\": 1.00, \"refund_reason\": \"Hủy trong vòng 24 giờ sau khi đặt\"}', 'BOOKING', 189, '2025-09-12 00:26:09.364050', 31),
(137, 'CREATE', '{\"payment_id\":142,\"booking_id\":194,\"amount\":900000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN07538F2C0FBF\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 142, '2025-09-15 14:13:21.882410', 26),
(138, 'PROCESS', '{\"contract_email\":\"antoinnguyen2@gmail.com\",\"profile_id\":43,\"bus_operator_id\":10,\"is_new_user\":true,\"action\":\"process_accepted_contract\"}', 'CONTRACT_USER', 7, '2025-09-15 22:10:14.713468', 10),
(139, 'REVIEW', '{\"email\":\"antoinnguyen2@gmail.com\",\"vat_code\":\"0123412341\",\"review_action\":\"APPROVE\",\"new_status\":\"ACCEPTED\",\"admin_note\":\"null\",\"action\":\"review\"}', 'CONTRACT', 7, '2025-09-15 22:10:14.735645', 10),
(140, 'CREATE', '{\"employee_id\":44,\"full_name\":\"hoàng hoài\",\"email\":\"hoai123@gmail.com\",\"role\":\"STAFF\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 44, '2025-09-15 22:11:24.187220', 43),
(141, 'CREATE', '{\"licensePlate\":\"88A-888.74\",\"modelName\":\"Mercedes Sprinter\",\"operatorId\":10,\"totalSeats\":80}', 'BUS', 47, '2025-09-15 22:12:16.199035', 43),
(142, 'UPDATE', '{\"employee_id\":44,\"full_name\":\"hoàng hoài\",\"email\":\"hoai123@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 44, '2025-09-15 22:13:03.135540', 43),
(143, 'UPDATE', '{\"employee_id\":44,\"full_name\":\"hoàng hoài\",\"email\":\"hoai123@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 44, '2025-09-15 22:13:48.614323', 43),
(144, 'CREATE', '{\"employee_id\":45,\"full_name\":\"Nguyễn An Toàn\",\"email\":\"sdjsdjk@gmail.com\",\"role\":\"STAFF\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 45, '2025-09-15 22:14:07.996157', 43),
(145, 'CREATE', '{\"employee_id\":46,\"full_name\":\"hvhoai\",\"email\":\"hoai123123@gmail.com\",\"role\":\"STAFF\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 46, '2025-09-15 22:14:10.340609', 43),
(146, 'UPDATE', '{\"employee_id\":46,\"full_name\":\"hvhoai\",\"email\":\"hoai123123@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 46, '2025-09-15 22:14:33.461070', 43),
(147, 'CREATE', '{\"licensePlate\":\"51B-678.96\",\"modelName\":\"Hyundai Aero\",\"operatorId\":10,\"totalSeats\":40}', 'BUS', 48, '2025-09-15 22:22:30.896196', 43),
(148, 'CREATE', '{\"employee_id\":47,\"full_name\":\"Hoàng Hoài\",\"email\":\"jsdfhjdjfhd@gmail.com\",\"role\":\"STAFF\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 47, '2025-09-15 22:23:11.441980', 43),
(149, 'DELETE', '{\"employee_id\":47,\"full_name\":\"Hoàng Hoài\",\"email\":\"jsdfhjdjfhd@gmail.com\",\"action\":\"hard_delete\"}', 'EMPLOYEE', 47, '2025-09-15 22:47:49.297229', 43),
(150, 'DELETE', '{\"employee_id\":45,\"full_name\":\"Nguyễn An Toàn\",\"email\":\"sdjsdjk@gmail.com\",\"action\":\"hard_delete\"}', 'EMPLOYEE', 45, '2025-09-15 22:48:02.301214', 43),
(151, 'DELETE', '{\"licensePlate\":\"88A-888.74\",\"modelName\":\"Mercedes Sprinter\",\"operatorName\":\"ANTOINNGUYEN2 COMPANY\",\"action\":\"hard_delete\"}', 'BUS', 47, '2025-09-15 22:48:45.456261', 43),
(152, 'CREATE', '{\"employee_id\":48,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"role\":\"STAFF\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 48, '2025-09-16 09:30:17.617191', 43),
(153, 'DELETE', '{\"employee_id\":48,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"action\":\"hard_delete\"}', 'EMPLOYEE', 48, '2025-09-16 09:49:46.453201', 43),
(154, 'CREATE', '{\"licensePlate\":\"51B-678.99\",\"modelName\":\"Hyundai Universe\",\"operatorId\":10,\"totalSeats\":40}', 'BUS', 49, '2025-09-16 10:00:23.913846', 43),
(155, 'CREATE', '{\"employee_id\":49,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"role\":\"STAFF\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 49, '2025-09-16 10:00:51.299172', 43),
(156, 'CREATE', '{\"licensePlate\":\"12B-123.12\",\"modelName\":\"Hyundai Aero\",\"operatorId\":10,\"totalSeats\":40}', 'BUS', 50, '2025-09-16 10:02:11.095504', 43),
(157, 'DELETE', '{\"employee_id\":49,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"action\":\"hard_delete\"}', 'EMPLOYEE', 49, '2025-09-16 10:05:25.790815', 43),
(158, 'DELETE', '{\"licensePlate\":\"12B-123.12\",\"modelName\":\"Hyundai Aero\",\"operatorName\":\"ANTOINNGUYEN2 COMPANY\",\"action\":\"hard_delete\"}', 'BUS', 50, '2025-09-16 10:09:15.133662', 43),
(159, 'CREATE', '{\"licensePlate\":\"12B-123.12\",\"modelName\":\"Hyundai Aero\",\"operatorId\":10,\"totalSeats\":40}', 'BUS', 51, '2025-09-16 10:21:05.184887', 43),
(160, 'DELETE', '{\"licensePlate\":\"12B-123.12\",\"modelName\":\"Hyundai Aero\",\"operatorName\":\"ANTOINNGUYEN2 COMPANY\",\"action\":\"hard_delete\"}', 'BUS', 51, '2025-09-16 10:21:17.435390', 43),
(161, 'CREATE', '{\"licensePlate\":\"36B-123.38\",\"modelName\":\"Hyundai Aero\",\"operatorId\":10,\"totalSeats\":40}', 'BUS', 52, '2025-09-16 10:28:04.017636', 43),
(162, 'DELETE', '{\"licensePlate\":\"36B-123.38\",\"modelName\":\"Hyundai Aero\",\"operatorName\":\"ANTOINNGUYEN2 COMPANY\",\"action\":\"hard_delete\"}', 'BUS', 52, '2025-09-16 10:28:14.666518', 43),
(163, 'CREATE', '{\"employee_id\":50,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"role\":\"STAFF\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 50, '2025-09-16 10:28:31.380189', 43),
(164, 'UPDATE', '{\"route_id\":4,\"route_name\":\"Bến xe Cần Thơ ⟶ Bến xe Vũng Tàu\",\"start_location_id\":5,\"end_location_id\":6,\"default_price\":200000.00,\"default_duration\":240,\"action\":\"update\"}', 'ROUTE', 4, '2025-09-16 10:31:40.922511', 43),
(165, 'UPDATE', '{\"route_id\":5,\"route_name\":\"Bến xe Nha Trang ⟶ Bến xe Miền Đông - Cổng 3\",\"start_location_id\":7,\"end_location_id\":1,\"default_price\":220000.00,\"default_duration\":300,\"action\":\"update\"}', 'ROUTE', 5, '2025-09-16 10:31:45.182617', 43),
(166, 'CREATE', '{\"route_id\":17,\"route_name\":\"Bến xe Nha Trang ⟶ Bến xe Đà Lạt\",\"start_location_id\":7,\"end_location_id\":3,\"default_price\":250000.00,\"default_duration\":180,\"action\":\"create\"}', 'ROUTE', 17, '2025-09-16 10:32:10.131267', 43),
(167, 'DELETE', '{\"employee_id\":50,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"action\":\"hard_delete\"}', 'EMPLOYEE', 50, '2025-09-16 11:08:53.214980', 43),
(168, 'CREATE', '{\"employee_id\":51,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"role\":\"STAFF\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 51, '2025-09-16 13:25:37.264476', 43),
(169, 'DELETE', '{\"employee_id\":51,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"action\":\"hard_delete\"}', 'EMPLOYEE', 51, '2025-09-16 13:25:45.177926', 43),
(170, 'CREATE', '{\"employee_id\":52,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"role\":\"STAFF\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 52, '2025-09-16 21:41:25.823441', 43),
(171, 'CREATE', '{\"licensePlate\":\"36B-123.39\",\"modelName\":\"Hyundai Aero\",\"operatorId\":10,\"totalSeats\":40}', 'BUS', 53, '2025-09-16 21:41:52.423620', 43),
(172, 'DELETE', '{\"employee_id\":52,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"action\":\"hard_delete\"}', 'EMPLOYEE', 52, '2025-09-16 21:42:09.822666', 43),
(173, 'DELETE', '{\"licensePlate\":\"36B-123.39\",\"modelName\":\"Hyundai Aero\",\"operatorName\":\"ANTOINNGUYEN2 COMPANY\",\"action\":\"hard_delete\"}', 'BUS', 53, '2025-09-16 21:42:14.612836', 43),
(174, 'CREATE', '{\"route_id\":18,\"route_name\":\"Bến xe Huế ⟶ Bến xe Cần Thơ\",\"start_location_id\":4,\"end_location_id\":5,\"default_price\":340000.00,\"default_duration\":340,\"action\":\"create\"}', 'ROUTE', 18, '2025-09-16 21:42:46.185594', 43),
(175, 'CREATE', '{\"employee_id\":53,\"full_name\":\"Nguyen Van b\",\"email\":\"Sincere@april.biz\",\"role\":\"STAFF\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 53, '2025-09-17 10:33:54.042792', 43),
(176, 'CREATE', '{\"employee_id\":54,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"role\":\"DRIVER\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 54, '2025-09-17 11:15:30.154594', 43),
(177, 'DELETE', '{\"licensePlate\":\"51H-554.36\",\"modelName\":\"Thaco Mobihome\",\"operatorName\":\"Nhà Xe AntoinNg\",\"action\":\"hard_delete\"}', 'BUS', 46, '2025-09-17 11:19:16.846102', 18),
(178, 'UPDATE', '{\"employee_id\":53,\"full_name\":\"Nguyen Van b\",\"email\":\"Sincere@april.biz\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 53, '2025-09-17 13:27:27.369221', 43),
(179, 'UPDATE', '{\"employee_id\":22,\"full_name\":\"Hoàng Văn Hoài\",\"email\":\"hoaihv@gmail.com\",\"status\":\"inactive\",\"operator_id\":1,\"action\":\"update\"}', 'EMPLOYEE', 22, '2025-09-17 16:31:14.387103', 18),
(180, 'UPDATE', '{\"employee_id\":22,\"full_name\":\"Hoàng Văn Hoài\",\"email\":\"hoaihv@gmail.com\",\"status\":\"active\",\"operator_id\":1,\"action\":\"update\"}', 'EMPLOYEE', 22, '2025-09-17 16:31:25.797907', 18),
(181, 'DELETE', '{\"promotion_id\":21,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":10000.00,\"status\":\"active\",\"action\":\"hard_delete\"}', 'PROMOTION', 21, '2025-09-17 21:16:41.064535', 10),
(182, 'UPDATE', '{\"promotion_id\":2,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":20.00,\"old_status\":\"active\",\"new_status\":\"inactive\",\"action\":\"update\"}', 'PROMOTION', 2, '2025-09-17 21:28:21.627589', 10),
(183, 'UPDATE', '{\"promotion_id\":4,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":15.00,\"old_status\":\"active\",\"new_status\":\"inactive\",\"action\":\"update\"}', 'PROMOTION', 4, '2025-09-17 22:26:22.310421', 10),
(184, 'UPDATE', '{\"promotion_id\":4,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":15.00,\"old_status\":\"inactive\",\"new_status\":\"active\",\"action\":\"update\"}', 'PROMOTION', 4, '2025-09-17 22:26:27.284525', 10),
(185, 'CREATE', '{\"payment_id\":143,\"booking_id\":208,\"amount\":960000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN6714AAC10723\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 143, '2025-09-18 00:28:29.170132', 31),
(186, 'CREATE', '{\"promotion_id\":23,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":100.00,\"usage_limit\":null,\"status\":\"active\",\"action\":\"create\"}', 'PROMOTION', 23, '2025-09-18 00:29:48.489508', 10),
(187, 'UPDATE', '{\"promotion_id\":23,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":9.00,\"old_status\":\"active\",\"new_status\":\"active\",\"action\":\"update\"}', 'PROMOTION', 23, '2025-09-18 00:30:08.137413', 10),
(188, 'CREATE', '{\"payment_id\":144,\"booking_id\":209,\"amount\":409500.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN023DD6120770\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 144, '2025-09-18 00:31:14.651431', 31),
(189, 'CREATE', '{\"payment_id\":145,\"booking_id\":210,\"amount\":500000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNF8C289BC9166\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 145, '2025-09-18 01:36:07.119355', 31),
(190, 'CREATE', '{\"payment_id\":146,\"booking_id\":211,\"amount\":3040000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNF971DA1A5C6A\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 146, '2025-09-18 03:23:06.348554', 31),
(191, 'DELETE', '{\"promotion_id\":23,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":9.00,\"status\":\"active\",\"action\":\"hard_delete\"}', 'PROMOTION', 23, '2025-09-18 03:23:51.201430', 10),
(192, 'DELETE', '{\"promotion_id\":11,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":30.00,\"status\":\"active\",\"action\":\"hard_delete\"}', 'PROMOTION', 11, '2025-09-18 03:23:57.795044', 10),
(193, 'DELETE', '{\"promotion_id\":2,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":20.00,\"status\":\"inactive\",\"action\":\"hard_delete\"}', 'PROMOTION', 2, '2025-09-18 03:24:02.388685', 10),
(194, 'CREATE', '{\"payment_id\":147,\"booking_id\":212,\"amount\":1602000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNA22B547ABCBA\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 147, '2025-09-18 03:52:29.376143', 31),
(195, 'DELETE', '{\"booking_code\":\"E64772\", \"refund_percentage\": 1.00, \"refund_reason\": \"Hủy trong vòng 24 giờ sau khi đặt\"}', 'BOOKING', 212, '2025-09-18 03:52:57.170074', 31),
(196, 'DELETE', '{\"promotion_id\":23,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":9.00,\"status\":\"active\",\"action\":\"hard_delete\",\"affected_bookings\":0,\"affected_user_promotions\":1}', 'PROMOTION', 23, '2025-09-18 03:54:32.117257', 10),
(197, 'DELETE', '{\"promotion_id\":22,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":20.00,\"status\":\"active\",\"action\":\"hard_delete\",\"affected_bookings\":0,\"affected_user_promotions\":1}', 'PROMOTION', 22, '2025-09-18 03:54:36.055049', 10),
(198, 'DELETE', '{\"promotion_id\":20,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":10.00,\"status\":\"active\",\"action\":\"hard_delete\",\"affected_bookings\":0,\"affected_user_promotions\":0}', 'PROMOTION', 20, '2025-09-18 03:54:40.363613', 10),
(199, 'DELETE', '{\"promotion_id\":9,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":10.00,\"status\":\"active\",\"action\":\"hard_delete\",\"affected_bookings\":0,\"affected_user_promotions\":2}', 'PROMOTION', 9, '2025-09-18 03:54:51.126964', 10),
(200, 'DELETE', '{\"promotion_id\":11,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":30.00,\"status\":\"active\",\"action\":\"hard_delete\",\"affected_bookings\":0,\"affected_user_promotions\":3}', 'PROMOTION', 11, '2025-09-18 03:54:54.191328', 10),
(201, 'DELETE', '{\"promotion_id\":12,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":10.00,\"status\":\"active\",\"action\":\"hard_delete\",\"affected_bookings\":0,\"affected_user_promotions\":2}', 'PROMOTION', 12, '2025-09-18 03:55:00.505475', 10),
(202, 'CREATE', '{\"promotion_id\":24,\"code\":\"152P6V\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":23.00,\"usage_limit\":null,\"status\":\"active\",\"action\":\"create\"}', 'PROMOTION', 24, '2025-09-18 04:06:10.468755', 10),
(203, 'AUTO_CLAIM', '{\"user_id\":31,\"promotion_id\":24,\"promotion_code\":\"152P6V\",\"discount_value\":23.00,\"action\":\"auto_claim_promotion\"}', 'USER_PROMOTION', 24, '2025-09-18 04:11:18.395823', 31),
(204, 'USE', '{\"user_id\":31,\"promotion_code\":\"152P6V\",\"promotion_id\":24,\"discount_value\":23.00,\"action\":\"mark_as_used\"}', 'USER_PROMOTION', 24, '2025-09-18 04:11:18.477970', 31),
(205, 'CREATE', '{\"payment_id\":148,\"booking_id\":213,\"amount\":385000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN366FC5BC29B5\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 148, '2025-09-18 04:11:18.712573', 31),
(206, 'CREATE', '{\"payment_id\":149,\"booking_id\":214,\"amount\":499000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXND0DCBA1F7249\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 149, '2025-09-18 04:11:50.901391', 31),
(207, 'CREATE', '{\"route_id\":19,\"route_name\":\"Bến xe Cần Thơ ⟶ Bến xe Nha Trang\",\"start_location_id\":5,\"end_location_id\":7,\"default_price\":5000000.00,\"default_duration\":1000,\"action\":\"create\"}', 'ROUTE', 19, '2025-09-18 04:13:45.009183', 18),
(208, 'CREATE', '{\"employee_id\":56,\"full_name\":\"Nguyễn An Quốc\",\"email\":\"quocng@gmail.com\",\"role\":\"DRIVER\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 56, '2025-09-18 09:47:37.657497', 43),
(209, 'UPDATE', '{\"employee_id\":56,\"full_name\":\"Nguyễn An Quốc\",\"email\":\"quocng@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 56, '2025-09-18 09:52:04.669178', 43),
(210, 'CREATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"role\":\"DRIVER\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 57, '2025-09-18 10:00:57.620850', 43),
(211, 'CREATE', '{\"payment_id\":150,\"booking_id\":215,\"amount\":3800000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN8D8D27C5EDF7\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 150, '2025-09-18 10:02:08.289971', 26),
(212, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 10:02:19.382392', 43),
(213, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 10:04:04.955975', 43),
(214, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 10:08:14.934988', 43),
(215, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 10:14:08.291865', 43),
(216, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 10:14:21.611784', 43),
(217, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 10:18:15.555993', 43),
(218, 'UPDATE', '{\"employee_id\":54,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 54, '2025-09-18 10:23:32.527980', 43),
(219, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 10:48:52.606999', 43),
(220, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 10:49:11.822785', 43),
(221, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 10:57:10.862351', 43),
(222, 'DELETE', '{\"name\":\"Nhà Xe Biển Xanh\",\"email\":\"operator5@busify.com\",\"action\":\"soft_delete\"}', 'BUS_OPERATOR', 5, '2025-09-18 10:58:27.313927', 10),
(223, 'DELETE', '{\"name\":\"Nhà Xe Biển Xanh\",\"email\":\"operator5@busify.com\",\"action\":\"soft_delete\"}', 'BUS_OPERATOR', 5, '2025-09-18 10:58:33.542100', 10),
(224, 'DELETE', '{\"name\":\"Nhà Xe Biển Xanh\",\"email\":\"operator5@busify.com\",\"action\":\"soft_delete\"}', 'BUS_OPERATOR', 5, '2025-09-18 10:58:36.443647', 10),
(225, 'DELETE', '{\"name\":\"Nhà Xe Biển Xanh\",\"email\":\"operator5@busify.com\",\"action\":\"soft_delete\"}', 'BUS_OPERATOR', 5, '2025-09-18 10:58:46.124496', 10),
(226, 'DELETE', '{\"name\":\"Nhà Xe Nha Trang\",\"email\":\"operator6@busify.com\",\"action\":\"soft_delete\"}', 'BUS_OPERATOR', 6, '2025-09-18 11:01:12.961872', 10),
(227, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 11:20:35.328113', 43),
(228, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 11:20:41.916486', 43),
(229, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 11:20:52.579678', 43),
(230, 'UPDATE', '{\"employee_id\":54,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 54, '2025-09-18 11:21:38.920609', 43),
(231, 'UPDATE', '{\"name\":\"HOAI19800 COMPANY\",\"email\":\"hoai19800@gmail.com\",\"status\":\"active\"}', 'BUS_OPERATOR', 8, '2025-09-18 11:28:21.251358', 10),
(232, 'CREATE', '{\"employee_id\":58,\"full_name\":\"Đặng Văn Đức\",\"email\":\"ducdv@gmail.com\",\"role\":\"DRIVER\",\"operator_id\":10,\"action\":\"create\"}', 'EMPLOYEE', 58, '2025-09-18 11:50:00.828373', 43),
(233, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 11:52:26.621169', 43),
(234, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 12:13:33.538687', 43),
(235, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 12:13:52.939435', 43),
(236, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 12:17:13.313948', 43),
(237, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-18 12:53:01.851729', 43),
(238, 'CREATE', '{\"payment_id\":151,\"booking_id\":217,\"amount\":500000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN7DD92BEC8CA6\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 151, '2025-09-18 12:58:53.884254', 31),
(239, 'CREATE', '{\"payment_id\":152,\"booking_id\":218,\"amount\":390000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNE12D28A9E149\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 152, '2025-09-19 22:20:54.230623', 28),
(240, 'UPDATE', '{\"employee_id\":54,\"full_name\":\"Antoin\",\"email\":\"antoin@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 54, '2025-09-19 22:30:54.282317', 43),
(241, 'UPDATE', '{\"oldStatus\":\"on_sell\",\"newStatus\":\"departed\",\"reason\":\"null\"}', 'TRIP_STATUS', 42, '2025-09-19 22:54:15.120749', 54),
(242, 'AUTO_CANCEL_TICKETS', '{\"trip_id\":42,\"cancelled_tickets_count\":4,\"reason\":\"Trip status changed to departed\"}', 'TRIP', 42, '2025-09-19 22:54:15.223977', 54),
(243, 'UPDATE', '{\"oldStatus\":\"departed\",\"newStatus\":\"arrived\",\"reason\":\"null\"}', 'TRIP_STATUS', 42, '2025-09-19 23:04:27.777900', 54),
(244, 'AUTO_COMPLETE_BOOKINGS', '{\"trip_id\":42,\"completed_bookings_count\":1,\"reason\":\"Trip status changed to arrived\"}', 'TRIP', 42, '2025-09-19 23:04:28.962194', 54),
(245, 'UPDATE_STATUS', '{\"ticket_code\":\"905E34\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 215, '2025-09-19 23:14:26.026333', 54),
(246, 'UPDATE_STATUS', '{\"ticket_code\":\"697C63\",\"previous_status\":\"valid\",\"new_status\":\"used\",\"reason\":\"Hành khách đã lên xe\"}', 'TICKET', 216, '2025-09-19 23:14:27.560938', 54),
(247, 'UPDATE', '{\"employee_id\":57,\"full_name\":\"Tôn Bá Trung\",\"email\":\"trungtb@gmail.com\",\"status\":\"active\",\"operator_id\":10,\"action\":\"update\"}', 'EMPLOYEE', 57, '2025-09-19 23:38:23.821633', 43),
(248, 'UPDATE', '{\"email\":\"binhnguyen130816@gmail.com\",\"fullName\":\"Duong\",\"roleId\":11}', 'USER', 55, '2025-09-20 08:25:05.000000', 10),
(249, 'CREATE', '{\"payment_id\":153,\"booking_id\":220,\"amount\":3000000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN5B363EF6D66E\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 153, '2025-09-20 09:39:21.000000', 28),
(250, 'DELETE', '{\"booking_code\":\"A6C5F0\", \"refund_percentage\": 1.00, \"refund_reason\": \"Hủy trong vòng 24 giờ sau khi đặt\"}', 'BOOKING', 220, '2025-09-20 09:44:12.000000', 28),
(251, 'UPDATE', '{\"promotion_id\":15,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":20.00,\"old_status\":\"active\",\"new_status\":\"active\",\"action\":\"update\"}', 'PROMOTION', 15, '2025-09-23 11:17:09.000000', 10),
(252, 'CREATE', '{\"payment_id\":154,\"booking_id\":221,\"amount\":2400000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN254E25224923\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 154, '2025-09-23 14:03:37.000000', 28),
(253, 'CREATE', '{\"payment_id\":155,\"booking_id\":222,\"amount\":1920000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXNFE53FB212D3A\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 155, '2025-09-23 15:47:12.000000', 28),
(254, 'UPDATE', '{\"promotion_id\":15,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":20.00,\"old_status\":\"active\",\"new_status\":\"active\",\"action\":\"update\"}', 'PROMOTION', 15, '2025-09-24 11:08:20.000000', 10),
(255, 'CREATE', '{\"promotion_id\":26,\"code\":\"null\",\"discount_type\":\"PERCENTAGE\",\"discount_value\":10.00,\"usage_limit\":null,\"status\":\"active\",\"action\":\"create\"}', 'PROMOTION', 26, '2025-09-24 13:04:06.000000', 10),
(256, 'CREATE', '{\"payment_id\":156,\"booking_id\":223,\"amount\":1440000.00,\"payment_method\":\"VNPAY\",\"transaction_code\":\"TXN0D3C5D6EDBB9\",\"status\":\"pending\",\"action\":\"create\"}', 'PAYMENT', 156, '2025-09-24 13:04:25.000000', 28);

-- --------------------------------------------------------

--
-- Table structure for table `blog_posts`
--

CREATE TABLE `blog_posts` (
  `id` bigint(20) NOT NULL,
  `content` longtext NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `excerpt` text DEFAULT NULL,
  `featured` bit(1) DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `published` bit(1) DEFAULT NULL,
  `published_at` datetime(6) DEFAULT NULL,
  `reading_time` int(11) DEFAULT NULL,
  `slug` varchar(250) NOT NULL,
  `title` varchar(200) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `view_count` bigint(20) DEFAULT NULL,
  `author_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `blog_posts`
--

INSERT INTO `blog_posts` (`id`, `content`, `created_at`, `excerpt`, `featured`, `image_url`, `published`, `published_at`, `reading_time`, `slug`, `title`, `updated_at`, `view_count`, `author_id`) VALUES
(1, '<p>Trung t&acirc;m Dự b&aacute;o kh&iacute; tượng thủy văn quốc gia, cho biết l&uacute;c 19h ng&agrave;y 19/9, vị tr&iacute; t&acirc;m b&atilde;o số 8 tr&ecirc;n đất liền ph&iacute;a Nam tỉnh Quảng Đ&ocirc;ng (Trung Quốc).</p>\r\n<p>Sức gi&oacute; mạnh nhất v&ugrave;ng gần t&acirc;m b&atilde;o cấp 8 (62-74km/h), giật cấp 10; di chuyển theo hướng T&acirc;y Bắc, tốc độ 5-10km/h. &nbsp;</p>\r\n<p>Đến 7h ng&agrave;y 20/9, b&atilde;o số 8 suy yếu dần th&agrave;nh &aacute;p thấp nhiệt đới sau l&agrave; một v&ugrave;ng &aacute;p thấp, tr&ecirc;n đất liền ph&iacute;a Nam tỉnh Quảng Đ&ocirc;ng (Trung Quốc).&nbsp;</p>\r\n<p>Do ảnh hưởng của b&atilde;o số 8 v&ugrave;ng biển ph&iacute;a Bắc khu vực Bắc Biển Đ&ocirc;ng c&oacute; gi&oacute; mạnh cấp 6-7, giật cấp 9; v&ugrave;ng gần t&acirc;m b&atilde;o mạnh cấp 8, giật cấp 10, s&oacute;ng biển cao 3-4,5m. Biển động mạnh.</p>\r\n<p>T&agrave;u thuyền hoạt động trong c&aacute;c v&ugrave;ng nguy hiểm n&oacute;i tr&ecirc;n đều c&oacute; khả năng chịu t&aacute;c động của d&ocirc;ng, lốc, gi&oacute; mạnh, s&oacute;ng lớn.</p>\r\n<p>Th&ocirc;ng tin về cơn b&atilde;o Ragasa, cơ quan kh&iacute; tượng cho biết, l&uacute;c 19h ng&agrave;y 19/9 b&atilde;o c&aacute;ch đảo Lu D&ocirc;ng (Philippines) khoảng 820km về ph&iacute;a Đ&ocirc;ng.</p>\r\n<p>Sức gi&oacute; mạnh nhất v&ugrave;ng gần t&acirc;m b&atilde;o cấp 9 (75-88km/h), giật cấp 11. B&atilde;o Ragasa di chuyển theo hướng T&acirc;y T&acirc;y Bắc với tốc độ khoảng 15km/h.</p>\r\n<p>Đến tối 20/9, b&atilde;o Ragasa c&aacute;ch đảo Lu D&ocirc;ng (Philippines) khoảng 680km về ph&iacute;a Đ&ocirc;ng. Sức gi&oacute; v&ugrave;ng gần t&acirc;m b&atilde;o cấp 11, giật cấp 13; di chuyển theo hướng T&acirc;y Bắc, mỗi giờ đi được khoảng 10km v&agrave; c&oacute; khả năng mạnh th&ecirc;m.</p>', '2025-09-19 22:13:31.000000', '(Dân trí) - Dự báo từ ngày 23/9 khu vực Bắc Biển Đông có thể chịu ảnh hưởng của bão Ragasa với cường độ mạnh nhất có thể lên tới cấp 14-16, giật trên cấp 17, sóng biển cao trên 10m.', b'1', 'https://res.cloudinary.com/dioi8edng/image/upload/v1758294810/blog-posts/file_1758294800526.png.png', b'1', '2025-09-19 22:13:31.000000', 1, 'bien-ong-sap-chiu-anh-huong-cua-bao-ragasa-co-gio-giat-tren-cap-17', 'Biển Đông sắp chịu ảnh hưởng của bão Ragasa, có gió giật trên cấp 17', '2025-09-23 15:54:17.000000', 3, 10),
(2, '<p>Bộ Nội vụ vừa c&oacute; c&ocirc;ng văn gửi UBND c&aacute;c tỉnh, th&agrave;nh phố trực thuộc Trung ương, đề nghị khẩn trương r&agrave; so&aacute;t, x&acirc;y dựng phương &aacute;n sắp xếp c&aacute;c đơn vị sự nghiệp c&ocirc;ng lập.&nbsp;</p>\r\n<p><strong>Giải thể c&aacute;c đơn vị yếu k&eacute;m</strong></p>\r\n<p>Theo c&ocirc;ng văn, c&aacute;c địa phương phải chủ động x&acirc;y dựng phương &aacute;n sắp xếp theo từng ng&agrave;nh, lĩnh vực, đảm bảo ph&ugrave; hợp với định hướng tại C&ocirc;ng văn số 59-CV/BCĐ của Ban Chỉ đạo Trung ương tổng kết Nghị quyết 18-NQ/TW. Bộ Nội vụ lưu &yacute; một số định hướng cụ thể:</p>\r\n<p>Mỗi tỉnh, th&agrave;nh phố kh&ocirc;ng c&oacute; qu&aacute; 3 Ban Quản l&yacute; dự &aacute;n cấp tỉnh; c&oacute; thể lập c&aacute;c ban quản l&yacute; khu vực li&ecirc;n x&atilde;, phường hoặc cấp x&atilde; nếu cần thiết. C&aacute;c Ban quản l&yacute; dự &aacute;n hoạt động theo cơ chế tự chủ t&agrave;i ch&iacute;nh, tự bảo đảm kinh ph&iacute; hoạt động.</p>\r\n<p>C&aacute;c địa phương phải r&agrave; so&aacute;t, sắp xếp lại v&agrave; tinh gọn c&aacute;c đơn vị sự nghiệp c&ocirc;ng lập trực thuộc UBND cấp tỉnh, c&aacute;c sở v&agrave; c&aacute;c tổ chức h&agrave;nh ch&iacute;nh kh&aacute;c thuộc UBND cấp tỉnh; đồng thời cơ cấu lại hoặc giải thể những đơn vị hoạt động k&eacute;m hiệu quả.</p>\r\n<p>B&ecirc;n cạnh đ&oacute;, địa phương chủ động nghi&ecirc;n cứu tổ chức một đơn vị sự nghiệp cấp x&atilde; cung cấp dịch vụ c&ocirc;ng cơ bản, thiết yếu như văn h&oacute;a, thể thao, th&ocirc;ng tin, truyền th&ocirc;ng, m&ocirc;i trường, n&ocirc;ng nghiệp.&nbsp;</p>\r\n<p><strong>Tinh gọn mạng lưới trường c&ocirc;ng lập</strong></p>', '2025-09-19 22:14:41.000000', '(Dân trí) - Bộ Nội vụ đốc thúc các địa phương khẩn trương rà soát, sắp xếp đơn vị sự nghiệp công lập, gửi phương án về Bộ trước ngày 25/9 để tinh gọn, nâng cao hiệu quả hoạt động.', b'0', 'https://res.cloudinary.com/dioi8edng/image/upload/v1758294881/blog-posts/file_1758294879574.jpg.jpg', b'1', '2025-09-19 22:14:41.000000', 1, 'ca-nuoc-se-sap-xep-tinh-gon-he-thong-truong-hoc-benh-vien', 'Cả nước sẽ sắp xếp, tinh gọn hệ thống trường học, bệnh viện', '2025-09-23 14:05:39.000000', 4, 10);

-- --------------------------------------------------------

--
-- Table structure for table `blog_post_tags`
--

CREATE TABLE `blog_post_tags` (
  `blog_post_id` bigint(20) NOT NULL,
  `tag` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `blog_post_tags`
--

INSERT INTO `blog_post_tags` (`blog_post_id`, `tag`) VALUES
(1, 'thời tiết'),
(1, 'tin tức'),
(2, 'tin tức');

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

CREATE TABLE `bookings` (
  `id` bigint(20) NOT NULL,
  `booking_code` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `guest_address` varchar(255) DEFAULT NULL,
  `guest_email` varchar(255) DEFAULT NULL,
  `guest_full_name` varchar(255) DEFAULT NULL,
  `guest_phone` varchar(255) DEFAULT NULL,
  `seat_number` varchar(255) NOT NULL,
  `status` enum('canceled_by_operator','canceled_by_user','completed','confirmed','pending') NOT NULL,
  `total_amount` decimal(38,2) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `agent_accept_booking_id` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `trip_id` bigint(20) NOT NULL,
  `selling_method` enum('OFFLINE','ONLINE') DEFAULT NULL,
  `promotion_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`id`, `booking_code`, `created_at`, `guest_address`, `guest_email`, `guest_full_name`, `guest_phone`, `seat_number`, `status`, `total_amount`, `updated_at`, `agent_accept_booking_id`, `customer_id`, `trip_id`, `selling_method`, `promotion_id`) VALUES
(1, 'BOOK123', '2025-07-24 12:00:00.000000', '456 Nguyễn Trãi, TP.HCM', 'quocla.21it@vku.udn.vn', 'Trần Thị Khách', '09876543212121', 'A1', 'canceled_by_operator', 500000.00, '2025-09-09 07:34:42.288251', NULL, 1, 1, NULL, NULL),
(2, 'BOOK124', '2025-07-24 13:00:00.000000', '34 Hai Bà Trưng, Huế', 'customer2@busify.com', 'Lê Văn Khách', '0976543210', 'C1', 'confirmed', 150000.00, '2025-07-24 13:00:00.000000', NULL, 6, 2, NULL, NULL),
(3, 'BOOK125', '2025-07-24 14:00:00.000000', '56 Nguyễn Huệ, Hà Nội', 'dg@busify.com', 'Phạm Thị Hành Khách', '0965432109', 'E1', 'canceled_by_operator', 250000.00, '2025-08-29 16:41:03.141416', NULL, 7, 3, NULL, NULL),
(4, 'BOOK126', '2025-07-24 15:00:00.000000', '90 Lý Thường Kiệt, Cần Thơ', 'quocla.21it@vku.udn.vn', 'Hoàng Văn Khách', '0954321098', 'C1', 'canceled_by_operator', 200000.00, '2025-08-25 06:57:50.659488', NULL, 8, 4, NULL, NULL),
(5, 'BOOK127', '2025-07-24 16:00:00.000000', '34 Hai Bà Trưng, Huế', 'customer2@busify.com', 'Lê Văn Khách', '0976543210', 'E1', 'confirmed', 220000.00, '2025-07-24 16:00:00.000000', NULL, 6, 5, NULL, NULL),
(6, 'BOOKING-1c96d426-e2ce-445a-b21e-6ee83fb3fdce', '2025-08-06 08:05:36.502850', '34 Hai Bà Trưng, Huế', 'hoaicoder260@gmail.com', 'Lê Văn Khách', '0976543210', 'A10, B13, A06', 'canceled_by_operator', 120000.00, '2025-09-09 07:34:32.014341', NULL, 18, 1, NULL, NULL),
(12, 'BOOKING-16dd4dca-4aea-45cb-8cf2-30a532792614', '2025-08-08 03:47:48.767281', NULL, 'antoin2901@gmail.com', NULL, NULL, 'A01', 'confirmed', 120000.00, '2025-08-21 03:52:31.253267', NULL, 18, 1, NULL, NULL),
(13, 'BOOKING-04382de0-3e47-4283-9dda-f5fd9e3d49bc', '2025-08-08 08:03:03.723501', NULL, NULL, NULL, NULL, 'B10', 'confirmed', 120000.00, '2025-08-18 05:43:27.230371', NULL, 18, 1, NULL, NULL),
(14, 'BOOKING-442dc346-2608-490e-8a91-4aefa187bf7a', '2025-08-18 04:38:45.121033', 'Đà Nẵng', 'antoinnguyen95@gmail.com', 'atoan', '0123456789', 'A.1.1', 'canceled_by_operator', 330000.00, '2025-09-11 17:00:15.661521', NULL, 18, 10, NULL, NULL),
(15, 'BOOKING-3a70f601-bcd7-4500-8630-0a2a0d7e4f27', '2025-08-18 05:21:14.161277', NULL, NULL, NULL, NULL, 'A.1.1', 'confirmed', 330000.00, '2025-08-18 05:22:08.586131', NULL, 18, 10, NULL, NULL),
(16, 'BOOKING-cc8e9321-20bc-4fa2-b912-099875efcc6c', '2025-08-18 05:25:03.865563', NULL, NULL, NULL, NULL, 'A.1.1', 'confirmed', 330000.00, '2025-08-18 05:26:06.621016', NULL, 18, 10, NULL, NULL),
(17, 'BOOKING-c8b6cd9e-6102-406b-851b-692ef37884e8', '2025-08-18 05:31:14.584756', NULL, NULL, NULL, NULL, 'A.1.1,B.1.1', 'confirmed', 660000.00, '2025-08-18 05:31:50.847952', NULL, 18, 10, NULL, NULL),
(18, 'BOOKING-10c6f28f-f940-4f8e-81a5-efadf2bd6cf4', '2025-08-18 05:37:04.580158', NULL, NULL, NULL, NULL, 'A.1.1,B.1.1', 'confirmed', 660000.00, '2025-08-18 05:37:30.028585', NULL, 18, 10, NULL, NULL),
(19, 'BOOKING-c722752f-fe81-4d74-952d-800044b1d71b', '2025-08-18 05:47:26.915259', NULL, NULL, NULL, NULL, 'A.1.1,B.1.1', 'confirmed', 660000.00, '2025-08-18 05:47:57.818202', NULL, 18, 10, NULL, NULL),
(20, 'BOOKING-57164a37-70a0-4313-8c13-18544a948e30', '2025-08-18 05:55:48.486000', NULL, NULL, NULL, NULL, 'A.1.1,B.1.1', 'confirmed', 660000.00, '2025-08-18 05:56:17.624102', NULL, 18, 10, NULL, NULL),
(21, 'BOOKING-5ef24ffb-fa30-4a1b-b923-28e2b41d98e8', '2025-08-18 06:02:59.355842', NULL, NULL, NULL, NULL, 'A.1.1,B.1.1', 'confirmed', 660000.00, '2025-08-18 06:03:27.902155', NULL, 18, 10, NULL, NULL),
(22, 'BOOKING-e96e0c7f-3fba-4b86-8a11-b8c8c1a3f65a', '2025-08-18 07:00:02.953999', NULL, NULL, NULL, NULL, 'A.1.1,B.1.1', 'confirmed', 660000.00, '2025-08-18 07:01:10.256390', NULL, 18, 10, NULL, NULL),
(23, 'BA458E', '2025-08-20 15:03:03.747601', NULL, NULL, NULL, NULL, 'B10', 'pending', 120000.00, '2025-08-20 15:03:03.747601', NULL, 18, 1, NULL, NULL),
(24, 'DB3A45', '2025-08-25 09:43:01.417240', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'A.1.1', 'confirmed', 156000.00, '2025-08-25 09:44:01.692598', NULL, 18, 20, NULL, NULL),
(25, '1E9F93', '2025-08-25 09:45:51.344082', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'A.1.1', 'confirmed', 156000.00, '2025-08-25 09:46:23.795265', NULL, 18, 20, NULL, NULL),
(26, '26440B', '2025-08-26 07:10:00.664089', NULL, 'hoaicalm@gmail.com', 'Hoai', '0912312311', 'B.2.1', 'confirmed', 831000.00, '2025-08-26 07:10:55.458804', NULL, 26, 23, NULL, NULL),
(27, '5334B3', '2025-08-26 14:30:00.149733', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'C.5.1', 'canceled_by_operator', 234000.00, '2025-09-10 03:06:07.097537', NULL, 18, 24, NULL, NULL),
(28, 'E6CB23', '2025-08-26 14:34:18.947540', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'B.10.1', 'completed', 234000.00, '2025-08-26 14:34:45.081122', NULL, 18, 24, NULL, NULL),
(29, '3C8202', '2025-08-26 16:23:19.557571', '123 Le Loi, Da Nang', 'vana@example.com', 'Nguyen Van B', '0905123456', 'A.1.2', 'completed', 250000.00, '2025-08-26 16:23:19.557571', NULL, 34, 24, NULL, NULL),
(30, 'A18503', '2025-08-26 16:28:34.962273', '123 Le Loi, Da Nang', 'vana@example.com', 'Nguyen Van B', '0905123456', 'A.10.1', 'canceled_by_user', 250000.00, '2025-08-31 04:49:00.155789', NULL, 34, 24, NULL, NULL),
(31, '09A5F0', '2025-08-26 16:40:25.164260', '123 Le Loi, Da Nang', 'vana@example.com', 'Nguyen Van B', '0905123456', 'A.10.1', 'canceled_by_user', 250000.00, '2025-08-31 04:50:18.304651', NULL, 34, 24, NULL, NULL),
(32, '4DF78A', '2025-08-26 16:41:18.946141', '123 Le Loi, Da Nang', 'vana@example.com', 'Nguyen Van B', '0905123456', 'A.10.1', 'canceled_by_user', 250000.00, '2025-09-05 04:48:37.076190', NULL, 34, 25, NULL, NULL),
(33, '20A344', '2025-08-26 16:47:37.388076', NULL, 'duongnt.21it@vku.udn.vn', 'Nguyen To Duong-CUSTOMER', '0328199217', 'A.2.1', 'completed', 234000.00, '2025-08-26 16:48:36.163633', NULL, 25, 24, NULL, NULL),
(34, 'C82F96', '2025-08-26 16:53:32.018412', '123 Le Loi, Da Nang', 'vana@example.com', 'Nguyen Van B', '0905123456', 'A.10.2', 'canceled_by_operator', 250000.00, '2025-08-26 16:54:32.758664', NULL, 26, 24, NULL, NULL),
(35, '10A01A', '2025-08-26 17:01:42.110604', NULL, 'huyboss@gmail.com', 'Nguyễn dg', '1234567893', 'A.9.1', 'canceled_by_operator', 234000.00, '2025-09-10 03:06:07.759357', NULL, 25, 24, NULL, NULL),
(36, 'EF5757', '2025-08-26 17:08:13.771527', NULL, 'huyboss@gmail.com', 'Nguyễn dg', '1234567893', 'A.9.1', 'completed', 234000.00, '2025-08-26 17:08:51.661655', NULL, 25, 24, NULL, NULL),
(37, 'C74CC9', '2025-08-26 17:13:19.440578', NULL, 'huyboss@gmail.com', 'Nguyễn dg', '1234567893', 'A.9.1', 'completed', 234000.00, '2025-08-26 17:14:03.865078', NULL, 25, 24, NULL, NULL),
(38, '680C83', '2025-08-27 02:18:23.460190', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'A.5.2', 'canceled_by_operator', 234000.00, '2025-08-27 02:33:24.484248', NULL, 26, 24, NULL, NULL),
(39, '75421F', '2025-08-27 06:34:55.459193', '123 Le Loi, Da Nang', 'vana@example.com', 'Nguyen Van B', '0905123456', 'A.10.2', 'pending', 250000.00, '2025-08-27 06:34:55.459193', NULL, 26, 24, NULL, NULL),
(40, '47B444', '2025-08-27 06:50:24.393247', '123 Le Loi, Da Nang', 'vana@example.com', 'Nguyen Van B', '0905123456', 'A.4.1', 'pending', 250000.00, '2025-08-27 06:50:24.393247', NULL, 26, 26, NULL, NULL),
(41, '5A119B', '2025-08-27 07:25:26.658296', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'B.5.1,C.5.1', 'pending', 4152733.00, '2025-08-27 07:25:26.658296', NULL, 26, 19, NULL, NULL),
(47, 'BF85E0', '2025-08-27 08:22:28.358842', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'B.3.1,C.3.1', 'pending', 4908668.00, '2025-08-27 08:22:28.358842', NULL, 26, 19, NULL, NULL),
(48, '9CCCCB', '2025-08-27 08:23:59.175787', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'B.3.1,C.3.1', 'pending', 4908668.00, '2025-08-27 08:23:59.175787', NULL, 26, 19, NULL, NULL),
(49, '473014', '2025-08-27 08:24:29.156110', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'B.8.1', 'canceled_by_operator', 2454334.00, '2025-09-10 03:06:07.759357', NULL, 26, 19, NULL, NULL),
(51, '16B1DE', '2025-08-27 08:46:15.753165', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'C.5.1,B.10.1', 'confirmed', 4908668.00, '2025-08-27 08:46:58.369146', NULL, 18, 19, NULL, NULL),
(52, 'F70C4D', '2025-08-27 08:47:06.037990', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'C.6.1,C.5.1', 'pending', 4908668.00, '2025-08-27 08:47:06.037990', NULL, 26, 19, NULL, NULL),
(53, '094A1C', '2025-08-27 08:53:59.587482', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'B.3.1,C.3.1', 'canceled_by_operator', 4908668.00, '2025-08-27 09:09:00.665597', NULL, 26, 19, NULL, NULL),
(55, 'D35938', '2025-08-27 09:14:15.029980', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'C.7.1,D.7.1', 'confirmed', 3926934.00, '2025-08-27 09:16:35.107941', NULL, 26, 19, NULL, NULL),
(57, '8B58C6', '2025-08-27 13:48:16.971202', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'B.4.1,C.4.1', 'pending', 3926934.00, '2025-08-27 13:48:16.971202', NULL, 26, 19, NULL, NULL),
(59, '31EEA8', '2025-08-27 13:54:08.964162', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'B.4.1,C.4.1', 'pending', 3926934.00, '2025-08-27 13:54:08.964162', NULL, 26, 19, NULL, NULL),
(60, 'D83981', '2025-08-27 14:00:09.994623', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'B.3.1,C.3.1', 'canceled_by_operator', 3926934.00, '2025-09-10 03:06:07.759357', NULL, 26, 19, NULL, NULL),
(61, 'CC5266', '2025-08-27 14:27:48.674295', NULL, 'duongnt.21it@vku.udn.vn', 'Nguyen To Duong', '0328199217', 'B.7.1', 'confirmed', 2454334.00, '2025-08-27 14:35:27.079144', NULL, 24, 19, NULL, NULL),
(63, 'CC8321', '2025-08-27 14:43:16.280764', NULL, 'huyboss@gmail.com', 'Nguyễn dg', '1234567891', 'A.10.1', 'canceled_by_operator', 2454334.00, '2025-09-10 03:06:07.759357', NULL, 24, 19, NULL, NULL),
(64, '8BA7EA', '2025-08-27 15:09:07.257370', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'B.4.1,C.4.1', 'canceled_by_operator', 4908668.00, '2025-09-10 03:06:07.759357', NULL, 26, 19, NULL, NULL),
(66, 'A3CF1F', '2025-08-27 15:22:44.499209', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0356138252', 'B.9.2,C.9.2', 'confirmed', 3908668.00, '2025-08-27 15:26:27.542494', NULL, 26, 19, NULL, NULL),
(67, 'C4198D', '2025-08-28 05:16:26.031920', NULL, 'antoin2901@gmail.com', 'Nguyễn An Toàn', '0123456789', 'B.10.1,C.10.1', 'completed', 300000.00, '2025-08-28 05:17:29.328410', NULL, 36, 2, NULL, NULL),
(68, 'B4CE19', '2025-08-28 06:18:51.792235', NULL, 'antoin2901@gmail.com', 'Nguyễn An Toàn', '0123456789', 'B.8.1', 'confirmed', 150000.00, '2025-08-28 06:19:41.196403', NULL, 36, 2, NULL, NULL),
(69, '5F7808', '2025-08-28 06:26:28.797823', NULL, 'antoin2901@gmail.com', 'Nguyễn An Toàn', '0123456789', 'C.8.1', 'confirmed', 150000.00, '2025-08-28 06:27:04.174788', NULL, 36, 2, NULL, NULL),
(70, '590624', '2025-08-28 06:30:03.037296', NULL, 'antoin2901@gmail.com', 'Nguyễn An Toàn', '0123456789', 'D.7.1', 'confirmed', 150000.00, '2025-08-28 06:30:45.931567', NULL, 36, 2, NULL, NULL),
(71, 'FFBB4F', '2025-08-28 06:43:12.116825', NULL, 'antoin2901@gmail.com', 'Nguyễn An Toàn', '0123456789', 'A.6.1', 'confirmed', 150000.00, '2025-08-28 06:43:48.851998', NULL, 36, 2, NULL, NULL),
(72, '169669', '2025-08-28 06:47:03.519831', NULL, 'antoin2901@gmail.com', 'Nguyễn An Toàn', '0123456789', 'C.5.1', 'confirmed', 150000.00, '2025-08-28 06:47:38.170017', NULL, 36, 2, NULL, NULL),
(73, 'CEED7C', '2025-08-28 06:57:22.527890', NULL, 'antoin2901@gmail.com', 'Nguyễn An Toàn', '0123456789', 'B.5.1', 'confirmed', 150000.00, '2025-08-28 06:58:00.932174', NULL, 36, 2, NULL, NULL),
(74, '055B28', '2025-08-28 07:20:31.107923', NULL, 'antoin2901@gmail.com', 'Nguyễn An Toàn', '0123456789', 'A.1.1', 'canceled_by_operator', 150000.00, '2025-09-10 03:06:07.759357', NULL, 36, 2, NULL, NULL),
(75, '1BC9CE', '2025-08-28 07:20:43.343353', NULL, 'antoin2901@gmail.com', 'Nguyễn An Toàn', '0123456789', 'A.1.1,B.1.1', 'confirmed', 300000.00, '2025-08-28 07:21:18.532563', NULL, 36, 2, NULL, NULL),
(76, 'D0DFC8', '2025-08-28 07:37:32.793700', NULL, 'antoin2901@gmail.com', 'Nguyễn An Toàn', '0123456789', 'C.1.1,D.1.1', 'canceled_by_operator', 300000.00, '2025-09-10 03:06:07.759357', NULL, 36, 2, NULL, NULL),
(77, 'EB1C47', '2025-08-28 07:38:11.336233', NULL, 'antoin2901@gmail.com', 'Nguyễn An Toàn', '0123456789', 'C.1.1,D.1.1', 'confirmed', 300000.00, '2025-08-28 07:39:48.242533', NULL, 36, 2, NULL, NULL),
(78, '4AF762', '2025-08-28 07:45:07.853039', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'A.2.1,B.2.1', 'confirmed', 300000.00, '2025-08-28 07:45:36.886022', NULL, 18, 2, NULL, NULL),
(79, '03D3D6', '2025-08-28 07:56:44.838708', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'C.2.1,D.2.1', 'confirmed', 300000.00, '2025-08-28 07:57:17.654605', NULL, 18, 2, NULL, NULL),
(80, 'F46969', '2025-08-28 14:02:50.667006', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'B.3.1,C.3.1', 'confirmed', 300000.00, '2025-08-28 14:03:28.853617', NULL, 18, 2, NULL, NULL),
(81, '741486', '2025-08-28 14:14:24.901679', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'C.9.1,B.9.1', 'confirmed', 300000.00, '2025-08-28 14:14:55.701074', NULL, 18, 2, NULL, NULL),
(82, '0041D4', '2025-08-28 14:16:50.204666', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'B.6.1,C.6.1', 'confirmed', 300000.00, '2025-08-28 14:17:28.628534', NULL, 18, 2, NULL, NULL),
(83, 'EAF87E', '2025-08-28 14:51:43.012451', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'B.7.1,C.7.1', 'confirmed', 300000.00, '2025-08-28 14:52:22.989301', NULL, 18, 2, NULL, NULL),
(84, '658ED3', '2025-08-29 02:16:49.072598', NULL, 'admin@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.10.1,B.10.1', 'pending', 300000.00, '2025-08-29 02:16:49.072598', NULL, 25, 2, NULL, NULL),
(85, '850A41', '2025-08-29 02:16:55.930583', NULL, 'admin@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.10.1,B.10.1', 'pending', 300000.00, '2025-08-29 02:16:55.930583', NULL, 25, 2, NULL, NULL),
(86, '9D70AD', '2025-08-29 02:17:01.041727', NULL, 'admin@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.10.1,B.10.1', 'pending', 300000.00, '2025-08-29 02:17:01.041727', NULL, 25, 2, NULL, NULL),
(87, 'E67EDF', '2025-08-29 02:17:07.036219', NULL, 'admin@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.10.1,B.10.1', 'pending', 300000.00, '2025-08-29 02:17:07.036219', NULL, 25, 2, NULL, NULL),
(88, '19F14C', '2025-08-29 02:17:08.144662', NULL, 'admin@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.10.1,B.10.1', 'pending', 300000.00, '2025-08-29 02:17:08.144662', NULL, 25, 2, NULL, NULL),
(89, 'A968BD', '2025-08-29 02:17:28.330446', NULL, 'admin@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.10.1,B.10.1', 'pending', 300000.00, '2025-08-29 02:17:28.330446', NULL, 25, 2, NULL, NULL),
(90, 'FD35C6', '2025-08-29 02:21:04.798214', NULL, 'stu@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.10.1', 'canceled_by_operator', 150000.00, '2025-09-10 03:06:07.759357', NULL, 25, 2, NULL, NULL),
(91, '517B30', '2025-08-29 02:21:59.141246', NULL, 'duongnguyen4203@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.10.2,B.10.2', 'pending', 300000.00, '2025-08-29 02:21:59.141246', NULL, 25, 2, NULL, NULL),
(92, '08CF41', '2025-08-29 02:22:01.486548', NULL, 'duongnguyen4203@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.10.2,B.10.2', 'pending', 300000.00, '2025-08-29 02:22:01.486548', NULL, 25, 2, NULL, NULL),
(93, 'E3267C', '2025-08-29 02:22:05.787809', NULL, 'duongnguyen4203@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.10.2,B.10.2', 'pending', 300000.00, '2025-08-29 02:22:05.787809', NULL, 25, 2, NULL, NULL),
(94, '4C2298', '2025-08-29 02:22:25.389473', NULL, 'duongnguyen4203@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.1.1,B.1.1', 'pending', 300000.00, '2025-08-29 02:22:25.389473', NULL, 25, 2, NULL, NULL),
(95, '22D9CF', '2025-08-29 02:34:49.792419', NULL, 'duongnguyen4203@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.1.1,B.1.1', 'confirmed', 300000.00, '2025-08-29 02:36:11.018624', NULL, 25, 2, NULL, NULL),
(96, 'D33E2C', '2025-08-29 05:31:46.479871', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'B.7.1,C.7.1', 'confirmed', 246000.00, '2025-08-29 05:32:32.087477', NULL, 18, 28, NULL, NULL),
(97, '0FB31C', '2025-08-29 05:36:25.531509', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'B.5.1,C.5.1', 'completed', 500000.00, '2025-08-29 05:37:00.406426', NULL, 18, 3, NULL, NULL),
(98, '8361AE', '2025-08-29 09:59:43.566277', NULL, 'admin@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'B.10.1,C.10.1', 'confirmed', 300000.00, '2025-08-29 10:00:21.964150', NULL, 25, 2, NULL, NULL),
(99, 'A7E366', '2025-08-29 14:10:32.744289', NULL, 'duongnguyen4203@gmail.com', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'A.10.1', 'canceled_by_operator', 150000.00, '2025-08-29 14:25:33.382706', NULL, 31, 2, NULL, NULL),
(100, 'ECDFDE', '2025-08-29 15:28:10.998196', NULL, 'duongnt.21it@vku.udn.vn', 'dn', '0328199217', 'A.10.1', 'canceled_by_operator', 250000.00, '2025-09-10 03:06:07.945792', NULL, 31, 2, NULL, NULL),
(101, 'A2938F', '2025-08-29 15:28:51.218923', NULL, 'duongnguyen0023@gmail.com', 'NGUYỄN TÔ DƯƠNG', '1234567891', 'B.10.1', 'canceled_by_operator', 212500.00, '2025-09-10 03:06:07.945792', NULL, 31, 2, NULL, NULL),
(137, 'EC692B', '2025-09-04 07:49:05.090413', NULL, 'hoai19800@gmail.com', 'HV HHoai', '0901234567', 'B.7.1', 'canceled_by_operator', 187200.00, '2025-09-05 01:52:19.437254', NULL, 28, 24, NULL, NULL),
(146, '22448B', '2025-09-04 14:35:15.009098', NULL, 'hoai19800@gmail.com', 'hoàng hoài', '0356138252', 'D.7.1', 'canceled_by_operator', 187200.00, '2025-09-04 14:50:15.217471', NULL, 28, 24, NULL, NULL),
(147, '3B6218', '2025-09-04 14:42:12.061109', NULL, 'hoaicalm@gmail.com', 'hoàng văn hoài', '0356138252', 'C.6.1', 'canceled_by_operator', 187200.00, '2025-09-04 14:57:12.269068', NULL, 26, 24, NULL, NULL),
(150, '84D9F7', '2025-09-04 16:38:59.580022', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'D.7.1', 'completed', 232000.00, '2025-09-04 16:42:12.377179', NULL, 18, 24, NULL, NULL),
(151, '3203A8', '2025-09-04 19:51:53.299546', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'D.6.1', 'canceled_by_operator', 232000.00, '2025-09-10 03:06:07.999721', NULL, 18, 24, NULL, NULL),
(152, '03143E', '2025-09-05 01:57:58.087765', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'C.8.1', 'canceled_by_operator', 234000.00, '2025-09-05 02:14:19.913332', NULL, 28, 24, NULL, NULL),
(153, '20D7EB', '2025-09-05 02:28:08.393786', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'C.8.1', 'canceled_by_operator', 231000.00, '2025-09-05 02:43:08.852594', NULL, 18, 24, NULL, NULL),
(154, 'FFC1CE', '2025-09-05 07:40:40.764353', NULL, 'nguyentoduong.2003@gmail.com', 'Duong', '0961724356', 'A.10.1', 'completed', 234000.00, '2025-09-05 07:41:22.869898', NULL, 31, 24, NULL, NULL),
(155, 'C53CEA', '2025-09-05 07:51:28.240693', NULL, 'nguyentoduong.2003@gmail.com', 'Duong', '0123456789', 'A.10.1', 'completed', 250000.00, '2025-09-05 07:52:03.830631', NULL, 31, 3, NULL, NULL),
(156, '076654', '2025-09-05 14:14:35.875420', NULL, 'antoinnguyen95@gmail.com', 'Antoin', '0123456789', 'C.10.1,D.10.1', 'completed', 370400.00, '2025-09-05 14:15:38.087741', NULL, 18, 24, NULL, NULL),
(157, '1C887D', '2025-09-07 16:10:46.830271', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'B.8.1,C.8.1', 'canceled_by_operator', 299520.00, '2025-09-10 03:06:08.042097', NULL, 28, 24, 'ONLINE', 2),
(158, '6DB104', '2025-09-09 14:32:18.540413', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'B.5.1,C.5.1', 'canceled_by_operator', 427280.00, '2025-09-09 14:47:18.698061', NULL, 28, 25, 'ONLINE', NULL),
(159, '2B635C', '2025-09-09 15:01:32.596381', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'B.9.1,C.9.1', 'canceled_by_operator', 697600.00, '2025-09-10 15:05:51.225654', NULL, 28, 25, 'ONLINE', NULL),
(160, '173C9B', '2025-09-09 15:04:36.168492', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'B.4.1,C.4.1', 'pending', 697600.00, '2025-09-09 15:04:36.168492', NULL, 28, 25, 'ONLINE', NULL),
(161, '75A379', '2025-09-09 17:36:07.158660', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'B.3.1,C.3.1', 'pending', 436000.00, '2025-09-09 17:36:07.158660', NULL, 28, 25, 'ONLINE', NULL),
(162, '4350BB', '2025-09-10 04:35:37.029637', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'C.6.1,D.6.1', 'pending', 697600.00, '2025-09-10 04:35:37.029637', NULL, 28, 25, 'ONLINE', NULL),
(163, '920F8A', '2025-09-10 05:55:06.149637', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.6.1,B.6.1', 'canceled_by_operator', 784800.00, '2025-09-10 06:28:48.073011', NULL, 28, 25, 'ONLINE', NULL),
(164, 'D4A19F', '2025-09-10 06:09:57.963154', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'B.8.1,C.8.1', 'pending', 872000.00, '2025-09-10 06:09:57.963154', NULL, 28, 25, 'ONLINE', NULL),
(165, '24DD4B', '2025-09-07 06:18:01.580657', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'C.7.1,D.7.1', 'completed', 872000.00, '2025-09-10 06:18:29.972484', NULL, 28, 25, 'ONLINE', NULL),
(166, 'E09FE1', '2025-09-10 06:20:03.552776', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.7.1,B.7.1', 'completed', 610400.00, '2025-09-10 06:20:36.906824', NULL, 28, 25, 'ONLINE', NULL),
(167, 'BA38C0', '2025-09-10 06:29:49.303294', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.10.1,B.10.1', 'completed', 697600.00, '2025-09-10 06:30:27.611311', NULL, 28, 25, 'ONLINE', NULL),
(168, '50157C', '2025-09-10 08:32:21.022081', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'A.10.1,B.10.1', 'canceled_by_operator', 307000.00, '2025-09-10 08:47:21.453338', NULL, 26, 31, 'ONLINE', NULL),
(169, '7E4405', '2025-09-10 08:33:37.103283', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'B.9.1,C.9.1', 'canceled_by_user', 350000.00, '2025-09-11 07:28:18.492091', NULL, 26, 31, 'ONLINE', NULL),
(170, '0472CE', '2025-09-10 08:55:42.069555', NULL, 'nguyentoduong.2003@gmail.com', 'test1', '0123456789', 'B.10.1', 'canceled_by_operator', 234000.00, '2025-09-10 09:13:24.811198', NULL, 31, 24, 'ONLINE', NULL),
(171, '301D82', '2025-09-10 14:28:48.546508', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'A.6.1,B.6.1', 'canceled_by_operator', 2698000.00, '2025-09-10 14:43:48.815355', NULL, 26, 34, 'ONLINE', NULL),
(172, '674C61', '2025-09-10 15:13:52.924055', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.1.1,B.1.1,C.1.1', 'completed', 300000.00, '2025-09-10 15:14:36.109152', NULL, 28, 38, 'ONLINE', NULL),
(173, '83B677', '2025-09-10 15:30:45.036606', NULL, 'nguyentoduong.2003@gmail.com', 'dg', '0123456789', 'A.2.1,B.2.1', 'canceled_by_user', 200000.00, '2025-09-10 16:32:17.022814', NULL, 31, 38, 'ONLINE', NULL),
(174, 'F80DD9', '2025-09-10 16:58:51.063719', NULL, 'nguyentoduong.2003@gmail.com', 'dg', '0123456789', 'A.1.1,B.1.1,C.1.1,D.1.1', 'completed', 2000000.00, '2025-09-10 16:59:27.321133', NULL, 31, 39, 'ONLINE', NULL),
(175, '174858', '2025-09-11 03:09:52.513331', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.6.1,B.6.1,C.6.1', 'canceled_by_operator', 4500000.00, '2025-09-11 06:58:19.292051', NULL, 28, 34, 'ONLINE', NULL),
(176, 'B9A382', '2025-09-11 03:41:19.560569', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.4.1,B.4.1,C.4.1,D.4.1', 'canceled_by_operator', 6000000.00, '2025-09-11 06:16:54.032023', NULL, 28, 34, 'ONLINE', NULL),
(177, '89FABF', '2025-09-11 03:43:29.347362', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.9.1,B.9.1,C.9.1,D.9.1', 'canceled_by_operator', 6000000.00, '2025-09-11 06:20:24.671002', NULL, 28, 34, 'ONLINE', NULL),
(178, '1B32B6', '2025-09-11 06:04:53.947777', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.8.1,C.8.1,B.8.1,D.8.1', 'canceled_by_operator', 6000000.00, '2025-09-11 06:06:30.974921', NULL, 28, 34, 'ONLINE', NULL),
(179, 'D09232', '2025-09-11 06:49:26.866971', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'B.8.1,C.8.1,D.8.1,B.7.1,C.7.1', 'canceled_by_operator', 7500000.00, '2025-09-11 06:50:51.256031', NULL, 28, 34, 'ONLINE', NULL),
(180, '62F108', '2025-09-11 06:56:23.625455', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.9.1,B.9.1,C.9.1', 'canceled_by_operator', 300000.00, '2025-09-11 07:03:36.429825', NULL, 28, 38, 'ONLINE', NULL),
(181, '54E432', '2025-09-11 07:14:18.458520', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.8.1,B.8.1,C.8.1,D.8.1,D.9.1,D.10.1,C.10.1,B.10.1,A.10.1', 'canceled_by_operator', 900000.00, '2025-09-11 07:26:19.915248', NULL, 28, 38, 'ONLINE', NULL),
(182, '46770D', '2025-09-11 07:17:04.574564', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.6.1,B.6.1,C.6.1,D.6.1', 'confirmed', 400000.00, '2025-09-11 07:17:35.488184', NULL, 28, 38, 'ONLINE', NULL),
(183, '094D35', '2025-09-11 07:18:27.885199', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.5.1,B.5.1,C.5.1,D.5.1', 'canceled_by_operator', 400000.00, '2025-09-11 07:23:55.324885', NULL, 28, 38, 'ONLINE', NULL),
(184, '8123B5', '2025-09-11 07:27:05.114989', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'A.2.1,B.2.1,C.2.1', 'canceled_by_user', 300000.00, '2025-09-11 14:43:01.809056', NULL, 26, 38, 'ONLINE', NULL),
(185, 'C328DB', '2025-09-11 14:32:52.353260', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'A.5.1,B.5.1', 'completed', 697600.00, '2025-09-11 14:33:20.260194', NULL, 26, 25, 'ONLINE', 15),
(186, '3314EB', '2025-09-11 14:44:04.325346', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'A.5.1,B.5.1', 'canceled_by_user', 1780000.00, '2025-09-11 14:45:27.043192', NULL, 26, 33, 'ONLINE', NULL),
(187, '188BC8', '2025-09-11 14:54:05.344883', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'B.4.1,C.4.1', 'canceled_by_user', 200000.00, '2025-09-11 14:55:12.028266', NULL, 26, 38, 'ONLINE', NULL),
(188, '42A9C2', '2025-09-11 15:46:35.434013', NULL, 'hoaicalm@gmail.com', 'Hoai', '0123412341', 'B.3.1, C.5.1, B.5.1', 'canceled_by_operator', 3600000.00, '2025-09-11 16:01:35.535179', NULL, NULL, 36, NULL, NULL),
(189, '127E6C', '2025-09-11 15:56:34.666939', NULL, 'nguyentoduong.2003@gmail.com', 'dg', '0123456789', 'A.10.1', 'canceled_by_user', 623000.00, '2025-09-11 17:26:09.164860', NULL, 31, 33, 'ONLINE', NULL),
(190, '2363EB', '2025-09-11 16:12:51.154250', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'A.7.1', 'canceled_by_user', 100000.00, '2025-09-11 16:14:53.300113', NULL, 26, 38, 'ONLINE', NULL),
(191, '0CACAC', '2025-09-11 16:24:35.358519', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'B.3.1,C.3.1', 'canceled_by_user', 200000.00, '2025-09-11 16:25:27.921801', NULL, 26, 38, 'ONLINE', NULL),
(192, 'C99A1E', '2025-09-11 16:29:14.510972', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'C.5.1,D.5.1', 'canceled_by_user', 872000.00, '2025-09-11 16:30:25.281867', NULL, 26, 25, 'ONLINE', NULL),
(193, 'D6D064', '2025-09-11 16:35:23.408221', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'C.5.1,D.5.1', 'canceled_by_user', 872000.00, '2025-09-11 16:36:18.641001', NULL, 26, 25, 'ONLINE', NULL),
(194, '46AA23', '2025-09-15 07:13:20.985331', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'B.5.1,C.5.1', 'canceled_by_operator', 900000.00, '2025-09-15 07:28:21.557959', NULL, 26, 39, 'ONLINE', NULL),
(205, '5FA5C0', '2025-09-16 14:44:29.231288', 'Đà Nẵng', 'antoinnguyen95@gmail.com', 'atoan', '0123456789', 'A.10.1, B.10.1', 'confirmed', 3800000.00, '2025-09-16 14:44:30.503198', NULL, NULL, 37, 'OFFLINE', NULL),
(206, '970F7C', '2025-09-17 09:05:12.290431', 'Đà Nẵng', 'antoin2901@gmail.com', 'An Dương', '0123456789', 'A.10.1, B.10.1', 'canceled_by_operator', 400000.00, '2025-09-17 09:20:12.531098', NULL, NULL, 42, 'OFFLINE', NULL),
(208, '2853AB', '2025-09-17 17:28:28.960415', NULL, 'nguyentoduong.2003@gmail.com', 'dg', '0123456789', 'B.3.1', 'canceled_by_operator', 960000.00, '2025-09-17 18:02:18.636180', NULL, 31, 36, 'ONLINE', NULL),
(209, '6F6B13', '2025-09-17 17:31:14.438264', NULL, 'nguyentoduong.2003@gmail.com', 'dg', '0123456789', 'B.1.1', 'canceled_by_operator', 409500.00, '2025-09-17 18:02:18.739680', NULL, 31, 40, 'ONLINE', NULL),
(210, 'F31DE9', '2025-09-17 18:36:06.918307', NULL, 'nguyentoduong.2003@gmail.com', 'dg', '0123456789', 'A.2.1', 'canceled_by_operator', 500000.00, '2025-09-17 20:19:19.494092', NULL, 31, 39, 'ONLINE', NULL),
(211, '74DA3D', '2025-09-17 20:23:06.106131', NULL, 'nguyentoduong.2003@gmail.com', 'dg', '0123456789', 'B.1.1,A.1.1', 'canceled_by_operator', 3040000.00, '2025-09-17 20:44:45.512882', NULL, 31, 37, 'ONLINE', NULL),
(212, 'E64772', '2025-09-17 20:52:29.064829', NULL, 'nguyentoduong.2003@gmail.com', 'dg', '0123456789', 'A.1.1,B.1.1', 'canceled_by_operator', 1602000.00, '2025-09-17 21:08:13.250673', NULL, 31, 33, 'ONLINE', NULL),
(213, 'E1A5CB', '2025-09-17 21:11:18.444718', NULL, 'nguyentoduong.2003@gmail.com', 'dg', '0123456789', 'A.1.1,B.1.1', 'canceled_by_operator', 385000.00, '2025-09-17 21:26:18.781345', NULL, 31, 43, 'ONLINE', 24),
(214, '844FE1', '2025-09-17 21:11:50.607544', NULL, 'nguyentoduong.2003@gmail.com', 'dg', '0123456789', 'A.2.1,B.2.1', 'canceled_by_operator', 499000.00, '2025-09-17 21:41:19.018113', NULL, 31, 43, 'ONLINE', NULL),
(215, '4780F1', '2025-09-18 03:02:06.475791', NULL, 'hoaicalm@gmail.com', 'hoàng hoài', '0901234561', 'B.5.1,C.5.1', 'confirmed', 3800000.00, '2025-09-18 03:03:02.033956', NULL, 26, 37, 'ONLINE', NULL),
(217, 'C193B3', '2025-09-18 05:58:53.681720', NULL, 'nguyentoduong.2003@gmail.com', 'dg', '0123456789', 'A.2.1', 'canceled_by_operator', 500000.00, '2025-09-19 15:06:03.639649', NULL, 31, 39, 'ONLINE', NULL),
(218, '0B9F27', '2025-09-19 15:20:54.014924', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'B.3.1,C.3.1', 'completed', 390000.00, '2025-09-19 15:22:04.548182', NULL, 28, 42, 'ONLINE', NULL),
(219, 'E04556', '2025-09-19 16:40:22.816593', 'Đà Nẵng', 'antoin2901@gmail.com', 'An Dương', '0123456789', 'B.5.1, C.5.1', 'canceled_by_operator', 400000.00, '2025-09-19 16:55:22.981926', NULL, NULL, 42, 'OFFLINE', NULL),
(220, 'A6C5F0', '2025-09-20 02:39:20.000000', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'A.6.1,B.6.1', 'canceled_by_operator', 3000000.00, '2025-09-20 02:44:10.000000', NULL, 28, 34, 'ONLINE', NULL),
(221, 'A8780E', '2025-09-23 07:03:37.000000', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'B.4.1,A.4.1', 'canceled_by_operator', 2400000.00, '2025-09-23 07:18:37.000000', NULL, 28, 36, 'ONLINE', NULL),
(222, '475EEC', '2025-09-23 08:47:12.000000', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'B.3.1,C.3.1', 'canceled_by_operator', 1920000.00, '2025-09-23 09:02:12.000000', NULL, 28, 36, 'ONLINE', NULL),
(223, 'F80EEC', '2025-09-24 06:04:25.000000', NULL, 'hoai19800@gmail.com', 'HOAI19800 COMPANY', '0123124121', 'C.9.1,D.9.1', 'canceled_by_operator', 1440000.00, '2025-09-24 06:19:25.000000', NULL, 28, 30, 'ONLINE', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `buses`
--

CREATE TABLE `buses` (
  `id` bigint(20) NOT NULL,
  `amenities` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`amenities`)),
  `license_plate` varchar(50) NOT NULL,
  `status` enum('active','out_of_service','under_maintenance') NOT NULL,
  `total_seats` int(11) NOT NULL,
  `operator_id` bigint(20) DEFAULT NULL,
  `seat_layout_id` int(11) DEFAULT NULL,
  `model_id` bigint(20) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL
) ;

--
-- Dumping data for table `buses`
--

INSERT INTO `buses` (`id`, `amenities`, `license_plate`, `status`, `total_seats`, `operator_id`, `seat_layout_id`, `model_id`, `model`) VALUES
(2, '{\"tv\": true, \"wifi\": true, \"toilet\": true, \"charging\": true, \"air_conditioner\": true}', '45K-555.55', 'active', 80, 1, 3, 3, NULL),
(3, '{\"tv\": false, \"wifi\": true, \"toilet\": false, \"charging\": false, \"air_conditioner\": true}', '51B-543.21', 'active', 80, 3, 3, 3, NULL),
(4, '{\"tv\": true, \"wifi\": false, \"toilet\": true, \"charging\": false, \"air_conditioner\": false}', '51L-987.65', 'active', 40, 4, 1, 4, NULL),
(5, '{\"tv\": false, \"wifi\": true, \"toilet\": true, \"charging\": true, \"air_conditioner\": true}', '51B-112.23', 'active', 80, 5, 2, 5, NULL),
(8, '{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": false, \"air_conditioner\": false}', '51B-123.46', 'active', 40, 1, 1, 1, NULL),
(9, '{\"tv\": true, \"wifi\": true, \"toilet\": false, \"charging\": true, \"air_conditioner\": false}', '36B-123.31', 'active', 80, 1, 2, 2, NULL),
(14, '{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": false, \"air_conditioner\": false}', '36B-123.32', 'active', 40, 1, 1, 1, NULL),
(16, '{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": false, \"air_conditioner\": false}', '36B-123.33', 'active', 40, 1, 1, 1, NULL),
(17, '{\"tv\": true, \"wifi\": true, \"toilet\": true, \"charging\": true, \"air_conditioner\": true}', '23S-223.24', 'active', 40, 1, 1, 4, NULL),
(19, '{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": true, \"air_conditioner\": false}', '23S-223.22', 'active', 40, 2, 1, 1, NULL),
(22, '{\"tv\": false, \"wifi\": false, \"toilet\": false, \"charging\": true, \"air_conditioner\": true}', '51B-678.80', 'active', 80, 1, 2, 5, NULL),
(24, '{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": true, \"air_conditioner\": false}', '51B-678.91', 'active', 80, 1, 2, 2, NULL),
(25, '{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": false, \"air_conditioner\": true}', '51B-678.92', 'active', 80, 2, 2, 1, NULL),
(27, '{\"tv\": false, \"wifi\": false, \"toilet\": false, \"charging\": true, \"air_conditioner\": true}', '51B-678.93', 'active', 40, 8, 1, 2, NULL),
(29, '{\"tv\": false, \"wifi\": false, \"toilet\": false, \"charging\": true, \"air_conditioner\": true}', '51B-678.95', 'active', 80, 8, 2, 1, NULL),
(32, '{\"wifi\": true, \"water\": false, \"charger\": true, \"airConditioner\": true}', '88A-888.88', 'active', 80, 8, 3, 1, NULL),
(44, '{\"tv\": true, \"wifi\": false, \"toilet\": true, \"charging\": false, \"air_conditioner\": true}', '51B-678.90', 'active', 40, 1, 1, 1, NULL),
(45, '{\"tv\": false, \"wifi\": true, \"toilet\": true, \"charging\": false, \"air_conditioner\": true}', '88A-888.81', 'active', 80, 1, 2, 1, NULL),
(48, '{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": true, \"air_conditioner\": true}', '51B-678.96', 'active', 40, 10, 1, 3, NULL),
(49, '{\"tv\": true, \"wifi\": false, \"toilet\": false, \"charging\": true, \"air_conditioner\": true}', '51B-678.99', 'active', 40, 10, 1, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `bus_images`
--

CREATE TABLE `bus_images` (
  `id` bigint(20) NOT NULL,
  `image_url` varchar(500) NOT NULL,
  `is_primary` bit(1) NOT NULL,
  `bus_id` bigint(20) NOT NULL,
  `public_id` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bus_images`
--

INSERT INTO `bus_images` (`id`, `image_url`, `is_primary`, `bus_id`, `public_id`) VALUES
(21, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757298001/busify/buses/images/file_1757297998930.jpg.jpg', b'0', 44, 'busify/buses/images/file_1757297998930.jpg'),
(22, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757298003/busify/buses/images/file_1757298001459.jpg.jpg', b'0', 44, 'busify/buses/images/file_1757298001459.jpg'),
(23, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757298006/busify/buses/images/file_1757298003913.jpg.jpg', b'0', 44, 'busify/buses/images/file_1757298003913.jpg'),
(27, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757298374/busify/buses/images/file_1757298372483.jpg.jpg', b'0', 2, 'busify/buses/images/file_1757298372483.jpg'),
(28, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757298377/busify/buses/images/file_1757298374923.jpg.jpg', b'0', 2, 'busify/buses/images/file_1757298374923.jpg'),
(29, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757298379/busify/buses/images/file_1757298377675.jpg.jpg', b'0', 2, 'busify/buses/images/file_1757298377675.jpg'),
(30, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757298382/busify/buses/images/file_1757298379922.jpg.jpg', b'0', 2, 'busify/buses/images/file_1757298379922.jpg'),
(34, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757473131/busify/buses/images/file_1757473129244.jpg.jpg', b'0', 16, 'busify/buses/images/file_1757473129244.jpg'),
(35, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757473134/busify/buses/images/file_1757473131929.jpg.jpg', b'0', 16, 'busify/buses/images/file_1757473131929.jpg'),
(36, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757473136/busify/buses/images/file_1757473134004.jpg.jpg', b'0', 16, 'busify/buses/images/file_1757473134004.jpg'),
(37, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757475279/busify/buses/images/file_1757475275015.jpg.jpg', b'0', 8, 'busify/buses/images/file_1757475275015.jpg'),
(38, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757475283/busify/buses/images/file_1757475279171.jpg.jpg', b'0', 8, 'busify/buses/images/file_1757475279171.jpg'),
(39, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757475287/busify/buses/images/file_1757475283961.jpg.jpg', b'0', 8, 'busify/buses/images/file_1757475283961.jpg'),
(40, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757475322/busify/buses/images/file_1757475319993.jpg.jpg', b'0', 14, 'busify/buses/images/file_1757475319993.jpg'),
(41, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757475324/busify/buses/images/file_1757475322322.jpg.jpg', b'0', 14, 'busify/buses/images/file_1757475322322.jpg'),
(42, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757475332/busify/buses/images/file_1757475324631.jpg.jpg', b'0', 14, 'busify/buses/images/file_1757475324631.jpg'),
(43, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757475871/busify/buses/images/file_1757475867909.jpg.jpg', b'0', 17, 'busify/buses/images/file_1757475867909.jpg'),
(44, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757475874/busify/buses/images/file_1757475871603.jpg.jpg', b'0', 17, 'busify/buses/images/file_1757475871603.jpg'),
(45, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757475876/busify/buses/images/file_1757475874163.jpg.jpg', b'0', 17, 'busify/buses/images/file_1757475874163.jpg'),
(46, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757475878/busify/buses/images/file_1757475876396.jpg.jpg', b'0', 17, 'busify/buses/images/file_1757475876396.jpg'),
(47, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757475881/busify/buses/images/file_1757475879266.jpg.jpg', b'0', 17, 'busify/buses/images/file_1757475879266.jpg'),
(48, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757476033/busify/buses/images/file_1757476030369.png.png', b'0', 17, 'busify/buses/images/file_1757476030369.png'),
(49, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757476035/busify/buses/images/file_1757476033058.jpg.jpg', b'0', 17, 'busify/buses/images/file_1757476033058.jpg'),
(50, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757476301/busify/buses/images/file_1757476297195.png.png', b'0', 22, 'busify/buses/images/file_1757476297195.png'),
(51, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757476305/busify/buses/images/file_1757476301878.jpg.jpg', b'0', 22, 'busify/buses/images/file_1757476301878.jpg'),
(52, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757476309/busify/buses/images/file_1757476305701.png.png', b'0', 22, 'busify/buses/images/file_1757476305701.png'),
(53, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757477000/busify/buses/images/file_1757476997672.jpg.jpg', b'0', 24, 'busify/buses/images/file_1757476997672.jpg'),
(54, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757477003/busify/buses/images/file_1757477000189.jpg.jpg', b'0', 24, 'busify/buses/images/file_1757477000189.jpg'),
(55, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757477006/busify/buses/images/file_1757477003580.jpg.jpg', b'0', 24, 'busify/buses/images/file_1757477003580.jpg'),
(56, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757477008/busify/buses/images/file_1757477006064.jpg.jpg', b'0', 24, 'busify/buses/images/file_1757477006064.jpg'),
(57, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757477011/busify/buses/images/file_1757477008784.jpg.jpg', b'0', 24, 'busify/buses/images/file_1757477008784.jpg'),
(58, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757477058/busify/buses/images/file_1757477056556.jpg.jpg', b'0', 9, 'busify/buses/images/file_1757477056556.jpg'),
(59, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757477062/busify/buses/images/file_1757477059711.jpg.jpg', b'0', 9, 'busify/buses/images/file_1757477059711.jpg'),
(60, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757477064/busify/buses/images/file_1757477062202.jpg.jpg', b'0', 9, 'busify/buses/images/file_1757477062202.jpg'),
(61, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757477066/busify/buses/images/file_1757477064669.jpg.jpg', b'0', 9, 'busify/buses/images/file_1757477064669.jpg'),
(62, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757477069/busify/buses/images/file_1757477066843.jpg.jpg', b'0', 9, 'busify/buses/images/file_1757477066843.jpg'),
(64, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757605247/busify/buses/images/file_1757605243733.jpg.jpg', b'0', 45, 'busify/buses/images/file_1757605243733.jpg'),
(69, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757949750/busify/buses/images/file_1757949742715.jpg.jpg', b'1', 48, 'busify/buses/images/file_1757949742715.jpg'),
(70, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757991625/busify/buses/images/file_1757991621040.jpg.jpg', b'1', 49, 'busify/buses/images/file_1757991621040.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `bus_models`
--

CREATE TABLE `bus_models` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bus_models`
--

INSERT INTO `bus_models` (`id`, `name`) VALUES
(3, 'Hyundai Aero'),
(1, 'Hyundai Universe'),
(4, 'Isuzu Samco'),
(5, 'Mercedes Sprinter'),
(2, 'Thaco Mobihome'),
(6, 'Volvo B11R');

-- --------------------------------------------------------

--
-- Table structure for table `bus_operators`
--

CREATE TABLE `bus_operators` (
  `operator_id` bigint(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `hotline` varchar(50) DEFAULT NULL,
  `license_path` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` enum('active','inactive','pending_approval','suspended') NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT 0,
  `avatar` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bus_operators`
--

INSERT INTO `bus_operators` (`operator_id`, `address`, `created_at`, `description`, `email`, `hotline`, `license_path`, `name`, `status`, `updated_at`, `owner_id`, `is_deleted`, `avatar`) VALUES
(1, '12 Nguyễn Văn Linh, Đà Nẵng', '2025-07-24 10:00:00.000000', 'Nhà xe uy tín', 'antoinnguyen95@gmail.com', '190070701', '/licenses/operator1.pdf', 'Chín Nghĩa 76', 'active', '2025-09-11 15:43:01.659359', 18, 0, 'https://res.cloudinary.com/dioi8edng/image/upload/v1757605384/busify/operators/avatars/file_1757605379455.jpg.jpg'),
(2, '12 Phạm Ngọc Thạch, Đà Lạt', '2025-07-24 11:00:00.000000', 'Nhà xe chất lượng cao', 'operator2@busify.com', '0931234567', '/licenses/operator2.pdf', 'Nhà Xe XYZ', 'active', '2025-07-24 11:00:00.000000', 24, 0, NULL),
(3, '45 Nguyễn Văn Cừ, Huế', '2025-07-24 11:00:00.000000', 'Dịch vụ thân thiện', 'operator3@busify.com', '0932234567', '/licenses/operator3.pdf', 'Nhà Xe Huế', 'pending_approval', '2025-07-24 11:00:00.000000', 5, 0, NULL),
(4, '67 Trần Hưng Đạo, Cần Thơ', '2025-07-24 11:00:00.000000', 'Nhà xe uy tín miền Tây', 'operator4@busify.com', '0933234567', '/licenses/operator4.pdf', 'Nhà Xe Miền Tây', 'active', '2025-07-24 11:00:00.000000', 5, 0, NULL),
(5, '89 Võ Thị Sáu, Vũng Tàu', '2025-07-24 11:00:00.000000', 'Chuyến xe an toàn', 'operator5@busify.com', '0934234567', '/licenses/operator5.pdf', 'Nhà Xe Biển Xanh', 'active', '2025-09-18 03:58:27.383893', 5, 1, NULL),
(6, '101 Lê Lai, Nha Trang', '2025-07-24 11:00:00.000000', 'Dịch vụ cao cấp', 'operator6@busify.com', '0935234567', '/licenses/operator6.pdf', 'Nhà Xe Nha Trang', 'active', '2025-09-18 04:01:12.994084', 5, 1, NULL),
(7, '123 ABC Street, Ho Chi Minh City', '2025-08-26 02:03:51.095099', 'Bus operator created from contract: 0101234562', 'hoaicoder2605@gmail.com', '0123456789', 'https://res.cloudinary.com/dioi8edng/image/upload/v1756173676/contracts/file_1756173674261.png.png', 'HOAICODER2605 COMPANY', 'active', '2025-08-26 02:03:51.095099', 27, 0, NULL),
(8, 'adsfadsf', '2025-08-26 07:19:43.925695', 'Bus operator created from contract: 0123142341', 'hoai19800@gmail.com', '0123124121', 'https://res.cloudinary.com/dioi8edng/image/upload/v1758169701/licenses/file_1758169696652.png.png', 'HOAI19800 COMPANY', 'active', '2025-09-18 04:28:21.181848', 28, 1, NULL),
(9, '123 ABC Street, Ho Chi Minh City', '2025-09-09 07:56:29.056916', 'Bus operator created from contract: 0101234563', 'hoai19800@gmail.com', '0123456789', 'https://res.cloudinary.com/dioi8edng/image/upload/v1756186052/contracts/file_1756186050719.png.png', 'HOAI19800 COMPANY', 'active', '2025-09-09 07:56:29.056916', 28, 0, NULL),
(10, 'Quang Tri\r\nTrieu Phong', '2025-09-15 15:10:14.644166', 'Bus operator created from contract: 0123412341', 'antoinnguyen2@gmail.com', '09012345611', 'https://res.cloudinary.com/dioi8edng/image/upload/v1757948816/contracts/file_1757948813492.jpg.jpg', 'ANTOINNGUYEN2 COMPANY', 'active', '2025-09-19 15:45:44.940352', 43, 0, 'https://res.cloudinary.com/dioi8edng/image/upload/v1758296744/busify/operators/avatars/file_1758296743550.jpg.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `chat_messages`
--

CREATE TABLE `chat_messages` (
  `id` bigint(20) NOT NULL,
  `content` text DEFAULT NULL,
  `recipient` varchar(255) DEFAULT NULL,
  `room_id` varchar(255) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `type` enum('CHAT','JOIN','LEAVE','SYSTEM_ASSIGN') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `chat_messages`
--

INSERT INTO `chat_messages` (`id`, `content`, `recipient`, `room_id`, `sender`, `timestamp`, `type`) VALUES
(81, 'test2', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-09 15:59:29.749078', 'CHAT'),
(82, 'Xin chào, tôi là Agent. Tôi sẽ hỗ trợ bạn ngay.', 'quocla.21it@vku.udn.vn', 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-09 15:59:29.864702', 'SYSTEM_ASSIGN'),
(83, 'tesst', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-09 15:59:36.432828', 'CHAT'),
(84, 'test3', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-09 15:59:51.660657', 'CHAT'),
(85, 'tets', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-09 16:33:05.447057', 'CHAT'),
(86, 'test', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-09 21:04:27.111630', 'CHAT'),
(87, 'test4', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-09 21:07:50.979698', 'CHAT'),
(88, 'test5', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-09 21:21:32.161603', 'CHAT'),
(89, 'test7', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-09 21:30:19.640854', 'CHAT'),
(90, 'test8', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-09 21:32:37.487065', 'CHAT'),
(91, 'test9', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-10 09:13:22.982685', 'CHAT'),
(92, 'test10', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-10 09:26:40.746032', 'CHAT'),
(93, 'test11', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-10 09:43:54.015097', 'CHAT'),
(94, 'test12', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-10 09:51:29.116627', 'CHAT'),
(95, 'test13', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-10 09:51:49.840236', 'CHAT'),
(96, 'test14', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-10 09:56:41.876810', 'CHAT'),
(97, 'test15', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-10 10:00:04.724222', 'CHAT'),
(98, 'test16', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-10 10:03:20.024800', 'CHAT'),
(99, 'test17', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-10 10:07:21.836834', 'CHAT'),
(100, 'test18', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-10 10:22:03.585101', 'CHAT'),
(101, 'test19', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-10 10:23:45.884049', 'CHAT'),
(102, 'tesst20', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-10 10:27:40.326156', 'CHAT'),
(103, 'tesst21', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-10 19:11:44.416966', 'CHAT'),
(104, 'test', NULL, '520bebac-c906-4251-b485-9ea74f9027bd', 'quocla.21it@vku.udn.vn', '2025-09-10 19:15:09.119294', 'CHAT'),
(105, 'Xin chào, tôi là Trần Hùng Anh. Tôi sẽ hỗ trợ bạn ngay.', 'quocla.21it@vku.udn.vn', '520bebac-c906-4251-b485-9ea74f9027bd', 'anhth@gmail.com', '2025-09-10 19:15:09.369021', 'SYSTEM_ASSIGN'),
(106, 'test', NULL, 'fb005de4-6aa4-4616-a69b-075919040a90', 'quocla.21it@vku.udn.vn', '2025-09-10 19:23:50.058141', 'CHAT'),
(107, 'Xin chào, tôi là Trần Hùng Anh. Tôi sẽ hỗ trợ bạn ngay.', 'quocla.21it@vku.udn.vn', 'fb005de4-6aa4-4616-a69b-075919040a90', 'anhth@gmail.com', '2025-09-10 19:23:50.324393', 'SYSTEM_ASSIGN'),
(108, 'test22', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-10 19:30:05.893924', 'CHAT'),
(109, 'test23', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-10 19:38:15.216004', 'CHAT'),
(110, 'test24', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-10 19:38:50.642856', 'CHAT'),
(111, 'test25', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-10 19:52:22.741686', 'CHAT'),
(112, 'test26', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-10 21:13:48.988391', 'CHAT'),
(113, 'test27', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-10 21:14:50.634598', 'CHAT'),
(114, 'test28', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 15:30:22.957698', 'CHAT'),
(115, 'test29', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 15:30:34.113427', 'CHAT'),
(116, '30', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 15:35:19.558786', 'CHAT'),
(117, '31', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 15:35:28.361735', 'CHAT'),
(118, '32', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:02:47.420494', 'CHAT'),
(119, '34', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:03:03.808077', 'CHAT'),
(120, 'test', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:04:09.381953', 'CHAT'),
(121, '1', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:04:21.664058', 'CHAT'),
(122, '2', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:09:26.397664', 'CHAT'),
(123, '3', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-11 16:09:38.092452', 'CHAT'),
(124, '4', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:09:44.507959', 'CHAT'),
(125, '5', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:09:54.141538', 'CHAT'),
(126, '6', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:41:03.749005', 'CHAT'),
(127, '7', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:41:17.276079', 'CHAT'),
(128, '8', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:41:28.166658', 'CHAT'),
(129, '9', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:41:36.973718', 'CHAT'),
(130, '10', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:41:40.633376', 'CHAT'),
(131, '11', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:42:24.932783', 'CHAT'),
(132, '12', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:45:21.262121', 'CHAT'),
(133, '13', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:45:28.063097', 'CHAT'),
(134, '14', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:45:34.030183', 'CHAT'),
(135, '15', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 16:45:44.847958', 'CHAT'),
(136, 'Làm phần mềm như c. hủy vé trước chuyến đi 2 ngày r mà ko hoàn tiền đmm', NULL, 'd330fb04-1ddc-419d-bbca-00b97cb6d27b', 'thuongtv.21it@vku.udn.vn', '2025-09-11 19:01:01.186332', 'CHAT'),
(137, 'Xin chào, tôi là Agent. Tôi sẽ hỗ trợ bạn ngay.', 'thuongtv.21it@vku.udn.vn', 'd330fb04-1ddc-419d-bbca-00b97cb6d27b', 'cs@gmail.com', '2025-09-11 19:01:01.834140', 'SYSTEM_ASSIGN'),
(138, 'wtf :v', NULL, 'd330fb04-1ddc-419d-bbca-00b97cb6d27b', 'cs@gmail.com', '2025-09-11 19:58:00.578971', 'CHAT'),
(139, 'yêu cầu và tiền của bản sẽ ko bao giờ được xử lí', NULL, 'd330fb04-1ddc-419d-bbca-00b97cb6d27b', 'cs@gmail.com', '2025-09-11 19:58:34.527760', 'CHAT'),
(140, '16', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 19:58:52.668682', 'CHAT'),
(141, '17', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 20:03:32.503605', 'CHAT'),
(142, '18', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 20:03:40.807326', 'CHAT'),
(143, '19', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 20:03:49.885682', 'CHAT'),
(144, '20', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-11 20:08:32.182464', 'CHAT'),
(145, '21', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-11 20:36:15.108122', 'CHAT'),
(146, '22', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-11 20:39:46.272816', 'CHAT'),
(147, '23', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-11 20:40:05.495885', 'CHAT'),
(148, '24', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-11 20:41:16.298334', 'CHAT'),
(149, '25', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-11 20:41:33.120652', 'CHAT'),
(150, '2', NULL, 'c6070e4f-ff57-44f6-ab50-088bab92ad2b', 'nguyentoduong.2003@gmail.com', '2025-09-11 23:54:47.212143', 'CHAT'),
(151, 'Xin chào, tôi là Trần Hùng Anh. Tôi sẽ hỗ trợ bạn ngay.', 'nguyentoduong.2003@gmail.com', 'c6070e4f-ff57-44f6-ab50-088bab92ad2b', 'anhth@gmail.com', '2025-09-11 23:54:47.510093', 'SYSTEM_ASSIGN'),
(152, 'đi nhậu k', NULL, 'c6070e4f-ff57-44f6-ab50-088bab92ad2b', 'nguyentoduong.2003@gmail.com', '2025-09-11 23:57:04.759016', 'CHAT'),
(153, 'dứt', NULL, 'c6070e4f-ff57-44f6-ab50-088bab92ad2b', 'anhth@gmail.com', '2025-09-11 23:57:12.078297', 'CHAT'),
(154, '22', NULL, 'c6070e4f-ff57-44f6-ab50-088bab92ad2b', 'nguyentoduong.2003@gmail.com', '2025-09-12 10:18:26.421269', 'CHAT'),
(155, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 17:53:30.787203', 'CHAT'),
(156, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 17:53:30.874497', 'CHAT'),
(157, 'Xin chào, tôi là Agent. Tôi sẽ hỗ trợ bạn ngay.', 'AI Bot', 'ai-nguyentoduong.2003@gmail.com', 'cs@gmail.com', '2025-09-15 17:53:31.092645', 'SYSTEM_ASSIGN'),
(158, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 17:54:00.775472', 'CHAT'),
(159, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 17:54:00.898952', 'CHAT'),
(160, 'nhậu nhiều tốt ko', 'AI Bot', 'ai-nguyentoduong.2003@gmail.com', 'nguyentoduong.2003@gmail.com', '2025-09-15 17:57:42.817838', 'CHAT'),
(161, 'Tôi hiểu bạn đang hỏi về: \"nhậu nhiều tốt ko\"\n\nTôi có thể hỗ trợ bạn về các vấn đề sau:\n? Đặt vé xe khách\n? Giá vé và khuyến mãi\n? Lịch trình và tuyến đường\n❌ Hủy vé và hoàn tiền\n? Thanh toán và vé điện tử\n? Thông tin bến xe\n\nBạn có thể hỏi cụ thể hơn hoặc liên hệ nhân viên hỗ trợ nếu cần giúp đỡ thêm. Tôi có thể giúp gì khác cho bạn không?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 17:57:45.610383', 'CHAT'),
(162, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:03:21.023676', 'CHAT'),
(163, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:03:21.207863', 'CHAT'),
(164, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:09:41.218115', 'CHAT'),
(165, 'hi', 'AI Bot', 'ai-nguyentoduong.2003@gmail.com', 'nguyentoduong.2003@gmail.com', '2025-09-15 18:09:45.647002', 'CHAT'),
(166, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:10:03.698613', 'CHAT'),
(167, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:10:03.776749', 'CHAT'),
(168, 'hello', 'AI Bot', 'ai-nguyentoduong.2003@gmail.com', 'nguyentoduong.2003@gmail.com', '2025-09-15 18:10:07.786630', 'CHAT'),
(169, 'Cảm ơn bạn đã chia sẻ: \"hi\"\n\nTôi là trợ lý AI của Busify và tôi sẵn sàng trò chuyện với bạn về nhiều chủ đề khác nhau! ?\n\n? **Bạn có thể hỏi tôi về:**\n• Đặt vé xe khách và du lịch\n• Tư vấn và thông tin hữu ích\n• Trò chuyện thông thường\n• Bất kỳ điều gì bạn quan tâm\n\nHãy thử hỏi tôi điều gì đó khác nhé!', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:10:26.016878', 'CHAT'),
(170, 'nhậu nhiều có tốt ko', 'AI Bot', 'ai-nguyentoduong.2003@gmail.com', 'nguyentoduong.2003@gmail.com', '2025-09-15 18:10:37.159392', 'CHAT'),
(171, 'Xin chào! Tôi là trợ lý AI của Busify. Tôi có thể giúp gì cho bạn hôm nay? ?\n\nBạn có thể hỏi tôi về bất kỳ điều gì - từ đặt vé xe khách đến những câu hỏi thông thường khác!', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:10:46.080293', 'CHAT'),
(172, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:10:56.004436', 'CHAT'),
(173, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:11:01.781307', 'CHAT'),
(174, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:11:01.863663', 'CHAT'),
(175, 'Về việc uống rượu bia:\n? Uống nhiều rượu bia không tốt cho sức khỏe\n⚠️ Có thể gây tổn hại gan, tim mạch, não bộ\n✅ Nếu uống, hãy uống có trách nhiệm và điều độ\n? Tuyệt đối không lái xe sau khi uống\n\nThay vào đó, hãy thử các hoạt động khỏe mạnh khác như thể thao, đọc sách, du lịch!\nNói về du lịch, Busify có nhiều tuyến xe đẹp để bạn khám phá đấy! ?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:11:06.138511', 'CHAT'),
(176, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:11:11.943611', 'CHAT'),
(177, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:11:12.150291', 'CHAT'),
(178, 'hello', 'AI Bot', 'ai-nguyentoduong.2003@gmail.com', 'nguyentoduong.2003@gmail.com', '2025-09-15 18:12:01.542513', 'CHAT'),
(179, 'Xin chào! Tôi là trợ lý AI của Busify. Tôi có thể giúp gì cho bạn hôm nay? ?\n\nBạn có thể hỏi tôi về bất kỳ điều gì - từ đặt vé xe khách đến những câu hỏi thông thường khác!', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:12:41.935587', 'CHAT'),
(180, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:15:51.090096', 'CHAT'),
(181, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-15 18:15:51.285930', 'CHAT'),
(182, 'hi', 'AI Bot', 'ai-nguyentoduong.2003@gmail.com', 'nguyentoduong.2003@gmail.com', '2025-09-15 18:15:54.727302', 'CHAT'),
(183, '26', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-16 11:02:48.586233', 'CHAT'),
(184, '27', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-16 11:03:00.619054', 'CHAT'),
(185, '28', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-16 11:03:08.966781', 'CHAT'),
(186, '29', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-16 11:03:27.919486', 'CHAT'),
(187, '30', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-16 11:04:23.937339', 'CHAT'),
(188, '31', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-16 11:04:37.092511', 'CHAT'),
(189, '32', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-16 11:17:22.791586', 'CHAT'),
(190, '32', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-16 11:19:55.994766', 'CHAT'),
(191, '33', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'quocla.21it@vku.udn.vn', '2025-09-16 11:20:14.128725', 'CHAT'),
(192, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 00:27:55.922668', 'CHAT'),
(193, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 00:27:56.068205', 'CHAT'),
(194, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 00:27:56.214869', 'CHAT'),
(195, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 00:28:14.231767', 'CHAT'),
(196, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 00:29:26.409036', 'CHAT'),
(197, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 00:29:26.505709', 'CHAT'),
(198, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 00:42:39.994740', 'CHAT'),
(199, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 00:42:39.997113', 'CHAT'),
(200, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 00:45:07.664821', 'CHAT'),
(201, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 00:45:07.943435', 'CHAT'),
(202, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 01:35:47.483371', 'CHAT'),
(203, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 01:35:47.572060', 'CHAT'),
(204, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 01:35:52.667280', 'CHAT'),
(205, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 01:35:52.702962', 'CHAT'),
(206, '2', NULL, '618ff4d9-29a0-49a5-9478-a0b902c46c3d', 'nguyentoduong.2003@gmail.com', '2025-09-18 03:21:15.369336', 'CHAT'),
(207, 'Xin chào, tôi là Agent. Tôi sẽ hỗ trợ bạn ngay.', 'nguyentoduong.2003@gmail.com', '618ff4d9-29a0-49a5-9478-a0b902c46c3d', 'cs@gmail.com', '2025-09-18 03:21:15.491234', 'SYSTEM_ASSIGN'),
(208, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 03:27:27.772553', 'CHAT'),
(209, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 03:27:28.136076', 'CHAT'),
(210, '2', NULL, '618ff4d9-29a0-49a5-9478-a0b902c46c3d', 'nguyentoduong.2003@gmail.com', '2025-09-18 03:47:18.345806', 'CHAT'),
(211, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 03:52:15.176428', 'CHAT'),
(212, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 03:52:15.249109', 'CHAT'),
(213, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 03:52:40.345107', 'CHAT'),
(214, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 03:52:40.459647', 'CHAT'),
(215, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 03:53:03.898104', 'CHAT'),
(216, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 03:53:03.972815', 'CHAT'),
(217, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 03:53:10.211261', 'CHAT'),
(218, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 03:53:10.336485', 'CHAT'),
(219, '2', 'cs@gmail.com', '618ff4d9-29a0-49a5-9478-a0b902c46c3d', 'nguyentoduong.2003@gmail.com', '2025-09-18 03:56:53.511038', 'CHAT'),
(220, 'fdfdf', 'nguyentoduong.2003@gmail.com', '618ff4d9-29a0-49a5-9478-a0b902c46c3d', 'cs@gmail.com', '2025-09-18 03:56:59.115675', 'CHAT'),
(221, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:00:21.184779', 'CHAT'),
(222, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:02:26.305555', 'CHAT'),
(223, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:02:26.357779', 'CHAT'),
(224, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:06:21.611514', 'CHAT'),
(225, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:06:21.743955', 'CHAT'),
(226, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:10:39.849741', 'CHAT'),
(227, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:10:40.152910', 'CHAT'),
(228, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:11:40.051530', 'CHAT'),
(229, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:11:57.833520', 'CHAT'),
(230, 'ngu ch', 'nguyentoduong.2003@gmail.com', '618ff4d9-29a0-49a5-9478-a0b902c46c3d', 'cs@gmail.com', '2025-09-18 04:12:40.181037', 'CHAT'),
(231, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:14:46.000124', 'CHAT'),
(232, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:17:17.402849', 'CHAT'),
(233, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:17:25.969544', 'CHAT'),
(234, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:17:26.072024', 'CHAT'),
(235, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:40:04.091822', 'CHAT'),
(236, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 04:40:04.146830', 'CHAT'),
(237, 'nam bú cu', NULL, '9f37c47f-2783-474a-b59c-296f6ab0159c', 'hoaicalm@gmail.com', '2025-09-18 10:08:32.842640', 'CHAT'),
(238, 'Xin chào, tôi là Agent. Tôi sẽ hỗ trợ bạn ngay.', 'hoaicalm@gmail.com', '9f37c47f-2783-474a-b59c-296f6ab0159c', 'cs@gmail.com', '2025-09-18 10:08:33.296589', 'SYSTEM_ASSIGN'),
(239, '?', NULL, '9f37c47f-2783-474a-b59c-296f6ab0159c', 'cs@gmail.com', '2025-09-18 10:58:52.089526', 'CHAT'),
(240, '34', NULL, 'f22de7dc-a4f7-45d4-a49b-9535e3cd8dfd', 'cs@gmail.com', '2025-09-18 11:02:15.951493', 'CHAT'),
(241, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 12:58:18.677214', 'CHAT'),
(242, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 12:58:18.857903', 'CHAT'),
(243, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 12:58:43.213176', 'CHAT'),
(244, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 12:58:43.315932', 'CHAT'),
(245, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 12:59:00.854663', 'CHAT'),
(246, 'Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?', 'nguyentoduong.2003@gmail.com', 'ai-nguyentoduong.2003@gmail.com', 'AI Bot', '2025-09-18 12:59:00.985329', 'CHAT');

-- --------------------------------------------------------

--
-- Table structure for table `complaints`
--

CREATE TABLE `complaints` (
  `complaints_id` bigint(20) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(255) NOT NULL,
  `status` enum('New','in_progress','pending','rejected','resolved') NOT NULL,
  `title` varchar(255) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `assigned_agent_id` bigint(20) DEFAULT NULL,
  `booking_id` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `complaints`
--

INSERT INTO `complaints` (`complaints_id`, `created_at`, `description`, `status`, `title`, `updated_at`, `assigned_agent_id`, `booking_id`, `customer_id`) VALUES
(1, '2025-07-25 22:00:00.000000', 'Ghế không thoải mái', 'rejected', 'Khiếu nại về ghế', '2025-09-05 14:23:36.387843', 25, 1, 34),
(2, '2025-07-25 14:00:00.000000', 'Xe đến muộn 30 phút', 'resolved', 'Khiếu nại về thời gian', '2025-09-05 14:42:03.891519', 25, 2, 34),
(3, '2025-07-25 15:00:00.000000', 'Wifi không hoạt động', 'resolved', 'Khiếu nại về wifi', '2025-09-15 14:31:40.516797', 25, 3, 34),
(4, '2025-08-27 16:34:11.091135', 'Chuyến xe lúc 8h sáng bị trễ hơn 30 phút so với lịch trình.', 'resolved', 'Xe đến trễ', '2025-09-15 15:02:55.025644', 25, 29, 34),
(5, '2025-08-27 16:35:45.243437', 'Chuyến xe lúc 8h sáng bị trễ hơn 30 phút so với lịch trình.', 'rejected', 'Xe đến trễ', '2025-09-15 15:03:04.510833', 25, 29, 34),
(6, '2025-08-27 16:35:47.665927', 'Chuyến xe lúc 8h sáng bị trễ hơn 30 phút so với lịch trình.', 'rejected', 'Xe đến trễ', '2025-09-15 15:04:37.954195', 25, 29, 34),
(7, '2025-08-27 21:15:37.464868', 'aaaaaaaaaaa', 'resolved', 'test1', '2025-09-15 15:29:09.014131', 25, 29, 34),
(8, '2025-08-27 21:16:04.633473', 'tesssssssssss', 'rejected', 'test11234', '2025-09-15 15:57:30.632948', 25, 29, 34),
(9, '2025-08-27 21:18:06.686910', 'Chuyến xe lúc 8h sáng bị trễ hơn 30 phút so với lịch trình', 'in_progress', 'Xe đến trễ', '2025-08-29 14:32:25.203823', 25, 29, 34),
(10, '2025-08-27 21:21:28.629791', 'Xe đến trễ', 'in_progress', 'Xe đến trễ', '2025-08-29 14:33:50.582092', 25, 29, 34),
(11, '2025-09-15 08:22:22.047000', 'Xe đến trễ', 'in_progress', 'Xe đến trễ', '2025-09-15 14:34:05.247000', 25, 29, 34);

-- --------------------------------------------------------

--
-- Table structure for table `contracts`
--

CREATE TABLE `contracts` (
  `id` bigint(20) NOT NULL,
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
  `updated_date` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `contracts`
--

INSERT INTO `contracts` (`id`, `vatcode`, `address`, `admin_note`, `approved_date`, `created_date`, `email`, `end_date`, `last_modified`, `last_modified_by`, `license_url`, `operation_area`, `phone`, `start_date`, `status`, `updated_date`) VALUES
(1, '0101234562', '123 ABC Street, Ho Chi Minh City', 'oke', '2025-08-26 02:03:50.863307', '2025-08-26 02:01:17.568803', 'hoaicoder2605@gmail.com', '2026-08-31 23:59:59.000000', '2025-08-26 02:03:50.863307', 'admin', 'https://res.cloudinary.com/dioi8edng/image/upload/v1756173676/contracts/file_1756173674261.png.png', 'Ho Chi Minh - Da Nang', '0123456789', '2025-09-01 00:00:00.000000', 'ACCEPTED', '2025-08-26 02:03:51.115137'),
(3, '0101234563', '123 ABC Street, Ho Chi Minh City', NULL, '2025-09-09 07:56:29.006799', '2025-08-26 05:27:33.550534', 'hoai19800@gmail.com', '2026-08-31 23:59:59.000000', '2025-09-09 07:56:29.005800', 'admin', 'https://res.cloudinary.com/dioi8edng/image/upload/v1756186052/contracts/file_1756186050719.png.png', 'Ho Chi Minh - Da Nang', '0123456789', '2025-09-01 00:00:00.000000', 'ACCEPTED', '2025-09-09 07:56:29.071728'),
(4, '0123142341', 'adsfadsf', NULL, '2025-08-26 07:19:43.780825', '2025-08-26 05:45:52.581262', 'hoai19800@gmail.com', '2025-09-18 09:50:00.000000', '2025-08-26 07:19:43.779819', 'admin', 'https://res.cloudinary.com/dioi8edng/image/upload/v1756187151/contracts/file_1756187149433.jpg.jpg', 'ho chi minh - da nang', '0123124121', '2025-08-28 08:49:00.000000', 'ACCEPTED', '2025-08-26 07:19:43.954995'),
(5, '01323152323', 'dsfase', NULL, NULL, '2025-08-26 07:16:19.966773', 'hoaicalm@gmail.com', '2025-10-16 12:20:00.000000', '2025-08-26 07:16:19.963260', 'system', 'https://res.cloudinary.com/dioi8edng/image/upload/v1756192579/contracts/file_1756192577610.jpg.jpg', 'ho chi minh - da hang', '0352342341', '2025-08-29 11:20:00.000000', 'PENDING', '2025-08-26 07:16:19.966773'),
(6, '0123412301', 'Quang Tri\r\nTrieu Phong', NULL, NULL, '2025-08-27 17:11:48.664375', 'hoaicalm@gmail.com', '2025-09-18 10:11:00.000000', '2025-08-27 17:11:48.662376', 'system', 'https://res.cloudinary.com/dioi8edng/image/upload/v1756314707/contracts/file_1756314702473.jpg.jpg', 'Đầ Nẵng - Quảng Trị', '0356138252', '2025-08-28 10:11:00.000000', 'PENDING', '2025-08-27 17:11:48.664375'),
(7, '0123412341', 'Quang Tri\r\nTrieu Phong', NULL, '2025-09-15 15:10:14.523788', '2025-09-15 15:06:56.763550', 'antoinnguyen2@gmail.com', '2025-09-30 08:06:00.000000', '2025-09-15 15:10:14.522757', 'admin', 'https://res.cloudinary.com/dioi8edng/image/upload/v1757948816/contracts/file_1757948813492.jpg.jpg', 'Đầ Nẵng - Quảng Trị', '09012345611', '2025-09-16 08:06:00.000000', 'ACCEPTED', '2025-09-15 15:10:14.664391');

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees` (
  `driver_license_number` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `operator_id` bigint(20) DEFAULT NULL,
  `employee_type` enum('DRIVER','STAFF') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`driver_license_number`, `id`, `operator_id`, `employee_type`) VALUES
('DL678901', 8, 2, 'DRIVER'),
('DL27292372', 17, 1, 'DRIVER'),
('DL2398232', 19, 1, 'DRIVER'),
('DL092322', 21, 1, 'DRIVER'),
('DL2938232', 22, 1, 'DRIVER'),
('DL2321322', 32, 1, 'DRIVER'),
(NULL, 33, 8, 'DRIVER'),
('sdafasdf', 40, 1, 'DRIVER'),
('31231234', 44, 10, 'DRIVER'),
('23452345', 46, 10, 'DRIVER'),
('239832832', 53, 10, 'DRIVER'),
('DL020323223', 54, 10, 'DRIVER'),
('DL090901', 56, 10, 'DRIVER'),
('', 57, 10, 'STAFF'),
(NULL, 58, 10, 'STAFF');

-- --------------------------------------------------------

--
-- Table structure for table `locations`
--

CREATE TABLE `locations` (
  `location_id` bigint(20) NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `region` enum('CENTRAL','NORTH','SOUTH') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `locations`
--

INSERT INTO `locations` (`location_id`, `address`, `city`, `latitude`, `longitude`, `name`, `region`) VALUES
(1, '292 Đinh Bộ Lĩnh, P.26, Bình Thạnh', 'TP.HCM', 10.782, 106.693, 'Bến xe Miền Đông - Cổng 3', 'CENTRAL'),
(2, '6 Giải Phóng, P. Giáp Bát, Hoàng Mai', 'Hà Nội', 20.98037, 105.8389251, 'Bến xe Giáp Bát', 'CENTRAL'),
(3, '01 Tô Hiến Thành, P.3', 'Đà Lạt', 11.94, 108.437, 'Bến xe Đà Lạt', 'CENTRAL'),
(4, '97 An Dương Vương, P. An Cựu', 'Huế', 16.467, 107.595, 'Bến xe Huế', 'CENTRAL'),
(5, '91B Nguyễn Văn Linh, P. An Khánh, Ninh Kiều', 'Cần Thơ', 10.045, 105.746, 'Bến xe Cần Thơ', 'CENTRAL'),
(6, '192 Nam Kỳ Khởi Nghĩa, P.3', 'Vũng Tàu', 10.346, 107.084, 'Bến xe Vũng Tàu', 'CENTRAL'),
(7, '58 Đường 23/10, P. Phương Sơn', 'Nha Trang', 12.25, 109.194, 'Bến xe Nha Trang', 'CENTRAL');

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `id` bigint(20) NOT NULL,
  `action_url` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `metadata` varchar(255) DEFAULT NULL,
  `read_at` datetime(6) DEFAULT NULL,
  `related_id` varchar(255) DEFAULT NULL,
  `status` enum('ARCHIVED','READ','UNREAD') DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `type` enum('MONTHLY_REPORT','OPERATOR_REGISTRATION','REVENUE_MILESTONE','SYSTEM_ALERT','WEEKLY_REPORT') DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`id`, `action_url`, `created_at`, `message`, `metadata`, `read_at`, `related_id`, `status`, `title`, `type`, `user_id`, `is_deleted`) VALUES
(1, '/admin/reports/monthly?month=8&year=2025', '2025-08-24 12:37:01.301597', '? Tổng doanh thu: 5,690,000 VNĐ\n? Nhà xe hoạt động: 5\n? Tổng chuyến xe: 11\n? Tổng hành khách: 12\n\n? Nhà xe doanh thu cao nhất: Nhà Xe ABC (5,690,000 VNĐ)\n\n? Tệp đính kèm: monthly_report_2025_08_20250824_123701.pdf\n\n? Nhấn để xem báo cáo chi tiết', '.\\storage\\reports\\monthly_report_2025_08_20250824_123701.pdf', '2025-08-24 13:23:29.230859', '2025-08', 'READ', '? Báo Cáo Doanh Thu Tháng tháng 8 2025', 'MONTHLY_REPORT', 10, 0),
(2, '/api/bus-operators/admin/reports/monthly?month=8&year=2025', '2025-08-24 13:07:15.623500', '? Tổng doanh thu: 5,690,000 VNĐ\n? Nhà xe hoạt động: 5\n? Tổng chuyến xe: 11\n? Tổng hành khách: 12\n\n? Nhà xe doanh thu cao nhất: Nhà Xe ABC (5,690,000 VNĐ)\n\n? Tệp đính kèm: monthly_report_2025_08_20250824_130715.pdf\n\n? Nhấn để xem báo cáo chi tiết', '.\\storage\\reports\\monthly_report_2025_08_20250824_130715.pdf', '2025-08-25 14:51:31.047150', '2025-08', 'READ', '? Báo Cáo Doanh Thu Tháng tháng 8 2025', 'MONTHLY_REPORT', 10, 0),
(3, '/api/bus-operators/admin/monthly-reports?month=8&year=2025', '2025-08-24 13:09:58.854174', '? Tổng doanh thu: 5,690,000 VNĐ\n? Nhà xe hoạt động: 5\n? Tổng chuyến xe: 11\n? Tổng hành khách: 12\n\n? Nhà xe doanh thu cao nhất: Nhà Xe ABC (5,690,000 VNĐ)\n\n? Tệp đính kèm: monthly_report_2025_08_20250824_130958.pdf\n\n? Nhấn để xem báo cáo chi tiết', '.\\storage\\reports\\monthly_report_2025_08_20250824_130958.pdf', '2025-08-25 14:50:59.997483', '2025-08', 'READ', '? Báo Cáo Doanh Thu Tháng tháng 8 2025', 'MONTHLY_REPORT', 10, 0),
(4, '/api/bus-operators/admin/monthly-reports?month=7&year=2025', '2025-08-25 11:12:31.510983', '? Tổng doanh thu: 0 VNĐ\n? Nhà xe hoạt động: 5\n? Tổng chuyến xe: 0\n? Tổng hành khách: 0\n\n? Nhà xe doanh thu cao nhất: Nhà Xe ABC (0 VNĐ)\n\n? Tệp đính kèm: monthly_report_2025_07_20250825_111231.pdf\n\n? Nhấn để xem báo cáo chi tiết', NULL, NULL, NULL, 'READ', '? Báo Cáo Doanh Thu Tháng tháng 7 2025', 'MONTHLY_REPORT', 10, NULL),
(5, '/api/bus-operators/admin/monthly-reports?month=7&year=2025', '2025-08-25 11:12:35.833640', '? Tổng doanh thu: 0 VNĐ\n? Nhà xe hoạt động: 5\n? Tổng chuyến xe: 0\n? Tổng hành khách: 0\n\n? Nhà xe doanh thu cao nhất: Nhà Xe ABC (0 VNĐ)\n\n? Tệp đính kèm: monthly_report_2025_07_20250825_111235.pdf\n\n? Nhấn để xem báo cáo chi tiết', NULL, NULL, NULL, 'READ', '? Báo Cáo Doanh Thu Tháng tháng 7 2025', 'MONTHLY_REPORT', 10, NULL),
(6, '/api/bus-operators/admin/monthly-reports?month=7&year=2025', '2025-08-25 11:12:40.782652', '? Tổng doanh thu: 0 VNĐ\n? Nhà xe hoạt động: 5\n? Tổng chuyến xe: 0\n? Tổng hành khách: 0\n\n? Nhà xe doanh thu cao nhất: Nhà Xe ABC (0 VNĐ)\n\n? Tệp đính kèm: monthly_report_2025_07_20250825_111240.pdf\n\n? Nhấn để xem báo cáo chi tiết', NULL, NULL, NULL, 'READ', '? Báo Cáo Doanh Thu Tháng tháng 7 2025', 'MONTHLY_REPORT', 10, NULL),
(7, '/api/bus-operators/admin/monthly-reports?month=7&year=2025', '2025-08-25 11:12:45.774707', '? Tổng doanh thu: 0 VNĐ\n? Nhà xe hoạt động: 5\n? Tổng chuyến xe: 0\n? Tổng hành khách: 0\n\n? Nhà xe doanh thu cao nhất: Nhà Xe ABC (0 VNĐ)\n\n? Tệp đính kèm: monthly_report_2025_07_20250825_111245.pdf\n\n? Nhấn để xem báo cáo chi tiết', NULL, NULL, NULL, 'READ', '? Báo Cáo Doanh Thu Tháng tháng 7 2025', 'MONTHLY_REPORT', 10, NULL),
(8, '/api/bus-operators/admin/monthly-reports?month=7&year=2025', '2025-08-25 11:12:50.770700', '? Tổng doanh thu: 0 VNĐ\n? Nhà xe hoạt động: 5\n? Tổng chuyến xe: 0\n? Tổng hành khách: 0\n\n? Nhà xe doanh thu cao nhất: Nhà Xe ABC (0 VNĐ)\n\n? Tệp đính kèm: monthly_report_2025_07_20250825_111250.pdf\n\n? Nhấn để xem báo cáo chi tiết', NULL, NULL, NULL, 'READ', '? Báo Cáo Doanh Thu Tháng tháng 7 2025', 'MONTHLY_REPORT', 10, NULL),
(9, '/api/bus-operators/admin/monthly-reports?month=8&year=2025', '2025-08-25 14:12:09.216074', '? Tổng doanh thu: 5,690,000 VNĐ <br>? Nhà xe hoạt động: 5<br>? Tổng chuyến xe: 12<br>? Tổng hành khách: 12<br><br>? Nhà xe doanh thu cao nhất: Nhà Xe ABC (5,690,000 VNĐ)<br><br>? Tệp đính kèm: monthly_report_2025_08_20250825_141209.pdf<br><br>? Nhấn để xem báo cáo chi tiết', NULL, NULL, NULL, 'READ', '? Báo Cáo Doanh Thu Tháng tháng 8 2025', 'MONTHLY_REPORT', 10, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `payment_id` bigint(20) NOT NULL,
  `amount` decimal(38,2) NOT NULL,
  `paid_at` datetime(6) DEFAULT NULL,
  `status` enum('completed','failed','pending','refunded') NOT NULL,
  `transaction_code` varchar(255) DEFAULT NULL,
  `booking_id` bigint(20) NOT NULL,
  `payment_gateway_id` varchar(255) DEFAULT NULL,
  `payment_method` enum('BANK_TRANSFER','CREDIT_CARD','PAYPAL','PAY_LATER','VNPAY') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`payment_id`, `amount`, `paid_at`, `status`, `transaction_code`, `booking_id`, `payment_gateway_id`, `payment_method`) VALUES
(1, 500000.00, '2025-07-24 12:05:00.000000', 'completed', 'TX123456', 6, NULL, 'BANK_TRANSFER'),
(2, 150000.00, '2025-07-24 13:05:00.000000', 'completed', 'TX123457', 2, NULL, 'BANK_TRANSFER'),
(3, 250000.00, NULL, 'pending', 'TX123458', 3, NULL, 'BANK_TRANSFER'),
(4, 200000.00, '2025-07-24 15:05:00.000000', 'completed', 'TX123459', 4, NULL, 'BANK_TRANSFER'),
(5, 220000.00, '2025-07-24 16:05:00.000000', 'completed', 'TX123460', 5, NULL, 'BANK_TRANSFER'),
(25, 120000.00, '2025-08-17 15:57:18.391431', 'completed', 'TXN431FD8BB4024', 12, NULL, 'VNPAY'),
(26, 330000.00, '2025-08-18 04:39:34.278877', 'completed', 'TXN9590B3E8DF64', 14, NULL, 'VNPAY'),
(27, 330000.00, '2025-08-18 05:22:08.571127', 'completed', 'TXN5376F19330E6', 15, NULL, 'VNPAY'),
(28, 330000.00, '2025-08-18 05:26:06.605016', 'completed', 'TXN6476C35277D3', 16, NULL, 'VNPAY'),
(29, 660000.00, '2025-08-18 05:31:50.831625', 'completed', 'TXN5CBE26FEE2DA', 17, NULL, 'VNPAY'),
(30, 660000.00, '2025-08-18 05:37:29.980577', 'completed', 'TXNF2B556C256C5', 18, NULL, 'VNPAY'),
(31, 120000.00, '2025-08-18 05:43:27.214380', 'completed', 'TXN2EC63DEE0D0B', 13, NULL, 'VNPAY'),
(32, 660000.00, '2025-08-18 05:47:57.777204', 'completed', 'TXN7A2B20D9BD19', 19, NULL, 'VNPAY'),
(33, 660000.00, '2025-08-18 05:56:17.583103', 'completed', 'TXNE3DFFA0A1FFE', 20, NULL, 'VNPAY'),
(34, 660000.00, '2025-08-18 06:03:27.862159', 'completed', 'TXN4C99A1C9933C', 21, NULL, 'VNPAY'),
(35, 660000.00, '2025-08-18 07:01:10.240391', 'completed', 'TXNED357273D4AA', 22, NULL, 'VNPAY'),
(36, 156000.00, '2025-08-25 09:44:01.595598', 'completed', 'TXN7399358348DF', 24, NULL, 'VNPAY'),
(37, 156000.00, '2025-08-25 09:46:23.678269', 'completed', 'TXNB518282A19A7', 25, NULL, 'VNPAY'),
(38, 831000.00, '2025-08-26 07:10:55.096690', 'completed', 'TXN17B3A9AEAACB', 26, NULL, 'VNPAY'),
(39, 234000.00, NULL, 'pending', 'TXN0278AE574512', 27, NULL, 'VNPAY'),
(40, 234000.00, '2025-08-26 14:34:44.897120', 'completed', 'TXN3B323AE4E33E', 28, NULL, 'VNPAY'),
(41, 234000.00, '2025-08-26 16:53:06.930522', 'completed', 'TXNEBE28E28918E', 33, NULL, 'VNPAY'),
(42, 234000.00, NULL, 'pending', 'TXNF09200B035FE', 35, NULL, 'VNPAY'),
(43, 234000.00, '2025-08-26 17:11:30.812975', 'completed', 'TXN06AF506F7EBC', 36, NULL, 'VNPAY'),
(44, 234000.00, '2025-08-26 17:14:03.413438', 'completed', 'TXNDA2CBAA25670', 37, NULL, 'VNPAY'),
(45, 234000.00, NULL, 'pending', 'TXN86DFB079226D', 38, NULL, 'VNPAY'),
(46, 2454334.00, NULL, 'pending', 'TXN9A8DBFA430F0', 49, NULL, 'VNPAY'),
(47, 4908668.00, '2025-08-27 08:46:58.246154', 'completed', 'TXN5D15AE7D8BAF', 51, NULL, 'VNPAY'),
(48, 4908668.00, NULL, 'pending', 'TXN5FAB36FDCFFA', 53, NULL, 'VNPAY'),
(49, 3926934.00, '2025-08-27 09:16:34.635071', 'completed', 'TXN40F7ED480D50', 55, NULL, 'VNPAY'),
(50, 3926934.00, NULL, 'pending', 'TXNFC10BD76C75F', 60, NULL, 'VNPAY'),
(51, 2454334.00, '2025-08-27 14:35:26.296191', 'completed', 'TXN4AC15BF4C5E3', 61, NULL, 'VNPAY'),
(52, 2454334.00, NULL, 'pending', 'TXNECEB9EFEA451', 63, NULL, 'VNPAY'),
(53, 4908668.00, NULL, 'pending', 'TXNFD0CA2183374', 64, NULL, 'VNPAY'),
(54, 3908668.00, '2025-08-27 15:26:27.428079', 'completed', 'TXN1D3AF53E97A2', 66, NULL, 'VNPAY'),
(55, 300000.00, '2025-08-28 05:17:28.472897', 'completed', 'TXN1CE10056C2DA', 67, NULL, 'VNPAY'),
(56, 150000.00, '2025-08-28 06:19:40.721405', 'completed', 'TXN4BCD9D134166', 68, NULL, 'VNPAY'),
(57, 150000.00, '2025-08-28 06:27:03.781968', 'completed', 'TXN2714234FC6D1', 69, NULL, 'VNPAY'),
(58, 150000.00, '2025-08-28 06:30:45.686567', 'completed', 'TXN343CE9C99BBA', 70, NULL, 'VNPAY'),
(59, 150000.00, '2025-08-28 06:43:48.663700', 'completed', 'TXN3ACBFFD78BF2', 71, NULL, 'VNPAY'),
(60, 150000.00, '2025-08-28 06:47:37.956339', 'completed', 'TXN55EFAE5A4DAD', 72, NULL, 'VNPAY'),
(61, 150000.00, '2025-08-28 06:58:00.823172', 'completed', 'TXNC5EA786EFEF2', 73, NULL, 'VNPAY'),
(62, 150000.00, NULL, 'pending', 'TXN2E438460200F', 74, NULL, 'VNPAY'),
(63, 300000.00, '2025-08-28 07:21:18.414565', 'completed', 'TXNF347F52485B1', 75, NULL, 'VNPAY'),
(64, 300000.00, NULL, 'pending', 'TXNED9382BA1ABC', 76, NULL, 'VNPAY'),
(65, 300000.00, '2025-08-28 07:39:47.867533', 'completed', 'TXN68CF3DA1E73B', 77, NULL, 'VNPAY'),
(66, 300000.00, '2025-08-28 07:45:36.762021', 'completed', 'TXN9DB9D577C4D2', 78, NULL, 'VNPAY'),
(67, 300000.00, '2025-08-28 07:57:17.491604', 'completed', 'TXN0F047B8FB856', 79, NULL, 'VNPAY'),
(68, 300000.00, '2025-08-28 14:03:28.681600', 'completed', 'TXN1ADC6F0340DD', 80, NULL, 'VNPAY'),
(69, 300000.00, '2025-08-28 14:14:55.577381', 'completed', 'TXN1C09F2E300BE', 81, NULL, 'VNPAY'),
(70, 300000.00, '2025-08-28 14:17:28.505535', 'completed', 'TXN0A0D4AD85511', 82, NULL, 'VNPAY'),
(71, 300000.00, '2025-08-28 14:52:22.883300', 'completed', 'TXN217362CCBD17', 83, NULL, 'VNPAY'),
(72, 150000.00, NULL, 'pending', 'TXN3C2F6DFF2D0D', 90, NULL, 'VNPAY'),
(73, 300000.00, '2025-08-29 02:36:10.888347', 'completed', 'TXN25D57D1C8F18', 95, NULL, 'VNPAY'),
(74, 246000.00, '2025-08-29 05:32:31.285262', 'completed', 'TXNBFDB33AFD346', 96, NULL, 'VNPAY'),
(75, 500000.00, '2025-08-29 05:36:59.771391', 'completed', 'TXN627686ABE3A0', 97, NULL, 'VNPAY'),
(76, 300000.00, '2025-08-29 10:00:21.477535', 'completed', 'TXN01B893AEFE9B', 98, NULL, 'VNPAY'),
(77, 150000.00, NULL, 'pending', 'TXN3541070FAC24', 99, NULL, 'VNPAY'),
(78, 250000.00, NULL, 'pending', 'TXN4D4DE731C8BE', 100, NULL, 'VNPAY'),
(79, 212500.00, NULL, 'pending', 'TXNA165AF092789', 101, NULL, 'VNPAY'),
(91, 187200.00, NULL, 'pending', 'TXN6593D0B8F40D', 137, NULL, 'VNPAY'),
(95, 187200.00, NULL, 'pending', 'TXNB224BB06D0FA', 146, NULL, 'VNPAY'),
(96, 187200.00, NULL, 'pending', 'TXNEBCB27451187', 147, NULL, 'VNPAY'),
(99, 232000.00, '2025-09-04 16:42:12.110177', 'completed', 'TXN451663367E5E', 150, NULL, 'VNPAY'),
(100, 232000.00, NULL, 'pending', 'TXNA166F10CB397', 151, NULL, 'VNPAY'),
(101, 234000.00, NULL, 'pending', 'TXN0ACE72A0A2C5', 152, NULL, 'VNPAY'),
(102, 231000.00, NULL, 'pending', 'TXNE9271A4D2C9E', 153, NULL, 'VNPAY'),
(103, 234000.00, '2025-09-05 07:41:21.809430', 'completed', 'TXNAC4C005AFBE2', 154, NULL, 'VNPAY'),
(104, 250000.00, '2025-09-05 07:52:03.576285', 'completed', 'TXNF8A832955E32', 155, NULL, 'VNPAY'),
(105, 370400.00, '2025-09-05 14:15:37.585739', 'completed', 'TXNABF95EDB92D2', 156, NULL, 'VNPAY'),
(106, 299520.00, NULL, 'pending', 'TXN51C9824BDA57', 157, NULL, 'VNPAY'),
(107, 427280.00, NULL, 'pending', 'TXN738B0A47BF9B', 158, NULL, 'VNPAY'),
(108, 697600.00, '2025-09-09 15:03:18.889347', 'completed', 'TXN9EF25C577177', 159, NULL, 'VNPAY'),
(109, 697600.00, '2025-09-09 15:05:06.095084', 'completed', 'TXN380421171D87', 160, NULL, 'VNPAY'),
(110, 436000.00, '2025-09-09 17:36:51.731545', 'completed', 'TXN1126700DAAA1', 161, NULL, 'VNPAY'),
(111, 697600.00, '2025-09-10 04:36:25.885107', 'completed', 'TXNEC702058F465', 162, NULL, 'VNPAY'),
(112, 784800.00, '2025-09-10 05:55:52.677155', 'completed', 'TXN67B5B4508116', 163, NULL, 'VNPAY'),
(113, 872000.00, '2025-09-10 06:10:27.559464', 'completed', 'TXN97DAB779D68C', 164, NULL, 'VNPAY'),
(114, 872000.00, '2025-09-10 06:18:29.593235', 'completed', 'TXN55738D344176', 165, NULL, 'VNPAY'),
(115, 610400.00, '2025-09-10 06:20:36.706343', 'completed', 'TXN48F551125302', 166, NULL, 'VNPAY'),
(116, 697600.00, '2025-09-10 06:30:27.462156', 'completed', 'TXNF1F0690B11B9', 167, NULL, 'VNPAY'),
(117, 307000.00, NULL, 'pending', 'TXNFD92B6454F7D', 168, NULL, 'VNPAY'),
(118, 350000.00, '2025-09-10 08:34:38.809619', 'refunded', 'TXNECCB186942B3', 169, NULL, 'VNPAY'),
(119, 234000.00, NULL, 'pending', 'TXN752E6E4C84AD', 170, NULL, 'VNPAY'),
(120, 2698000.00, NULL, 'pending', 'TXN87B35607971D', 171, NULL, 'VNPAY'),
(121, 300000.00, '2025-09-10 15:14:35.622129', 'completed', 'TXNDEE918E81D84', 172, NULL, 'VNPAY'),
(122, 200000.00, '2025-09-10 15:31:11.581377', 'completed', 'TXN3F55A11FCD8A', 173, NULL, 'VNPAY'),
(123, 2000000.00, '2025-09-10 16:59:25.115374', 'completed', 'TXNAE64B8573928', 174, NULL, 'VNPAY'),
(124, 4500000.00, '2025-09-11 03:11:19.128752', 'refunded', 'TXN718F2E2109A0', 175, '15164588', 'VNPAY'),
(125, 6000000.00, NULL, 'failed', 'TXNBFBA8830EF37', 176, NULL, 'VNPAY'),
(126, 6000000.00, '2025-09-11 03:44:18.233174', 'refunded', 'TXN78AADB8F4D1A', 177, '15164629', 'VNPAY'),
(127, 6000000.00, '2025-09-11 06:05:48.760185', 'refunded', 'TXND552BF00F368', 178, '15164771', 'VNPAY'),
(128, 7500000.00, '2025-09-11 06:50:07.616455', 'refunded', 'TXND374C4728E0A', 179, '15164819', 'VNPAY'),
(129, 300000.00, '2025-09-11 06:56:56.557915', 'completed', 'TXNB61812F03D8C', 180, '15164836', 'VNPAY'),
(130, 900000.00, '2025-09-11 07:14:53.065969', 'completed', 'TXN7703F7091942', 181, '15164870', 'VNPAY'),
(131, 400000.00, '2025-09-11 07:17:35.301102', 'completed', 'TXN8B4C49D85E1D', 182, '15164876', 'VNPAY'),
(132, 400000.00, '2025-09-11 07:18:58.374295', 'completed', 'TXNEC922A59CF58', 183, '15164880', 'VNPAY'),
(133, 300000.00, '2025-09-11 07:27:34.525859', 'completed', 'TXN94DEC96A70DB', 184, '15164894', 'VNPAY'),
(134, 697600.00, '2025-09-11 14:33:20.001047', 'completed', 'TXN5E5752B8A315', 185, '15165416', 'VNPAY'),
(135, 1780000.00, '2025-09-11 14:44:40.652412', 'refunded', 'TXN43BEC937E035', 186, '15165428', 'VNPAY'),
(136, 200000.00, '2025-09-11 14:54:48.373487', 'completed', 'TXN6EDCA2D97397', 187, '15165442', 'VNPAY'),
(137, 623000.00, '2025-09-11 15:57:26.515357', 'refunded', 'TXNDCCB74F16521', 189, '15165502', 'VNPAY'),
(138, 100000.00, '2025-09-11 16:13:24.375954', 'completed', 'TXN3F61CFA611EC', 190, '15165515', 'VNPAY'),
(139, 200000.00, '2025-09-11 16:25:07.941407', 'completed', 'TXNE8B10E83F13F', 191, '15165524', 'VNPAY'),
(140, 872000.00, '2025-09-11 16:30:01.460257', 'refunded', 'TXN89F3E417D645', 192, '15165526', 'VNPAY'),
(141, 872000.00, '2025-09-11 16:35:57.170829', 'refunded', 'TXN03C581C1E801', 193, '15165529', 'VNPAY'),
(142, 900000.00, NULL, 'pending', 'TXN07538F2C0FBF', 194, NULL, 'VNPAY'),
(143, 960000.00, NULL, 'pending', 'TXN6714AAC10723', 208, NULL, 'VNPAY'),
(144, 409500.00, NULL, 'pending', 'TXN023DD6120770', 209, NULL, 'VNPAY'),
(145, 500000.00, NULL, 'pending', 'TXNF8C289BC9166', 210, NULL, 'VNPAY'),
(146, 3040000.00, NULL, 'pending', 'TXNF971DA1A5C6A', 211, NULL, 'VNPAY'),
(147, 1602000.00, NULL, 'pending', 'TXNA22B547ABCBA', 212, NULL, 'VNPAY'),
(148, 385000.00, NULL, 'pending', 'TXN366FC5BC29B5', 213, NULL, 'VNPAY'),
(149, 499000.00, NULL, 'pending', 'TXND0DCBA1F7249', 214, NULL, 'VNPAY'),
(150, 3800000.00, '2025-09-18 03:03:01.241539', 'completed', 'TXN8D8D27C5EDF7', 215, '15172117', 'VNPAY'),
(151, 500000.00, NULL, 'pending', 'TXN7DD92BEC8CA6', 217, NULL, 'VNPAY'),
(152, 390000.00, '2025-09-19 15:22:04.308058', 'completed', 'TXNE12D28A9E149', 218, '15174342', 'VNPAY'),
(153, 3000000.00, '2025-09-20 02:40:07.000000', 'refunded', 'TXN5B363EF6D66E', 220, '15174541', 'VNPAY'),
(154, 2400000.00, NULL, 'pending', 'TXN254E25224923', 221, NULL, 'VNPAY'),
(155, 1920000.00, NULL, 'pending', 'TXNFE53FB212D3A', 222, NULL, 'VNPAY'),
(156, 1440000.00, NULL, 'pending', 'TXN0D3C5D6EDBB9', 223, NULL, 'VNPAY');

-- --------------------------------------------------------

--
-- Table structure for table `permission`
--

CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `permissions`
--

CREATE TABLE `permissions` (
  `id` bigint(20) NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `profiles`
--

CREATE TABLE `profiles` (
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `status` enum('active','inactive','suspended') NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `profiles`
--

INSERT INTO `profiles` (`address`, `created_at`, `full_name`, `phone_number`, `status`, `updated_at`, `id`, `user_id`) VALUES
('456 Nguyễn Trãi, TP.HCM', '2025-07-24 10:00:00.000000', 'Trần Thị Khách', '0987654321', 'active', '2025-07-24 10:00:00.000000', 1, NULL),
('789 Lê Lợi, Đà Nẵng', '2025-07-24 10:00:00.000000', 'Lê Văn Nhà Xe', '0901234567', 'active', '2025-07-24 10:00:00.000000', 3, NULL),
('12 Phạm Ngọc Thạch, Đà Lạt', '2025-07-24 11:00:00.000000', 'Nguyễn Thị Nhà Xe', '0931234567', 'active', '2025-07-24 11:00:00.000000', 5, NULL),
('34 Hai Bà Trưng, Huế', '2025-07-24 11:00:00.000000', 'Lê Văn Khách', '0976543210', 'active', '2025-07-24 11:00:00.000000', 6, NULL),
('56 Nguyễn Huệ, Hà Nội', '2025-07-24 11:00:00.000000', 'Phạm Thị Hành Khách', '0965432109', 'active', '2025-07-24 11:00:00.000000', 7, NULL),
('78 Lê Duẩn, Đà Nẵng', '2025-07-24 11:00:00.000000', 'Trần Văn Tài Xế', '0943210987', 'suspended', '2025-09-09 08:20:50.128804', 8, NULL),
('ĐA', '2025-08-16 20:11:37.040784', 'Nguyễn Văn A', '0967070842', 'suspended', '2025-08-20 03:10:45.294043', 17, NULL),
('Đà Nẵng', '2025-08-17 14:00:30.527919', 'Antoin', '0123456789', 'active', '2025-09-17 21:36:47.992697', 18, NULL),
('Đà Nẵng', '2025-08-20 02:53:11.094487', 'Trần Văn Hùng', '0129328232', 'active', '2025-09-11 14:37:01.204854', 19, NULL),
('Đà Nẵng', '2025-08-20 03:32:28.946466', 'Trần Hùng Anh', '0906030013', 'inactive', '2025-09-12 03:15:45.006315', 21, NULL),
('Đà Nẵng', '2025-08-20 14:26:27.393212', 'Hoàng Văn Hoài', '0129328232', 'active', '2025-09-17 21:41:20.499486', 22, NULL),
(NULL, '2025-08-21 03:50:10.693659', 'Duong_Customer', '0123456789', 'active', '2025-09-10 17:17:54.771474', 24, NULL),
('ádfad', '2025-08-25 14:13:24.837520', 'hoàng hoài', '0901234561', 'active', '2025-09-18 02:40:27.798702', 26, NULL),
('123 ABC Street, Ho Chi Minh City', '2025-08-26 02:03:51.032037', 'HOAICODER2605 COMPANY', '0123456789', 'active', '2025-08-26 02:14:16.635154', 27, NULL),
('adsfadsf', '2025-08-26 07:19:43.000000', 'HOAI19800 COMPANY', '0123124121', 'active', '2025-09-24 05:39:51.000000', 28, NULL),
(NULL, '2025-08-26 14:43:05.235487', 'NGUYỄN TÔ DƯƠNG', '0123456789', 'active', '2025-08-26 14:43:32.217913', 29, NULL),
('ok', '2025-08-26 14:50:24.889451', 'Duong', '0123456789', 'inactive', '2025-08-29 10:17:45.786183', 30, NULL),
(NULL, '2025-08-26 15:12:57.326924', 'dg', '0123456789', 'inactive', '2025-09-17 21:40:03.888965', 31, NULL),
('Da Nang', '2025-08-26 20:23:03.583881', 'Hạo Thiên Khuyển', '0906030012', 'active', '2025-09-17 07:21:24.759842', 32, NULL),
(NULL, '2025-08-26 20:28:49.210547', 'Nguyễn Bá Quang', NULL, 'active', '2025-09-17 07:21:00.476159', 33, NULL),
(NULL, '2025-08-27 04:50:28.327881', 'aaaaaaaaaaaaaaaaaaaaaaa', '0963872757', 'inactive', '2025-09-16 04:02:41.997502', 34, NULL),
(NULL, '2025-08-27 16:46:44.071800', 'Hoai', '09012345611', 'active', '2025-08-27 16:46:44.071800', 35, NULL),
(NULL, '2025-08-28 02:27:42.540102', 'Antoin', '0123456789', 'active', '2025-09-17 08:26:23.518713', 36, NULL),
('Dn', '2025-08-29 12:49:23.489802', 'cus1', '0121345463', 'inactive', '2025-08-29 12:49:40.915259', 37, NULL),
('dna', '2025-08-29 13:37:36.836303', 'DƯƠNG NT', '0121345463', 'inactive', '2025-09-05 16:52:02.522605', 38, NULL),
(NULL, '2025-08-29 15:49:04.373791', 'Kiếm tiền cưới vợ', '1234567899', 'active', '2025-09-17 06:37:38.970816', 39, NULL),
('Quang Tri', '2025-09-05 08:39:29.564977', 'Nguyễn Hoàng Anh', '0356138252', 'active', '2025-09-17 07:20:41.415388', 40, NULL),
('DNA', '2025-09-05 17:02:00.808241', 'THƯƠNG EM', '0868688868', 'inactive', '2025-09-05 17:02:06.323810', 41, NULL),
('DNG', '2025-09-09 07:31:15.451952', 'User', '0121345463', 'inactive', '2025-09-09 07:48:59.944239', 42, NULL),
('Quang Tri\r\nTrieu Phong', '2025-09-15 15:10:14.000000', 'ANTOINNGUYEN2 COMPANY', '09012345611', 'active', '2025-09-20 01:10:17.000000', 43, NULL),
('Quang Tri', '2025-09-15 15:11:24.166301', 'hoàng hoài', '0356138252', 'active', '2025-09-17 06:38:46.453785', 44, NULL),
('ádfad', '2025-09-15 15:14:10.079174', 'hvhoai', '0901234567', 'active', '2025-09-15 15:14:33.430137', 46, NULL),
('Da Nang', '2025-09-17 03:33:53.702032', 'Nguyen Van b', '0129231232', 'active', '2025-09-17 06:27:27.321702', 53, NULL),
('Da Nang', '2025-09-17 04:15:29.000000', 'Antoin', '0906030012', 'active', '2025-09-20 01:29:19.000000', 54, NULL),
('adfasdfe', '2025-09-17 18:16:15.000000', 'Duong', '0961724356', 'active', '2025-09-20 01:26:30.000000', 55, NULL),
('Đà Nẵng', '2025-09-18 02:47:37.506793', 'Nguyễn An Quốc', '09012345678', 'active', '2025-09-18 03:03:47.744106', 56, NULL),
('Cam Le', '2025-09-18 03:00:57.000000', 'Tôn Bá Trung', '0123456789', 'active', '2025-09-20 01:40:18.000000', 57, NULL),
(NULL, '2025-09-18 04:50:00.752734', 'Đặng Văn Đức', NULL, 'active', '2025-09-18 04:50:00.752734', 58, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `promotions`
--

CREATE TABLE `promotions` (
  `promotion_id` bigint(20) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `discount_type` enum('FIXED_AMOUNT','PERCENTAGE') NOT NULL,
  `discount_value` decimal(38,2) NOT NULL,
  `end_date` date NOT NULL,
  `min_order_value` decimal(38,2) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `promotion_type` enum('auto','coupon') DEFAULT NULL,
  `start_date` date NOT NULL,
  `status` enum('active','expired','inactive') NOT NULL,
  `usage_limit` int(11) DEFAULT NULL,
  `campaign_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `promotions`
--

INSERT INTO `promotions` (`promotion_id`, `code`, `discount_type`, `discount_value`, `end_date`, `min_order_value`, `priority`, `promotion_type`, `start_date`, `status`, `usage_limit`, `campaign_id`) VALUES
(2, NULL, 'PERCENTAGE', 20.00, '2025-10-22', 20000.00, NULL, 'coupon', '2025-09-17', 'inactive', 100, NULL),
(4, NULL, 'PERCENTAGE', 15.00, '2025-09-25', NULL, 1, 'coupon', '2025-09-24', 'active', 11, 5),
(10, NULL, 'PERCENTAGE', 20.00, '2025-10-14', 100000.00, 1, 'auto', '2025-09-17', 'active', NULL, NULL),
(13, NULL, 'PERCENTAGE', 20.00, '2025-10-31', 100000.00, 1, 'auto', '2025-09-25', 'active', NULL, 5),
(14, 'QV5IEM', 'PERCENTAGE', 20.00, '2025-10-22', 20000.00, NULL, 'coupon', '2025-09-17', 'active', 100, NULL),
(15, NULL, 'PERCENTAGE', 20.00, '2025-10-30', 20000.00, NULL, 'coupon', '2025-09-24', 'active', 100, 5),
(24, '152P6V', 'PERCENTAGE', 23.00, '2025-09-24', NULL, 1, 'coupon', '2025-09-18', 'active', NULL, NULL),
(25, NULL, 'FIXED_AMOUNT', 10000.00, '2025-10-23', NULL, 0, 'auto', '2025-09-19', 'active', NULL, 5),
(26, NULL, 'PERCENTAGE', 10.00, '2025-10-22', NULL, 1, 'auto', '2025-09-24', 'active', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `promotion_campaigns`
--

CREATE TABLE `promotion_campaigns` (
  `campaign_id` bigint(20) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `banner_url` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `promotion_campaigns`
--

INSERT INTO `promotion_campaigns` (`campaign_id`, `active`, `banner_url`, `description`, `end_date`, `start_date`, `title`, `deleted`) VALUES
(1, b'1', 'https://res.cloudinary.com/dioi8edng/image/upload/v1757316696/campaigns/file_1757316693422.jpg.jpg', 'hehe1', '2025-10-24', '2025-09-15', 'Chào mừng lễ', 1),
(2, b'1', 'https://res.cloudinary.com/dioi8edng/image/upload/v1757343228/campaigns/file_1757343226939.jpg.jpg', 'hehe', '2025-10-17', '2025-09-09', 'Chào mừng lễ', 1),
(3, b'1', 'https://res.cloudinary.com/dioi8edng/image/upload/v1757343266/campaigns/file_1757343265738.jpg.jpg', 'hehe', '2025-10-16', '2025-09-09', 'Chào mừng lễ', 1),
(4, b'1', 'https://res.cloudinary.com/dioi8edng/image/upload/v1757391066/campaigns/file_1757391063939.jpg.jpg', 'tết', '2025-10-29', '2025-09-17', 'tết', 1),
(5, b'1', 'https://res.cloudinary.com/dioi8edng/image/upload/v1757407490/campaigns/file_1757407487484.jpg.jpg', 'Giảm giá 30% cho toàn bộ sản phẩm trong mùa hè.', '2025-10-23', '2025-09-19', 'Khuyến mãi thu rực rỡ', 0),
(6, b'1', 'https://res.cloudinary.com/dioi8edng/image/upload/v1757428234/campaigns/file_1757428231326.jpg.jpg', '12 ', '2025-09-26', '2025-09-18', 'Tết 2025', 1);

-- --------------------------------------------------------

--
-- Table structure for table `promotion_conditions`
--

CREATE TABLE `promotion_conditions` (
  `id` bigint(20) NOT NULL,
  `condition_type` enum('COMPLETE_SURVEY','FIRST_PURCHASE','REFERRAL_COUNT','VIEW_VIDEO','WATCH_AD') NOT NULL,
  `condition_value` varchar(255) DEFAULT NULL,
  `is_required` bit(1) NOT NULL,
  `promotion_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `promotion_conditions`
--

INSERT INTO `promotion_conditions` (`id`, `condition_type`, `condition_value`, `is_required`, `promotion_id`) VALUES
(2, 'VIEW_VIDEO', 'v=LSVvtlxrO7I', b'1', 15);

-- --------------------------------------------------------

--
-- Table structure for table `promotion_user`
--

CREATE TABLE `promotion_user` (
  `promotion_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `refunds`
--

CREATE TABLE `refunds` (
  `refund_id` bigint(20) NOT NULL,
  `cancellation_fee` decimal(10,2) DEFAULT 0.00,
  `completed_at` datetime(6) DEFAULT NULL,
  `gateway_refund_id` varchar(100) DEFAULT NULL,
  `gateway_response` text DEFAULT NULL,
  `net_refund_amount` decimal(10,2) NOT NULL,
  `notes` text DEFAULT NULL,
  `processed_at` datetime(6) DEFAULT NULL,
  `refund_amount` decimal(10,2) NOT NULL,
  `refund_reason` varchar(500) DEFAULT NULL,
  `refund_transaction_code` varchar(100) DEFAULT NULL,
  `requested_at` datetime(6) NOT NULL,
  `status` enum('CANCELLED','COMPLETED','FAILED','PENDING','PROCESSING','REJECTED') NOT NULL,
  `payment_id` bigint(20) NOT NULL,
  `requested_by` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `refunds`
--

INSERT INTO `refunds` (`refund_id`, `cancellation_fee`, `completed_at`, `gateway_refund_id`, `gateway_response`, `net_refund_amount`, `notes`, `processed_at`, `refund_amount`, `refund_reason`, `refund_transaction_code`, `requested_at`, `status`, `payment_id`, `requested_by`) VALUES
(2, 300000.00, '2025-09-11 04:26:00.187720', '15164681', '{\"vnp_ResponseId\":\"140eb03ca5bf47118e6db285e0e3e516\",\"vnp_Command\":\"refund\",\"vnp_ResponseCode\":\"00\",\"vnp_Message\":\"Refund success\",\"vnp_TmnCode\":\"ZSO520R7\",\"vnp_TxnRef\":\"TXN78AADB8F4D1A\",\"vnp_Amount\":\"570000000\",\"vnp_OrderInfo\":\"Same day cancellation - customer emergency\",\"vnp_BankCode\":\"NCB\",\"vnp_PayDate\":\"20250911112601\",\"vnp_TransactionNo\":\"15164681\",\"vnp_TransactionType\":\"03\",\"vnp_TransactionStatus\":\"05\",\"vnp_SecureHash\":\"58111541400b8b8d5fac1e0672da22734f8fc6b7b3aaeb824840d3c9e729eb5f866a86b1c15aaf80e5d815588eec74b0c36e032388632f15d42bc1ea12e81dae\"}', 5700000.00, 'Cancelling within same day - should have 0% cancellation fee', '2025-09-11 04:26:00.187720', 6000000.00, 'Same day cancellation - customer emergency', 'REF_20250911_1757562326655_7A909DF9', '2025-09-11 03:45:26.656856', 'COMPLETED', 126, 28),
(3, 1800000.00, '2025-09-11 06:06:32.364943', '15164772', '{\"vnp_ResponseId\":\"03a4a7eaacc0475590a53126e31cbea1\",\"vnp_Command\":\"refund\",\"vnp_ResponseCode\":\"00\",\"vnp_Message\":\"Refund success\",\"vnp_TmnCode\":\"ZSO520R7\",\"vnp_TxnRef\":\"TXND552BF00F368\",\"vnp_Amount\":\"420000000\",\"vnp_OrderInfo\":\"Booking cancelled by user/operator\",\"vnp_BankCode\":\"NCB\",\"vnp_PayDate\":\"20250911130634\",\"vnp_TransactionNo\":\"15164772\",\"vnp_TransactionType\":\"03\",\"vnp_TransactionStatus\":\"05\",\"vnp_SecureHash\":\"a14f2fe86f0783452c794e38a605a5863c3b725fa6495b789294042f8615c5db2ee19543757a8d770bc0cf0cc7d34da51035c202ffb028808d9e621378673f6b\"}', 4200000.00, 'Automatic refund due to booking cancellation', '2025-09-11 06:06:32.364943', 6000000.00, 'Booking cancelled by user/operator', 'REF_20250911_1757570791082_502C371A', '2025-09-11 06:06:31.083516', 'COMPLETED', 127, 28),
(4, 2250000.00, '2025-09-11 06:50:52.625475', '15164821', '{\"vnp_ResponseId\":\"c47e3d0c97574ca59ed9f81d242bb182\",\"vnp_Command\":\"refund\",\"vnp_ResponseCode\":\"00\",\"vnp_Message\":\"Refund success\",\"vnp_TmnCode\":\"ZSO520R7\",\"vnp_TxnRef\":\"TXND374C4728E0A\",\"vnp_Amount\":\"525000000\",\"vnp_OrderInfo\":\"Booking cancelled by user/operator\",\"vnp_BankCode\":\"NCB\",\"vnp_PayDate\":\"20250911135054\",\"vnp_TransactionNo\":\"15164821\",\"vnp_TransactionType\":\"03\",\"vnp_TransactionStatus\":\"05\",\"vnp_SecureHash\":\"357ae30a21f39433a88ec9bb891b8ca100d13ffafc3cdb793f11d713b3148748d09be889f8fdbfbf07d418e2cf0ffd5b23fe94cb28bdd7dce77341bc162fbab0\"}', 5250000.00, 'Automatic refund due to booking cancellation', '2025-09-11 06:50:52.625475', 7500000.00, 'Booking cancelled by user/operator', 'REF_20250911_1757573451374_8141BD0F', '2025-09-11 06:50:51.374774', 'COMPLETED', 128, 28),
(5, 1350000.00, '2025-09-11 06:58:20.606099', '15164842', '{\"vnp_ResponseId\":\"347eb99dbb16420c8988094818d5567a\",\"vnp_Command\":\"refund\",\"vnp_ResponseCode\":\"00\",\"vnp_Message\":\"Refund success\",\"vnp_TmnCode\":\"ZSO520R7\",\"vnp_TxnRef\":\"TXN718F2E2109A0\",\"vnp_Amount\":\"315000000\",\"vnp_OrderInfo\":\"Booking cancelled by user/operator\",\"vnp_BankCode\":\"NCB\",\"vnp_PayDate\":\"20250911135822\",\"vnp_TransactionNo\":\"15164842\",\"vnp_TransactionType\":\"03\",\"vnp_TransactionStatus\":\"05\",\"vnp_SecureHash\":\"bbad93cb4c597d45d22060c2464fe74b5c763659ce81c2a18236909698ea17b79a76d1c854691bb01253ecda1cca6a7b90e5eccdc5447467e8c19b6a3fca33f3\"}', 3150000.00, 'Automatic refund due to booking cancellation', '2025-09-11 06:58:20.607054', 4500000.00, 'Booking cancelled by user/operator', 'REF_20250911_1757573899451_620D064F', '2025-09-11 06:58:19.451110', 'COMPLETED', 124, 28),
(6, 105000.00, '2025-09-11 07:28:21.241165', '15164897', '{\"vnp_ResponseId\":\"c30def0b9c804328bc2784b6f82449bf\",\"vnp_Command\":\"refund\",\"vnp_ResponseCode\":\"00\",\"vnp_Message\":\"Refund success\",\"vnp_TmnCode\":\"ZSO520R7\",\"vnp_TxnRef\":\"TXNECCB186942B3\",\"vnp_Amount\":\"24500000\",\"vnp_OrderInfo\":\"Booking cancelled by user/operator\",\"vnp_BankCode\":\"NCB\",\"vnp_PayDate\":\"20250911142821\",\"vnp_TransactionNo\":\"15164897\",\"vnp_TransactionType\":\"03\",\"vnp_TransactionStatus\":\"05\",\"vnp_SecureHash\":\"986117a300a8bdaf1dcc7613c6f1b1044cd30fb14aa501eb292e088333881fdb85a210b87b8240f9227e2d056390e15ab7d8d8ed819e606c2fe43b8746f09741\"}', 245000.00, 'Automatic refund due to booking cancellation', '2025-09-11 07:28:21.241165', 350000.00, 'Booking cancelled by user/operator', 'REF_20250911_1757575698603_77AB8F77', '2025-09-11 07:28:18.603991', 'COMPLETED', 118, 26),
(7, 534000.00, '2025-09-11 14:45:28.471394', '15165429', '{\"vnp_ResponseId\":\"e95c8332196949dc834e8d1e0ad5d666\",\"vnp_Command\":\"refund\",\"vnp_ResponseCode\":\"00\",\"vnp_Message\":\"Refund success\",\"vnp_TmnCode\":\"ZSO520R7\",\"vnp_TxnRef\":\"TXN43BEC937E035\",\"vnp_Amount\":\"124600000\",\"vnp_OrderInfo\":\"Booking cancelled by user/operator\",\"vnp_BankCode\":\"NCB\",\"vnp_PayDate\":\"20250911214531\",\"vnp_TransactionNo\":\"15165429\",\"vnp_TransactionType\":\"03\",\"vnp_TransactionStatus\":\"05\",\"vnp_SecureHash\":\"ac2fe5baed63f4918c0fd3685a38713763d94fad21fddf547d661b1b2f787f10e76f15558240e313adc469e8c95c14df42f2f0d1a099bc1cd0efdb1ba692b4e3\"}', 1246000.00, 'Automatic refund due to booking cancellation', '2025-09-11 14:45:28.471394', 1780000.00, 'Booking cancelled by user/operator', 'REF_20250911_1757601927090_408DEAD0', '2025-09-11 14:45:27.093505', 'COMPLETED', 135, 26),
(8, 261600.00, '2025-09-11 16:30:26.643992', '15165527', '{\"vnp_ResponseId\":\"7e8f9508202644daa07199c38699f470\",\"vnp_Command\":\"refund\",\"vnp_ResponseCode\":\"00\",\"vnp_Message\":\"Refund success\",\"vnp_TmnCode\":\"ZSO520R7\",\"vnp_TxnRef\":\"TXN89F3E417D645\",\"vnp_Amount\":\"61040000\",\"vnp_OrderInfo\":\"Booking cancelled by user/operator\",\"vnp_BankCode\":\"NCB\",\"vnp_PayDate\":\"20250911233029\",\"vnp_TransactionNo\":\"15165527\",\"vnp_TransactionType\":\"03\",\"vnp_TransactionStatus\":\"05\",\"vnp_SecureHash\":\"fce94dc10aa7339a2b922c3e877d8005a346665c3988ae2bdb95eaeb2ed84a279245e50f5e57fff34c1f4a5ca072fc561fdbd94537be96a5d353808022d72102\"}', 610400.00, 'Automatic refund due to booking cancellation', '2025-09-11 16:30:26.643992', 872000.00, 'Booking cancelled by user/operator', 'REF_20250911_1757608225323_678B6FE1', '2025-09-11 16:30:25.323893', 'COMPLETED', 140, 26),
(9, 261600.00, '2025-09-11 16:36:19.860953', '15165531', '{\"vnp_ResponseId\":\"a9754e52475a4a87b8f73bbf6cc80118\",\"vnp_Command\":\"refund\",\"vnp_ResponseCode\":\"00\",\"vnp_Message\":\"Refund success\",\"vnp_TmnCode\":\"ZSO520R7\",\"vnp_TxnRef\":\"TXN03C581C1E801\",\"vnp_Amount\":\"61040000\",\"vnp_OrderInfo\":\"Booking cancelled by user/operator\",\"vnp_BankCode\":\"NCB\",\"vnp_PayDate\":\"20250911233622\",\"vnp_TransactionNo\":\"15165531\",\"vnp_TransactionType\":\"03\",\"vnp_TransactionStatus\":\"05\",\"vnp_SecureHash\":\"d3a2c9cb2c5df56c7c61511f91afb30530dffc7226ba88fd73bec9b3ec96a63e4619cdcd675eba467c1f6be7d758c201678af5176127e241cef92bb8afd21797\"}', 610400.00, 'Automatic refund due to booking cancellation', '2025-09-11 16:36:19.861958', 872000.00, 'Booking cancelled by user/operator', 'REF_20250911_1757608578693_F16AB1A3', '2025-09-11 16:36:18.694016', 'COMPLETED', 141, 26),
(10, 186900.00, '2025-09-11 17:10:18.299315', '15165537', '{\"vnp_ResponseId\":\"99189a3bc04a491bab27800457bafc9e\",\"vnp_Command\":\"refund\",\"vnp_ResponseCode\":\"00\",\"vnp_Message\":\"Refund success\",\"vnp_TmnCode\":\"ZSO520R7\",\"vnp_TxnRef\":\"TXNDCCB74F16521\",\"vnp_Amount\":\"43610000\",\"vnp_OrderInfo\":\"Booking cancelled by user/operator\",\"vnp_BankCode\":\"NCB\",\"vnp_PayDate\":\"20250912001015\",\"vnp_TransactionNo\":\"15165537\",\"vnp_TransactionType\":\"03\",\"vnp_TransactionStatus\":\"05\",\"vnp_SecureHash\":\"25dc9855cafa49ee4b8d0ac3de4c134b81b34744f3848ccebc945d8aa7569254040a7cfe1de55179c728a4ee07ec9dc487c3d40197d1eb569f3813be2d89090f\"}', 436100.00, 'Automatic refund due to booking cancellation', '2025-09-11 17:10:18.299315', 623000.00, 'Booking cancelled by user/operator', 'REF_20250912_1757610615774_3034DEAE', '2025-09-11 17:10:15.774959', 'COMPLETED', 137, 31),
(11, 0.00, '2025-09-20 02:44:12.000000', '15174543', '{\"vnp_ResponseId\":\"d7eb55d7009544c3bea082345dd1e626\",\"vnp_Command\":\"refund\",\"vnp_ResponseCode\":\"00\",\"vnp_Message\":\"Refund success\",\"vnp_TmnCode\":\"ZSO520R7\",\"vnp_TxnRef\":\"TXN5B363EF6D66E\",\"vnp_Amount\":\"3000000\",\"vnp_OrderInfo\":\"Booking cancelled by user/operator\",\"vnp_BankCode\":\"NCB\",\"vnp_PayDate\":\"20250920094412\",\"vnp_TransactionNo\":\"15174543\",\"vnp_TransactionType\":\"03\",\"vnp_TransactionStatus\":\"05\",\"vnp_SecureHash\":\"bb05e7561eff5c9fdd81952c7ef0bdf4a3a177fe8a3b08d3452f411c2abbf915c0e835efd0b903f55b0c942c9ff2898a187f2fd445a8b0d11ffdd5ae55a43191\"}', 3000000.00, 'Automatic refund due to booking cancellation', '2025-09-20 02:44:12.000000', 3000000.00, 'Booking cancelled by user/operator', 'REF_20250920_1758336250566_5F145168', '2025-09-20 02:44:10.000000', 'COMPLETED', 153, 28);

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `review_id` bigint(20) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `rating` int(11) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `trip_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`review_id`, `comment`, `created_at`, `rating`, `customer_id`, `trip_id`) VALUES
(1, 'Chuyến đi tuyệt vời!', '2025-07-25 21:00:00.000000', 5, 3, 1),
(2, 'Chuyến đi thoải mái, tài xế thân thiện', '2025-07-25 13:00:00.000000', 4, 3, 2),
(3, 'Xe sạch sẽ nhưng đến muộn 15 phút', '2025-07-25 17:00:00.000000', 3, 3, 3),
(5, 'Điều hòa lúc có lúc không', '2025-07-25 18:00:00.000000', 3, 3, 5),
(8, 'Thái độ tài xế lồi lõm', '2025-08-04 14:02:28.672474', 2, 3, 2),
(9, 'Thái độ tài xế lồi lõm', '2025-08-04 14:09:49.583860', 2, 3, 2),
(10, 'djsdjskldskldsds', '2025-08-25 16:49:35.305259', 5, 2, 20),
(11, 'Somthing', '2025-09-11 09:58:34.800250', 5, 28, 25),
(12, 'How the fk is this shit work', '2025-09-11 09:59:57.313760', 5, 28, 38),
(13, 'tôi đánh giá xe tốt', '2025-09-11 23:18:23.210182', 5, 31, 33),
(14, 'xe chất lượng', '2025-09-11 23:19:17.783596', 4, 31, 33),
(15, 'chất lượng\n', '2025-09-11 23:21:29.377012', 5, 31, 33),
(16, 'xe chạy nhanh dód', '2025-09-11 23:44:35.993283', 3, 31, 33),
(17, 'dádadsdsd', '2025-09-11 23:46:00.731832', 2, 31, 33),
(18, 'fdcxcxcxcxc', '2025-09-12 00:06:06.168534', 5, 31, 33);

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`role_id`, `name`, `description`) VALUES
(1, 'ADMIN', NULL),
(2, 'CUSTOMER', NULL),
(9, 'OPERATOR', NULL),
(10, 'STAFF', NULL),
(11, 'CUSTOMER_SERVICE', NULL),
(12, 'DRIVER', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `role_permissions`
--

CREATE TABLE `role_permissions` (
  `role_id` int(11) NOT NULL,
  `permission_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `routes`
--

CREATE TABLE `routes` (
  `route_id` bigint(20) NOT NULL,
  `default_duration_minutes` int(11) DEFAULT NULL,
  `default_price` decimal(10,2) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `end_location_id` bigint(20) DEFAULT NULL,
  `start_location_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `routes`
--

INSERT INTO `routes` (`route_id`, `default_duration_minutes`, `default_price`, `name`, `end_location_id`, `start_location_id`) VALUES
(1, 720, 500000.00, 'Bến xe Miền Đông - Cổng 3 ⟶ Bến xe Giáp Bát', 2, 1),
(2, 180, 150000.00, 'Bến xe Miền Đông - Cổng 3 ⟶ Bến xe Đà Lạt', 3, 1),
(3, 480, 300000.00, 'Bến xe Đà Lạt ⟶ Bến xe Miền Đông - Cổng 3', 1, 3),
(4, 240, 200000.00, 'Bến xe Cần Thơ ⟶ Bến xe Vũng Tàu', 6, 5),
(5, 300, 220000.00, 'Bến xe Nha Trang ⟶ Bến xe Miền Đông - Cổng 3', 1, 7),
(6, 480, 300000.00, 'Bến xe Đà Lạt ⟶ Bến xe Huế', 4, 3),
(12, 720, 123232.00, 'Bến xe Miền Đông - Cổng 3 ⟶ Bến xe Giáp Bát', 2, 1),
(13, 320, 123000.00, 'Bến xe Nha Trang ⟶ Bến xe Giáp Bát', 2, 7),
(17, 180, 250000.00, 'Bến xe Nha Trang ⟶ Bến xe Đà Lạt', 3, 7),
(18, 340, 340000.00, 'Bến xe Huế ⟶ Bến xe Cần Thơ', 5, 4),
(19, 1000, 5000000.00, 'Bến xe Cần Thơ ⟶ Bến xe Nha Trang', 7, 5);

-- --------------------------------------------------------

--
-- Table structure for table `route_stops`
--

CREATE TABLE `route_stops` (
  `stop_order` int(11) DEFAULT NULL,
  `time_offset_from_start` int(11) DEFAULT NULL,
  `location_id` bigint(20) NOT NULL,
  `route_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `route_stops`
--

INSERT INTO `route_stops` (`stop_order`, `time_offset_from_start`, `location_id`, `route_id`) VALUES
(1, 30, 1, 19),
(2, 60, 3, 19);

-- --------------------------------------------------------

--
-- Table structure for table `scores`
--

CREATE TABLE `scores` (
  `score_id` bigint(20) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `points` int(11) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `scores`
--

INSERT INTO `scores` (`score_id`, `created_at`, `points`, `updated_at`, `user_id`) VALUES
(1, '2025-08-29 07:13:40.694340', 3, '2025-09-05 14:14:36.405703', 18),
(2, '2025-08-29 07:42:12.076789', 0, '2025-09-10 14:28:49.065485', 26),
(3, '2025-08-29 07:42:12.161079', 1, '2025-08-29 07:42:12.161079', 24),
(4, '2025-08-29 15:05:56.150762', 3, '2025-08-29 15:05:56.198770', 25),
(5, '2025-08-29 15:15:41.482790', 1, '2025-08-29 15:15:41.482790', 1),
(6, '2025-08-29 15:17:05.881777', 1, '2025-08-29 15:17:05.881777', 6),
(7, '2025-09-05 14:55:26.393258', 0, '2025-09-17 21:11:51.182120', 31);

-- --------------------------------------------------------

--
-- Table structure for table `score_history`
--

CREATE TABLE `score_history` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `points_added` int(11) NOT NULL,
  `booking_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `action_type` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `score_history`
--

INSERT INTO `score_history` (`id`, `created_at`, `points_added`, `booking_id`, `user_id`, `action_type`) VALUES
(1, '2025-08-29 07:13:40.703342', 2, 97, 18, ''),
(2, '2025-08-29 07:31:14.556720', 2, 96, 18, ''),
(3, '2025-08-29 07:42:11.943070', 2, 51, 18, ''),
(4, '2025-08-29 07:42:12.094781', 2, 55, 26, ''),
(5, '2025-08-29 07:42:12.183073', 1, 61, 24, ''),
(6, '2025-08-29 07:42:12.257261', 2, 66, 26, ''),
(7, '2025-08-29 14:38:42.533885', 1, 24, 18, ''),
(8, '2025-08-29 14:38:42.558884', 1, 25, 18, ''),
(9, '2025-08-29 14:44:01.447871', 1, 26, 26, ''),
(10, '2025-08-29 15:05:56.129761', 1, 28, 18, ''),
(11, '2025-08-29 15:05:56.154766', 1, 33, 25, ''),
(12, '2025-08-29 15:05:56.168780', 1, 36, 25, ''),
(13, '2025-08-29 15:05:56.192802', 1, 37, 25, ''),
(14, '2025-08-29 15:15:41.488284', 1, 1, 1, ''),
(15, '2025-08-29 15:15:41.500283', 3, 6, 18, ''),
(16, '2025-08-29 15:15:41.518574', 1, 12, 18, ''),
(17, '2025-08-29 15:15:41.537658', 1, 13, 18, ''),
(18, '2025-08-29 15:17:05.886780', 1, 5, 6, ''),
(20, '2025-09-04 16:38:59.887061', -2, 150, 18, ''),
(21, '2025-09-04 19:51:53.688560', -2, 151, 18, ''),
(22, '2025-09-05 02:28:09.418449', -3, 153, 18, ''),
(23, '2025-09-05 14:14:36.377706', -4, 156, 18, ''),
(24, '2025-09-05 14:55:26.400370', 1, 154, 31, ''),
(25, '2025-09-10 08:32:22.555392', -1, 168, 26, 'USED'),
(26, '2025-09-10 08:33:37.792448', -2, 169, 26, 'USED'),
(27, '2025-09-10 14:28:49.037582', -2, 171, 26, 'USED'),
(28, '2025-09-17 21:11:51.125181', -1, 214, 31, 'USED');

-- --------------------------------------------------------

--
-- Table structure for table `seat_layouts`
--

CREATE TABLE `seat_layouts` (
  `id` int(11) NOT NULL,
  `layout_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`layout_data`)),
  `name` varchar(255) NOT NULL
) ;

--
-- Dumping data for table `seat_layouts`
--

INSERT INTO `seat_layouts` (`id`, `layout_data`, `name`) VALUES
(1, '{\"cols\": 4, \"rows\": 10, \"floors\": 1}', 'Standard 40 seats'),
(2, '{\"cols\": 4, \"rows\": 10, \"floors\": 2}', 'Standard 32 seats'),
(3, '{\"cols\": 4, \"rows\": 10, \"floors\": 2}', 'Luxury 48 seats');

-- --------------------------------------------------------

--
-- Table structure for table `spring_session`
--

CREATE TABLE `spring_session` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint(20) NOT NULL,
  `LAST_ACCESS_TIME` bigint(20) NOT NULL,
  `MAX_INACTIVE_INTERVAL` int(11) NOT NULL,
  `EXPIRY_TIME` bigint(20) NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `spring_session`
--

INSERT INTO `spring_session` (`PRIMARY_ID`, `SESSION_ID`, `CREATION_TIME`, `LAST_ACCESS_TIME`, `MAX_INACTIVE_INTERVAL`, `EXPIRY_TIME`, `PRINCIPAL_NAME`) VALUES
('8024411e-d206-45c2-8e2d-d1ec73f71394', '2a190dd9-efa2-4d00-a019-1795d89c7bef', 1756176045144, 1756176136368, 1800, 1756177936368, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `spring_session_attributes`
--

CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `spring_session_attributes`
--

INSERT INTO `spring_session_attributes` (`SESSION_PRIMARY_ID`, `ATTRIBUTE_NAME`, `ATTRIBUTE_BYTES`) VALUES
('8024411e-d206-45c2-8e2d-d1ec73f71394', 'org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository.AUTHORIZATION_REQUEST', 0xaced00057372004c6f72672e737072696e676672616d65776f726b2e73656375726974792e6f61757468322e636f72652e656e64706f696e742e4f4175746832417574686f72697a6174696f6e52657175657374000000000000026c02000a4c00146164646974696f6e616c506172616d657465727374000f4c6a6176612f7574696c2f4d61703b4c000a6174747269627574657371007e00014c0016617574686f72697a6174696f6e4772616e74547970657400414c6f72672f737072696e676672616d65776f726b2f73656375726974792f6f61757468322f636f72652f417574686f72697a6174696f6e4772616e74547970653b4c0017617574686f72697a6174696f6e526571756573745572697400124c6a6176612f6c616e672f537472696e673b4c0010617574686f72697a6174696f6e55726971007e00034c0008636c69656e74496471007e00034c000a726564697265637455726971007e00034c000c726573706f6e7365547970657400534c6f72672f737072696e676672616d65776f726b2f73656375726974792f6f61757468322f636f72652f656e64706f696e742f4f4175746832417574686f72697a6174696f6e526573706f6e7365547970653b4c000673636f70657374000f4c6a6176612f7574696c2f5365743b4c0005737461746571007e00037870737200256a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c654d6170f1a5a8fe74f520420200014c00016d71007e00017870737200176a6176612e7574696c2e4c696e6b6564486173684d617034c04e5c106cc0fb0200015a000a6163636573734f72646572787200116a6176612e7574696c2e486173684d61700520dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c770800000010000000017400056e6f6e636574002b4859746d44616b4b48676363647577525042643543524d53782d4b3948437836766a77714b4c5a4d566f6f78007371007e00207371007e00093f4000000000000c7708000000100000000274000f726567697374726174696f6e5f6964740006676f6f676c6571007e000c740080513765693147354d6a5877786439556e7a744e447055783837336d70387a63313455426936775638687a46516867356c7964595677654b4359786a315677497a64633832304e5868693934614c4d2d555f417234535951676233586f42305047494631685a4a5f4b6445487550502d355a46384c4b6b7a4e5f6b477730764c7278007372003f6f72672e737072696e676672616d65776f726b2e73656375726974792e6f61757468322e636f72652e417574686f72697a6174696f6e4772616e7454797065000000000000026c0200014c000576616c756571007e00037870740012617574686f72697a6174696f6e5f636f646574015268747470733a2f2f6163636f756e74732e676f6f676c652e636f6d2f6f2f6f61757468322f617574683f726573706f6e73655f747970653d636f646526636c69656e745f69643d313034383139353734373031352d6d663733666f6e6266613832726532733638306a646268676d6b6439333064682e617070732e676f6f676c6575736572636f6e74656e742e636f6d2673636f70653d6f70656e696425323070726f66696c65253230656d61696c2673746174653d4b386d3976565a446b574f576d6e5f757a7972625358766448596a2d54396e69417238497633656c62644d2533442672656469726563745f7572693d687474703a2f2f6c6f63616c686f73743a383038302f6c6f67696e2f6f61757468322f636f64652f676f6f676c65266e6f6e63653d4859746d44616b4b48676363647577525042643543524d53782d4b3948437836766a77714b4c5a4d566f6f74002968747470733a2f2f6163636f756e74732e676f6f676c652e636f6d2f6f2f6f61757468322f61757468740049313034383139353734373031352d6d663733666f6e6266613832726532733638306a646268676d6b6439333064682e617070732e676f6f676c6575736572636f6e74656e742e636f6d74002e687474703a2f2f6c6f63616c686f73743a383038302f6c6f67696e2f6f61757468322f636f64652f676f6f676c65737200516f72672e737072696e676672616d65776f726b2e73656375726974792e6f61757468322e636f72652e656e64706f696e742e4f4175746832417574686f72697a6174696f6e526573706f6e736554797065000000000000026c0200014c000576616c756571007e00037870740004636f6465737200256a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65536574801d92d18f9b80550200007872002c6a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65436f6c6c656374696f6e19420080cb5ef71e0200014c0001637400164c6a6176612f7574696c2f436f6c6c656374696f6e3b7870737200176a6176612e7574696c2e4c696e6b656448617368536574d86cd75a95dd2a1e020000787200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000037400066f70656e696474002070726f66696c65740005656d61696c7874002c4b386d3976565a446b574f576d6e5f757a7972625358766448596a2d54396e69417238497633656c62644d3d),
('f829b138-41e6-48ba-8b7c-3ab8c541c09b', 'org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository.AUTHORIZATION_REQUEST', 0xaced00057372004c6f72672e737072696e676672616d65776f726b2e73656375726974792e6f61757468322e636f72652e656e64706f696e742e4f4175746832417574686f72697a6174696f6e52657175657374000000000000026c02000a4c00146164646974696f6e616c506172616d657465727374000f4c6a6176612f7574696c2f4d61703b4c000a6174747269627574657371007e00014c0016617574686f72697a6174696f6e4772616e74547970657400414c6f72672f737072696e676672616d65776f726b2f73656375726974792f6f61757468322f636f72652f417574686f72697a6174696f6e4772616e74547970653b4c0017617574686f72697a6174696f6e526571756573745572697400124c6a6176612f6c616e672f537472696e673b4c0010617574686f72697a6174696f6e55726971007e00034c0008636c69656e74496471007e00034c000a726564697265637455726971007e00034c000c726573706f6e7365547970657400534c6f72672f737072696e676672616d65776f726b2f73656375726974792f6f61757468322f636f72652f656e64706f696e742f4f4175746832417574686f72697a6174696f6e526573706f6e7365547970653b4c000673636f70657374000f4c6a6176612f7574696c2f5365743b4c0005737461746571007e00037870737200256a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c654d6170f1a5a8fe74f520420200014c00016d71007e00017870737200176a6176612e7574696c2e4c696e6b6564486173684d617034c04e5c106cc0fb0200015a000a6163636573734f72646572787200116a6176612e7574696c2e486173684d61700520dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c770800000010000000017400056e6f6e636574002b775f4c6761445242634d4976536b6930646132485f4a63467871764e4b6a2d50503836316f376a534b316f78007371007e00207371007e00093f4000000000000c7708000000100000000274000f726567697374726174696f6e5f6964740006676f6f676c6571007e000c74008075694e6c526f727535654966533444524a697243676b547271334955646938354b68465077665a485a4b4945554262314e766c51497764707474675649724a727a5358703877503672433553535f705f494f5a61725f435874734f57506f74495a415236397268326d6552674c4e43714753467377414143434d626a4c4e334478007372003f6f72672e737072696e676672616d65776f726b2e73656375726974792e6f61757468322e636f72652e417574686f72697a6174696f6e4772616e7454797065000000000000026c0200014c000576616c756571007e00037870740012617574686f72697a6174696f6e5f636f646574015268747470733a2f2f6163636f756e74732e676f6f676c652e636f6d2f6f2f6f61757468322f617574683f726573706f6e73655f747970653d636f646526636c69656e745f69643d313034383139353734373031352d6d663733666f6e6266613832726532733638306a646268676d6b6439333064682e617070732e676f6f676c6575736572636f6e74656e742e636f6d2673636f70653d6f70656e696425323070726f66696c65253230656d61696c2673746174653d426356705a32686267755f794568473777645f665262635632716a55664e6b5342595374676d656a7272492533442672656469726563745f7572693d687474703a2f2f6c6f63616c686f73743a383038302f6c6f67696e2f6f61757468322f636f64652f676f6f676c65266e6f6e63653d775f4c6761445242634d4976536b6930646132485f4a63467871764e4b6a2d50503836316f376a534b316f74002968747470733a2f2f6163636f756e74732e676f6f676c652e636f6d2f6f2f6f61757468322f61757468740049313034383139353734373031352d6d663733666f6e6266613832726532733638306a646268676d6b6439333064682e617070732e676f6f676c6575736572636f6e74656e742e636f6d74002e687474703a2f2f6c6f63616c686f73743a383038302f6c6f67696e2f6f61757468322f636f64652f676f6f676c65737200516f72672e737072696e676672616d65776f726b2e73656375726974792e6f61757468322e636f72652e656e64706f696e742e4f4175746832417574686f72697a6174696f6e526573706f6e736554797065000000000000026c0200014c000576616c756571007e00037870740004636f6465737200256a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65536574801d92d18f9b80550200007872002c6a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65436f6c6c656374696f6e19420080cb5ef71e0200014c0001637400164c6a6176612f7574696c2f436f6c6c656374696f6e3b7870737200176a6176612e7574696c2e4c696e6b656448617368536574d86cd75a95dd2a1e020000787200116a6176612e7574696c2e48617368536574ba44859596b8b7340300007870770c000000103f400000000000037400066f70656e696474002070726f66696c65740005656d61696c7874002c426356705a32686267755f794568473777645f665262635632716a55664e6b5342595374676d656a7272493d);

-- --------------------------------------------------------

--
-- Table structure for table `tickets`
--

CREATE TABLE `tickets` (
  `ticket_id` bigint(20) NOT NULL,
  `passenger_name` varchar(255) NOT NULL,
  `passenger_phone` varchar(255) DEFAULT NULL,
  `price` decimal(38,2) NOT NULL,
  `seat_number` varchar(255) NOT NULL,
  `status` enum('cancelled','used','valid') NOT NULL,
  `ticket_code` varchar(255) NOT NULL,
  `booking_id` bigint(20) NOT NULL,
  `sell_method` enum('AUTO','MANUAL') NOT NULL,
  `seller_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tickets`
--

INSERT INTO `tickets` (`ticket_id`, `passenger_name`, `passenger_phone`, `price`, `seat_number`, `status`, `ticket_code`, `booking_id`, `sell_method`, `seller_id`) VALUES
(1, 'Nguyen Thao Vien', '0987654321', 500000.00, 'B.1.2', 'cancelled', 'TICKET123', 1, 'AUTO', NULL),
(2, 'Lê Văn Khách', '0976543210', 150000.00, 'A05', 'cancelled', 'TICKET124', 2, 'AUTO', NULL),
(3, 'Phạm Thị Hành Khách', '0123456789', 250000.00, 'B.1.2', 'cancelled', 'TICKET125', 3, 'AUTO', NULL),
(4, 'Hoàng Văn Khách', '0954321098', 200000.00, 'B10', 'cancelled', 'TICKET126', 4, 'AUTO', NULL),
(5, 'Lê Văn Khách', '0976543210', 220000.00, 'A17', 'valid', 'TICKET127', 5, 'AUTO', NULL),
(7, 'Trần Thị Khách', '0987654321', 500000.00, 'B12', 'valid', 'TICKET128', 1, 'AUTO', NULL),
(39, 'Antoin', '0901020304', 500000.00, 'A.1.2', 'valid', '02A328', 12, 'AUTO', NULL),
(40, 'Antoin', '0901020304', 500000.00, 'A01', 'used', '5AADC3', 12, 'AUTO', NULL),
(41, 'Antoin', '0123456789', 500000.00, 'A01', 'valid', '4FA6EC', 12, 'AUTO', NULL),
(42, 'Antoin', '0123456789', 500000.00, 'A01', 'valid', '5665D5', 12, 'AUTO', NULL),
(43, 'Antoin', '0123456789', 330000.00, 'A.1.1', 'valid', 'F332F0', 14, 'AUTO', NULL),
(44, 'Antoin', '0123456789', 330000.00, 'A.1.1', 'valid', 'A1CEE3', 15, 'AUTO', NULL),
(45, 'Antoin', '0123456789', 330000.00, 'A.1.1', 'valid', '8B225C', 16, 'AUTO', NULL),
(46, 'Antoin', '0123456789', 330000.00, 'A.1.1', 'valid', '478079', 17, 'AUTO', NULL),
(47, 'Antoin', '0123456789', 330000.00, 'B.1.1', 'valid', '5B1860', 17, 'AUTO', NULL),
(48, 'Antoin', '0123456789', 330000.00, 'A.1.1', 'valid', 'A03E8D', 18, 'AUTO', NULL),
(49, 'Antoin', '0123456789', 330000.00, 'B.1.1', 'valid', '00E0B9', 18, 'AUTO', NULL),
(50, 'Antoin', '0123456789', 500000.00, 'B10', 'valid', 'C97C97', 13, 'AUTO', NULL),
(51, 'Antoin', '0123456789', 330000.00, 'A.1.1', 'valid', 'ECFDF3', 19, 'AUTO', NULL),
(52, 'Antoin', '0123456789', 330000.00, 'B.1.1', 'valid', '109D87', 19, 'AUTO', NULL),
(53, 'Antoin', '0123456789', 330000.00, 'A.1.1', 'valid', 'A7389D', 20, 'AUTO', NULL),
(54, 'Antoin', '0123456789', 330000.00, 'B.1.1', 'valid', '474E2D', 20, 'AUTO', NULL),
(55, 'Antoin', '0123456789', 330000.00, 'A.1.1', 'valid', '0C1805', 21, 'AUTO', NULL),
(56, 'Antoin', '0123456789', 330000.00, 'B.1.1', 'valid', 'ED93C0', 21, 'AUTO', NULL),
(57, 'Antoin', '0123456789', 330000.00, 'A.1.1', 'valid', 'BB6A80', 22, 'AUTO', NULL),
(58, 'Antoin', '0123456789', 330000.00, 'B.1.1', 'valid', '1A429B', 22, 'AUTO', NULL),
(59, 'Antoin', '0123456789', 156000.00, 'A.1.1', 'valid', 'FE467C', 24, 'AUTO', NULL),
(60, 'Antoin', '0123456789', 156000.00, 'A.1.1', 'valid', '1C7A77', 25, 'AUTO', NULL),
(61, 'Hoai', '0912312311', 831000.00, 'B.2.1', 'valid', '722E8D', 26, 'AUTO', NULL),
(62, 'Antoin', '0123456789', 234000.00, 'B.10.1', 'valid', '1D77C5', 28, 'AUTO', NULL),
(63, 'Nguyen To Duong-CUSTOMER', '0328199217', 234000.00, 'A.2.1', 'valid', '230371', 33, 'AUTO', NULL),
(64, 'Nguyen To Duong-CUSTOMER', '0328199217', 234000.00, 'A.2.1', 'valid', '7DF72A', 33, 'AUTO', NULL),
(65, 'Nguyen To Duong-CUSTOMER', '0328199217', 234000.00, 'A.2.1', 'valid', 'F4BC2A', 33, 'AUTO', NULL),
(66, 'Nguyen To Duong-CUSTOMER', '0328199217', 234000.00, 'A.2.1', 'valid', '4C0118', 33, 'AUTO', NULL),
(67, 'Nguyễn dg', '1234567893', 234000.00, 'A.9.1', 'valid', 'C2D91C', 36, 'AUTO', NULL),
(68, 'Nguyễn dg', '1234567893', 234000.00, 'A.9.1', 'valid', '8B48AA', 36, 'AUTO', NULL),
(69, 'Nguyễn dg', '1234567893', 234000.00, 'A.9.1', 'valid', '4AD026', 36, 'AUTO', NULL),
(70, 'Nguyễn dg', '1234567893', 234000.00, 'A.9.1', 'valid', '638855', 36, 'AUTO', NULL),
(71, 'Nguyễn dg', '1234567893', 234000.00, 'A.9.1', 'valid', '699765', 36, 'AUTO', NULL),
(72, 'Nguyễn dg', '1234567893', 234000.00, 'A.9.1', 'valid', 'F74D5D', 36, 'AUTO', NULL),
(73, 'Nguyễn dg', '1234567893', 234000.00, 'A.9.1', 'valid', '618102', 36, 'AUTO', NULL),
(74, 'Nguyễn dg', '1234567893', 234000.00, 'A.9.1', 'valid', 'C2F1EF', 37, 'AUTO', NULL),
(75, 'Antoin', '0123456789', 2454334.00, 'C.5.1', 'cancelled', '83CB2E', 51, 'AUTO', NULL),
(76, 'Antoin', '0123456789', 2454334.00, 'B.10.1', 'valid', 'C4AE5C', 51, 'AUTO', NULL),
(77, 'hoàng hoài', '0356138252', 1963467.00, 'C.7.1', 'valid', '1C75C4', 55, 'AUTO', NULL),
(78, 'hoàng hoài', '0356138252', 1963467.00, 'D.7.1', 'valid', '084004', 55, 'AUTO', NULL),
(79, 'Duong_Customer', '0123456789', 2454334.00, 'B.7.1', 'valid', '4360CC', 61, 'AUTO', NULL),
(80, 'hoàng hoài', '0356138252', 1954334.00, 'B.9.2', 'valid', 'FA1B40', 66, 'AUTO', NULL),
(81, 'hoàng hoài', '0356138252', 1954334.00, 'C.9.2', 'valid', 'F642AB', 66, 'AUTO', NULL),
(82, 'Nguyễn An Toàn', '0123456789', 150000.00, 'B.10.1', 'valid', '8735FD', 67, 'AUTO', NULL),
(83, 'Nguyễn An Toàn', '0123456789', 150000.00, 'C.10.1', 'valid', 'F620C0', 67, 'AUTO', NULL),
(84, 'Nguyễn An Toàn', '0123456789', 150000.00, 'B.8.1', 'valid', '43758B', 68, 'AUTO', NULL),
(85, 'Nguyễn An Toàn', '0123456789', 150000.00, 'C.8.1', 'valid', 'F945D2', 69, 'AUTO', NULL),
(86, 'Nguyễn An Toàn', '0123456789', 150000.00, 'D.7.1', 'valid', '0E6F5C', 70, 'AUTO', NULL),
(87, 'Nguyễn An Toàn', '0123456789', 150000.00, 'A.6.1', 'valid', '7F100C', 71, 'AUTO', NULL),
(88, 'Nguyễn An Toàn', '0123456789', 150000.00, 'C.5.1', 'valid', 'CF1ABC', 72, 'AUTO', NULL),
(89, 'Nguyễn An Toàn', '0123456789', 150000.00, 'B.5.1', 'valid', '156FE2', 73, 'AUTO', NULL),
(90, 'Nguyễn An Toàn', '0123456789', 150000.00, 'A.1.1', 'valid', '798A5B', 75, 'AUTO', NULL),
(91, 'Nguyễn An Toàn', '0123456789', 150000.00, 'B.1.1', 'valid', 'DAF47E', 75, 'AUTO', NULL),
(92, 'Nguyễn An Toàn', '0123456789', 150000.00, 'C.1.1', 'valid', '58A9EB', 77, 'AUTO', NULL),
(93, 'Nguyễn An Toàn', '0123456789', 150000.00, 'D.1.1', 'valid', '2D764D', 77, 'AUTO', NULL),
(94, 'Antoin', '0123456789', 150000.00, 'A.2.1', 'valid', 'CFE76F', 78, 'AUTO', NULL),
(95, 'Antoin', '0123456789', 150000.00, 'B.2.1', 'valid', 'C20309', 78, 'AUTO', NULL),
(96, 'Antoin', '0123456789', 150000.00, 'C.2.1', 'valid', '9D4BDA', 79, 'AUTO', NULL),
(97, 'Antoin', '0123456789', 150000.00, 'D.2.1', 'valid', '062139', 79, 'AUTO', NULL),
(98, 'Antoin', '0123456789', 150000.00, 'B.3.1', 'valid', 'A08670', 80, 'AUTO', NULL),
(99, 'Antoin', '0123456789', 150000.00, 'C.3.1', 'valid', '2B4556', 80, 'AUTO', NULL),
(100, 'Antoin', '0123456789', 150000.00, 'C.9.1', 'valid', '9124B3', 81, 'AUTO', NULL),
(101, 'Antoin', '0123456789', 150000.00, 'B.9.1', 'valid', '1F460F', 81, 'AUTO', NULL),
(102, 'Antoin', '0123456789', 150000.00, 'B.6.1', 'valid', 'F08FB1', 82, 'AUTO', NULL),
(103, 'Antoin', '0123456789', 150000.00, 'C.6.1', 'valid', '0F32F8', 82, 'AUTO', NULL),
(104, 'Antoin', '0123456789', 150000.00, 'B.7.1', 'valid', '604405', 83, 'AUTO', NULL),
(105, 'Antoin', '0123456789', 150000.00, 'C.7.1', 'valid', '3DEBD8', 83, 'AUTO', NULL),
(106, 'NGUYỄN TÔ DƯƠNG', '0123456789', 150000.00, 'A.1.1', 'valid', 'D9C6E1', 95, 'AUTO', NULL),
(107, 'NGUYỄN TÔ DƯƠNG', '0123456789', 150000.00, 'B.1.1', 'valid', '063452', 95, 'AUTO', NULL),
(108, 'Antoin', '0123456789', 123000.00, 'B.7.1', 'valid', 'AA40E9', 96, 'AUTO', NULL),
(109, 'Antoin', '0123456789', 123000.00, 'C.7.1', 'valid', '4200CB', 96, 'AUTO', NULL),
(110, 'Antoin', '0123456787', 250000.00, 'B.5.1', 'used', '169399', 97, 'AUTO', NULL),
(111, 'Antoin', '0123456789', 250000.00, 'C.5.1', 'used', '05FA30', 97, 'AUTO', NULL),
(112, 'NGUYỄN TÔ DƯƠNG', '0123456789', 150000.00, 'B.10.1', 'valid', '2908E4', 98, 'AUTO', NULL),
(113, 'NGUYỄN TÔ DƯƠNG', '0123456789', 150000.00, 'C.10.1', 'used', 'E44D80', 98, 'AUTO', NULL),
(115, 'Antoin', '0123456789', 232000.00, 'D.7.1', 'valid', 'D99E57', 150, 'AUTO', NULL),
(116, 'dg', '0123456789', 234000.00, 'A.10.1', 'valid', '6A522B', 154, 'AUTO', NULL),
(117, 'dg', '0123456789', 250000.00, 'A.10.1', 'used', 'E81183', 155, 'AUTO', NULL),
(118, 'Antoin', '0123456789', 185200.00, 'C.10.1', 'used', '90CEA3', 156, 'AUTO', NULL),
(119, 'Antoin', '0123456789', 185200.00, 'D.10.1', 'used', 'F3B118', 156, 'AUTO', NULL),
(120, 'HOAI19800 COMPANY', '0123124121', 436000.00, 'C.7.1', 'valid', '202100', 165, 'AUTO', NULL),
(121, 'HOAI19800 COMPANY', '0123124121', 436000.00, 'D.7.1', 'valid', '5215CE', 165, 'AUTO', NULL),
(122, 'HOAI19800 COMPANY', '0123124121', 305200.00, 'A.7.1', 'valid', 'E835D7', 166, 'AUTO', NULL),
(123, 'HOAI19800 COMPANY', '0123124121', 305200.00, 'B.7.1', 'valid', '72411D', 166, 'AUTO', NULL),
(124, 'HOAI19800 COMPANY', '0123124121', 348800.00, 'A.10.1', 'valid', '852738', 167, 'AUTO', NULL),
(125, 'HOAI19800 COMPANY', '0123124121', 348800.00, 'B.10.1', 'valid', '21E03A', 167, 'AUTO', NULL),
(126, 'hoàng hoài', '0901234561', 175000.00, 'B.9.1', 'valid', 'B1C188', 169, 'AUTO', NULL),
(127, 'hoàng hoài', '0901234561', 175000.00, 'C.9.1', 'valid', '29AFF2', 169, 'AUTO', NULL),
(128, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'A.1.1', 'used', '56B1E4', 172, 'AUTO', NULL),
(129, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'B.1.1', 'used', 'B1DA17', 172, 'AUTO', NULL),
(130, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'C.1.1', 'used', 'AB1026', 172, 'AUTO', NULL),
(131, 'dg', '0123456789', 100000.00, 'A.2.1', 'used', '35F6DA', 173, 'AUTO', NULL),
(132, 'dg', '0123456789', 100000.00, 'B.2.1', 'used', '8D41A0', 173, 'AUTO', NULL),
(133, 'dg', '0123456789', 500000.00, 'A.1.1', 'used', '4E2C78', 174, 'AUTO', NULL),
(134, 'dg', '0123456789', 500000.00, 'B.1.1', 'used', '6D29D8', 174, 'AUTO', NULL),
(135, 'dg', '0123456789', 500000.00, 'C.1.1', 'valid', '299893', 174, 'AUTO', NULL),
(136, 'dg', '0123456789', 500000.00, 'D.1.1', 'valid', 'A28E84', 174, 'AUTO', NULL),
(137, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'A.6.1', 'valid', '3AF1FE', 175, 'AUTO', NULL),
(138, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'B.6.1', 'valid', 'F37B13', 175, 'AUTO', NULL),
(139, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'C.6.1', 'valid', '1E4E6C', 175, 'AUTO', NULL),
(140, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'A.4.1', 'valid', '0A446D', 176, 'AUTO', NULL),
(141, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'B.4.1', 'valid', '4E9E80', 176, 'AUTO', NULL),
(142, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'C.4.1', 'valid', 'E6CCBD', 176, 'AUTO', NULL),
(143, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'D.4.1', 'valid', '2D9843', 176, 'AUTO', NULL),
(144, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'A.9.1', 'valid', '835C55', 177, 'AUTO', NULL),
(145, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'B.9.1', 'valid', 'E0668F', 177, 'AUTO', NULL),
(146, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'C.9.1', 'valid', 'D55ADA', 177, 'AUTO', NULL),
(147, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'D.9.1', 'valid', 'FE5F4F', 177, 'AUTO', NULL),
(148, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'A.8.1', 'valid', 'C62CEC', 178, 'AUTO', NULL),
(149, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'C.8.1', 'valid', '64E49E', 178, 'AUTO', NULL),
(150, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'B.8.1', 'valid', '40A025', 178, 'AUTO', NULL),
(151, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'D.8.1', 'valid', 'E14BA5', 178, 'AUTO', NULL),
(152, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'B.8.1', 'valid', '55C5D7', 179, 'AUTO', NULL),
(153, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'C.8.1', 'valid', 'B4EEC9', 179, 'AUTO', NULL),
(154, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'D.8.1', 'valid', '1740DC', 179, 'AUTO', NULL),
(155, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'B.7.1', 'valid', '6F65D5', 179, 'AUTO', NULL),
(156, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'C.7.1', 'valid', 'C42105', 179, 'AUTO', NULL),
(157, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'A.9.1', 'valid', '974614', 180, 'AUTO', NULL),
(158, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'B.9.1', 'valid', 'CB1450', 180, 'AUTO', NULL),
(159, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'C.9.1', 'valid', '23F01B', 180, 'AUTO', NULL),
(160, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'A.8.1', 'valid', 'BA0195', 181, 'AUTO', NULL),
(161, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'B.8.1', 'valid', '9B1D11', 181, 'AUTO', NULL),
(162, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'C.8.1', 'valid', 'D7E3BB', 181, 'AUTO', NULL),
(163, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'D.8.1', 'valid', 'B10A9F', 181, 'AUTO', NULL),
(164, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'D.9.1', 'valid', '9175F7', 181, 'AUTO', NULL),
(165, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'D.10.1', 'valid', 'DE8E68', 181, 'AUTO', NULL),
(166, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'C.10.1', 'valid', 'BB3F66', 181, 'AUTO', NULL),
(167, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'B.10.1', 'valid', 'AD00B1', 181, 'AUTO', NULL),
(168, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'A.10.1', 'valid', '7B0434', 181, 'AUTO', NULL),
(169, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'A.6.1', 'valid', '4881F4', 182, 'AUTO', NULL),
(170, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'B.6.1', 'valid', 'C941E1', 182, 'AUTO', NULL),
(171, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'C.6.1', 'valid', 'BA2AC9', 182, 'AUTO', NULL),
(172, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'D.6.1', 'valid', 'F4DD7B', 182, 'AUTO', NULL),
(173, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'A.5.1', 'valid', '8E30EE', 183, 'AUTO', NULL),
(174, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'B.5.1', 'valid', '532D0D', 183, 'AUTO', NULL),
(175, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'C.5.1', 'valid', 'BF58EA', 183, 'AUTO', NULL),
(176, 'HOAI19800 COMPANY', '0123124121', 100000.00, 'D.5.1', 'valid', '8518A6', 183, 'AUTO', NULL),
(177, 'hoàng hoài', '0901234561', 100000.00, 'A.2.1', 'valid', 'BB1AA5', 184, 'AUTO', NULL),
(178, 'hoàng hoài', '0901234561', 100000.00, 'B.2.1', 'valid', '0A11F6', 184, 'AUTO', NULL),
(179, 'hoàng hoài', '0901234561', 100000.00, 'C.2.1', 'valid', '98E70F', 184, 'AUTO', NULL),
(180, 'hoàng hoài', '0901234561', 348800.00, 'A.5.1', 'used', '5D0306', 185, 'AUTO', NULL),
(181, 'hoàng hoài', '0901234561', 348800.00, 'B.5.1', 'used', 'A30D05', 185, 'AUTO', NULL),
(182, 'hoàng hoài', '0901234561', 890000.00, 'A.5.1', 'valid', 'A478CD', 186, 'AUTO', NULL),
(183, 'hoàng hoài', '0901234561', 890000.00, 'B.5.1', 'valid', 'A1A2E1', 186, 'AUTO', NULL),
(184, 'hoàng hoài', '0901234561', 100000.00, 'B.4.1', 'valid', 'A5AA79', 187, 'AUTO', NULL),
(185, 'hoàng hoài', '0901234561', 100000.00, 'C.4.1', 'valid', '1FBAB0', 187, 'AUTO', NULL),
(186, 'Hoai', '0123412341', 1200000.00, 'B.3.1', 'valid', '08C716', 188, 'MANUAL', 18),
(187, 'Hoai', '0123412341', 1200000.00, 'C.5.1', 'valid', 'E2D7E2', 188, 'MANUAL', 18),
(188, 'Hoai', '0123412341', 1200000.00, 'B.5.1', 'valid', '55EC44', 188, 'MANUAL', 18),
(189, 'dg', '0123456789', 623000.00, 'A.10.1', 'used', '7A39EA', 189, 'AUTO', NULL),
(190, 'hoàng hoài', '0901234561', 100000.00, 'A.7.1', 'valid', 'CF2291', 190, 'AUTO', NULL),
(191, 'hoàng hoài', '0901234561', 100000.00, 'B.3.1', 'valid', '6A4FAD', 191, 'AUTO', NULL),
(192, 'hoàng hoài', '0901234561', 100000.00, 'C.3.1', 'valid', '96F615', 191, 'AUTO', NULL),
(193, 'hoàng hoài', '0901234561', 436000.00, 'C.5.1', 'valid', 'A4DFD4', 192, 'AUTO', NULL),
(194, 'hoàng hoài', '0901234561', 436000.00, 'D.5.1', 'cancelled', '5CB04B', 192, 'AUTO', NULL),
(195, 'hoàng hoài', '0901234561', 436000.00, 'C.5.1', 'valid', '631E81', 193, 'AUTO', NULL),
(196, 'hoàng hoài', '0901234561', 436000.00, 'D.5.1', 'valid', '2A3023', 193, 'AUTO', NULL),
(209, 'atoan', '0123456789', 1900000.00, 'A.10.1', 'valid', 'F6D8CF', 205, 'MANUAL', 18),
(210, 'atoan', '0123456789', 1900000.00, 'B.10.1', 'valid', 'C74777', 205, 'MANUAL', 18),
(211, 'An Dương', '0123456789', 200000.00, 'A.10.1', 'valid', '9BA2FF', 206, 'MANUAL', 43),
(212, 'An Dương', '0123456789', 200000.00, 'B.10.1', 'valid', '508676', 206, 'MANUAL', 43),
(213, 'hoàng hoài', '0901234561', 1900000.00, 'B.5.1', 'valid', '2A0537', 215, 'AUTO', NULL),
(214, 'hoàng hoài', '0901234561', 1900000.00, 'C.5.1', 'valid', '91385F', 215, 'AUTO', NULL),
(215, 'HOAI19800 COMPANY', '0123124121', 195000.00, 'B.3.1', 'used', '905E34', 218, 'AUTO', NULL),
(216, 'HOAI19800 COMPANY', '0123124121', 195000.00, 'C.3.1', 'used', '697C63', 218, 'AUTO', NULL),
(217, 'An Dương', '0123456789', 200000.00, 'B.5.1', 'valid', '81CA52', 219, 'MANUAL', 57),
(218, 'An Dương', '0123456789', 200000.00, 'C.5.1', 'valid', '1AD9AF', 219, 'MANUAL', 57),
(219, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'A.6.1', 'valid', 'A68700', 220, 'AUTO', NULL),
(220, 'HOAI19800 COMPANY', '0123124121', 1500000.00, 'B.6.1', 'valid', '2669C7', 220, 'AUTO', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `trips`
--

CREATE TABLE `trips` (
  `trip_id` bigint(20) NOT NULL,
  `departure_time` datetime(6) NOT NULL,
  `estimated_arrival_time` datetime(6) DEFAULT NULL,
  `price_per_seat` decimal(10,2) NOT NULL,
  `bus_id` bigint(20) DEFAULT NULL,
  `driver_id` bigint(20) DEFAULT NULL,
  `route_id` bigint(20) DEFAULT NULL,
  `status` enum('arrived','cancelled','delayed','departed','on_sell','scheduled') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `trips`
--

INSERT INTO `trips` (`trip_id`, `departure_time`, `estimated_arrival_time`, `price_per_seat`, `bus_id`, `driver_id`, `route_id`, `status`) VALUES
(1, '2025-08-20 08:00:00.000000', '2025-08-20 13:00:00.000000', 500000.00, 2, 17, 5, 'scheduled'),
(2, '2025-08-29 23:00:00.000000', '2025-08-30 02:00:00.000000', 250000.00, 8, 32, 2, 'arrived'),
(3, '2025-08-29 22:00:00.000000', '2025-08-30 06:00:00.000000', 250000.00, 2, 22, 3, 'arrived'),
(4, '2025-08-09 11:00:00.000000', '2025-08-09 15:00:00.000000', 200000.00, 5, 22, 4, 'arrived'),
(5, '2025-08-09 12:00:00.000000', '2025-08-09 17:00:00.000000', 220000.00, 2, 32, 5, 'arrived'),
(6, '2025-08-22 08:00:00.000000', '2025-08-22 16:00:00.000000', 300000.00, 25, 8, 6, 'arrived'),
(10, '2025-08-26 12:00:00.000000', '2025-08-27 00:00:00.000000', 330000.00, 2, 21, 1, 'arrived'),
(12, '2025-08-19 12:00:00.000000', '2025-08-20 00:00:00.000000', 200000.00, 2, 8, 1, 'arrived'),
(16, '2025-08-19 09:20:00.000000', '2025-08-19 21:20:00.000000', 123455.00, 2, 8, 1, 'arrived'),
(17, '2025-08-20 22:00:00.000000', '2025-08-21 10:00:00.000000', 1223121.00, 2, 8, 1, 'arrived'),
(18, '2025-08-21 17:00:00.000000', '2025-08-22 05:00:00.000000', 432423.00, 2, 8, 1, 'arrived'),
(19, '2025-08-27 17:00:00.000000', '2025-08-28 05:00:00.000000', 2454334.00, 2, 17, 1, 'arrived'),
(20, '2025-08-25 17:00:00.000000', '2025-08-26 05:00:00.000000', 156000.00, 2, 8, 1, 'arrived'),
(21, '2025-08-26 09:00:00.000000', '2025-08-26 21:00:00.000000', 234000.00, 2, 19, 1, 'arrived'),
(23, '2025-08-26 07:00:00.000000', '2025-08-26 15:00:00.000000', 831000.00, 9, 21, 3, 'arrived'),
(24, '2025-09-05 02:00:00.000000', '2025-09-06 10:00:00.000000', 234000.00, 2, 40, 3, 'arrived'),
(25, '2025-09-12 05:00:00.000000', '2025-09-13 13:00:00.000000', 436000.00, 9, 19, 3, 'arrived'),
(26, '2025-08-27 17:00:00.000000', '2025-08-27 22:20:00.000000', 232000.00, 2, 17, 1, 'scheduled'),
(27, '2025-08-27 19:00:00.000000', '2025-08-28 07:00:00.000000', 231000.00, 27, 33, 1, 'arrived'),
(28, '2025-08-29 17:00:00.000000', '2025-08-30 01:00:00.000000', 123000.00, 14, 19, 3, 'arrived'),
(30, '2025-09-30 01:30:00.000000', '2025-09-30 06:30:00.000000', 800000.00, 16, 19, 5, 'arrived'),
(31, '2025-09-14 17:00:00.000000', '2025-09-14 20:00:00.000000', 220000.00, 14, 21, 2, 'scheduled'),
(32, '2025-09-11 17:00:00.000000', '2025-09-12 01:00:00.000000', 90000.00, 16, 22, 3, 'arrived'),
(33, '2025-09-23 17:00:00.000000', '2025-09-24 01:00:00.000000', 890000.00, 22, 32, 3, 'arrived'),
(34, '2025-09-24 17:00:00.000000', '2025-09-25 05:00:00.000000', 1500000.00, 14, 40, 12, 'scheduled'),
(35, '2025-09-24 17:00:00.000000', '2025-09-25 01:00:00.000000', 700000.00, 14, 22, 3, 'scheduled'),
(36, '2025-09-25 17:00:00.000000', '2025-09-26 05:00:00.000000', 1200000.00, 8, 22, 12, 'scheduled'),
(37, '2025-10-08 17:00:00.000000', '2025-10-09 05:00:00.000000', 1900000.00, 22, 21, 12, 'scheduled'),
(38, '2025-09-11 20:11:04.000000', '2025-09-12 08:11:04.000000', 100000.00, 2, 40, 1, 'arrived'),
(39, '2025-09-19 17:00:00.000000', '2025-09-19 22:00:00.000000', 500000.00, 8, 22, 5, 'arrived'),
(40, '2025-09-21 17:00:00.000000', '2025-09-21 22:00:00.000000', 450000.00, 8, 22, 5, 'on_sell'),
(41, '2025-09-28 00:07:00.000000', '2025-09-28 04:07:00.000000', 100000.00, 45, 40, 4, 'scheduled'),
(42, '2025-09-29 00:07:06.000000', '2025-09-29 08:07:06.000000', 200000.00, 48, 54, 3, 'scheduled'),
(43, '2025-09-26 17:00:08.000000', '2025-09-26 21:00:08.000000', 250000.00, 9, 22, 4, 'scheduled'),
(44, '2025-09-30 01:20:00.000000', '2025-09-30 09:20:00.000000', 800000.00, 8, 32, 6, 'scheduled'),
(45, '2025-09-21 17:00:00.000000', '2025-09-22 09:40:00.000000', 500000.00, 2, 40, 19, 'scheduled'),
(46, '2025-09-20 23:00:06.000000', '2025-09-21 02:00:06.000000', 100000.00, 49, 54, 2, 'on_sell');

-- --------------------------------------------------------

--
-- Table structure for table `trip_seats`
--

CREATE TABLE `trip_seats` (
  `seat_number` varchar(255) NOT NULL,
  `trip_id` bigint(20) NOT NULL,
  `locked_at` datetime(6) DEFAULT NULL,
  `status` enum('available','booked','locked') NOT NULL,
  `locking_user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `trip_seats`
--

INSERT INTO `trip_seats` (`seat_number`, `trip_id`, `locked_at`, `status`, `locking_user_id`) VALUES
('A.1.1', 1, NULL, 'available', NULL),
('A.1.1', 2, NULL, 'available', NULL),
('A.1.1', 3, NULL, 'available', NULL),
('A.1.1', 5, NULL, 'available', NULL),
('A.1.1', 10, NULL, 'available', NULL),
('A.1.1', 19, NULL, 'available', NULL),
('A.1.1', 20, NULL, 'available', NULL),
('A.1.1', 21, NULL, 'available', NULL),
('A.1.1', 23, NULL, 'available', NULL),
('A.1.1', 24, '2025-09-04 10:50:11.464454', 'locked', 18),
('A.1.1', 25, NULL, 'available', NULL),
('A.1.1', 26, NULL, 'available', NULL),
('A.1.1', 27, NULL, 'available', NULL),
('A.1.1', 28, NULL, 'available', NULL),
('A.1.1', 30, NULL, 'available', NULL),
('A.1.1', 31, NULL, 'available', NULL),
('A.1.1', 32, NULL, 'available', NULL),
('A.1.1', 33, '2025-09-18 03:52:29.129925', 'locked', 31),
('A.1.1', 34, NULL, 'available', NULL),
('A.1.1', 35, NULL, 'available', NULL),
('A.1.1', 36, NULL, 'available', NULL),
('A.1.1', 37, '2025-09-18 03:23:06.177878', 'locked', 31),
('A.1.1', 38, '2025-09-10 22:13:52.942235', 'booked', 28),
('A.1.1', 39, '2025-09-10 23:58:51.137617', 'booked', 31),
('A.1.1', 40, NULL, 'available', NULL),
('A.1.1', 41, NULL, 'available', NULL),
('A.1.1', 42, NULL, 'available', NULL),
('A.1.1', 43, NULL, 'available', NULL),
('A.1.1', 44, NULL, 'available', NULL),
('A.1.1', 45, NULL, 'available', NULL),
('A.1.1', 46, NULL, 'available', NULL),
('A.1.2', 1, NULL, 'available', NULL),
('A.1.2', 3, NULL, 'available', NULL),
('A.1.2', 5, NULL, 'available', NULL),
('A.1.2', 10, NULL, 'available', NULL),
('A.1.2', 19, NULL, 'available', NULL),
('A.1.2', 20, NULL, 'available', NULL),
('A.1.2', 21, NULL, 'available', NULL),
('A.1.2', 23, NULL, 'available', NULL),
('A.1.2', 24, NULL, 'available', NULL),
('A.1.2', 25, NULL, 'available', NULL),
('A.1.2', 26, NULL, 'available', NULL),
('A.1.2', 33, NULL, 'available', NULL),
('A.1.2', 37, NULL, 'available', NULL),
('A.1.2', 38, NULL, 'available', NULL),
('A.1.2', 41, NULL, 'available', NULL),
('A.1.2', 43, NULL, 'available', NULL),
('A.1.2', 45, NULL, 'available', NULL),
('A.10.1', 1, NULL, 'available', NULL),
('A.10.1', 2, NULL, 'available', NULL),
('A.10.1', 3, '2025-09-05 14:51:28.308733', 'booked', 31),
('A.10.1', 5, NULL, 'available', NULL),
('A.10.1', 10, NULL, 'available', NULL),
('A.10.1', 19, NULL, 'available', NULL),
('A.10.1', 20, NULL, 'available', NULL),
('A.10.1', 21, NULL, 'available', NULL),
('A.10.1', 23, NULL, 'available', NULL),
('A.10.1', 24, '2025-09-05 14:40:40.799856', 'booked', 31),
('A.10.1', 25, '2025-09-10 13:29:49.343527', 'booked', 28),
('A.10.1', 26, NULL, 'available', NULL),
('A.10.1', 27, NULL, 'available', NULL),
('A.10.1', 28, NULL, 'available', NULL),
('A.10.1', 30, NULL, 'available', NULL),
('A.10.1', 31, NULL, 'available', NULL),
('A.10.1', 32, NULL, 'available', NULL),
('A.10.1', 33, '2025-09-11 22:56:34.832904', 'available', 31),
('A.10.1', 34, NULL, 'available', NULL),
('A.10.1', 35, NULL, 'available', NULL),
('A.10.1', 36, NULL, 'available', NULL),
('A.10.1', 37, NULL, 'available', NULL),
('A.10.1', 38, '2025-09-11 14:14:18.745985', 'available', 28),
('A.10.1', 39, NULL, 'available', NULL),
('A.10.1', 40, NULL, 'available', NULL),
('A.10.1', 41, NULL, 'available', NULL),
('A.10.1', 42, '2025-09-17 16:05:12.327433', 'booked', 43),
('A.10.1', 43, NULL, 'available', NULL),
('A.10.1', 44, NULL, 'available', NULL),
('A.10.1', 45, NULL, 'available', NULL),
('A.10.1', 46, NULL, 'available', NULL),
('A.10.2', 1, NULL, 'available', NULL),
('A.10.2', 3, NULL, 'available', NULL),
('A.10.2', 5, NULL, 'available', NULL),
('A.10.2', 10, NULL, 'available', NULL),
('A.10.2', 19, NULL, 'available', NULL),
('A.10.2', 20, NULL, 'available', NULL),
('A.10.2', 21, NULL, 'available', NULL),
('A.10.2', 23, NULL, 'available', NULL),
('A.10.2', 24, NULL, 'available', NULL),
('A.10.2', 25, NULL, 'available', NULL),
('A.10.2', 26, NULL, 'available', NULL),
('A.10.2', 33, NULL, 'available', NULL),
('A.10.2', 37, NULL, 'available', NULL),
('A.10.2', 38, NULL, 'available', NULL),
('A.10.2', 41, NULL, 'available', NULL),
('A.10.2', 43, NULL, 'available', NULL),
('A.10.2', 45, NULL, 'available', NULL),
('A.2.1', 1, NULL, 'available', NULL),
('A.2.1', 2, NULL, 'available', NULL),
('A.2.1', 3, NULL, 'available', NULL),
('A.2.1', 5, NULL, 'available', NULL),
('A.2.1', 10, NULL, 'available', NULL),
('A.2.1', 19, NULL, 'available', NULL),
('A.2.1', 20, NULL, 'available', NULL),
('A.2.1', 21, NULL, 'available', NULL),
('A.2.1', 23, NULL, 'available', NULL),
('A.2.1', 24, NULL, 'available', NULL),
('A.2.1', 25, NULL, 'available', NULL),
('A.2.1', 26, NULL, 'available', NULL),
('A.2.1', 27, NULL, 'available', NULL),
('A.2.1', 28, NULL, 'available', NULL),
('A.2.1', 30, NULL, 'available', NULL),
('A.2.1', 31, NULL, 'available', NULL),
('A.2.1', 32, NULL, 'available', NULL),
('A.2.1', 33, NULL, 'available', NULL),
('A.2.1', 34, NULL, 'available', NULL),
('A.2.1', 35, NULL, 'available', NULL),
('A.2.1', 36, NULL, 'available', NULL),
('A.2.1', 37, NULL, 'available', NULL),
('A.2.1', 38, '2025-09-11 14:27:05.154543', 'booked', 26),
('A.2.1', 39, NULL, 'available', NULL),
('A.2.1', 40, NULL, 'available', NULL),
('A.2.1', 41, NULL, 'available', NULL),
('A.2.1', 42, NULL, 'available', NULL),
('A.2.1', 43, NULL, 'available', NULL),
('A.2.1', 44, NULL, 'available', NULL),
('A.2.1', 45, NULL, 'available', NULL),
('A.2.1', 46, NULL, 'available', NULL),
('A.2.2', 1, NULL, 'available', NULL),
('A.2.2', 3, NULL, 'available', NULL),
('A.2.2', 5, NULL, 'available', NULL),
('A.2.2', 10, NULL, 'available', NULL),
('A.2.2', 19, NULL, 'available', NULL),
('A.2.2', 20, NULL, 'available', NULL),
('A.2.2', 21, NULL, 'available', NULL),
('A.2.2', 23, NULL, 'available', NULL),
('A.2.2', 24, NULL, 'available', NULL),
('A.2.2', 25, NULL, 'available', NULL),
('A.2.2', 26, NULL, 'available', NULL),
('A.2.2', 33, NULL, 'available', NULL),
('A.2.2', 37, NULL, 'available', NULL),
('A.2.2', 38, NULL, 'available', NULL),
('A.2.2', 41, NULL, 'available', NULL),
('A.2.2', 43, NULL, 'available', NULL),
('A.2.2', 45, NULL, 'available', NULL),
('A.3.1', 1, NULL, 'available', NULL),
('A.3.1', 2, NULL, 'available', NULL),
('A.3.1', 3, NULL, 'available', NULL),
('A.3.1', 5, NULL, 'available', NULL),
('A.3.1', 10, NULL, 'available', NULL),
('A.3.1', 19, NULL, 'available', NULL),
('A.3.1', 20, NULL, 'available', NULL),
('A.3.1', 21, NULL, 'available', NULL),
('A.3.1', 23, NULL, 'available', NULL),
('A.3.1', 24, NULL, 'available', NULL),
('A.3.1', 25, NULL, 'available', NULL),
('A.3.1', 26, NULL, 'available', NULL),
('A.3.1', 27, NULL, 'available', NULL),
('A.3.1', 28, NULL, 'available', NULL),
('A.3.1', 30, NULL, 'available', NULL),
('A.3.1', 31, NULL, 'available', NULL),
('A.3.1', 32, NULL, 'available', NULL),
('A.3.1', 33, NULL, 'available', NULL),
('A.3.1', 34, NULL, 'available', NULL),
('A.3.1', 35, NULL, 'available', NULL),
('A.3.1', 36, NULL, 'available', NULL),
('A.3.1', 37, NULL, 'available', NULL),
('A.3.1', 38, NULL, 'available', NULL),
('A.3.1', 39, NULL, 'available', NULL),
('A.3.1', 40, NULL, 'available', NULL),
('A.3.1', 41, NULL, 'available', NULL),
('A.3.1', 42, NULL, 'available', NULL),
('A.3.1', 43, NULL, 'available', NULL),
('A.3.1', 44, NULL, 'available', NULL),
('A.3.1', 45, NULL, 'available', NULL),
('A.3.1', 46, NULL, 'available', NULL),
('A.3.2', 1, NULL, 'available', NULL),
('A.3.2', 3, NULL, 'available', NULL),
('A.3.2', 5, NULL, 'available', NULL),
('A.3.2', 10, NULL, 'available', NULL),
('A.3.2', 19, NULL, 'available', NULL),
('A.3.2', 20, NULL, 'available', NULL),
('A.3.2', 21, NULL, 'available', NULL),
('A.3.2', 23, NULL, 'available', NULL),
('A.3.2', 24, NULL, 'available', NULL),
('A.3.2', 25, NULL, 'available', NULL),
('A.3.2', 26, NULL, 'available', NULL),
('A.3.2', 33, NULL, 'available', NULL),
('A.3.2', 37, NULL, 'available', NULL),
('A.3.2', 38, NULL, 'available', NULL),
('A.3.2', 41, NULL, 'available', NULL),
('A.3.2', 43, NULL, 'available', NULL),
('A.3.2', 45, NULL, 'available', NULL),
('A.4.1', 1, NULL, 'available', NULL),
('A.4.1', 2, NULL, 'available', NULL),
('A.4.1', 3, NULL, 'available', NULL),
('A.4.1', 5, NULL, 'available', NULL),
('A.4.1', 10, NULL, 'available', NULL),
('A.4.1', 19, NULL, 'available', NULL),
('A.4.1', 20, NULL, 'available', NULL),
('A.4.1', 21, NULL, 'available', NULL),
('A.4.1', 23, NULL, 'available', NULL),
('A.4.1', 24, '2025-09-04 14:31:45.449908', 'locked', 18),
('A.4.1', 25, NULL, 'available', NULL),
('A.4.1', 26, NULL, 'available', NULL),
('A.4.1', 27, NULL, 'available', NULL),
('A.4.1', 28, NULL, 'available', NULL),
('A.4.1', 30, NULL, 'available', NULL),
('A.4.1', 31, NULL, 'available', NULL),
('A.4.1', 32, NULL, 'available', NULL),
('A.4.1', 33, NULL, 'available', NULL),
('A.4.1', 34, NULL, 'available', NULL),
('A.4.1', 35, NULL, 'available', NULL),
('A.4.1', 36, NULL, 'available', NULL),
('A.4.1', 37, NULL, 'available', NULL),
('A.4.1', 38, NULL, 'available', NULL),
('A.4.1', 39, NULL, 'available', NULL),
('A.4.1', 40, NULL, 'available', NULL),
('A.4.1', 41, NULL, 'available', NULL),
('A.4.1', 42, NULL, 'available', NULL),
('A.4.1', 43, NULL, 'available', NULL),
('A.4.1', 44, NULL, 'available', NULL),
('A.4.1', 45, NULL, 'available', NULL),
('A.4.1', 46, NULL, 'available', NULL),
('A.4.2', 1, NULL, 'available', NULL),
('A.4.2', 3, NULL, 'available', NULL),
('A.4.2', 5, NULL, 'available', NULL),
('A.4.2', 10, NULL, 'available', NULL),
('A.4.2', 19, NULL, 'available', NULL),
('A.4.2', 20, NULL, 'available', NULL),
('A.4.2', 21, NULL, 'available', NULL),
('A.4.2', 23, NULL, 'available', NULL),
('A.4.2', 24, NULL, 'available', NULL),
('A.4.2', 25, NULL, 'available', NULL),
('A.4.2', 26, NULL, 'available', NULL),
('A.4.2', 33, NULL, 'available', NULL),
('A.4.2', 37, NULL, 'available', NULL),
('A.4.2', 38, NULL, 'available', NULL),
('A.4.2', 41, NULL, 'available', NULL),
('A.4.2', 43, NULL, 'available', NULL),
('A.4.2', 45, NULL, 'available', NULL),
('A.5.1', 1, NULL, 'available', NULL),
('A.5.1', 2, NULL, 'available', NULL),
('A.5.1', 3, NULL, 'available', NULL),
('A.5.1', 5, NULL, 'available', NULL),
('A.5.1', 10, NULL, 'available', NULL),
('A.5.1', 19, NULL, 'available', NULL),
('A.5.1', 20, NULL, 'available', NULL),
('A.5.1', 21, NULL, 'available', NULL),
('A.5.1', 23, NULL, 'available', NULL),
('A.5.1', 24, '2025-09-04 14:20:55.524866', 'locked', 18),
('A.5.1', 25, '2025-09-11 21:32:52.471174', 'booked', 26),
('A.5.1', 26, NULL, 'available', NULL),
('A.5.1', 27, NULL, 'available', NULL),
('A.5.1', 28, NULL, 'available', NULL),
('A.5.1', 30, NULL, 'available', NULL),
('A.5.1', 31, NULL, 'available', NULL),
('A.5.1', 32, NULL, 'available', NULL),
('A.5.1', 33, '2025-09-11 21:44:04.338486', 'available', 26),
('A.5.1', 34, NULL, 'available', NULL),
('A.5.1', 35, NULL, 'available', NULL),
('A.5.1', 36, NULL, 'available', NULL),
('A.5.1', 37, NULL, 'available', NULL),
('A.5.1', 38, '2025-09-11 14:18:27.917247', 'booked', 28),
('A.5.1', 39, NULL, 'available', NULL),
('A.5.1', 40, NULL, 'available', NULL),
('A.5.1', 41, NULL, 'available', NULL),
('A.5.1', 42, NULL, 'available', NULL),
('A.5.1', 43, NULL, 'available', NULL),
('A.5.1', 44, NULL, 'available', NULL),
('A.5.1', 45, NULL, 'available', NULL),
('A.5.1', 46, NULL, 'available', NULL),
('A.5.2', 1, NULL, 'available', NULL),
('A.5.2', 3, NULL, 'available', NULL),
('A.5.2', 5, NULL, 'available', NULL),
('A.5.2', 10, NULL, 'available', NULL),
('A.5.2', 19, NULL, 'available', NULL),
('A.5.2', 20, NULL, 'available', NULL),
('A.5.2', 21, NULL, 'available', NULL),
('A.5.2', 23, NULL, 'available', NULL),
('A.5.2', 24, NULL, 'available', NULL),
('A.5.2', 25, NULL, 'available', NULL),
('A.5.2', 26, NULL, 'available', NULL),
('A.5.2', 33, NULL, 'available', NULL),
('A.5.2', 37, NULL, 'available', NULL),
('A.5.2', 38, NULL, 'available', NULL),
('A.5.2', 41, NULL, 'available', NULL),
('A.5.2', 43, NULL, 'available', NULL),
('A.5.2', 45, NULL, 'available', NULL),
('A.6.1', 1, NULL, 'available', NULL),
('A.6.1', 2, NULL, 'available', NULL),
('A.6.1', 3, NULL, 'available', NULL),
('A.6.1', 5, NULL, 'available', NULL),
('A.6.1', 10, NULL, 'available', NULL),
('A.6.1', 19, NULL, 'available', NULL),
('A.6.1', 20, NULL, 'available', NULL),
('A.6.1', 21, NULL, 'available', NULL),
('A.6.1', 23, NULL, 'available', NULL),
('A.6.1', 24, NULL, 'available', NULL),
('A.6.1', 25, NULL, 'available', NULL),
('A.6.1', 26, NULL, 'available', NULL),
('A.6.1', 27, NULL, 'available', NULL),
('A.6.1', 28, NULL, 'available', NULL),
('A.6.1', 30, NULL, 'available', NULL),
('A.6.1', 31, NULL, 'available', NULL),
('A.6.1', 32, NULL, 'available', NULL),
('A.6.1', 33, NULL, 'available', NULL),
('A.6.1', 34, '2025-09-20 09:39:20.000000', 'available', 28),
('A.6.1', 35, NULL, 'available', NULL),
('A.6.1', 36, NULL, 'available', NULL),
('A.6.1', 37, NULL, 'available', NULL),
('A.6.1', 38, '2025-09-11 14:17:04.598175', 'booked', 28),
('A.6.1', 39, NULL, 'available', NULL),
('A.6.1', 40, NULL, 'available', NULL),
('A.6.1', 41, NULL, 'available', NULL),
('A.6.1', 42, NULL, 'available', NULL),
('A.6.1', 43, NULL, 'available', NULL),
('A.6.1', 44, NULL, 'available', NULL),
('A.6.1', 45, NULL, 'available', NULL),
('A.6.1', 46, NULL, 'available', NULL),
('A.6.2', 1, NULL, 'available', NULL),
('A.6.2', 3, NULL, 'available', NULL),
('A.6.2', 5, NULL, 'available', NULL),
('A.6.2', 10, NULL, 'available', NULL),
('A.6.2', 19, NULL, 'available', NULL),
('A.6.2', 20, NULL, 'available', NULL),
('A.6.2', 21, NULL, 'available', NULL),
('A.6.2', 23, NULL, 'available', NULL),
('A.6.2', 24, NULL, 'available', NULL),
('A.6.2', 25, NULL, 'available', NULL),
('A.6.2', 26, NULL, 'available', NULL),
('A.6.2', 33, NULL, 'available', NULL),
('A.6.2', 37, NULL, 'available', NULL),
('A.6.2', 38, NULL, 'available', NULL),
('A.6.2', 41, NULL, 'available', NULL),
('A.6.2', 43, NULL, 'available', NULL),
('A.6.2', 45, NULL, 'available', NULL),
('A.7.1', 1, NULL, 'available', NULL),
('A.7.1', 2, NULL, 'available', NULL),
('A.7.1', 3, NULL, 'available', NULL),
('A.7.1', 5, NULL, 'available', NULL),
('A.7.1', 10, NULL, 'available', NULL),
('A.7.1', 19, NULL, 'available', NULL),
('A.7.1', 20, NULL, 'available', NULL),
('A.7.1', 21, NULL, 'available', NULL),
('A.7.1', 23, NULL, 'available', NULL),
('A.7.1', 24, '2025-09-04 23:24:09.849842', 'locked', 18),
('A.7.1', 25, '2025-09-10 13:20:03.669397', 'booked', 28),
('A.7.1', 26, NULL, 'available', NULL),
('A.7.1', 27, NULL, 'available', NULL),
('A.7.1', 28, NULL, 'available', NULL),
('A.7.1', 30, NULL, 'available', NULL),
('A.7.1', 31, NULL, 'available', NULL),
('A.7.1', 32, NULL, 'available', NULL),
('A.7.1', 33, NULL, 'available', NULL),
('A.7.1', 34, NULL, 'available', NULL),
('A.7.1', 35, NULL, 'available', NULL),
('A.7.1', 36, NULL, 'available', NULL),
('A.7.1', 37, NULL, 'available', NULL),
('A.7.1', 38, '2025-09-11 23:12:51.171313', 'booked', 26),
('A.7.1', 39, NULL, 'available', NULL),
('A.7.1', 40, NULL, 'available', NULL),
('A.7.1', 41, NULL, 'available', NULL),
('A.7.1', 42, NULL, 'available', NULL),
('A.7.1', 43, NULL, 'available', NULL),
('A.7.1', 44, NULL, 'available', NULL),
('A.7.1', 45, NULL, 'available', NULL),
('A.7.1', 46, NULL, 'available', NULL),
('A.7.2', 1, NULL, 'available', NULL),
('A.7.2', 3, NULL, 'available', NULL),
('A.7.2', 5, NULL, 'available', NULL),
('A.7.2', 10, NULL, 'available', NULL),
('A.7.2', 19, NULL, 'available', NULL),
('A.7.2', 20, NULL, 'available', NULL),
('A.7.2', 21, NULL, 'available', NULL),
('A.7.2', 23, NULL, 'available', NULL),
('A.7.2', 24, NULL, 'available', NULL),
('A.7.2', 25, NULL, 'available', NULL),
('A.7.2', 26, NULL, 'available', NULL),
('A.7.2', 33, NULL, 'available', NULL),
('A.7.2', 37, NULL, 'available', NULL),
('A.7.2', 38, NULL, 'available', NULL),
('A.7.2', 41, NULL, 'available', NULL),
('A.7.2', 43, NULL, 'available', NULL),
('A.7.2', 45, NULL, 'available', NULL),
('A.8.1', 1, NULL, 'available', NULL),
('A.8.1', 2, NULL, 'available', NULL),
('A.8.1', 3, NULL, 'available', NULL),
('A.8.1', 5, NULL, 'available', NULL),
('A.8.1', 10, NULL, 'available', NULL),
('A.8.1', 19, NULL, 'available', NULL),
('A.8.1', 20, NULL, 'available', NULL),
('A.8.1', 21, NULL, 'available', NULL),
('A.8.1', 23, NULL, 'available', NULL),
('A.8.1', 24, NULL, 'available', NULL),
('A.8.1', 25, NULL, 'available', NULL),
('A.8.1', 26, NULL, 'available', NULL),
('A.8.1', 27, NULL, 'available', NULL),
('A.8.1', 28, NULL, 'available', NULL),
('A.8.1', 30, NULL, 'available', NULL),
('A.8.1', 31, NULL, 'available', NULL),
('A.8.1', 32, NULL, 'available', NULL),
('A.8.1', 33, NULL, 'available', NULL),
('A.8.1', 34, NULL, 'available', NULL),
('A.8.1', 35, NULL, 'available', NULL),
('A.8.1', 36, NULL, 'available', NULL),
('A.8.1', 37, NULL, 'available', NULL),
('A.8.1', 38, '2025-09-11 14:14:18.553653', 'available', 28),
('A.8.1', 39, NULL, 'available', NULL),
('A.8.1', 40, NULL, 'available', NULL),
('A.8.1', 41, NULL, 'available', NULL),
('A.8.1', 42, NULL, 'available', NULL),
('A.8.1', 43, NULL, 'available', NULL),
('A.8.1', 44, NULL, 'available', NULL),
('A.8.1', 45, NULL, 'available', NULL),
('A.8.1', 46, NULL, 'available', NULL),
('A.8.2', 1, NULL, 'available', NULL),
('A.8.2', 3, NULL, 'available', NULL),
('A.8.2', 5, NULL, 'available', NULL),
('A.8.2', 10, NULL, 'available', NULL),
('A.8.2', 19, NULL, 'available', NULL),
('A.8.2', 20, NULL, 'available', NULL),
('A.8.2', 21, NULL, 'available', NULL),
('A.8.2', 23, NULL, 'available', NULL),
('A.8.2', 24, NULL, 'available', NULL),
('A.8.2', 25, NULL, 'available', NULL),
('A.8.2', 26, NULL, 'available', NULL),
('A.8.2', 33, NULL, 'available', NULL),
('A.8.2', 37, NULL, 'available', NULL),
('A.8.2', 38, NULL, 'available', NULL),
('A.8.2', 41, NULL, 'available', NULL),
('A.8.2', 43, NULL, 'available', NULL),
('A.8.2', 45, NULL, 'available', NULL),
('A.9.1', 1, NULL, 'available', NULL),
('A.9.1', 2, NULL, 'available', NULL),
('A.9.1', 3, NULL, 'available', NULL),
('A.9.1', 5, NULL, 'available', NULL),
('A.9.1', 10, NULL, 'available', NULL),
('A.9.1', 19, NULL, 'available', NULL),
('A.9.1', 20, NULL, 'available', NULL),
('A.9.1', 21, NULL, 'available', NULL),
('A.9.1', 23, NULL, 'available', NULL),
('A.9.1', 24, NULL, 'available', NULL),
('A.9.1', 25, NULL, 'available', NULL),
('A.9.1', 26, NULL, 'available', NULL),
('A.9.1', 27, NULL, 'available', NULL),
('A.9.1', 28, NULL, 'available', NULL),
('A.9.1', 30, NULL, 'available', NULL),
('A.9.1', 31, NULL, 'available', NULL),
('A.9.1', 32, NULL, 'available', NULL),
('A.9.1', 33, NULL, 'available', NULL),
('A.9.1', 34, NULL, 'available', NULL),
('A.9.1', 35, NULL, 'available', NULL),
('A.9.1', 36, NULL, 'available', NULL),
('A.9.1', 37, NULL, 'available', NULL),
('A.9.1', 38, '2025-09-11 13:56:23.687604', 'booked', 28),
('A.9.1', 39, NULL, 'available', NULL),
('A.9.1', 40, NULL, 'available', NULL),
('A.9.1', 41, NULL, 'available', NULL),
('A.9.1', 42, NULL, 'available', NULL),
('A.9.1', 43, NULL, 'available', NULL),
('A.9.1', 44, NULL, 'available', NULL),
('A.9.1', 45, NULL, 'available', NULL),
('A.9.1', 46, NULL, 'available', NULL),
('A.9.2', 1, NULL, 'available', NULL),
('A.9.2', 3, NULL, 'available', NULL),
('A.9.2', 5, NULL, 'available', NULL),
('A.9.2', 10, NULL, 'available', NULL),
('A.9.2', 19, NULL, 'available', NULL),
('A.9.2', 20, NULL, 'available', NULL),
('A.9.2', 21, NULL, 'available', NULL),
('A.9.2', 23, NULL, 'available', NULL),
('A.9.2', 24, NULL, 'available', NULL),
('A.9.2', 25, NULL, 'available', NULL),
('A.9.2', 26, NULL, 'available', NULL),
('A.9.2', 33, NULL, 'available', NULL),
('A.9.2', 37, NULL, 'available', NULL),
('A.9.2', 38, NULL, 'available', NULL),
('A.9.2', 41, NULL, 'available', NULL),
('A.9.2', 43, NULL, 'available', NULL),
('A.9.2', 45, NULL, 'available', NULL),
('B.1.1', 1, NULL, 'available', NULL),
('B.1.1', 2, NULL, 'available', NULL),
('B.1.1', 3, NULL, 'available', NULL),
('B.1.1', 5, NULL, 'available', NULL),
('B.1.1', 10, NULL, 'available', NULL),
('B.1.1', 19, NULL, 'available', NULL),
('B.1.1', 20, NULL, 'available', NULL),
('B.1.1', 21, NULL, 'available', NULL),
('B.1.1', 23, NULL, 'available', NULL),
('B.1.1', 24, '2025-09-04 10:59:08.737026', 'locked', 18),
('B.1.1', 25, NULL, 'available', NULL),
('B.1.1', 26, NULL, 'available', NULL),
('B.1.1', 27, NULL, 'available', NULL),
('B.1.1', 28, NULL, 'available', NULL),
('B.1.1', 30, NULL, 'available', NULL),
('B.1.1', 31, NULL, 'available', NULL),
('B.1.1', 32, NULL, 'available', NULL),
('B.1.1', 33, '2025-09-18 03:52:29.173476', 'locked', 31),
('B.1.1', 34, NULL, 'available', NULL),
('B.1.1', 35, NULL, 'available', NULL),
('B.1.1', 36, NULL, 'available', NULL),
('B.1.1', 37, '2025-09-18 03:23:06.149749', 'locked', 31),
('B.1.1', 38, '2025-09-10 22:13:52.953263', 'booked', 28),
('B.1.1', 39, '2025-09-10 23:58:51.254496', 'booked', 31),
('B.1.1', 40, NULL, 'available', NULL),
('B.1.1', 41, NULL, 'available', NULL),
('B.1.1', 42, NULL, 'available', NULL),
('B.1.1', 43, NULL, 'available', NULL),
('B.1.1', 44, NULL, 'available', NULL),
('B.1.1', 45, NULL, 'available', NULL),
('B.1.1', 46, NULL, 'available', NULL),
('B.1.2', 1, NULL, 'available', NULL),
('B.1.2', 3, NULL, 'available', NULL),
('B.1.2', 5, NULL, 'available', NULL),
('B.1.2', 10, NULL, 'available', NULL),
('B.1.2', 19, NULL, 'available', NULL),
('B.1.2', 20, NULL, 'available', NULL),
('B.1.2', 21, NULL, 'available', NULL),
('B.1.2', 23, NULL, 'available', NULL),
('B.1.2', 24, NULL, 'available', NULL),
('B.1.2', 25, NULL, 'available', NULL),
('B.1.2', 26, NULL, 'available', NULL),
('B.1.2', 33, NULL, 'available', NULL),
('B.1.2', 37, NULL, 'available', NULL),
('B.1.2', 38, NULL, 'available', NULL),
('B.1.2', 41, NULL, 'available', NULL),
('B.1.2', 43, NULL, 'available', NULL),
('B.1.2', 45, NULL, 'available', NULL),
('B.10.1', 1, NULL, 'available', NULL),
('B.10.1', 2, NULL, 'available', NULL),
('B.10.1', 3, NULL, 'available', NULL),
('B.10.1', 5, NULL, 'available', NULL),
('B.10.1', 10, NULL, 'available', NULL),
('B.10.1', 19, NULL, 'available', NULL),
('B.10.1', 20, NULL, 'available', NULL),
('B.10.1', 21, NULL, 'available', NULL),
('B.10.1', 23, NULL, 'available', NULL),
('B.10.1', 24, NULL, 'available', NULL),
('B.10.1', 25, '2025-09-10 13:29:49.418197', 'booked', 28),
('B.10.1', 26, NULL, 'available', NULL),
('B.10.1', 27, NULL, 'available', NULL),
('B.10.1', 28, NULL, 'available', NULL),
('B.10.1', 30, NULL, 'available', NULL),
('B.10.1', 31, NULL, 'available', NULL),
('B.10.1', 32, NULL, 'available', NULL),
('B.10.1', 33, NULL, 'available', NULL),
('B.10.1', 34, NULL, 'available', NULL),
('B.10.1', 35, NULL, 'available', NULL),
('B.10.1', 36, NULL, 'available', NULL),
('B.10.1', 37, NULL, 'available', NULL),
('B.10.1', 38, '2025-09-11 14:14:18.726989', 'available', 28),
('B.10.1', 39, NULL, 'available', NULL),
('B.10.1', 40, NULL, 'available', NULL),
('B.10.1', 41, NULL, 'available', NULL),
('B.10.1', 42, '2025-09-17 16:05:12.352712', 'booked', 43),
('B.10.1', 43, NULL, 'available', NULL),
('B.10.1', 44, NULL, 'available', NULL),
('B.10.1', 45, NULL, 'available', NULL),
('B.10.1', 46, NULL, 'available', NULL),
('B.10.2', 1, NULL, 'available', NULL),
('B.10.2', 3, NULL, 'available', NULL),
('B.10.2', 5, NULL, 'available', NULL),
('B.10.2', 10, NULL, 'available', NULL),
('B.10.2', 19, NULL, 'available', NULL),
('B.10.2', 20, NULL, 'available', NULL),
('B.10.2', 21, NULL, 'available', NULL),
('B.10.2', 23, NULL, 'available', NULL),
('B.10.2', 24, NULL, 'available', NULL),
('B.10.2', 25, NULL, 'available', NULL),
('B.10.2', 26, NULL, 'available', NULL),
('B.10.2', 33, NULL, 'available', NULL),
('B.10.2', 37, NULL, 'available', NULL),
('B.10.2', 38, NULL, 'available', NULL),
('B.10.2', 41, NULL, 'available', NULL),
('B.10.2', 43, NULL, 'available', NULL),
('B.10.2', 45, NULL, 'available', NULL),
('B.2.1', 1, NULL, 'available', NULL),
('B.2.1', 2, NULL, 'available', NULL),
('B.2.1', 3, NULL, 'available', NULL),
('B.2.1', 5, NULL, 'available', NULL),
('B.2.1', 10, NULL, 'available', NULL),
('B.2.1', 19, NULL, 'available', NULL),
('B.2.1', 20, NULL, 'available', NULL),
('B.2.1', 21, NULL, 'available', NULL),
('B.2.1', 23, NULL, 'available', NULL),
('B.2.1', 24, NULL, 'available', NULL),
('B.2.1', 25, NULL, 'available', NULL),
('B.2.1', 26, NULL, 'available', NULL),
('B.2.1', 27, NULL, 'available', NULL),
('B.2.1', 28, NULL, 'available', NULL),
('B.2.1', 30, NULL, 'available', NULL),
('B.2.1', 31, NULL, 'available', NULL),
('B.2.1', 32, NULL, 'available', NULL),
('B.2.1', 33, NULL, 'available', NULL),
('B.2.1', 34, NULL, 'available', NULL),
('B.2.1', 35, NULL, 'available', NULL),
('B.2.1', 36, NULL, 'available', NULL),
('B.2.1', 37, NULL, 'available', NULL),
('B.2.1', 38, '2025-09-11 14:27:05.197374', 'booked', 26),
('B.2.1', 39, NULL, 'available', NULL),
('B.2.1', 40, NULL, 'available', NULL),
('B.2.1', 41, NULL, 'available', NULL),
('B.2.1', 42, NULL, 'available', NULL),
('B.2.1', 43, NULL, 'available', NULL),
('B.2.1', 44, NULL, 'available', NULL),
('B.2.1', 45, NULL, 'available', NULL),
('B.2.1', 46, NULL, 'available', NULL),
('B.2.2', 1, NULL, 'available', NULL),
('B.2.2', 3, NULL, 'available', NULL),
('B.2.2', 5, NULL, 'available', NULL),
('B.2.2', 10, NULL, 'available', NULL),
('B.2.2', 19, NULL, 'available', NULL),
('B.2.2', 20, NULL, 'available', NULL),
('B.2.2', 21, NULL, 'available', NULL),
('B.2.2', 23, NULL, 'available', NULL),
('B.2.2', 24, NULL, 'available', NULL),
('B.2.2', 25, NULL, 'available', NULL),
('B.2.2', 26, NULL, 'available', NULL),
('B.2.2', 33, NULL, 'available', NULL),
('B.2.2', 37, NULL, 'available', NULL),
('B.2.2', 38, NULL, 'available', NULL),
('B.2.2', 41, NULL, 'available', NULL),
('B.2.2', 43, NULL, 'available', NULL),
('B.2.2', 45, NULL, 'available', NULL),
('B.3.1', 1, NULL, 'available', NULL),
('B.3.1', 2, NULL, 'available', NULL),
('B.3.1', 3, NULL, 'available', NULL),
('B.3.1', 5, NULL, 'available', NULL),
('B.3.1', 10, NULL, 'available', NULL),
('B.3.1', 19, NULL, 'available', NULL),
('B.3.1', 20, NULL, 'available', NULL),
('B.3.1', 21, NULL, 'available', NULL),
('B.3.1', 23, NULL, 'available', NULL),
('B.3.1', 24, NULL, 'available', NULL),
('B.3.1', 25, '2025-09-10 00:36:07.195628', 'locked', 28),
('B.3.1', 26, NULL, 'available', NULL),
('B.3.1', 27, NULL, 'available', NULL),
('B.3.1', 28, NULL, 'available', NULL),
('B.3.1', 30, NULL, 'available', NULL),
('B.3.1', 31, NULL, 'available', NULL),
('B.3.1', 32, NULL, 'available', NULL),
('B.3.1', 33, NULL, 'available', NULL),
('B.3.1', 34, NULL, 'available', NULL),
('B.3.1', 35, NULL, 'available', NULL),
('B.3.1', 36, NULL, 'available', NULL),
('B.3.1', 37, NULL, 'available', NULL),
('B.3.1', 38, '2025-09-11 23:24:35.430518', 'booked', 26),
('B.3.1', 39, NULL, 'available', NULL),
('B.3.1', 40, NULL, 'available', NULL),
('B.3.1', 41, NULL, 'available', NULL),
('B.3.1', 42, '2025-09-19 22:20:54.049579', 'booked', 28),
('B.3.1', 43, NULL, 'available', NULL),
('B.3.1', 44, NULL, 'available', NULL),
('B.3.1', 45, NULL, 'available', NULL),
('B.3.1', 46, NULL, 'available', NULL),
('B.3.2', 1, NULL, 'available', NULL),
('B.3.2', 3, NULL, 'available', NULL),
('B.3.2', 5, NULL, 'available', NULL),
('B.3.2', 10, NULL, 'available', NULL),
('B.3.2', 19, NULL, 'available', NULL),
('B.3.2', 20, NULL, 'available', NULL),
('B.3.2', 21, NULL, 'available', NULL),
('B.3.2', 23, NULL, 'available', NULL),
('B.3.2', 24, NULL, 'available', NULL),
('B.3.2', 25, NULL, 'available', NULL),
('B.3.2', 26, NULL, 'available', NULL),
('B.3.2', 33, NULL, 'available', NULL),
('B.3.2', 37, NULL, 'available', NULL),
('B.3.2', 38, NULL, 'available', NULL),
('B.3.2', 41, NULL, 'available', NULL),
('B.3.2', 43, NULL, 'available', NULL),
('B.3.2', 45, NULL, 'available', NULL),
('B.4.1', 1, NULL, 'available', NULL),
('B.4.1', 2, NULL, 'available', NULL),
('B.4.1', 3, NULL, 'available', NULL),
('B.4.1', 5, NULL, 'available', NULL),
('B.4.1', 10, NULL, 'available', NULL),
('B.4.1', 19, NULL, 'available', NULL),
('B.4.1', 20, NULL, 'available', NULL),
('B.4.1', 21, NULL, 'available', NULL),
('B.4.1', 23, NULL, 'available', NULL),
('B.4.1', 24, '2025-09-04 14:59:49.256491', 'locked', 36),
('B.4.1', 25, '2025-09-09 22:04:36.179508', 'locked', 28),
('B.4.1', 26, NULL, 'available', NULL),
('B.4.1', 27, NULL, 'available', NULL),
('B.4.1', 28, NULL, 'available', NULL),
('B.4.1', 30, NULL, 'available', NULL),
('B.4.1', 31, NULL, 'available', NULL),
('B.4.1', 32, NULL, 'available', NULL),
('B.4.1', 33, NULL, 'available', NULL),
('B.4.1', 34, NULL, 'available', NULL),
('B.4.1', 35, NULL, 'available', NULL),
('B.4.1', 36, NULL, 'available', NULL),
('B.4.1', 37, NULL, 'available', NULL),
('B.4.1', 38, '2025-09-11 21:54:05.414436', 'booked', 26),
('B.4.1', 39, NULL, 'available', NULL),
('B.4.1', 40, NULL, 'available', NULL),
('B.4.1', 41, NULL, 'available', NULL),
('B.4.1', 42, NULL, 'available', NULL),
('B.4.1', 43, NULL, 'available', NULL),
('B.4.1', 44, NULL, 'available', NULL),
('B.4.1', 45, NULL, 'available', NULL),
('B.4.1', 46, NULL, 'available', NULL),
('B.4.2', 1, NULL, 'available', NULL),
('B.4.2', 3, NULL, 'available', NULL),
('B.4.2', 5, NULL, 'available', NULL),
('B.4.2', 10, NULL, 'available', NULL),
('B.4.2', 19, NULL, 'available', NULL),
('B.4.2', 20, NULL, 'available', NULL),
('B.4.2', 21, NULL, 'available', NULL),
('B.4.2', 23, NULL, 'available', NULL),
('B.4.2', 24, NULL, 'available', NULL),
('B.4.2', 25, NULL, 'available', NULL),
('B.4.2', 26, NULL, 'available', NULL),
('B.4.2', 33, NULL, 'available', NULL),
('B.4.2', 37, NULL, 'available', NULL),
('B.4.2', 38, NULL, 'available', NULL),
('B.4.2', 41, NULL, 'available', NULL),
('B.4.2', 43, NULL, 'available', NULL),
('B.4.2', 45, NULL, 'available', NULL),
('B.5.1', 1, NULL, 'available', NULL),
('B.5.1', 2, NULL, 'available', NULL),
('B.5.1', 3, NULL, 'available', NULL),
('B.5.1', 5, NULL, 'available', NULL),
('B.5.1', 10, NULL, 'available', NULL),
('B.5.1', 19, NULL, 'available', NULL),
('B.5.1', 20, NULL, 'available', NULL),
('B.5.1', 21, NULL, 'available', NULL),
('B.5.1', 23, NULL, 'available', NULL),
('B.5.1', 24, '2025-09-04 14:05:47.640150', 'locked', 18),
('B.5.1', 25, '2025-09-11 21:32:52.497640', 'booked', 26),
('B.5.1', 26, NULL, 'available', NULL),
('B.5.1', 27, NULL, 'available', NULL),
('B.5.1', 28, NULL, 'available', NULL),
('B.5.1', 30, NULL, 'available', NULL),
('B.5.1', 31, NULL, 'available', NULL),
('B.5.1', 32, NULL, 'available', NULL),
('B.5.1', 33, '2025-09-11 21:44:04.353792', 'available', 26),
('B.5.1', 34, NULL, 'available', NULL),
('B.5.1', 35, NULL, 'available', NULL),
('B.5.1', 36, NULL, 'available', NULL),
('B.5.1', 37, '2025-09-18 10:02:06.533977', 'booked', 26),
('B.5.1', 38, '2025-09-11 14:18:27.978820', 'booked', 28),
('B.5.1', 39, NULL, 'available', NULL),
('B.5.1', 40, NULL, 'available', NULL),
('B.5.1', 41, NULL, 'available', NULL),
('B.5.1', 42, '2025-09-19 23:40:22.835064', 'booked', 57),
('B.5.1', 43, NULL, 'available', NULL),
('B.5.1', 44, NULL, 'available', NULL),
('B.5.1', 45, NULL, 'available', NULL),
('B.5.1', 46, NULL, 'available', NULL),
('B.5.2', 1, NULL, 'available', NULL),
('B.5.2', 3, NULL, 'available', NULL),
('B.5.2', 5, NULL, 'available', NULL),
('B.5.2', 10, NULL, 'available', NULL),
('B.5.2', 19, NULL, 'available', NULL),
('B.5.2', 20, NULL, 'available', NULL),
('B.5.2', 21, NULL, 'available', NULL),
('B.5.2', 23, NULL, 'available', NULL),
('B.5.2', 24, NULL, 'available', NULL),
('B.5.2', 25, NULL, 'available', NULL),
('B.5.2', 26, NULL, 'available', NULL),
('B.5.2', 33, NULL, 'available', NULL),
('B.5.2', 37, NULL, 'available', NULL),
('B.5.2', 38, NULL, 'available', NULL),
('B.5.2', 41, NULL, 'available', NULL),
('B.5.2', 43, NULL, 'available', NULL),
('B.5.2', 45, NULL, 'available', NULL),
('B.6.1', 1, NULL, 'available', NULL),
('B.6.1', 2, NULL, 'available', NULL),
('B.6.1', 3, NULL, 'available', NULL),
('B.6.1', 5, NULL, 'available', NULL),
('B.6.1', 10, NULL, 'available', NULL),
('B.6.1', 19, NULL, 'available', NULL),
('B.6.1', 20, NULL, 'available', NULL),
('B.6.1', 21, NULL, 'available', NULL),
('B.6.1', 23, NULL, 'available', NULL),
('B.6.1', 24, NULL, 'available', NULL),
('B.6.1', 25, NULL, 'available', NULL),
('B.6.1', 26, NULL, 'available', NULL),
('B.6.1', 27, NULL, 'available', NULL),
('B.6.1', 28, NULL, 'available', NULL),
('B.6.1', 30, NULL, 'available', NULL),
('B.6.1', 31, NULL, 'available', NULL),
('B.6.1', 32, NULL, 'available', NULL),
('B.6.1', 33, NULL, 'available', NULL),
('B.6.1', 34, '2025-09-20 09:39:20.000000', 'available', 28),
('B.6.1', 35, NULL, 'available', NULL),
('B.6.1', 36, NULL, 'available', NULL),
('B.6.1', 37, NULL, 'available', NULL),
('B.6.1', 38, '2025-09-11 14:17:04.622935', 'booked', 28),
('B.6.1', 39, NULL, 'available', NULL),
('B.6.1', 40, NULL, 'available', NULL),
('B.6.1', 41, NULL, 'available', NULL),
('B.6.1', 42, NULL, 'available', NULL),
('B.6.1', 43, NULL, 'available', NULL),
('B.6.1', 44, NULL, 'available', NULL),
('B.6.1', 45, NULL, 'available', NULL),
('B.6.1', 46, NULL, 'available', NULL),
('B.6.2', 1, NULL, 'available', NULL),
('B.6.2', 3, NULL, 'available', NULL),
('B.6.2', 5, NULL, 'available', NULL),
('B.6.2', 10, NULL, 'available', NULL),
('B.6.2', 19, NULL, 'available', NULL),
('B.6.2', 20, NULL, 'available', NULL),
('B.6.2', 21, NULL, 'available', NULL),
('B.6.2', 23, NULL, 'available', NULL),
('B.6.2', 24, NULL, 'available', NULL),
('B.6.2', 25, NULL, 'available', NULL),
('B.6.2', 26, NULL, 'available', NULL),
('B.6.2', 33, NULL, 'available', NULL),
('B.6.2', 37, NULL, 'available', NULL),
('B.6.2', 38, NULL, 'available', NULL),
('B.6.2', 41, NULL, 'available', NULL),
('B.6.2', 43, NULL, 'available', NULL),
('B.6.2', 45, NULL, 'available', NULL),
('B.7.1', 1, NULL, 'available', NULL),
('B.7.1', 2, NULL, 'available', NULL),
('B.7.1', 3, NULL, 'available', NULL),
('B.7.1', 5, NULL, 'available', NULL),
('B.7.1', 10, NULL, 'available', NULL),
('B.7.1', 19, NULL, 'available', NULL),
('B.7.1', 20, NULL, 'available', NULL),
('B.7.1', 21, NULL, 'available', NULL),
('B.7.1', 23, NULL, 'available', NULL),
('B.7.1', 24, '2025-09-04 14:49:05.170456', 'locked', 28),
('B.7.1', 25, '2025-09-10 13:20:03.725306', 'booked', 28),
('B.7.1', 26, NULL, 'available', NULL),
('B.7.1', 27, NULL, 'available', NULL),
('B.7.1', 28, NULL, 'available', NULL),
('B.7.1', 30, NULL, 'available', NULL),
('B.7.1', 31, NULL, 'available', NULL),
('B.7.1', 32, NULL, 'available', NULL),
('B.7.1', 33, NULL, 'available', NULL),
('B.7.1', 34, NULL, 'available', NULL),
('B.7.1', 35, NULL, 'available', NULL),
('B.7.1', 36, NULL, 'available', NULL),
('B.7.1', 37, NULL, 'available', NULL),
('B.7.1', 38, NULL, 'available', NULL),
('B.7.1', 39, NULL, 'available', NULL),
('B.7.1', 40, NULL, 'available', NULL),
('B.7.1', 41, NULL, 'available', NULL),
('B.7.1', 42, NULL, 'available', NULL),
('B.7.1', 43, NULL, 'available', NULL),
('B.7.1', 44, NULL, 'available', NULL),
('B.7.1', 45, NULL, 'available', NULL),
('B.7.1', 46, NULL, 'available', NULL),
('B.7.2', 1, NULL, 'available', NULL),
('B.7.2', 3, NULL, 'available', NULL),
('B.7.2', 5, NULL, 'available', NULL),
('B.7.2', 10, NULL, 'available', NULL),
('B.7.2', 19, NULL, 'available', NULL),
('B.7.2', 20, NULL, 'available', NULL),
('B.7.2', 21, NULL, 'available', NULL),
('B.7.2', 23, NULL, 'available', NULL),
('B.7.2', 24, NULL, 'available', NULL),
('B.7.2', 25, NULL, 'available', NULL),
('B.7.2', 26, NULL, 'available', NULL),
('B.7.2', 33, NULL, 'available', NULL),
('B.7.2', 37, NULL, 'available', NULL),
('B.7.2', 38, NULL, 'available', NULL),
('B.7.2', 41, NULL, 'available', NULL),
('B.7.2', 43, NULL, 'available', NULL),
('B.7.2', 45, NULL, 'available', NULL),
('B.8.1', 1, NULL, 'available', NULL),
('B.8.1', 2, NULL, 'available', NULL),
('B.8.1', 3, NULL, 'available', NULL),
('B.8.1', 5, NULL, 'available', NULL),
('B.8.1', 10, NULL, 'available', NULL),
('B.8.1', 19, NULL, 'available', NULL),
('B.8.1', 20, NULL, 'available', NULL),
('B.8.1', 21, NULL, 'available', NULL),
('B.8.1', 23, NULL, 'available', NULL),
('B.8.1', 24, '2025-09-07 23:10:46.858270', 'locked', 28),
('B.8.1', 25, '2025-09-10 13:09:58.050649', 'locked', 28),
('B.8.1', 26, NULL, 'available', NULL),
('B.8.1', 27, NULL, 'available', NULL),
('B.8.1', 28, NULL, 'available', NULL),
('B.8.1', 30, NULL, 'available', NULL),
('B.8.1', 31, NULL, 'available', NULL),
('B.8.1', 32, NULL, 'available', NULL),
('B.8.1', 33, NULL, 'available', NULL),
('B.8.1', 34, NULL, 'available', NULL),
('B.8.1', 35, NULL, 'available', NULL),
('B.8.1', 36, NULL, 'available', NULL),
('B.8.1', 37, NULL, 'available', NULL),
('B.8.1', 38, '2025-09-11 14:14:18.590993', 'available', 28),
('B.8.1', 39, NULL, 'available', NULL),
('B.8.1', 40, NULL, 'available', NULL),
('B.8.1', 41, NULL, 'available', NULL),
('B.8.1', 42, NULL, 'available', NULL),
('B.8.1', 43, NULL, 'available', NULL),
('B.8.1', 44, NULL, 'available', NULL),
('B.8.1', 45, NULL, 'available', NULL),
('B.8.1', 46, NULL, 'available', NULL),
('B.8.2', 1, NULL, 'available', NULL),
('B.8.2', 3, NULL, 'available', NULL),
('B.8.2', 5, NULL, 'available', NULL),
('B.8.2', 10, NULL, 'available', NULL),
('B.8.2', 19, NULL, 'available', NULL),
('B.8.2', 20, NULL, 'available', NULL),
('B.8.2', 21, NULL, 'available', NULL),
('B.8.2', 23, NULL, 'available', NULL),
('B.8.2', 24, NULL, 'available', NULL),
('B.8.2', 25, NULL, 'available', NULL),
('B.8.2', 26, NULL, 'available', NULL),
('B.8.2', 33, NULL, 'available', NULL),
('B.8.2', 37, NULL, 'available', NULL),
('B.8.2', 38, NULL, 'available', NULL),
('B.8.2', 41, NULL, 'available', NULL),
('B.8.2', 43, NULL, 'available', NULL),
('B.8.2', 45, NULL, 'available', NULL),
('B.9.1', 1, NULL, 'available', NULL),
('B.9.1', 2, NULL, 'available', NULL),
('B.9.1', 3, NULL, 'available', NULL),
('B.9.1', 5, NULL, 'available', NULL),
('B.9.1', 10, NULL, 'available', NULL),
('B.9.1', 19, NULL, 'available', NULL),
('B.9.1', 20, NULL, 'available', NULL),
('B.9.1', 21, NULL, 'available', NULL),
('B.9.1', 23, NULL, 'available', NULL),
('B.9.1', 24, NULL, 'available', NULL),
('B.9.1', 25, NULL, 'available', NULL),
('B.9.1', 26, NULL, 'available', NULL),
('B.9.1', 27, NULL, 'available', NULL),
('B.9.1', 28, NULL, 'available', NULL),
('B.9.1', 30, NULL, 'available', NULL),
('B.9.1', 31, '2025-09-10 15:33:37.153433', 'available', 26),
('B.9.1', 32, NULL, 'available', NULL),
('B.9.1', 33, NULL, 'available', NULL),
('B.9.1', 34, NULL, 'available', NULL),
('B.9.1', 35, NULL, 'available', NULL),
('B.9.1', 36, NULL, 'available', NULL),
('B.9.1', 37, NULL, 'available', NULL),
('B.9.1', 38, '2025-09-11 13:56:23.711637', 'booked', 28),
('B.9.1', 39, NULL, 'available', NULL),
('B.9.1', 40, NULL, 'available', NULL),
('B.9.1', 41, NULL, 'available', NULL),
('B.9.1', 42, NULL, 'available', NULL),
('B.9.1', 43, NULL, 'available', NULL),
('B.9.1', 44, NULL, 'available', NULL),
('B.9.1', 45, NULL, 'available', NULL),
('B.9.1', 46, NULL, 'available', NULL),
('B.9.2', 1, NULL, 'available', NULL),
('B.9.2', 3, NULL, 'available', NULL),
('B.9.2', 5, NULL, 'available', NULL),
('B.9.2', 10, NULL, 'available', NULL),
('B.9.2', 19, NULL, 'available', NULL),
('B.9.2', 20, NULL, 'available', NULL),
('B.9.2', 21, NULL, 'available', NULL),
('B.9.2', 23, NULL, 'available', NULL),
('B.9.2', 24, NULL, 'available', NULL),
('B.9.2', 25, NULL, 'available', NULL),
('B.9.2', 26, NULL, 'available', NULL),
('B.9.2', 33, NULL, 'available', NULL),
('B.9.2', 37, NULL, 'available', NULL),
('B.9.2', 38, NULL, 'available', NULL),
('B.9.2', 41, NULL, 'available', NULL),
('B.9.2', 43, NULL, 'available', NULL),
('B.9.2', 45, NULL, 'available', NULL),
('C.1.1', 1, NULL, 'available', NULL),
('C.1.1', 2, NULL, 'available', NULL),
('C.1.1', 3, NULL, 'available', NULL),
('C.1.1', 5, NULL, 'available', NULL),
('C.1.1', 10, NULL, 'available', NULL),
('C.1.1', 19, NULL, 'available', NULL),
('C.1.1', 20, NULL, 'available', NULL),
('C.1.1', 21, NULL, 'available', NULL),
('C.1.1', 23, NULL, 'available', NULL),
('C.1.1', 24, '2025-09-04 12:55:00.461857', 'booked', 18),
('C.1.1', 25, NULL, 'available', NULL),
('C.1.1', 26, NULL, 'available', NULL),
('C.1.1', 27, NULL, 'available', NULL),
('C.1.1', 28, NULL, 'available', NULL),
('C.1.1', 30, NULL, 'available', NULL),
('C.1.1', 31, NULL, 'available', NULL),
('C.1.1', 32, NULL, 'available', NULL),
('C.1.1', 33, NULL, 'available', NULL),
('C.1.1', 34, NULL, 'available', NULL),
('C.1.1', 35, NULL, 'available', NULL),
('C.1.1', 36, NULL, 'available', NULL),
('C.1.1', 37, NULL, 'available', NULL),
('C.1.1', 38, '2025-09-10 22:13:52.963383', 'booked', 28),
('C.1.1', 39, '2025-09-10 23:58:51.300461', 'booked', 31),
('C.1.1', 40, NULL, 'available', NULL),
('C.1.1', 41, NULL, 'available', NULL),
('C.1.1', 42, NULL, 'available', NULL),
('C.1.1', 43, NULL, 'available', NULL),
('C.1.1', 44, NULL, 'available', NULL),
('C.1.1', 45, NULL, 'available', NULL),
('C.1.1', 46, NULL, 'available', NULL),
('C.1.2', 1, NULL, 'available', NULL),
('C.1.2', 3, NULL, 'available', NULL),
('C.1.2', 5, NULL, 'available', NULL),
('C.1.2', 10, NULL, 'available', NULL),
('C.1.2', 19, NULL, 'available', NULL),
('C.1.2', 20, NULL, 'available', NULL),
('C.1.2', 21, NULL, 'available', NULL),
('C.1.2', 23, NULL, 'available', NULL),
('C.1.2', 24, NULL, 'available', NULL),
('C.1.2', 25, NULL, 'available', NULL),
('C.1.2', 26, NULL, 'available', NULL),
('C.1.2', 33, NULL, 'available', NULL),
('C.1.2', 37, NULL, 'available', NULL),
('C.1.2', 38, NULL, 'available', NULL),
('C.1.2', 41, NULL, 'available', NULL),
('C.1.2', 43, NULL, 'available', NULL),
('C.1.2', 45, NULL, 'available', NULL),
('C.10.1', 1, NULL, 'available', NULL),
('C.10.1', 2, NULL, 'available', NULL),
('C.10.1', 3, NULL, 'available', NULL),
('C.10.1', 5, NULL, 'available', NULL),
('C.10.1', 10, NULL, 'available', NULL),
('C.10.1', 19, NULL, 'available', NULL),
('C.10.1', 20, NULL, 'available', NULL),
('C.10.1', 21, NULL, 'available', NULL),
('C.10.1', 23, NULL, 'available', NULL),
('C.10.1', 24, '2025-09-05 21:14:35.950138', 'booked', 18),
('C.10.1', 25, NULL, 'available', NULL),
('C.10.1', 26, NULL, 'available', NULL),
('C.10.1', 27, NULL, 'available', NULL),
('C.10.1', 28, NULL, 'available', NULL),
('C.10.1', 30, NULL, 'available', NULL),
('C.10.1', 31, NULL, 'available', NULL),
('C.10.1', 32, NULL, 'available', NULL),
('C.10.1', 33, NULL, 'available', NULL),
('C.10.1', 34, NULL, 'available', NULL),
('C.10.1', 35, NULL, 'available', NULL),
('C.10.1', 36, NULL, 'available', NULL),
('C.10.1', 37, NULL, 'available', NULL),
('C.10.1', 38, '2025-09-11 14:14:18.702979', 'available', 28),
('C.10.1', 39, NULL, 'available', NULL),
('C.10.1', 40, NULL, 'available', NULL),
('C.10.1', 41, NULL, 'available', NULL),
('C.10.1', 42, NULL, 'available', NULL),
('C.10.1', 43, NULL, 'available', NULL),
('C.10.1', 44, NULL, 'available', NULL),
('C.10.1', 45, NULL, 'available', NULL),
('C.10.1', 46, NULL, 'available', NULL),
('C.10.2', 1, NULL, 'available', NULL),
('C.10.2', 3, NULL, 'available', NULL),
('C.10.2', 5, NULL, 'available', NULL),
('C.10.2', 10, NULL, 'available', NULL),
('C.10.2', 19, NULL, 'available', NULL),
('C.10.2', 20, NULL, 'available', NULL),
('C.10.2', 21, NULL, 'available', NULL),
('C.10.2', 23, NULL, 'available', NULL),
('C.10.2', 24, NULL, 'available', NULL),
('C.10.2', 25, NULL, 'available', NULL),
('C.10.2', 26, NULL, 'available', NULL),
('C.10.2', 33, NULL, 'available', NULL),
('C.10.2', 37, NULL, 'available', NULL),
('C.10.2', 38, NULL, 'available', NULL),
('C.10.2', 41, NULL, 'available', NULL),
('C.10.2', 43, NULL, 'available', NULL),
('C.10.2', 45, NULL, 'available', NULL),
('C.2.1', 1, NULL, 'available', NULL),
('C.2.1', 2, NULL, 'available', NULL),
('C.2.1', 3, NULL, 'available', NULL),
('C.2.1', 5, NULL, 'available', NULL),
('C.2.1', 10, NULL, 'available', NULL),
('C.2.1', 19, NULL, 'available', NULL),
('C.2.1', 20, NULL, 'available', NULL),
('C.2.1', 21, NULL, 'available', NULL),
('C.2.1', 23, NULL, 'available', NULL),
('C.2.1', 24, NULL, 'available', NULL),
('C.2.1', 25, NULL, 'available', NULL),
('C.2.1', 26, NULL, 'available', NULL),
('C.2.1', 27, NULL, 'available', NULL),
('C.2.1', 28, NULL, 'available', NULL),
('C.2.1', 30, NULL, 'available', NULL),
('C.2.1', 31, NULL, 'available', NULL),
('C.2.1', 32, NULL, 'available', NULL),
('C.2.1', 33, NULL, 'available', NULL),
('C.2.1', 34, NULL, 'available', NULL),
('C.2.1', 35, NULL, 'available', NULL),
('C.2.1', 36, NULL, 'available', NULL),
('C.2.1', 37, NULL, 'available', NULL),
('C.2.1', 38, '2025-09-11 14:27:05.225910', 'booked', 26),
('C.2.1', 39, NULL, 'available', NULL),
('C.2.1', 40, NULL, 'available', NULL),
('C.2.1', 41, NULL, 'available', NULL),
('C.2.1', 42, NULL, 'available', NULL),
('C.2.1', 43, NULL, 'available', NULL),
('C.2.1', 44, NULL, 'available', NULL),
('C.2.1', 45, NULL, 'available', NULL),
('C.2.1', 46, NULL, 'available', NULL),
('C.2.2', 1, NULL, 'available', NULL),
('C.2.2', 3, NULL, 'available', NULL),
('C.2.2', 5, NULL, 'available', NULL),
('C.2.2', 10, NULL, 'available', NULL),
('C.2.2', 19, NULL, 'available', NULL),
('C.2.2', 20, NULL, 'available', NULL),
('C.2.2', 21, NULL, 'available', NULL),
('C.2.2', 23, NULL, 'available', NULL),
('C.2.2', 24, NULL, 'available', NULL),
('C.2.2', 25, NULL, 'available', NULL),
('C.2.2', 26, NULL, 'available', NULL),
('C.2.2', 33, NULL, 'available', NULL),
('C.2.2', 37, NULL, 'available', NULL),
('C.2.2', 38, NULL, 'available', NULL),
('C.2.2', 41, NULL, 'available', NULL),
('C.2.2', 43, NULL, 'available', NULL),
('C.2.2', 45, NULL, 'available', NULL),
('C.3.1', 1, NULL, 'available', NULL),
('C.3.1', 2, NULL, 'available', NULL),
('C.3.1', 3, NULL, 'available', NULL),
('C.3.1', 5, NULL, 'available', NULL),
('C.3.1', 10, NULL, 'available', NULL),
('C.3.1', 19, NULL, 'available', NULL),
('C.3.1', 20, NULL, 'available', NULL),
('C.3.1', 21, NULL, 'available', NULL),
('C.3.1', 23, NULL, 'available', NULL),
('C.3.1', 24, NULL, 'available', NULL),
('C.3.1', 25, '2025-09-10 00:36:07.216632', 'locked', 28),
('C.3.1', 26, NULL, 'available', NULL),
('C.3.1', 27, NULL, 'available', NULL),
('C.3.1', 28, NULL, 'available', NULL),
('C.3.1', 30, NULL, 'available', NULL),
('C.3.1', 31, NULL, 'available', NULL),
('C.3.1', 32, NULL, 'available', NULL),
('C.3.1', 33, NULL, 'available', NULL),
('C.3.1', 34, NULL, 'available', NULL),
('C.3.1', 35, NULL, 'available', NULL),
('C.3.1', 36, NULL, 'available', NULL),
('C.3.1', 37, NULL, 'available', NULL),
('C.3.1', 38, '2025-09-11 23:24:35.477034', 'booked', 26),
('C.3.1', 39, NULL, 'available', NULL),
('C.3.1', 40, NULL, 'available', NULL),
('C.3.1', 41, NULL, 'available', NULL),
('C.3.1', 42, '2025-09-19 22:20:54.076714', 'booked', 28),
('C.3.1', 43, NULL, 'available', NULL),
('C.3.1', 44, NULL, 'available', NULL),
('C.3.1', 45, NULL, 'available', NULL),
('C.3.1', 46, NULL, 'available', NULL),
('C.3.2', 1, NULL, 'available', NULL),
('C.3.2', 3, NULL, 'available', NULL),
('C.3.2', 5, NULL, 'available', NULL),
('C.3.2', 10, NULL, 'available', NULL),
('C.3.2', 19, NULL, 'available', NULL),
('C.3.2', 20, NULL, 'available', NULL),
('C.3.2', 21, NULL, 'available', NULL),
('C.3.2', 23, NULL, 'available', NULL),
('C.3.2', 24, NULL, 'available', NULL),
('C.3.2', 25, NULL, 'available', NULL),
('C.3.2', 26, NULL, 'available', NULL),
('C.3.2', 33, NULL, 'available', NULL),
('C.3.2', 37, NULL, 'available', NULL),
('C.3.2', 38, NULL, 'available', NULL),
('C.3.2', 41, NULL, 'available', NULL),
('C.3.2', 43, NULL, 'available', NULL),
('C.3.2', 45, NULL, 'available', NULL),
('C.4.1', 1, NULL, 'available', NULL),
('C.4.1', 2, NULL, 'available', NULL),
('C.4.1', 3, NULL, 'available', NULL),
('C.4.1', 5, NULL, 'available', NULL),
('C.4.1', 10, NULL, 'available', NULL),
('C.4.1', 19, NULL, 'available', NULL),
('C.4.1', 20, NULL, 'available', NULL),
('C.4.1', 21, NULL, 'available', NULL),
('C.4.1', 23, NULL, 'available', NULL),
('C.4.1', 24, '2025-09-04 14:39:32.408101', 'locked', 36),
('C.4.1', 25, '2025-09-09 22:04:36.189931', 'locked', 28),
('C.4.1', 26, NULL, 'available', NULL),
('C.4.1', 27, NULL, 'available', NULL),
('C.4.1', 28, NULL, 'available', NULL),
('C.4.1', 30, NULL, 'available', NULL),
('C.4.1', 31, NULL, 'available', NULL),
('C.4.1', 32, NULL, 'available', NULL),
('C.4.1', 33, NULL, 'available', NULL),
('C.4.1', 34, NULL, 'available', NULL),
('C.4.1', 35, NULL, 'available', NULL),
('C.4.1', 36, NULL, 'available', NULL),
('C.4.1', 37, NULL, 'available', NULL),
('C.4.1', 38, '2025-09-11 21:54:05.460678', 'booked', 26),
('C.4.1', 39, NULL, 'available', NULL),
('C.4.1', 40, NULL, 'available', NULL),
('C.4.1', 41, NULL, 'available', NULL),
('C.4.1', 42, NULL, 'available', NULL),
('C.4.1', 43, NULL, 'available', NULL),
('C.4.1', 44, NULL, 'available', NULL),
('C.4.1', 45, NULL, 'available', NULL),
('C.4.1', 46, NULL, 'available', NULL),
('C.4.2', 1, NULL, 'available', NULL),
('C.4.2', 3, NULL, 'available', NULL),
('C.4.2', 5, NULL, 'available', NULL),
('C.4.2', 10, NULL, 'available', NULL),
('C.4.2', 19, NULL, 'available', NULL),
('C.4.2', 20, NULL, 'available', NULL),
('C.4.2', 21, NULL, 'available', NULL),
('C.4.2', 23, NULL, 'available', NULL),
('C.4.2', 24, NULL, 'available', NULL),
('C.4.2', 25, NULL, 'available', NULL),
('C.4.2', 26, NULL, 'available', NULL),
('C.4.2', 33, NULL, 'available', NULL),
('C.4.2', 37, NULL, 'available', NULL),
('C.4.2', 38, NULL, 'available', NULL),
('C.4.2', 41, NULL, 'available', NULL),
('C.4.2', 43, NULL, 'available', NULL),
('C.4.2', 45, NULL, 'available', NULL),
('C.5.1', 1, NULL, 'available', NULL),
('C.5.1', 2, NULL, 'available', NULL),
('C.5.1', 3, NULL, 'available', NULL),
('C.5.1', 5, NULL, 'available', NULL),
('C.5.1', 10, NULL, 'available', NULL),
('C.5.1', 19, NULL, 'available', NULL),
('C.5.1', 20, NULL, 'available', NULL),
('C.5.1', 21, NULL, 'available', NULL),
('C.5.1', 23, NULL, 'available', NULL),
('C.5.1', 24, NULL, 'available', NULL),
('C.5.1', 25, '2025-09-11 23:35:23.421218', 'available', 26),
('C.5.1', 26, NULL, 'available', NULL),
('C.5.1', 27, NULL, 'available', NULL),
('C.5.1', 28, NULL, 'available', NULL),
('C.5.1', 30, NULL, 'available', NULL),
('C.5.1', 31, NULL, 'available', NULL),
('C.5.1', 32, NULL, 'available', NULL),
('C.5.1', 33, NULL, 'available', NULL),
('C.5.1', 34, NULL, 'available', NULL),
('C.5.1', 35, NULL, 'available', NULL),
('C.5.1', 36, NULL, 'available', NULL),
('C.5.1', 37, '2025-09-18 10:02:06.562234', 'booked', 26),
('C.5.1', 38, '2025-09-11 14:18:28.030218', 'booked', 28),
('C.5.1', 39, NULL, 'available', NULL),
('C.5.1', 40, NULL, 'available', NULL),
('C.5.1', 41, NULL, 'available', NULL),
('C.5.1', 42, '2025-09-19 23:40:22.853387', 'booked', 57),
('C.5.1', 43, NULL, 'available', NULL),
('C.5.1', 44, NULL, 'available', NULL),
('C.5.1', 45, NULL, 'available', NULL),
('C.5.1', 46, NULL, 'available', NULL),
('C.5.2', 1, NULL, 'available', NULL),
('C.5.2', 3, NULL, 'available', NULL),
('C.5.2', 5, NULL, 'available', NULL),
('C.5.2', 10, NULL, 'available', NULL),
('C.5.2', 19, NULL, 'available', NULL),
('C.5.2', 20, NULL, 'available', NULL),
('C.5.2', 21, NULL, 'available', NULL),
('C.5.2', 23, NULL, 'available', NULL),
('C.5.2', 24, NULL, 'available', NULL),
('C.5.2', 25, NULL, 'available', NULL),
('C.5.2', 26, NULL, 'available', NULL),
('C.5.2', 33, NULL, 'available', NULL),
('C.5.2', 37, NULL, 'available', NULL),
('C.5.2', 38, NULL, 'available', NULL),
('C.5.2', 41, NULL, 'available', NULL),
('C.5.2', 43, NULL, 'available', NULL),
('C.5.2', 45, NULL, 'available', NULL),
('C.6.1', 1, NULL, 'available', NULL),
('C.6.1', 2, NULL, 'available', NULL),
('C.6.1', 3, NULL, 'available', NULL),
('C.6.1', 5, NULL, 'available', NULL),
('C.6.1', 10, NULL, 'available', NULL),
('C.6.1', 19, NULL, 'available', NULL),
('C.6.1', 20, NULL, 'available', NULL),
('C.6.1', 21, NULL, 'available', NULL),
('C.6.1', 23, NULL, 'available', NULL),
('C.6.1', 24, NULL, 'available', NULL),
('C.6.1', 25, '2025-09-10 11:35:37.084916', 'locked', 28),
('C.6.1', 26, NULL, 'available', NULL),
('C.6.1', 27, NULL, 'available', NULL),
('C.6.1', 28, NULL, 'available', NULL),
('C.6.1', 30, NULL, 'available', NULL),
('C.6.1', 31, NULL, 'available', NULL),
('C.6.1', 32, NULL, 'available', NULL),
('C.6.1', 33, NULL, 'available', NULL),
('C.6.1', 34, NULL, 'available', NULL),
('C.6.1', 35, NULL, 'available', NULL),
('C.6.1', 36, NULL, 'available', NULL),
('C.6.1', 37, NULL, 'available', NULL),
('C.6.1', 38, '2025-09-11 14:17:04.640518', 'booked', 28),
('C.6.1', 39, NULL, 'available', NULL),
('C.6.1', 40, NULL, 'available', NULL),
('C.6.1', 41, NULL, 'available', NULL),
('C.6.1', 42, NULL, 'available', NULL),
('C.6.1', 43, NULL, 'available', NULL),
('C.6.1', 44, NULL, 'available', NULL);
INSERT INTO `trip_seats` (`seat_number`, `trip_id`, `locked_at`, `status`, `locking_user_id`) VALUES
('C.6.1', 45, NULL, 'available', NULL),
('C.6.1', 46, NULL, 'available', NULL),
('C.6.2', 1, NULL, 'available', NULL),
('C.6.2', 3, NULL, 'available', NULL),
('C.6.2', 5, NULL, 'available', NULL),
('C.6.2', 10, NULL, 'available', NULL),
('C.6.2', 19, NULL, 'available', NULL),
('C.6.2', 20, NULL, 'available', NULL),
('C.6.2', 21, NULL, 'available', NULL),
('C.6.2', 23, NULL, 'available', NULL),
('C.6.2', 24, NULL, 'available', NULL),
('C.6.2', 25, NULL, 'available', NULL),
('C.6.2', 26, NULL, 'available', NULL),
('C.6.2', 33, NULL, 'available', NULL),
('C.6.2', 37, NULL, 'available', NULL),
('C.6.2', 38, NULL, 'available', NULL),
('C.6.2', 41, NULL, 'available', NULL),
('C.6.2', 43, NULL, 'available', NULL),
('C.6.2', 45, NULL, 'available', NULL),
('C.7.1', 1, NULL, 'available', NULL),
('C.7.1', 2, NULL, 'available', NULL),
('C.7.1', 3, NULL, 'available', NULL),
('C.7.1', 5, NULL, 'available', NULL),
('C.7.1', 10, NULL, 'available', NULL),
('C.7.1', 19, NULL, 'available', NULL),
('C.7.1', 20, NULL, 'available', NULL),
('C.7.1', 21, NULL, 'available', NULL),
('C.7.1', 23, NULL, 'available', NULL),
('C.7.1', 24, '2025-09-04 23:34:13.524599', 'locked', 18),
('C.7.1', 25, '2025-09-10 13:18:01.647558', 'booked', 28),
('C.7.1', 26, NULL, 'available', NULL),
('C.7.1', 27, NULL, 'available', NULL),
('C.7.1', 28, NULL, 'available', NULL),
('C.7.1', 30, NULL, 'available', NULL),
('C.7.1', 31, NULL, 'available', NULL),
('C.7.1', 32, NULL, 'available', NULL),
('C.7.1', 33, NULL, 'available', NULL),
('C.7.1', 34, NULL, 'available', NULL),
('C.7.1', 35, NULL, 'available', NULL),
('C.7.1', 36, NULL, 'available', NULL),
('C.7.1', 37, NULL, 'available', NULL),
('C.7.1', 38, NULL, 'available', NULL),
('C.7.1', 39, NULL, 'available', NULL),
('C.7.1', 40, NULL, 'available', NULL),
('C.7.1', 41, NULL, 'available', NULL),
('C.7.1', 42, NULL, 'available', NULL),
('C.7.1', 43, NULL, 'available', NULL),
('C.7.1', 44, NULL, 'available', NULL),
('C.7.1', 45, NULL, 'available', NULL),
('C.7.1', 46, NULL, 'available', NULL),
('C.7.2', 1, NULL, 'available', NULL),
('C.7.2', 3, NULL, 'available', NULL),
('C.7.2', 5, NULL, 'available', NULL),
('C.7.2', 10, NULL, 'available', NULL),
('C.7.2', 19, NULL, 'available', NULL),
('C.7.2', 20, NULL, 'available', NULL),
('C.7.2', 21, NULL, 'available', NULL),
('C.7.2', 23, NULL, 'available', NULL),
('C.7.2', 24, NULL, 'available', NULL),
('C.7.2', 25, NULL, 'available', NULL),
('C.7.2', 26, NULL, 'available', NULL),
('C.7.2', 33, NULL, 'available', NULL),
('C.7.2', 37, NULL, 'available', NULL),
('C.7.2', 38, NULL, 'available', NULL),
('C.7.2', 41, NULL, 'available', NULL),
('C.7.2', 43, NULL, 'available', NULL),
('C.7.2', 45, NULL, 'available', NULL),
('C.8.1', 1, NULL, 'available', NULL),
('C.8.1', 2, NULL, 'available', NULL),
('C.8.1', 3, NULL, 'available', NULL),
('C.8.1', 5, NULL, 'available', NULL),
('C.8.1', 10, NULL, 'available', NULL),
('C.8.1', 19, NULL, 'available', NULL),
('C.8.1', 20, NULL, 'available', NULL),
('C.8.1', 21, NULL, 'available', NULL),
('C.8.1', 23, NULL, 'available', NULL),
('C.8.1', 24, '2025-09-07 23:10:46.888284', 'locked', 28),
('C.8.1', 25, '2025-09-10 13:09:58.091559', 'locked', 28),
('C.8.1', 26, NULL, 'available', NULL),
('C.8.1', 27, NULL, 'available', NULL),
('C.8.1', 28, NULL, 'available', NULL),
('C.8.1', 30, NULL, 'available', NULL),
('C.8.1', 31, NULL, 'available', NULL),
('C.8.1', 32, NULL, 'available', NULL),
('C.8.1', 33, NULL, 'available', NULL),
('C.8.1', 34, NULL, 'available', NULL),
('C.8.1', 35, NULL, 'available', NULL),
('C.8.1', 36, NULL, 'available', NULL),
('C.8.1', 37, NULL, 'available', NULL),
('C.8.1', 38, '2025-09-11 14:14:18.611989', 'available', 28),
('C.8.1', 39, NULL, 'available', NULL),
('C.8.1', 40, NULL, 'available', NULL),
('C.8.1', 41, NULL, 'available', NULL),
('C.8.1', 42, NULL, 'available', NULL),
('C.8.1', 43, NULL, 'available', NULL),
('C.8.1', 44, NULL, 'available', NULL),
('C.8.1', 45, NULL, 'available', NULL),
('C.8.1', 46, NULL, 'available', NULL),
('C.8.2', 1, NULL, 'available', NULL),
('C.8.2', 3, NULL, 'available', NULL),
('C.8.2', 5, NULL, 'available', NULL),
('C.8.2', 10, NULL, 'available', NULL),
('C.8.2', 19, NULL, 'available', NULL),
('C.8.2', 20, NULL, 'available', NULL),
('C.8.2', 21, NULL, 'available', NULL),
('C.8.2', 23, NULL, 'available', NULL),
('C.8.2', 24, NULL, 'available', NULL),
('C.8.2', 25, NULL, 'available', NULL),
('C.8.2', 26, NULL, 'available', NULL),
('C.8.2', 33, NULL, 'available', NULL),
('C.8.2', 37, NULL, 'available', NULL),
('C.8.2', 38, NULL, 'available', NULL),
('C.8.2', 41, NULL, 'available', NULL),
('C.8.2', 43, NULL, 'available', NULL),
('C.8.2', 45, NULL, 'available', NULL),
('C.9.1', 1, NULL, 'available', NULL),
('C.9.1', 2, NULL, 'available', NULL),
('C.9.1', 3, NULL, 'available', NULL),
('C.9.1', 5, NULL, 'available', NULL),
('C.9.1', 10, NULL, 'available', NULL),
('C.9.1', 19, NULL, 'available', NULL),
('C.9.1', 20, NULL, 'available', NULL),
('C.9.1', 21, NULL, 'available', NULL),
('C.9.1', 23, NULL, 'available', NULL),
('C.9.1', 24, NULL, 'available', NULL),
('C.9.1', 25, NULL, 'available', NULL),
('C.9.1', 26, NULL, 'available', NULL),
('C.9.1', 27, NULL, 'available', NULL),
('C.9.1', 28, NULL, 'available', NULL),
('C.9.1', 30, NULL, 'available', NULL),
('C.9.1', 31, '2025-09-10 15:33:37.192874', 'available', 26),
('C.9.1', 32, NULL, 'available', NULL),
('C.9.1', 33, NULL, 'available', NULL),
('C.9.1', 34, NULL, 'available', NULL),
('C.9.1', 35, NULL, 'available', NULL),
('C.9.1', 36, NULL, 'available', NULL),
('C.9.1', 37, NULL, 'available', NULL),
('C.9.1', 38, '2025-09-11 13:56:23.749609', 'booked', 28),
('C.9.1', 39, NULL, 'available', NULL),
('C.9.1', 40, NULL, 'available', NULL),
('C.9.1', 41, NULL, 'available', NULL),
('C.9.1', 42, NULL, 'available', NULL),
('C.9.1', 43, NULL, 'available', NULL),
('C.9.1', 44, NULL, 'available', NULL),
('C.9.1', 45, NULL, 'available', NULL),
('C.9.1', 46, NULL, 'available', NULL),
('C.9.2', 1, NULL, 'available', NULL),
('C.9.2', 3, NULL, 'available', NULL),
('C.9.2', 5, NULL, 'available', NULL),
('C.9.2', 10, NULL, 'available', NULL),
('C.9.2', 19, NULL, 'available', NULL),
('C.9.2', 20, NULL, 'available', NULL),
('C.9.2', 21, NULL, 'available', NULL),
('C.9.2', 23, NULL, 'available', NULL),
('C.9.2', 24, NULL, 'available', NULL),
('C.9.2', 25, NULL, 'available', NULL),
('C.9.2', 26, NULL, 'available', NULL),
('C.9.2', 33, NULL, 'available', NULL),
('C.9.2', 37, NULL, 'available', NULL),
('C.9.2', 38, NULL, 'available', NULL),
('C.9.2', 41, NULL, 'available', NULL),
('C.9.2', 43, NULL, 'available', NULL),
('C.9.2', 45, NULL, 'available', NULL),
('D.1.1', 1, NULL, 'available', NULL),
('D.1.1', 2, NULL, 'available', NULL),
('D.1.1', 3, NULL, 'available', NULL),
('D.1.1', 5, NULL, 'available', NULL),
('D.1.1', 10, NULL, 'available', NULL),
('D.1.1', 19, NULL, 'available', NULL),
('D.1.1', 20, NULL, 'available', NULL),
('D.1.1', 21, NULL, 'available', NULL),
('D.1.1', 23, NULL, 'available', NULL),
('D.1.1', 24, '2025-09-04 16:42:41.041124', 'locked', 18),
('D.1.1', 25, NULL, 'available', NULL),
('D.1.1', 26, NULL, 'available', NULL),
('D.1.1', 27, NULL, 'available', NULL),
('D.1.1', 28, NULL, 'available', NULL),
('D.1.1', 30, NULL, 'available', NULL),
('D.1.1', 31, NULL, 'available', NULL),
('D.1.1', 32, NULL, 'available', NULL),
('D.1.1', 33, NULL, 'available', NULL),
('D.1.1', 34, NULL, 'available', NULL),
('D.1.1', 35, NULL, 'available', NULL),
('D.1.1', 36, NULL, 'available', NULL),
('D.1.1', 37, NULL, 'available', NULL),
('D.1.1', 38, NULL, 'available', NULL),
('D.1.1', 39, '2025-09-10 23:58:51.343305', 'booked', 31),
('D.1.1', 40, NULL, 'available', NULL),
('D.1.1', 41, NULL, 'available', NULL),
('D.1.1', 42, NULL, 'available', NULL),
('D.1.1', 43, NULL, 'available', NULL),
('D.1.1', 44, NULL, 'available', NULL),
('D.1.1', 45, NULL, 'available', NULL),
('D.1.1', 46, NULL, 'available', NULL),
('D.1.2', 1, NULL, 'available', NULL),
('D.1.2', 3, NULL, 'available', NULL),
('D.1.2', 5, NULL, 'available', NULL),
('D.1.2', 10, NULL, 'available', NULL),
('D.1.2', 19, NULL, 'available', NULL),
('D.1.2', 20, NULL, 'available', NULL),
('D.1.2', 21, NULL, 'available', NULL),
('D.1.2', 23, NULL, 'available', NULL),
('D.1.2', 24, NULL, 'available', NULL),
('D.1.2', 25, NULL, 'available', NULL),
('D.1.2', 26, NULL, 'available', NULL),
('D.1.2', 33, NULL, 'available', NULL),
('D.1.2', 37, NULL, 'available', NULL),
('D.1.2', 38, NULL, 'available', NULL),
('D.1.2', 41, NULL, 'available', NULL),
('D.1.2', 43, NULL, 'available', NULL),
('D.1.2', 45, NULL, 'available', NULL),
('D.10.1', 1, NULL, 'available', NULL),
('D.10.1', 2, NULL, 'available', NULL),
('D.10.1', 3, NULL, 'available', NULL),
('D.10.1', 5, NULL, 'available', NULL),
('D.10.1', 10, NULL, 'available', NULL),
('D.10.1', 19, NULL, 'available', NULL),
('D.10.1', 20, NULL, 'available', NULL),
('D.10.1', 21, NULL, 'available', NULL),
('D.10.1', 23, NULL, 'available', NULL),
('D.10.1', 24, '2025-09-05 21:14:35.987703', 'booked', 18),
('D.10.1', 25, NULL, 'available', NULL),
('D.10.1', 26, NULL, 'available', NULL),
('D.10.1', 27, NULL, 'available', NULL),
('D.10.1', 28, NULL, 'available', NULL),
('D.10.1', 30, NULL, 'available', NULL),
('D.10.1', 31, NULL, 'available', NULL),
('D.10.1', 32, NULL, 'available', NULL),
('D.10.1', 33, NULL, 'available', NULL),
('D.10.1', 34, NULL, 'available', NULL),
('D.10.1', 35, NULL, 'available', NULL),
('D.10.1', 36, NULL, 'available', NULL),
('D.10.1', 37, NULL, 'available', NULL),
('D.10.1', 38, '2025-09-11 14:14:18.683585', 'available', 28),
('D.10.1', 39, NULL, 'available', NULL),
('D.10.1', 40, NULL, 'available', NULL),
('D.10.1', 41, NULL, 'available', NULL),
('D.10.1', 42, NULL, 'available', NULL),
('D.10.1', 43, NULL, 'available', NULL),
('D.10.1', 44, NULL, 'available', NULL),
('D.10.1', 45, NULL, 'available', NULL),
('D.10.1', 46, NULL, 'available', NULL),
('D.10.2', 1, NULL, 'available', NULL),
('D.10.2', 3, NULL, 'available', NULL),
('D.10.2', 5, NULL, 'available', NULL),
('D.10.2', 10, NULL, 'available', NULL),
('D.10.2', 19, NULL, 'available', NULL),
('D.10.2', 20, NULL, 'available', NULL),
('D.10.2', 21, NULL, 'available', NULL),
('D.10.2', 23, NULL, 'available', NULL),
('D.10.2', 24, NULL, 'available', NULL),
('D.10.2', 25, NULL, 'available', NULL),
('D.10.2', 26, NULL, 'available', NULL),
('D.10.2', 33, NULL, 'available', NULL),
('D.10.2', 37, NULL, 'available', NULL),
('D.10.2', 38, NULL, 'available', NULL),
('D.10.2', 41, NULL, 'available', NULL),
('D.10.2', 43, NULL, 'available', NULL),
('D.10.2', 45, NULL, 'available', NULL),
('D.2.1', 1, NULL, 'available', NULL),
('D.2.1', 2, NULL, 'available', NULL),
('D.2.1', 3, NULL, 'available', NULL),
('D.2.1', 5, NULL, 'available', NULL),
('D.2.1', 10, NULL, 'available', NULL),
('D.2.1', 19, NULL, 'available', NULL),
('D.2.1', 20, NULL, 'available', NULL),
('D.2.1', 21, NULL, 'available', NULL),
('D.2.1', 23, NULL, 'available', NULL),
('D.2.1', 24, NULL, 'available', NULL),
('D.2.1', 25, NULL, 'available', NULL),
('D.2.1', 26, NULL, 'available', NULL),
('D.2.1', 27, NULL, 'available', NULL),
('D.2.1', 28, NULL, 'available', NULL),
('D.2.1', 30, NULL, 'available', NULL),
('D.2.1', 31, NULL, 'available', NULL),
('D.2.1', 32, NULL, 'available', NULL),
('D.2.1', 33, NULL, 'available', NULL),
('D.2.1', 34, NULL, 'available', NULL),
('D.2.1', 35, NULL, 'available', NULL),
('D.2.1', 36, NULL, 'available', NULL),
('D.2.1', 37, NULL, 'available', NULL),
('D.2.1', 38, NULL, 'available', NULL),
('D.2.1', 39, NULL, 'available', NULL),
('D.2.1', 40, NULL, 'available', NULL),
('D.2.1', 41, NULL, 'available', NULL),
('D.2.1', 42, NULL, 'available', NULL),
('D.2.1', 43, NULL, 'available', NULL),
('D.2.1', 44, NULL, 'available', NULL),
('D.2.1', 45, NULL, 'available', NULL),
('D.2.1', 46, NULL, 'available', NULL),
('D.2.2', 1, NULL, 'available', NULL),
('D.2.2', 3, NULL, 'available', NULL),
('D.2.2', 5, NULL, 'available', NULL),
('D.2.2', 10, NULL, 'available', NULL),
('D.2.2', 19, NULL, 'available', NULL),
('D.2.2', 20, NULL, 'available', NULL),
('D.2.2', 21, NULL, 'available', NULL),
('D.2.2', 23, NULL, 'available', NULL),
('D.2.2', 24, NULL, 'available', NULL),
('D.2.2', 25, NULL, 'available', NULL),
('D.2.2', 26, NULL, 'available', NULL),
('D.2.2', 33, NULL, 'available', NULL),
('D.2.2', 37, NULL, 'available', NULL),
('D.2.2', 38, NULL, 'available', NULL),
('D.2.2', 41, NULL, 'available', NULL),
('D.2.2', 43, NULL, 'available', NULL),
('D.2.2', 45, NULL, 'available', NULL),
('D.3.1', 1, NULL, 'available', NULL),
('D.3.1', 2, NULL, 'available', NULL),
('D.3.1', 3, NULL, 'available', NULL),
('D.3.1', 5, NULL, 'available', NULL),
('D.3.1', 10, NULL, 'available', NULL),
('D.3.1', 19, NULL, 'available', NULL),
('D.3.1', 20, NULL, 'available', NULL),
('D.3.1', 21, NULL, 'available', NULL),
('D.3.1', 23, NULL, 'available', NULL),
('D.3.1', 24, NULL, 'available', NULL),
('D.3.1', 25, NULL, 'available', NULL),
('D.3.1', 26, NULL, 'available', NULL),
('D.3.1', 27, NULL, 'available', NULL),
('D.3.1', 28, NULL, 'available', NULL),
('D.3.1', 30, NULL, 'available', NULL),
('D.3.1', 31, NULL, 'available', NULL),
('D.3.1', 32, NULL, 'available', NULL),
('D.3.1', 33, NULL, 'available', NULL),
('D.3.1', 34, NULL, 'available', NULL),
('D.3.1', 35, NULL, 'available', NULL),
('D.3.1', 36, NULL, 'available', NULL),
('D.3.1', 37, NULL, 'available', NULL),
('D.3.1', 38, NULL, 'available', NULL),
('D.3.1', 39, NULL, 'available', NULL),
('D.3.1', 40, NULL, 'available', NULL),
('D.3.1', 41, NULL, 'available', NULL),
('D.3.1', 42, NULL, 'available', NULL),
('D.3.1', 43, NULL, 'available', NULL),
('D.3.1', 44, NULL, 'available', NULL),
('D.3.1', 45, NULL, 'available', NULL),
('D.3.1', 46, NULL, 'available', NULL),
('D.3.2', 1, NULL, 'available', NULL),
('D.3.2', 3, NULL, 'available', NULL),
('D.3.2', 5, NULL, 'available', NULL),
('D.3.2', 10, NULL, 'available', NULL),
('D.3.2', 19, NULL, 'available', NULL),
('D.3.2', 20, NULL, 'available', NULL),
('D.3.2', 21, NULL, 'available', NULL),
('D.3.2', 23, NULL, 'available', NULL),
('D.3.2', 24, NULL, 'available', NULL),
('D.3.2', 25, NULL, 'available', NULL),
('D.3.2', 26, NULL, 'available', NULL),
('D.3.2', 33, NULL, 'available', NULL),
('D.3.2', 37, NULL, 'available', NULL),
('D.3.2', 38, NULL, 'available', NULL),
('D.3.2', 41, NULL, 'available', NULL),
('D.3.2', 43, NULL, 'available', NULL),
('D.3.2', 45, NULL, 'available', NULL),
('D.4.1', 1, NULL, 'available', NULL),
('D.4.1', 2, NULL, 'available', NULL),
('D.4.1', 3, NULL, 'available', NULL),
('D.4.1', 5, NULL, 'available', NULL),
('D.4.1', 10, NULL, 'available', NULL),
('D.4.1', 19, NULL, 'available', NULL),
('D.4.1', 20, NULL, 'available', NULL),
('D.4.1', 21, NULL, 'available', NULL),
('D.4.1', 23, NULL, 'available', NULL),
('D.4.1', 24, '2025-09-04 14:41:37.648162', 'locked', 36),
('D.4.1', 25, NULL, 'available', NULL),
('D.4.1', 26, NULL, 'available', NULL),
('D.4.1', 27, NULL, 'available', NULL),
('D.4.1', 28, NULL, 'available', NULL),
('D.4.1', 30, NULL, 'available', NULL),
('D.4.1', 31, NULL, 'available', NULL),
('D.4.1', 32, NULL, 'available', NULL),
('D.4.1', 33, NULL, 'available', NULL),
('D.4.1', 34, NULL, 'available', NULL),
('D.4.1', 35, NULL, 'available', NULL),
('D.4.1', 36, NULL, 'available', NULL),
('D.4.1', 37, NULL, 'available', NULL),
('D.4.1', 38, NULL, 'available', NULL),
('D.4.1', 39, NULL, 'available', NULL),
('D.4.1', 40, NULL, 'available', NULL),
('D.4.1', 41, NULL, 'available', NULL),
('D.4.1', 42, NULL, 'available', NULL),
('D.4.1', 43, NULL, 'available', NULL),
('D.4.1', 44, NULL, 'available', NULL),
('D.4.1', 45, NULL, 'available', NULL),
('D.4.1', 46, NULL, 'available', NULL),
('D.4.2', 1, NULL, 'available', NULL),
('D.4.2', 3, NULL, 'available', NULL),
('D.4.2', 5, NULL, 'available', NULL),
('D.4.2', 10, NULL, 'available', NULL),
('D.4.2', 19, NULL, 'available', NULL),
('D.4.2', 20, NULL, 'available', NULL),
('D.4.2', 21, NULL, 'available', NULL),
('D.4.2', 23, NULL, 'available', NULL),
('D.4.2', 24, NULL, 'available', NULL),
('D.4.2', 25, NULL, 'available', NULL),
('D.4.2', 26, NULL, 'available', NULL),
('D.4.2', 33, NULL, 'available', NULL),
('D.4.2', 37, NULL, 'available', NULL),
('D.4.2', 38, NULL, 'available', NULL),
('D.4.2', 41, NULL, 'available', NULL),
('D.4.2', 43, NULL, 'available', NULL),
('D.4.2', 45, NULL, 'available', NULL),
('D.5.1', 1, NULL, 'available', NULL),
('D.5.1', 2, NULL, 'available', NULL),
('D.5.1', 3, NULL, 'available', NULL),
('D.5.1', 5, NULL, 'available', NULL),
('D.5.1', 10, NULL, 'available', NULL),
('D.5.1', 19, NULL, 'available', NULL),
('D.5.1', 20, NULL, 'available', NULL),
('D.5.1', 21, NULL, 'available', NULL),
('D.5.1', 23, NULL, 'available', NULL),
('D.5.1', 24, '2025-09-04 14:19:37.873242', 'locked', 18),
('D.5.1', 25, '2025-09-11 23:35:23.435503', 'available', 26),
('D.5.1', 26, NULL, 'available', NULL),
('D.5.1', 27, NULL, 'available', NULL),
('D.5.1', 28, NULL, 'available', NULL),
('D.5.1', 30, NULL, 'available', NULL),
('D.5.1', 31, NULL, 'available', NULL),
('D.5.1', 32, NULL, 'available', NULL),
('D.5.1', 33, NULL, 'available', NULL),
('D.5.1', 34, NULL, 'available', NULL),
('D.5.1', 35, NULL, 'available', NULL),
('D.5.1', 36, NULL, 'available', NULL),
('D.5.1', 37, NULL, 'available', NULL),
('D.5.1', 38, '2025-09-11 14:18:28.066048', 'booked', 28),
('D.5.1', 39, NULL, 'available', NULL),
('D.5.1', 40, NULL, 'available', NULL),
('D.5.1', 41, NULL, 'available', NULL),
('D.5.1', 42, NULL, 'available', NULL),
('D.5.1', 43, NULL, 'available', NULL),
('D.5.1', 44, NULL, 'available', NULL),
('D.5.1', 45, NULL, 'available', NULL),
('D.5.1', 46, NULL, 'available', NULL),
('D.5.2', 1, NULL, 'available', NULL),
('D.5.2', 3, NULL, 'available', NULL),
('D.5.2', 5, NULL, 'available', NULL),
('D.5.2', 10, NULL, 'available', NULL),
('D.5.2', 19, NULL, 'available', NULL),
('D.5.2', 20, NULL, 'available', NULL),
('D.5.2', 21, NULL, 'available', NULL),
('D.5.2', 23, NULL, 'available', NULL),
('D.5.2', 24, NULL, 'available', NULL),
('D.5.2', 25, NULL, 'available', NULL),
('D.5.2', 26, NULL, 'available', NULL),
('D.5.2', 33, NULL, 'available', NULL),
('D.5.2', 37, NULL, 'available', NULL),
('D.5.2', 38, NULL, 'available', NULL),
('D.5.2', 41, NULL, 'available', NULL),
('D.5.2', 43, NULL, 'available', NULL),
('D.5.2', 45, NULL, 'available', NULL),
('D.6.1', 1, NULL, 'available', NULL),
('D.6.1', 2, NULL, 'available', NULL),
('D.6.1', 3, NULL, 'available', NULL),
('D.6.1', 5, NULL, 'available', NULL),
('D.6.1', 10, NULL, 'available', NULL),
('D.6.1', 19, NULL, 'available', NULL),
('D.6.1', 20, NULL, 'available', NULL),
('D.6.1', 21, NULL, 'available', NULL),
('D.6.1', 23, NULL, 'available', NULL),
('D.6.1', 24, NULL, 'available', NULL),
('D.6.1', 25, '2025-09-10 11:35:37.127984', 'locked', 28),
('D.6.1', 26, NULL, 'available', NULL),
('D.6.1', 27, NULL, 'available', NULL),
('D.6.1', 28, NULL, 'available', NULL),
('D.6.1', 30, NULL, 'available', NULL),
('D.6.1', 31, NULL, 'available', NULL),
('D.6.1', 32, NULL, 'available', NULL),
('D.6.1', 33, NULL, 'available', NULL),
('D.6.1', 34, NULL, 'available', NULL),
('D.6.1', 35, NULL, 'available', NULL),
('D.6.1', 36, NULL, 'available', NULL),
('D.6.1', 37, NULL, 'available', NULL),
('D.6.1', 38, '2025-09-11 14:17:04.656360', 'booked', 28),
('D.6.1', 39, NULL, 'available', NULL),
('D.6.1', 40, NULL, 'available', NULL),
('D.6.1', 41, NULL, 'available', NULL),
('D.6.1', 42, NULL, 'available', NULL),
('D.6.1', 43, NULL, 'available', NULL),
('D.6.1', 44, NULL, 'available', NULL),
('D.6.1', 45, NULL, 'available', NULL),
('D.6.1', 46, NULL, 'available', NULL),
('D.6.2', 1, NULL, 'available', NULL),
('D.6.2', 3, NULL, 'available', NULL),
('D.6.2', 5, NULL, 'available', NULL),
('D.6.2', 10, NULL, 'available', NULL),
('D.6.2', 19, NULL, 'available', NULL),
('D.6.2', 20, NULL, 'available', NULL),
('D.6.2', 21, NULL, 'available', NULL),
('D.6.2', 23, NULL, 'available', NULL),
('D.6.2', 24, NULL, 'available', NULL),
('D.6.2', 25, NULL, 'available', NULL),
('D.6.2', 26, NULL, 'available', NULL),
('D.6.2', 33, NULL, 'available', NULL),
('D.6.2', 37, NULL, 'available', NULL),
('D.6.2', 38, NULL, 'available', NULL),
('D.6.2', 41, NULL, 'available', NULL),
('D.6.2', 43, NULL, 'available', NULL),
('D.6.2', 45, NULL, 'available', NULL),
('D.7.1', 1, NULL, 'available', NULL),
('D.7.1', 2, NULL, 'available', NULL),
('D.7.1', 3, NULL, 'available', NULL),
('D.7.1', 5, NULL, 'available', NULL),
('D.7.1', 10, NULL, 'available', NULL),
('D.7.1', 19, NULL, 'available', NULL),
('D.7.1', 20, NULL, 'available', NULL),
('D.7.1', 21, NULL, 'available', NULL),
('D.7.1', 23, NULL, 'available', NULL),
('D.7.1', 24, '2025-09-04 23:38:59.592988', 'booked', 18),
('D.7.1', 25, '2025-09-10 13:18:01.687926', 'booked', 28),
('D.7.1', 26, NULL, 'available', NULL),
('D.7.1', 27, NULL, 'available', NULL),
('D.7.1', 28, NULL, 'available', NULL),
('D.7.1', 30, NULL, 'available', NULL),
('D.7.1', 31, NULL, 'available', NULL),
('D.7.1', 32, NULL, 'available', NULL),
('D.7.1', 33, NULL, 'available', NULL),
('D.7.1', 34, NULL, 'available', NULL),
('D.7.1', 35, NULL, 'available', NULL),
('D.7.1', 36, NULL, 'available', NULL),
('D.7.1', 37, NULL, 'available', NULL),
('D.7.1', 38, NULL, 'available', NULL),
('D.7.1', 39, NULL, 'available', NULL),
('D.7.1', 40, NULL, 'available', NULL),
('D.7.1', 41, NULL, 'available', NULL),
('D.7.1', 42, NULL, 'available', NULL),
('D.7.1', 43, NULL, 'available', NULL),
('D.7.1', 44, NULL, 'available', NULL),
('D.7.1', 45, NULL, 'available', NULL),
('D.7.1', 46, NULL, 'available', NULL),
('D.7.2', 1, NULL, 'available', NULL),
('D.7.2', 3, NULL, 'available', NULL),
('D.7.2', 5, NULL, 'available', NULL),
('D.7.2', 10, NULL, 'available', NULL),
('D.7.2', 19, NULL, 'available', NULL),
('D.7.2', 20, NULL, 'available', NULL),
('D.7.2', 21, NULL, 'available', NULL),
('D.7.2', 23, NULL, 'available', NULL),
('D.7.2', 24, NULL, 'available', NULL),
('D.7.2', 25, NULL, 'available', NULL),
('D.7.2', 26, NULL, 'available', NULL),
('D.7.2', 33, NULL, 'available', NULL),
('D.7.2', 37, NULL, 'available', NULL),
('D.7.2', 38, NULL, 'available', NULL),
('D.7.2', 41, NULL, 'available', NULL),
('D.7.2', 43, NULL, 'available', NULL),
('D.7.2', 45, NULL, 'available', NULL),
('D.8.1', 1, NULL, 'available', NULL),
('D.8.1', 2, NULL, 'available', NULL),
('D.8.1', 3, NULL, 'available', NULL),
('D.8.1', 5, NULL, 'available', NULL),
('D.8.1', 10, NULL, 'available', NULL),
('D.8.1', 19, NULL, 'available', NULL),
('D.8.1', 20, NULL, 'available', NULL),
('D.8.1', 21, NULL, 'available', NULL),
('D.8.1', 23, NULL, 'available', NULL),
('D.8.1', 24, NULL, 'available', NULL),
('D.8.1', 25, NULL, 'available', NULL),
('D.8.1', 26, NULL, 'available', NULL),
('D.8.1', 27, NULL, 'available', NULL),
('D.8.1', 28, NULL, 'available', NULL),
('D.8.1', 30, NULL, 'available', NULL),
('D.8.1', 31, NULL, 'available', NULL),
('D.8.1', 32, NULL, 'available', NULL),
('D.8.1', 33, NULL, 'available', NULL),
('D.8.1', 34, NULL, 'available', NULL),
('D.8.1', 35, NULL, 'available', NULL),
('D.8.1', 36, NULL, 'available', NULL),
('D.8.1', 37, NULL, 'available', NULL),
('D.8.1', 38, '2025-09-11 14:14:18.628023', 'available', 28),
('D.8.1', 39, NULL, 'available', NULL),
('D.8.1', 40, NULL, 'available', NULL),
('D.8.1', 41, NULL, 'available', NULL),
('D.8.1', 42, NULL, 'available', NULL),
('D.8.1', 43, NULL, 'available', NULL),
('D.8.1', 44, NULL, 'available', NULL),
('D.8.1', 45, NULL, 'available', NULL),
('D.8.1', 46, NULL, 'available', NULL),
('D.8.2', 1, NULL, 'available', NULL),
('D.8.2', 3, NULL, 'available', NULL),
('D.8.2', 5, NULL, 'available', NULL),
('D.8.2', 10, NULL, 'available', NULL),
('D.8.2', 19, NULL, 'available', NULL),
('D.8.2', 20, NULL, 'available', NULL),
('D.8.2', 21, NULL, 'available', NULL),
('D.8.2', 23, NULL, 'available', NULL),
('D.8.2', 24, NULL, 'available', NULL),
('D.8.2', 25, NULL, 'available', NULL),
('D.8.2', 26, NULL, 'available', NULL),
('D.8.2', 33, NULL, 'available', NULL),
('D.8.2', 37, NULL, 'available', NULL),
('D.8.2', 38, NULL, 'available', NULL),
('D.8.2', 41, NULL, 'available', NULL),
('D.8.2', 43, NULL, 'available', NULL),
('D.8.2', 45, NULL, 'available', NULL),
('D.9.1', 1, NULL, 'available', NULL),
('D.9.1', 2, NULL, 'available', NULL),
('D.9.1', 3, NULL, 'available', NULL),
('D.9.1', 5, NULL, 'available', NULL),
('D.9.1', 10, NULL, 'available', NULL),
('D.9.1', 19, NULL, 'available', NULL),
('D.9.1', 20, NULL, 'available', NULL),
('D.9.1', 21, NULL, 'available', NULL),
('D.9.1', 23, NULL, 'available', NULL),
('D.9.1', 24, NULL, 'available', NULL),
('D.9.1', 25, NULL, 'available', NULL),
('D.9.1', 26, NULL, 'available', NULL),
('D.9.1', 27, NULL, 'available', NULL),
('D.9.1', 28, NULL, 'available', NULL),
('D.9.1', 30, NULL, 'available', NULL),
('D.9.1', 31, NULL, 'available', NULL),
('D.9.1', 32, NULL, 'available', NULL),
('D.9.1', 33, NULL, 'available', NULL),
('D.9.1', 34, NULL, 'available', NULL),
('D.9.1', 35, NULL, 'available', NULL),
('D.9.1', 36, NULL, 'available', NULL),
('D.9.1', 37, NULL, 'available', NULL),
('D.9.1', 38, '2025-09-11 14:14:18.651886', 'available', 28),
('D.9.1', 39, NULL, 'available', NULL),
('D.9.1', 40, NULL, 'available', NULL),
('D.9.1', 41, NULL, 'available', NULL),
('D.9.1', 42, NULL, 'available', NULL),
('D.9.1', 43, NULL, 'available', NULL),
('D.9.1', 44, NULL, 'available', NULL),
('D.9.1', 45, NULL, 'available', NULL),
('D.9.1', 46, NULL, 'available', NULL),
('D.9.2', 1, NULL, 'available', NULL),
('D.9.2', 3, NULL, 'available', NULL),
('D.9.2', 5, NULL, 'available', NULL),
('D.9.2', 10, NULL, 'available', NULL),
('D.9.2', 19, NULL, 'available', NULL),
('D.9.2', 20, NULL, 'available', NULL),
('D.9.2', 21, NULL, 'available', NULL),
('D.9.2', 23, NULL, 'available', NULL),
('D.9.2', 24, NULL, 'available', NULL),
('D.9.2', 25, NULL, 'available', NULL),
('D.9.2', 26, NULL, 'available', NULL),
('D.9.2', 33, NULL, 'available', NULL),
('D.9.2', 37, NULL, 'available', NULL),
('D.9.2', 38, NULL, 'available', NULL),
('D.9.2', 41, NULL, 'available', NULL),
('D.9.2', 43, NULL, 'available', NULL),
('D.9.2', 45, NULL, 'available', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `auth_provider` enum('FACEBOOK','GOOGLE','LOCAL') NOT NULL,
  `email_verified` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `password_hash`, `refresh_token`, `role_id`, `auth_provider`, `email_verified`) VALUES
(1, 'customer1@busify.com', '$2a$10$exampleHash', NULL, NULL, 'LOCAL', b'0'),
(2, 'operator@busify.com', '$2a$10$MZ3Y7i8XVpRhbZWDrBymo.segWcbqWSRSoSiku4LAiHikCGTbnJ9K', NULL, 9, 'LOCAL', b'0'),
(3, 'driver@busify.com', '$2a$10$exampleHash', NULL, 2, 'LOCAL', b'0'),
(5, 'customer2@busify.com', '$2a$10$exampleHash', NULL, NULL, 'LOCAL', b'0'),
(6, 'customer3@busify.com', '$2a$10$exampleHash', NULL, NULL, 'LOCAL', b'0'),
(7, 'driver2@busify.com', '$2a$10$exampleHash', NULL, NULL, 'LOCAL', b'0'),
(8, 'customer4@busify.com', '$2a$10$MZ3Y7i8XVpRhbZWDrBymo.segWcbqWSRSoSiku4LAiHikCGTbnJ9K', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjdXN0b21lcjRAYnVzaWZ5LmNvbSIsImlhdCI6MTc1NzQwNjA1MCwiZXhwIjoxNzU4MDEwODUwfQ.QrA7GPJqEDgvOW_XQEsXnhwKvwjNiPpedokgx1-dhUw', 12, 'LOCAL', b'0'),
(10, 'admin@gmail.com', '$2a$10$NkxbpFM873h8cygyP.EVxeynAbyCKSNHbyvuGb5SwbscSVvos1H7m', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3NTg2ODY4NDgsImV4cCI6MTc1OTI5MTY0OH0.3QK674SKMRZtwRxYPBvBHwDq8duZbW5S9EUrNHQeAwI', 1, 'LOCAL', b'1'),
(17, 'nguyenvana@gmail.com', '$2a$10$MZ3Y7i8XVpRhbZWDrBymo.segWcbqWSRSoSiku4LAiHikCGTbnJ9K', NULL, 12, 'LOCAL', b'0'),
(18, 'antoinnguyen95@gmail.com', '$2a$10$PfTudr3FNRwpjglJUuSYCOOgNanrCyV81J6yZx1hq/bPaDUsb.hoC', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvaW5uZ3V5ZW45NUBnbWFpbC5jb20iLCJpYXQiOjE3NTgxNDUwMDcsImV4cCI6MTc1ODc0OTgwN30.us7ETaVpIheB8GPZ5J08474Ue-A_6JFoGd5bHt6bM2g', 9, 'LOCAL', b'1'),
(19, 'hungtv@gmail.com', '$2a$10$NkxbpFM873h8cygyP.EVxeynAbyCKSNHbyvuGb5SwbscSVvos1H7m', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJodW5ndHZAZ21haWwuY29tIiwiaWF0IjoxNzU3NjAxNDIxLCJleHAiOjE3NTgyMDYyMjF9.fm1mnlY-lEIlEdI2ZHWqA31DcVSL8HUQ5FslicWXawU', 12, 'LOCAL', b'1'),
(21, 'anhth@gmail.com', '$2a$10$0vwubdCMNzv/S.3N/no8gOWk3QdP2KvfZ9Li/zij11EeWzEGV84GG', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbmh0aEBnbWFpbC5jb20iLCJpYXQiOjE3NTc2NDY5NDQsImV4cCI6MTc1ODI1MTc0NH0.eTcQf-Uy7bUVtAMeZnfZw6jLZ5XOy2XW9ElfPeQKUoY', 12, 'LOCAL', b'1'),
(22, 'hoaihv@gmail.com', '$2a$10$NkxbpFM873h8cygyP.EVxeynAbyCKSNHbyvuGb5SwbscSVvos1H7m', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob2FpaHZAZ21haWwuY29tIiwiaWF0IjoxNzU4MTQ1MjgwLCJleHAiOjE3NTg3NTAwODB9.3P1DwmyjEnpH5b04J-xo4PvikJ4a7P8WlNN2jb9qdVo', 12, 'LOCAL', b'1'),
(24, 'tinhocdinhcao@gmail.com', '$2a$10$MZ3Y7i8XVpRhbZWDrBymo.segWcbqWSRSoSiku4LAiHikCGTbnJ9K', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW5ob2NkaW5oY2FvQGdtYWlsLmNvbSIsImlhdCI6MTc1NzUyNDY3NCwiZXhwIjoxNzU4MTI5NDc0fQ.wDFV0_yDT5AT4PP4yrL2j_O9RLamKMZf132HgUDwjXk', 9, 'LOCAL', b'1'),
(25, 'cs@gmail.com', '$2a$10$NkxbpFM873h8cygyP.EVxeynAbyCKSNHbyvuGb5SwbscSVvos1H7m', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjc0BnbWFpbC5jb20iLCJpYXQiOjE3NTgxNjIxNDIsImV4cCI6MTc1ODc2Njk0Mn0.u03BWrXm6oI__oL22F8mUImBtdfDjhIHKVs5aL3jlnw', 11, 'LOCAL', b'1'),
(26, 'hoaicalm@gmail.com', '$2a$10$CEsWUgr6KFRUrvlR7H9VCe804s1jBRy9jVWOlVv5Vl4e2BNfgfJBS', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob2FpY2FsbUBnbWFpbC5jb20iLCJpYXQiOjE3NTgxNjMyMjcsImV4cCI6MTc1ODc2ODAyN30.uHQ0B39mzck0g0pL5VBjZ43MpdJ0_UBTCOysusAvzfw', 9, 'LOCAL', b'1'),
(27, 'hoaicoder2605@gmail.com', '$2a$10$37RQuOHH.rY2jer5aP5c9eMIcXitmXxm1HGhzRacFQjSRZwlhNC/e', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob2FpY29kZXIyNjA1QGdtYWlsLmNvbSIsImlhdCI6MTc1NjE3NDQ1NiwiZXhwIjoxNzU2Nzc5MjU2fQ.2nQaua0eZ5b1FlmoIuIZv1qUS4xyZVfTxe4yLLv8HIk', 9, 'LOCAL', b'1'),
(28, 'hoai19800@gmail.com', '$2a$10$UvmvqX.mv0dKSH/l3hww0eIdb1nmi6HwC7qT.yCN5y/rTOQ5NYj1e', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob2FpMTk4MDBAZ21haWwuY29tIiwiaWF0IjoxNzU4NjkyMzkxLCJleHAiOjE3NTkyOTcxOTF9.7sG6Ze7WxrvzE81HAZ6Irc5fuuAX0wnFCAv3E2a6fqI', 9, 'LOCAL', b'1'),
(29, 'duongnt.21it@vku.udn.vn', '$10$MZ3Y7i8XVpRhbZWDrBymo.segWcbqWSRSoSiku4LAiHikCGTbnJ9K', NULL, 2, 'LOCAL', b'1'),
(30, 'nguyentoduong.2019@gmail.com', '$2a$10$WauuPLFco6UPaJyS6zcczOcH0zj3wSKdexxq/NU.nWRKaoUclbAUS', NULL, 2, 'LOCAL', b'1'),
(31, 'nguyentoduong.2003@gmail.com', '$2a$10$NkxbpFM873h8cygyP.EVxeynAbyCKSNHbyvuGb5SwbscSVvos1H7m', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZ3V5ZW50b2R1b25nLjIwMDNAZ21haWwuY29tIiwiaWF0IjoxNzU4MTQ1MjAzLCJleHAiOjE3NTg3NTAwMDN9.jKt1vUzSJfY9chCA3iMyDEIT28MCtBMPvRssd9pYNcI', 2, 'LOCAL', b'1'),
(32, 'khuyenht@gmail.com', '$2a$10$8fz5uqUCAt8D7y1eXYpOuexxd8AYqo6.L5lGv4du0MPH92x9QwB1.', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraHV5ZW5odEBnbWFpbC5jb20iLCJpYXQiOjE3NTgwOTM2ODQsImV4cCI6MTc1ODY5ODQ4NH0.jXnOZ7ZD3O1vr6-6kgVSbwI6p6kAHz1IxnuPHZSxZ9M', 12, 'LOCAL', b'1'),
(33, 'quangnb@gmail.com', '$2a$10$8fz5uqUCAt8D7y1eXYpOuexxd8AYqo6.L5lGv4du0MPH92x9QwB1.', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxdWFuZ25iQGdtYWlsLmNvbSIsImlhdCI6MTc1ODA5MzY2MCwiZXhwIjoxNzU4Njk4NDYwfQ.xZ1xqMakJP7Y_Ty0ZP2vaOeiOtd8TJ5kUa1XN4TgKKQ', 12, 'LOCAL', b'1'),
(34, 'quocla.21it@vku.udn.vn', '$2a$10$Mc1/DEgrGMZakkkqsTKZIOjFjYPBP4gvbAvRZfVyGWSJtDBrlOc/6', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxdW9jbGEuMjFpdEB2a3UudWRuLnZuIiwiaWF0IjoxNzU3OTk1MzYxLCJleHAiOjE3NTg2MDAxNjF9.zHVWLDN7XQROutsxrahSf7_2GO4D3a_gjup6FbgTwNA', 2, 'LOCAL', b'1'),
(35, 'hoaicoder260@gmail.com', '$2a$10$NeVXBNQjHronhy5KkN7pUOo80c0HEQlrpvUk26wExnj7IhSN5ITvG', NULL, 2, 'LOCAL', b'0'),
(36, 'antoin2901@gmail.com', '$2a$10$nU6ijat9SYWpyd73ZSrPr.mhCe.GkNpitXqDZc66g5F10imyLM2.a', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvaW4yOTAxQGdtYWlsLmNvbSIsImlhdCI6MTc1ODA5NzU4MiwiZXhwIjoxNzU4NzAyMzgyfQ.SZkjstM0o1DU1I95KlK19pugm5lBNhzuZxx7z7tiJ7w', 2, 'LOCAL', b'1'),
(37, 'cus1@gmail.com', '$2a$10$18A8v5bHY5RT6PjqKoBWmur6er2ZKRZ9WLmfG.YvBQ0P/Lz1Z80gG', NULL, 2, 'LOCAL', b'0'),
(38, 'dg423@gmail.com', '$2a$10$OZEpEZo445KuMD6d2bkT2e69ZZIp1UX9vq0iZ1SLu3TcBSBtjiBtO', NULL, 2, 'LOCAL', b'0'),
(39, 'thuongtv.21it@vku.udn.vn', '$2a$10$jo2tw1qs4WL.TYcWPfaPmuYlN7K7nmyZ.2IBZYgVZHZ85qpYoDyX.', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aHVvbmd0di4yMWl0QHZrdS51ZG4udm4iLCJpYXQiOjE3NTgwOTEwNTgsImV4cCI6MTc1ODY5NTg1OH0.XLqywedH6DLOrqkKaBNc67D4GzmPcJzYLNnctyDjVm8', 2, 'LOCAL', b'1'),
(40, 'anhnh@gmail.com', '$2a$10$8fz5uqUCAt8D7y1eXYpOuexxd8AYqo6.L5lGv4du0MPH92x9QwB1.', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbmhuaEBnbWFpbC5jb20iLCJpYXQiOjE3NTgwOTM2NDEsImV4cCI6MTc1ODY5ODQ0MX0.ZqjnYDpI8c9extywxi3G6PkHyTdLK9MeOc7DmVP3hGc', 12, 'LOCAL', b'1'),
(41, 'thuongem@gmail.com', '$2a$10$Bu5gh5px18g6hFWymvwfAejK/WEDvmjOENVUJ6UX37lkJeOfvQque', NULL, 2, 'LOCAL', b'1'),
(42, 'vc@gmail.com', '$2a$10$w5X4CKlJytjtOp5sb1IAieliYuelsoURwF96HOke6IFGwri8xEfYC', NULL, 12, 'LOCAL', b'0'),
(43, 'antoinnguyen2@gmail.com', '$2a$10$aBzkxlOODx6s8ZZnWTVTjOc417qYYyJZC4jW/Wile9gPXKh3KhUcy', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvaW5uZ3V5ZW4yQGdtYWlsLmNvbSIsImlhdCI6MTc1ODMzMDYxNywiZXhwIjoxNzU4OTM1NDE3fQ.DbpG-PmmwTA_haUMpjU_1NmULs_b8ZqGOhEaj4v74eI', 9, 'LOCAL', b'1'),
(44, 'hoai123@gmail.com', '$2a$10$kyYT7HJgkiYIBDEfuKPyHOCOTCykLPrRyXmRwTDdTIeZ8QmuEW11G', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob2FpMTIzQGdtYWlsLmNvbSIsImlhdCI6MTc1ODA5MTEyNiwiZXhwIjoxNzU4Njk1OTI2fQ.25GzdcK0c0PVU_pSuwlH6lii2rHRrPzZ4wdbgpicwWw', 12, 'LOCAL', b'1'),
(46, 'hoai123123@gmail.com', '$2a$10$.m03RbTmevFa1gt2DHAUvOy7HYP09/F2M1aAzOxGqMmLTSOCC9tJO', NULL, 12, 'LOCAL', b'1'),
(53, 'Sincere@april.biz', '$2a$10$h0uM2LglYFCE59QjQHNgoeL4w3H4XYyhkgRCqoGpJJRlkx.VGBlDW', NULL, 12, 'LOCAL', b'1'),
(54, 'antoin@gmail.com', '$2a$10$6VozRSEfL.QDoJrBp8yEXu3W8GvkrwYX3xPYXCYmFijj8bDGpRn5G', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvaW5AZ21haWwuY29tIiwiaWF0IjoxNzU4MzMxNzU5LCJleHAiOjE3NTg5MzY1NTl9.BYrercpyiVZlEL1fW7RjZ4yc2-UB3fsfzfEOrlZfajE', 12, 'LOCAL', b'1'),
(55, 'binhnguyen130816@gmail.com', '$2a$10$WrfXOwCKhMXserRpWGTGXuo8ox8tDuh9YSy/L8SBuadz4VQ6jAeQ.', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaW5obmd1eWVuMTMwODE2QGdtYWlsLmNvbSIsImlhdCI6MTc1ODMzMTU5MCwiZXhwIjoxNzU4OTM2MzkwfQ.tBbKQIn9bexZ63vbNvFZNM7V-_6kwedJrOdlrLvZx44', 11, 'LOCAL', b'1'),
(56, 'quocng@gmail.com', '$2a$10$OBPlgkxMbz0Do7Xfru/U1ujKWaQxCwOKFKqdYdSqUi5d6bpXmAJTa', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxdW9jbmdAZ21haWwuY29tIiwiaWF0IjoxNzU4MTY0NjI3LCJleHAiOjE3NTg3Njk0Mjd9.XB9KOY21c8vhIsjpqftdXidZQF-7Q1OHIfvsQpJdqQs', 12, 'LOCAL', b'1'),
(57, 'trungtb@gmail.com', '$2a$10$5BzWABV/fyr4rzlYROTKxO2nGC69abBskHszH9WIe0bv0h3PhvwUO', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cnVuZ3RiQGdtYWlsLmNvbSIsImlhdCI6MTc1ODMzMjQxOCwiZXhwIjoxNzU4OTM3MjE4fQ.k_5RCUHW3Gkz8O6M_KYrN9rtWVZtjZB1zMp-RD0amy0', 10, 'LOCAL', b'1'),
(58, 'ducdv@gmail.com', '$2a$10$mKcM1H/z6vJVgoQ16VziZ.rOeGV6f/S/SHSpYo7BxR/yYgUxS9sjC', NULL, 10, 'LOCAL', b'1');

-- --------------------------------------------------------

--
-- Table structure for table `user_promotions`
--

CREATE TABLE `user_promotions` (
  `promotion_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `claimed_at` datetime(6) NOT NULL,
  `is_used` bit(1) NOT NULL,
  `used_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_promotions`
--

INSERT INTO `user_promotions` (`promotion_id`, `user_id`, `claimed_at`, `is_used`, `used_at`) VALUES
(2, 26, '2025-09-07 23:10:37.720088', b'0', NULL),
(2, 28, '2025-09-07 23:10:44.985013', b'0', NULL),
(10, 28, '2025-09-23 15:47:12.000000', b'1', '2025-09-23 15:47:12.000000'),
(13, 26, '2025-09-10 15:33:37.113287', b'1', '2025-09-10 15:33:37.113287'),
(13, 28, '2025-09-10 13:29:49.312447', b'1', '2025-09-10 13:29:49.312447'),
(13, 31, '2025-09-18 03:23:06.119683', b'1', '2025-09-18 03:23:06.119683'),
(15, 26, '2025-09-11 21:32:45.085880', b'1', '2025-09-11 21:32:52.394393'),
(24, 31, '2025-09-18 04:11:18.367412', b'0', NULL),
(25, 28, '2025-09-19 22:20:54.027929', b'1', '2025-09-19 22:20:54.027929'),
(26, 28, '2025-09-24 13:04:25.000000', b'1', '2025-09-24 13:04:25.000000');

-- --------------------------------------------------------

--
-- Table structure for table `user_promotion_conditions`
--

CREATE TABLE `user_promotion_conditions` (
  `id` bigint(20) NOT NULL,
  `completed_at` datetime(6) DEFAULT NULL,
  `is_completed` bit(1) NOT NULL,
  `progress_data` varchar(255) DEFAULT NULL,
  `promotion_condition_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_promotion_conditions`
--

INSERT INTO `user_promotion_conditions` (`id`, `completed_at`, `is_completed`, `progress_data`, `promotion_condition_id`, `user_id`) VALUES
(1, '2025-09-24 12:40:12.000000', b'1', '{\"viewed\":true,\"progress\":0.9480103333333334}', 2, 28);

-- --------------------------------------------------------

--
-- Table structure for table `verification_tokens`
--

CREATE TABLE `verification_tokens` (
  `id` bigint(20) NOT NULL,
  `created_date` datetime(6) NOT NULL,
  `expiry_date` datetime(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  `token_type` enum('ACCOUNT_ACTIVATION','EMAIL_VERIFICATION','PASSWORD_RESET') DEFAULT NULL,
  `used` bit(1) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `audit_logs`
--
ALTER TABLE `audit_logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKjs4iimve3y0xssbtve5ysyef0` (`user_id`);

--
-- Indexes for table `blog_posts`
--
ALTER TABLE `blog_posts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_blog_posts_slug` (`slug`),
  ADD KEY `idx_blog_posts_featured` (`featured`),
  ADD KEY `idx_blog_posts_created_at` (`created_at`),
  ADD KEY `FKlog64k5g2l1679hjl2wuyyk5n` (`author_id`);

--
-- Indexes for table `blog_post_tags`
--
ALTER TABLE `blog_post_tags`
  ADD KEY `FK9lwi4pg2kl7ce7pa3r3yotb9w` (`blog_post_id`);

--
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKq97166k18hklq6ls46osbrftx` (`booking_code`),
  ADD KEY `idx_booking_customerID_createdAt` (`customer_id`,`created_at`),
  ADD KEY `FKegnh9ksa308rvreolxjjgxfbn` (`agent_accept_booking_id`),
  ADD KEY `FK76g5jpvf8bcqejvp5d2vgrnjb` (`trip_id`),
  ADD KEY `FKk4byobgkjv3y3952wwpxyep7o` (`promotion_id`);

--
-- Indexes for table `buses`
--
ALTER TABLE `buses`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uc_buses_license_plate` (`license_plate`),
  ADD KEY `idx_buses_licensePlate` (`license_plate`),
  ADD KEY `FKwmt4p1sd2pys4x800ha06ste` (`operator_id`),
  ADD KEY `FK71mdanlbyn55bepxk8j27n3g6` (`seat_layout_id`),
  ADD KEY `FKnyhvro1aecuy0rfu8da4a4tls` (`model_id`);

--
-- Indexes for table `bus_images`
--
ALTER TABLE `bus_images`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKjpvjkjl092v7bmfhi0gdgcc2j` (`bus_id`);

--
-- Indexes for table `bus_models`
--
ALTER TABLE `bus_models`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK8y17oiagnmlkr9c3xnv61c7yj` (`name`);

--
-- Indexes for table `bus_operators`
--
ALTER TABLE `bus_operators`
  ADD PRIMARY KEY (`operator_id`),
  ADD KEY `FKhdysn8wxp8hnchty3yuo3nkii` (`owner_id`);

--
-- Indexes for table `chat_messages`
--
ALTER TABLE `chat_messages`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `complaints`
--
ALTER TABLE `complaints`
  ADD PRIMARY KEY (`complaints_id`),
  ADD KEY `FKdjr8v1k0sie3eioouxacnfjsx` (`assigned_agent_id`),
  ADD KEY `FK9iay3myxtp9bh1vf9iuj2rich` (`booking_id`),
  ADD KEY `FKhtvrekom2uyukk0u8e95np1se` (`customer_id`);

--
-- Indexes for table `contracts`
--
ALTER TABLE `contracts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKcxaca0d8qugontspm79o8mfk5` (`vatcode`);

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_driver_license` (`driver_license_number`),
  ADD KEY `FK8yuomapwk6ker3oiqr5uo8axa` (`operator_id`);

--
-- Indexes for table `locations`
--
ALTER TABLE `locations`
  ADD PRIMARY KEY (`location_id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`payment_id`),
  ADD UNIQUE KEY `UK8inpv30544qjykcwa6ck7pusy` (`transaction_code`),
  ADD KEY `FKc52o2b1jkxttngufqp3t7jr3h` (`booking_id`);

--
-- Indexes for table `permission`
--
ALTER TABLE `permission`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK2ojme20jpga3r4r79tdso17gi` (`name`);

--
-- Indexes for table `permissions`
--
ALTER TABLE `permissions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKpnvtwliis6p05pn6i3ndjrqt2` (`name`);

--
-- Indexes for table `profiles`
--
ALTER TABLE `profiles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK4ixsj6aqve5pxrbw2u0oyk8bb` (`user_id`);

--
-- Indexes for table `promotions`
--
ALTER TABLE `promotions`
  ADD PRIMARY KEY (`promotion_id`),
  ADD UNIQUE KEY `UKjdho73ymbyu46p2hh562dk4kk` (`code`),
  ADD KEY `FKh48248rg5tt6ejid94ul2j6g8` (`campaign_id`);

--
-- Indexes for table `promotion_campaigns`
--
ALTER TABLE `promotion_campaigns`
  ADD PRIMARY KEY (`campaign_id`);

--
-- Indexes for table `promotion_conditions`
--
ALTER TABLE `promotion_conditions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK1pkxv7uc4v3x81rble604gsni` (`promotion_id`);

--
-- Indexes for table `promotion_user`
--
ALTER TABLE `promotion_user`
  ADD PRIMARY KEY (`promotion_id`,`user_id`),
  ADD KEY `FK9dw5l6m4oguuqpi4cr3uxf6eq` (`user_id`);

--
-- Indexes for table `refunds`
--
ALTER TABLE `refunds`
  ADD PRIMARY KEY (`refund_id`),
  ADD UNIQUE KEY `UKa3jt3rriqq7js4pdyswpc4x89` (`refund_transaction_code`),
  ADD KEY `FKpt9ic0j1y6xwlej99wnynvnpy` (`payment_id`),
  ADD KEY `FK1normc5g6it6fpqbrklfxmert` (`requested_by`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`review_id`),
  ADD KEY `FKkquncb1glvrldaui8v52xfd5q` (`customer_id`),
  ADD KEY `FKe70h9t86py3fbswj0gw16v0by` (`trip_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `role_permissions`
--
ALTER TABLE `role_permissions`
  ADD PRIMARY KEY (`role_id`,`permission_id`),
  ADD KEY `FKegdk29eiy7mdtefy5c7eirr6e` (`permission_id`);

--
-- Indexes for table `routes`
--
ALTER TABLE `routes`
  ADD PRIMARY KEY (`route_id`),
  ADD KEY `FKtqxldvq6vfex3mna2wxpuw9an` (`end_location_id`),
  ADD KEY `FKja5uqif3mnj3ff3idvepoifar` (`start_location_id`);

--
-- Indexes for table `route_stops`
--
ALTER TABLE `route_stops`
  ADD PRIMARY KEY (`location_id`,`route_id`),
  ADD KEY `FK63y33daxb1qs5nbnkuicbpkej` (`route_id`);

--
-- Indexes for table `scores`
--
ALTER TABLE `scores`
  ADD PRIMARY KEY (`score_id`),
  ADD UNIQUE KEY `UKiwa2pu9prwycj32i04g71sk7b` (`user_id`),
  ADD KEY `idx_scores_userId_createdAt` (`user_id`,`created_at`);

--
-- Indexes for table `score_history`
--
ALTER TABLE `score_history`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKmwhe7bwrr6gs2ki2i0frsh9tx` (`booking_id`),
  ADD KEY `FKhd7knvy68f2ns4d0umdsuv43l` (`user_id`);

--
-- Indexes for table `seat_layouts`
--
ALTER TABLE `seat_layouts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `spring_session`
--
ALTER TABLE `spring_session`
  ADD PRIMARY KEY (`PRIMARY_ID`),
  ADD UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  ADD KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  ADD KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`);

--
-- Indexes for table `spring_session_attributes`
--
ALTER TABLE `spring_session_attributes`
  ADD PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`);

--
-- Indexes for table `tickets`
--
ALTER TABLE `tickets`
  ADD PRIMARY KEY (`ticket_id`),
  ADD UNIQUE KEY `UKcvl4jbu5fln08ltem9rrmtp8w` (`ticket_code`),
  ADD KEY `FKefja4avuu7g29t78mxifrsynb` (`booking_id`),
  ADD KEY `FK6g0x2n6x1ftnahopwof2otg0r` (`seller_id`);

--
-- Indexes for table `trips`
--
ALTER TABLE `trips`
  ADD PRIMARY KEY (`trip_id`),
  ADD KEY `idx_trips_departureTime_routeId` (`departure_time`,`route_id`),
  ADD KEY `FK2vg7b2xayoq4ogt2kbsot4juq` (`bus_id`),
  ADD KEY `FKevvmqtfyigychrwa7y8c14sht` (`driver_id`),
  ADD KEY `FKm7ci3blm9wj2k0d94chu18y7s` (`route_id`);

--
-- Indexes for table `trip_seats`
--
ALTER TABLE `trip_seats`
  ADD PRIMARY KEY (`seat_number`,`trip_id`),
  ADD KEY `FKiv2sj369u16d4iyoi6wkvnn11` (`locking_user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_users_email` (`email`),
  ADD KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`);

--
-- Indexes for table `user_promotions`
--
ALTER TABLE `user_promotions`
  ADD PRIMARY KEY (`promotion_id`,`user_id`),
  ADD KEY `FK9n3907kmcsshrnc6cowfmn3xp` (`user_id`);

--
-- Indexes for table `user_promotion_conditions`
--
ALTER TABLE `user_promotion_conditions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKmq8wxr97924cr7acop1bdnaxp` (`promotion_condition_id`),
  ADD KEY `FKufs13cetw04qbmrp8pjitlo` (`user_id`);

--
-- Indexes for table `verification_tokens`
--
ALTER TABLE `verification_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6q9nsb665s9f8qajm3j07kd1e` (`token`),
  ADD UNIQUE KEY `UKdqp95ggn6gvm865km5muba2o5` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `audit_logs`
--
ALTER TABLE `audit_logs`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `blog_posts`
--
ALTER TABLE `blog_posts`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=224;

--
-- AUTO_INCREMENT for table `buses`
--
ALTER TABLE `buses`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `bus_images`
--
ALTER TABLE `bus_images`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=75;

--
-- AUTO_INCREMENT for table `bus_models`
--
ALTER TABLE `bus_models`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `bus_operators`
--
ALTER TABLE `bus_operators`
  MODIFY `operator_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `chat_messages`
--
ALTER TABLE `chat_messages`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=247;

--
-- AUTO_INCREMENT for table `complaints`
--
ALTER TABLE `complaints`
  MODIFY `complaints_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `contracts`
--
ALTER TABLE `contracts`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `locations`
--
ALTER TABLE `locations`
  MODIFY `location_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `payment_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=157;

--
-- AUTO_INCREMENT for table `permission`
--
ALTER TABLE `permission`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `permissions`
--
ALTER TABLE `permissions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `promotions`
--
ALTER TABLE `promotions`
  MODIFY `promotion_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `promotion_campaigns`
--
ALTER TABLE `promotion_campaigns`
  MODIFY `campaign_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `promotion_conditions`
--
ALTER TABLE `promotion_conditions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `refunds`
--
ALTER TABLE `refunds`
  MODIFY `refund_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `review_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `routes`
--
ALTER TABLE `routes`
  MODIFY `route_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `scores`
--
ALTER TABLE `scores`
  MODIFY `score_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `score_history`
--
ALTER TABLE `score_history`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `seat_layouts`
--
ALTER TABLE `seat_layouts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tickets`
--
ALTER TABLE `tickets`
  MODIFY `ticket_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=221;

--
-- AUTO_INCREMENT for table `trips`
--
ALTER TABLE `trips`
  MODIFY `trip_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT for table `user_promotion_conditions`
--
ALTER TABLE `user_promotion_conditions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `verification_tokens`
--
ALTER TABLE `verification_tokens`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `audit_logs`
--
ALTER TABLE `audit_logs`
  ADD CONSTRAINT `FKjs4iimve3y0xssbtve5ysyef0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `blog_posts`
--
ALTER TABLE `blog_posts`
  ADD CONSTRAINT `FKlog64k5g2l1679hjl2wuyyk5n` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `blog_post_tags`
--
ALTER TABLE `blog_post_tags`
  ADD CONSTRAINT `FK9lwi4pg2kl7ce7pa3r3yotb9w` FOREIGN KEY (`blog_post_id`) REFERENCES `blog_posts` (`id`);

--
-- Constraints for table `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `FK76g5jpvf8bcqejvp5d2vgrnjb` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`trip_id`),
  ADD CONSTRAINT `FKegnh9ksa308rvreolxjjgxfbn` FOREIGN KEY (`agent_accept_booking_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKib6gjgj2e9binkktxmm175bmm` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKk4byobgkjv3y3952wwpxyep7o` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`promotion_id`);

--
-- Constraints for table `buses`
--
ALTER TABLE `buses`
  ADD CONSTRAINT `FK71mdanlbyn55bepxk8j27n3g6` FOREIGN KEY (`seat_layout_id`) REFERENCES `seat_layouts` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `FKnyhvro1aecuy0rfu8da4a4tls` FOREIGN KEY (`model_id`) REFERENCES `bus_models` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `FKwmt4p1sd2pys4x800ha06ste` FOREIGN KEY (`operator_id`) REFERENCES `bus_operators` (`operator_id`) ON DELETE SET NULL;

--
-- Constraints for table `bus_images`
--
ALTER TABLE `bus_images`
  ADD CONSTRAINT `FKjpvjkjl092v7bmfhi0gdgcc2j` FOREIGN KEY (`bus_id`) REFERENCES `buses` (`id`);

--
-- Constraints for table `bus_operators`
--
ALTER TABLE `bus_operators`
  ADD CONSTRAINT `FK1s2plmqc02rou11x8g1k4eawh` FOREIGN KEY (`owner_id`) REFERENCES `profiles` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `FKhdysn8wxp8hnchty3yuo3nkii` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `complaints`
--
ALTER TABLE `complaints`
  ADD CONSTRAINT `FK9iay3myxtp9bh1vf9iuj2rich` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`),
  ADD CONSTRAINT `FKdjr8v1k0sie3eioouxacnfjsx` FOREIGN KEY (`assigned_agent_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKhtvrekom2uyukk0u8e95np1se` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `employees`
--
ALTER TABLE `employees`
  ADD CONSTRAINT `FK3c8y10sx8hu44w2qvr8gbqxj9` FOREIGN KEY (`id`) REFERENCES `profiles` (`id`),
  ADD CONSTRAINT `FK8yuomapwk6ker3oiqr5uo8axa` FOREIGN KEY (`operator_id`) REFERENCES `bus_operators` (`operator_id`) ON DELETE SET NULL;

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `FKc52o2b1jkxttngufqp3t7jr3h` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`);

--
-- Constraints for table `profiles`
--
ALTER TABLE `profiles`
  ADD CONSTRAINT `FK410q61iev7klncmpqfuo85ivh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FK55e5d3sfwkob62wtprm633alk` FOREIGN KEY (`id`) REFERENCES `users` (`id`);

--
-- Constraints for table `promotions`
--
ALTER TABLE `promotions`
  ADD CONSTRAINT `FKh48248rg5tt6ejid94ul2j6g8` FOREIGN KEY (`campaign_id`) REFERENCES `promotion_campaigns` (`campaign_id`);

--
-- Constraints for table `promotion_conditions`
--
ALTER TABLE `promotion_conditions`
  ADD CONSTRAINT `FK1pkxv7uc4v3x81rble604gsni` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`promotion_id`);

--
-- Constraints for table `promotion_user`
--
ALTER TABLE `promotion_user`
  ADD CONSTRAINT `FK3ap8kx8m6rhj1sp2yfwqjnx84` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`promotion_id`),
  ADD CONSTRAINT `FK9dw5l6m4oguuqpi4cr3uxf6eq` FOREIGN KEY (`user_id`) REFERENCES `profiles` (`id`);

--
-- Constraints for table `refunds`
--
ALTER TABLE `refunds`
  ADD CONSTRAINT `FK1normc5g6it6fpqbrklfxmert` FOREIGN KEY (`requested_by`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKpt9ic0j1y6xwlej99wnynvnpy` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`payment_id`);

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `FKe70h9t86py3fbswj0gw16v0by` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`trip_id`),
  ADD CONSTRAINT `FKkquncb1glvrldaui8v52xfd5q` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `role_permissions`
--
ALTER TABLE `role_permissions`
  ADD CONSTRAINT `FKegdk29eiy7mdtefy5c7eirr6e` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`),
  ADD CONSTRAINT `FKh0v7u4w7mttcu81o8wegayr8e` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`),
  ADD CONSTRAINT `FKn5fotdgk8d1xvo8nav9uv3muc` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`);

--
-- Constraints for table `routes`
--
ALTER TABLE `routes`
  ADD CONSTRAINT `FKja5uqif3mnj3ff3idvepoifar` FOREIGN KEY (`start_location_id`) REFERENCES `locations` (`location_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `FKtqxldvq6vfex3mna2wxpuw9an` FOREIGN KEY (`end_location_id`) REFERENCES `locations` (`location_id`) ON DELETE SET NULL;

--
-- Constraints for table `route_stops`
--
ALTER TABLE `route_stops`
  ADD CONSTRAINT `FK63y33daxb1qs5nbnkuicbpkej` FOREIGN KEY (`route_id`) REFERENCES `routes` (`route_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FKp1fka8xfu6pf91algbjueupey` FOREIGN KEY (`location_id`) REFERENCES `locations` (`location_id`) ON DELETE CASCADE;

--
-- Constraints for table `scores`
--
ALTER TABLE `scores`
  ADD CONSTRAINT `FKtkgoiahryd4yntgywbqyyw8o8` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `score_history`
--
ALTER TABLE `score_history`
  ADD CONSTRAINT `FKbfmawtelxb14ec4ade75d8dt0` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`),
  ADD CONSTRAINT `FKhd7knvy68f2ns4d0umdsuv43l` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `tickets`
--
ALTER TABLE `tickets`
  ADD CONSTRAINT `FK6g0x2n6x1ftnahopwof2otg0r` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKefja4avuu7g29t78mxifrsynb` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`);

--
-- Constraints for table `trips`
--
ALTER TABLE `trips`
  ADD CONSTRAINT `FK2vg7b2xayoq4ogt2kbsot4juq` FOREIGN KEY (`bus_id`) REFERENCES `buses` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `FKevvmqtfyigychrwa7y8c14sht` FOREIGN KEY (`driver_id`) REFERENCES `employees` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `FKm7ci3blm9wj2k0d94chu18y7s` FOREIGN KEY (`route_id`) REFERENCES `routes` (`route_id`) ON DELETE SET NULL;

--
-- Constraints for table `trip_seats`
--
ALTER TABLE `trip_seats`
  ADD CONSTRAINT `FKiv2sj369u16d4iyoi6wkvnn11` FOREIGN KEY (`locking_user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE SET NULL;

--
-- Constraints for table `user_promotions`
--
ALTER TABLE `user_promotions`
  ADD CONSTRAINT `FK9n3907kmcsshrnc6cowfmn3xp` FOREIGN KEY (`user_id`) REFERENCES `profiles` (`id`),
  ADD CONSTRAINT `FKt48sqypmf97fs19jgx5t9lglr` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`promotion_id`);

--
-- Constraints for table `user_promotion_conditions`
--
ALTER TABLE `user_promotion_conditions`
  ADD CONSTRAINT `FKmq8wxr97924cr7acop1bdnaxp` FOREIGN KEY (`promotion_condition_id`) REFERENCES `promotion_conditions` (`id`),
  ADD CONSTRAINT `FKufs13cetw04qbmrp8pjitlo` FOREIGN KEY (`user_id`) REFERENCES `profiles` (`id`);

--
-- Constraints for table `verification_tokens`
--
ALTER TABLE `verification_tokens`
  ADD CONSTRAINT `FK54y8mqsnq1rtyf581sfmrbp4f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
