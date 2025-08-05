-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 31, 2025 at 06:01 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `busify`
--

-- --------------------------------------------------------

--
-- Table structure for table `audit_logs`
--

CREATE TABLE `audit_logs`
(
    `id`            bigint(20)   NOT NULL,
    `action`        varchar(255) NOT NULL,
    `details`       longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`details`)),
    `target_entity` varchar(255)                                       DEFAULT NULL,
    `target_id`     bigint(20)                                         DEFAULT NULL,
    `timestamp`     datetime(6)  NOT NULL,
    `user_id`       bigint(20)                                         DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `audit_logs`
--

INSERT INTO `audit_logs` (`id`, `action`, `details`, `target_entity`, `target_id`, `timestamp`, `user_id`)
VALUES (1, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK123\"}', 'BOOKING', 1, '2025-07-24 12:00:00.000000', 2),
       (2, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK124\"}', 'BOOKING', 2, '2025-07-24 13:00:00.000000', 6),
       (3, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK125\"}', 'BOOKING', 3, '2025-07-24 14:00:00.000000', 7),
       (4, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK126\"}', 'BOOKING', 4, '2025-07-24 15:00:00.000000', 9),
       (5, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK127\"}', 'BOOKING', 5, '2025-07-24 16:00:00.000000', 6);

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

CREATE TABLE `bookings`
(
    `id`                      bigint(20)                                                                         NOT NULL,
    `booking_code`            varchar(255)                                                                       NOT NULL,
    `created_at`              datetime(6)                                                                        NOT NULL,
    `guest_address`           varchar(255) DEFAULT NULL,
    `guest_email`             varchar(255) DEFAULT NULL,
    `guest_full_name`         varchar(255) DEFAULT NULL,
    `guest_phone`             varchar(255) DEFAULT NULL,
    `seat_number`             varchar(255)                                                                       NOT NULL,
    `status`                  enum ('canceled_by_operator','canceled_by_user','completed','confirmed','pending') NOT NULL,
    `total_amount`            decimal(38, 2)                                                                     NOT NULL,
    `updated_at`              datetime(6)                                                                        NOT NULL,
    `agent_accept_booking_id` bigint(20)   DEFAULT NULL,
    `customer_id`             bigint(20)   DEFAULT NULL,
    `trip_id`                 bigint(20)                                                                         NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`id`, `booking_code`, `created_at`, `guest_address`, `guest_email`, `guest_full_name`,
                        `guest_phone`, `seat_number`, `status`, `total_amount`, `updated_at`, `agent_accept_booking_id`,
                        `customer_id`, `trip_id`)
VALUES (1, 'BOOK123', '2025-07-24 12:00:00.000000', '456 Nguyễn Trãi, TP.HCM', 'customer1@busify.com', 'Trần Thị Khách',
        '0987654321', 'A1', 'confirmed', 500000.00, '2025-07-24 12:00:00.000000', NULL, 2, 1),
       (2, 'BOOK124', '2025-07-24 13:00:00.000000', '34 Hai Bà Trưng, Huế', 'customer2@busify.com', 'Lê Văn Khách',
        '0976543210', 'C1', 'confirmed', 150000.00, '2025-07-24 13:00:00.000000', NULL, 6, 2),
       (3, 'BOOK125', '2025-07-24 14:00:00.000000', '56 Nguyễn Huệ, Hà Nội', 'customer3@busify.com',
        'Phạm Thị Hành Khách', '0965432109', 'E1', 'pending', 250000.00, '2025-07-24 14:00:00.000000', NULL, 7, 3),
       (4, 'BOOK126', '2025-07-24 15:00:00.000000', '90 Lý Thường Kiệt, Cần Thơ', 'customer4@busify.com',
        'Hoàng Văn Khách', '0954321098', 'C1', 'confirmed', 200000.00, '2025-07-24 15:00:00.000000', NULL, 9, 4),
       (5, 'BOOK127', '2025-07-24 16:00:00.000000', '34 Hai Bà Trưng, Huế', 'customer2@busify.com', 'Lê Văn Khách',
        '0976543210', 'E1', 'confirmed', 220000.00, '2025-07-24 16:00:00.000000', NULL, 6, 5);

-- --------------------------------------------------------

--
-- Table structure for table `booking_promotions`
--

CREATE TABLE `booking_promotions`
(
    `booking_id`   bigint(20) NOT NULL,
    `promotion_id` bigint(20) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `booking_promotions`
--

INSERT INTO `booking_promotions` (`booking_id`, `promotion_id`)
VALUES (1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `buses`
--

CREATE TABLE `buses`
(
    `id`             bigint(20)                                           NOT NULL,
    `amenities`      longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`amenities`)),
    `license_plate`  varchar(50)                                          NOT NULL,
    `model`          varchar(255)                                       DEFAULT NULL,
    `status`         enum ('active','out_of_service','under_maintenance') NOT NULL,
    `total_seats`    int(11)                                              NOT NULL,
    `operator_id`    bigint(20)                                         DEFAULT NULL,
    `seat_layout_id` int(11)                                            DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `buses`
--

INSERT INTO `buses` (`id`, `amenities`, `license_plate`, `model`, `status`, `total_seats`, `operator_id`,
                     `seat_layout_id`)
VALUES (1, '{\"wifi\": true, \"air_conditioner\": true}', '51B-12345', 'Hyundai Universe', 'active', 40, 1, 1),
       (2, '{\"wifi\": true, \"air_conditioner\": true, \"usb_charging\": true}', '51B-67890', 'Thaco Mobihome',
        'active', 32, 2, 2),
       (3, '{\"wifi\": true, \"air_conditioner\": true}', '51B-54321', 'Hyundai Aero', 'active', 48, 3, 3),
       (4, '{\"wifi\": false, \"air_conditioner\": true}', '51B-98765', 'Isuzu Samco', 'under_maintenance', 40, 4, 1),
       (5, '{\"wifi\": true, \"air_conditioner\": true, \"tv\": true}', '51B-11223', 'Mercedes Sprinter', 'active', 32,
        5, 2),
       (6, '{\"wifi\": true, \"air_conditioner\": true}', '51B-44556', 'Volvo B11R', 'active', 48, 6, 3);

-- --------------------------------------------------------

--
-- Table structure for table `bus_operators`
--

CREATE TABLE `bus_operators`
(
    `operator_id`  bigint(20)                                                NOT NULL,
    `address`      varchar(255) DEFAULT NULL,
    `created_at`   datetime(6)                                               NOT NULL,
    `description`  varchar(255) DEFAULT NULL,
    `email`        varchar(255)                                              NOT NULL,
    `hotline`      varchar(50)  DEFAULT NULL,
    `license_path` varchar(255)                                              NOT NULL,
    `name`         varchar(255)                                              NOT NULL,
    `status`       enum ('active','inactive','pending_approval','suspended') NOT NULL,
    `updated_at`   datetime(6)                                               NOT NULL,
    `owner_id`     bigint(20)   DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `bus_operators`
--

INSERT INTO `bus_operators` (`operator_id`, `address`, `created_at`, `description`, `email`, `hotline`, `license_path`,
                             `name`, `status`, `updated_at`, `owner_id`)
VALUES (1, '789 Lê Lợi, Đà Nẵng', '2025-07-24 10:00:00.000000', 'Nhà xe uy tín', 'operator@busify.com', '0901234567',
        '/licenses/operator1.pdf', 'Nhà Xe ABC', 'active', '2025-07-24 10:00:00.000000', 3),
       (2, '12 Phạm Ngọc Thạch, Đà Lạt', '2025-07-24 11:00:00.000000', 'Nhà xe chất lượng cao', 'operator2@busify.com',
        '0931234567', '/licenses/operator2.pdf', 'Nhà Xe XYZ', 'active', '2025-07-24 11:00:00.000000', 5),
       (3, '45 Nguyễn Văn Cừ, Huế', '2025-07-24 11:00:00.000000', 'Dịch vụ thân thiện', 'operator3@busify.com',
        '0932234567', '/licenses/operator3.pdf', 'Nhà Xe Huế', 'pending_approval', '2025-07-24 11:00:00.000000', 5),
       (4, '67 Trần Hưng Đạo, Cần Thơ', '2025-07-24 11:00:00.000000', 'Nhà xe uy tín miền Tây', 'operator4@busify.com',
        '0933234567', '/licenses/operator4.pdf', 'Nhà Xe Miền Tây', 'active', '2025-07-24 11:00:00.000000', 5),
       (5, '89 Võ Thị Sáu, Vũng Tàu', '2025-07-24 11:00:00.000000', 'Chuyến xe an toàn', 'operator5@busify.com',
        '0934234567', '/licenses/operator5.pdf', 'Nhà Xe Biển Xanh', 'active', '2025-07-24 11:00:00.000000', 5),
       (6, '101 Lê Lai, Nha Trang', '2025-07-24 11:00:00.000000', 'Dịch vụ cao cấp', 'operator6@busify.com',
        '0935234567', '/licenses/operator6.pdf', 'Nhà Xe Nha Trang', 'active', '2025-07-24 11:00:00.000000', 5);

-- --------------------------------------------------------

--
-- Table structure for table `complaints`
--

CREATE TABLE `complaints`
(
    `complaints_id`     bigint(20)                                                 NOT NULL,
    `created_at`        datetime(6)                                                NOT NULL,
    `description`       varchar(255)                                               NOT NULL,
    `status`            enum ('New','in_progress','pending','rejected','resolved') NOT NULL,
    `title`             varchar(255)                                               NOT NULL,
    `updated_at`        datetime(6)                                                NOT NULL,
    `assigned_agent_id` bigint(20) DEFAULT NULL,
    `booking_id`        bigint(20) DEFAULT NULL,
    `customer_id`       bigint(20)                                                 NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `complaints`
--

INSERT INTO `complaints` (`complaints_id`, `created_at`, `description`, `status`, `title`, `updated_at`,
                          `assigned_agent_id`, `booking_id`, `customer_id`)
VALUES (1, '2025-07-25 22:00:00.000000', 'Ghế không thoải mái', 'New', 'Khiếu nại về ghế', '2025-07-25 22:00:00.000000',
        NULL, 1, 2),
       (2, '2025-07-25 14:00:00.000000', 'Xe đến muộn 30 phút', 'pending', 'Khiếu nại về thời gian',
        '2025-07-25 14:00:00.000000', NULL, 2, 6),
       (3, '2025-07-25 15:00:00.000000', 'Wifi không hoạt động', 'New', 'Khiếu nại về wifi',
        '2025-07-25 15:00:00.000000', NULL, 3, 7);

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees`
(
    `driver_license_number` varchar(255) DEFAULT NULL,
    `employee_type`         enum ('DRIVER','TICKET_OFFICER') NOT NULL,
    `id`                    bigint(20)                       NOT NULL,
    `operator_id`           bigint(20)   DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`driver_license_number`, `employee_type`, `id`, `operator_id`)
VALUES ('DL123456', 'DRIVER', 4, 1),
       ('DL678901', 'DRIVER', 8, 2);

-- --------------------------------------------------------

--
-- Table structure for table `locations`
--

CREATE TABLE `locations`
(
    `location_id` bigint(20)   NOT NULL,
    `address`     varchar(255) NOT NULL,
    `city`        varchar(255) NOT NULL,
    `latitude`    double DEFAULT NULL,
    `longitude`   double DEFAULT NULL,
    `name`        varchar(255) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `locations`
--

INSERT INTO `locations` (`location_id`, `address`, `city`, `latitude`, `longitude`, `name`)
VALUES (1, 'Bến xe Miền Đông', 'TP.HCM', 10.782, 106.693, 'Bến xe Miền Đông'),
       (2, 'Bến xe Giáp Bát', 'Hà Nội', 20.987, 105.841, 'Bến xe Giáp Bát'),
       (3, 'Bến xe Đà Lạt', 'Đà Lạt', 11.94, 108.437, 'Bến xe Đà Lạt'),
       (4, 'Bến xe Huế', 'Huế', 16.467, 107.595, 'Bến xe Huế'),
       (5, 'Bến xe Cần Thơ', 'Cần Thơ', 10.045, 105.746, 'Bến xe Cần Thơ'),
       (6, 'Bến xe Vũng Tàu', 'Vũng Tàu', 10.346, 107.084, 'Bến xe Vũng Tàu'),
       (7, 'Bến xe Nha Trang', 'Nha Trang', 12.25, 109.194, 'Bến xe Nha Trang');

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments`
(
    `payment_id`       bigint(20)                                       NOT NULL,
    `amount`           decimal(38, 2)                                   NOT NULL,
    `paid_at`          datetime(6)  DEFAULT NULL,
    `payment_method`   varchar(255)                                     NOT NULL,
    `status`           enum ('completed','failed','pending','refunded') NOT NULL,
    `transaction_code` varchar(255) DEFAULT NULL,
    `booking_id`       bigint(20)                                       NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`payment_id`, `amount`, `paid_at`, `payment_method`, `status`, `transaction_code`, `booking_id`)
VALUES (1, 500000.00, '2025-07-24 12:05:00.000000', 'CREDIT_CARD', 'completed', 'TX123456', 1),
       (2, 150000.00, '2025-07-24 13:05:00.000000', 'CREDIT_CARD', 'completed', 'TX123457', 2),
       (3, 250000.00, NULL, 'BANK_TRANSFER', 'pending', 'TX123458', 3),
       (4, 200000.00, '2025-07-24 15:05:00.000000', 'CASH', 'completed', 'TX123459', 4),
       (5, 220000.00, '2025-07-24 16:05:00.000000', 'CREDIT_CARD', 'completed', 'TX123460', 5);

-- --------------------------------------------------------

--
-- Table structure for table `profiles`
--

CREATE TABLE `profiles`
(
    `address`      varchar(255) DEFAULT NULL,
    `created_at`   datetime(6)                            NOT NULL,
    `full_name`    varchar(255)                           NOT NULL,
    `phone_number` varchar(255) DEFAULT NULL,
    `status`       enum ('active','inactive','suspended') NOT NULL,
    `updated_at`   datetime(6)                            NOT NULL,
    `id`           bigint(20)                             NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `profiles`
--

INSERT INTO `profiles` (`address`, `created_at`, `full_name`, `phone_number`, `status`, `updated_at`, `id`)
VALUES ('123 Đường Láng, Hà Nội', '2025-07-24 10:00:00.000000', 'Nguyễn Văn Admin', '0912345678', 'active',
        '2025-07-24 10:00:00.000000', 1),
       ('456 Nguyễn Trãi, TP.HCM', '2025-07-24 10:00:00.000000', 'Trần Thị Khách', '0987654321', 'active',
        '2025-07-24 10:00:00.000000', 2),
       ('789 Lê Lợi, Đà Nẵng', '2025-07-24 10:00:00.000000', 'Lê Văn Nhà Xe', '0901234567', 'active',
        '2025-07-24 10:00:00.000000', 3),
       ('101 Trần Phú, Nha Trang', '2025-07-24 10:00:00.000000', 'Phạm Văn Tài Xế', '0923456789', 'active',
        '2025-07-24 10:00:00.000000', 4),
       ('12 Phạm Ngọc Thạch, Đà Lạt', '2025-07-24 11:00:00.000000', 'Nguyễn Thị Nhà Xe', '0931234567', 'active',
        '2025-07-24 11:00:00.000000', 5),
       ('34 Hai Bà Trưng, Huế', '2025-07-24 11:00:00.000000', 'Lê Văn Khách', '0976543210', 'active',
        '2025-07-24 11:00:00.000000', 6),
       ('56 Nguyễn Huệ, Hà Nội', '2025-07-24 11:00:00.000000', 'Phạm Thị Hành Khách', '0965432109', 'active',
        '2025-07-24 11:00:00.000000', 7),
       ('78 Lê Duẩn, Đà Nẵng', '2025-07-24 11:00:00.000000', 'Trần Văn Tài Xế', '0943210987', 'active',
        '2025-07-24 11:00:00.000000', 8),
       ('90 Lý Thường Kiệt, Cần Thơ', '2025-07-24 11:00:00.000000', 'Hoàng Văn Khách', '0954321098', 'active',
        '2025-07-24 11:00:00.000000', 9);

-- --------------------------------------------------------

--
-- Table structure for table `promotions`
--

CREATE TABLE `promotions`
(
    `promotion_id`   bigint(20)                           NOT NULL,
    `code`           varchar(255)                         NOT NULL,
    `discount_type`  enum ('FIXED_AMOUNT','PERCENTAGE')   NOT NULL,
    `discount_value` decimal(38, 2)                       NOT NULL,
    `end_date`       date                                 NOT NULL,
    `start_date`     date                                 NOT NULL,
    `status`         enum ('active','expired','inactive') NOT NULL,
    `usage_limit`    int(11) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `promotions`
--

INSERT INTO `promotions` (`promotion_id`, `code`, `discount_type`, `discount_value`, `end_date`, `start_date`, `status`,
                          `usage_limit`)
VALUES (1, 'DISCOUNT10', 'PERCENTAGE', 10.00, '2025-08-01', '2025-07-24', 'active', 100);

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews`
(
    `review_id`   bigint(20)  NOT NULL,
    `comment`     varchar(255) DEFAULT NULL,
    `created_at`  datetime(6) NOT NULL,
    `rating`      int(11)     NOT NULL,
    `customer_id` bigint(20)  NOT NULL,
    `trip_id`     bigint(20)  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`review_id`, `comment`, `created_at`, `rating`, `customer_id`, `trip_id`)
VALUES (1, 'Chuyến đi tuyệt vời!', '2025-07-25 21:00:00.000000', 5, 2, 1),
       (2, 'Chuyến đi thoải mái, tài xế thân thiện', '2025-07-25 13:00:00.000000', 4, 6, 2),
       (3, 'Xe sạch sẽ nhưng đến muộn 15 phút', '2025-07-25 17:00:00.000000', 3, 7, 3),
       (4, 'Dịch vụ tốt, sẽ quay lại!', '2025-07-25 16:00:00.000000', 5, 9, 4),
       (5, 'Wifi hơi yếu nhưng tổng thể ổn', '2025-07-25 18:00:00.000000', 4, 6, 5),
       (6, 'Tài xế lái an toàn, rất hài lòng', '2025-07-25 22:00:00.000000', 5, 7, 2),
       (7, 'Ghế ngồi êm, chuyến đi suôn sẻ', '2025-07-25 23:00:00.000000', 4, 9, 5);

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles`
(
    `role_id` int(11)      NOT NULL,
    `name`    varchar(255) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`role_id`, `name`)
VALUES (1, 'ADMIN');

-- --------------------------------------------------------

--
-- Table structure for table `routes`
--

CREATE TABLE `routes`
(
    `route_id`                 bigint(20)   NOT NULL,
    `default_duration_minutes` int(11)        DEFAULT NULL,
    `default_price`            decimal(10, 2) DEFAULT NULL,
    `name`                     varchar(255) NOT NULL,
    `end_location_id`          bigint(20)     DEFAULT NULL,
    `start_location_id`        bigint(20)     DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `routes`
--

INSERT INTO `routes` (`route_id`, `default_duration_minutes`, `default_price`, `name`, `end_location_id`,
                      `start_location_id`)
VALUES (1, 720, 500000.00, 'TP.HCM - Hà Nội', 2, 1),
       (2, 180, 150000.00, 'TP.HCM - Đà Lạt', 3, 1),
       (3, 360, 250000.00, 'Huế - Đà Nẵng', 1, 4),
       (4, 240, 200000.00, 'Cần Thơ - Vũng Tàu', 6, 5),
       (5, 300, 220000.00, 'Nha Trang - TP.HCM', 1, 7),
       (6, 480, 300000.00, 'Hà Nội - Huế', 4, 2);

-- --------------------------------------------------------

--
-- Table structure for table `route_stops`
--

CREATE TABLE `route_stops`
(
    `stop_order`             int(11) DEFAULT NULL,
    `time_offset_from_start` int(11) DEFAULT NULL,
    `location_id`            bigint(20) NOT NULL,
    `route_id`               bigint(20) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `route_stops`
--

INSERT INTO `route_stops` (`stop_order`, `time_offset_from_start`, `location_id`, `route_id`)
VALUES (1, 0, 1, 1),
       (1, 0, 1, 2),
       (2, 360, 1, 3),
       (2, 300, 1, 5),
       (2, 720, 2, 1),
       (1, 0, 2, 6),
       (2, 180, 3, 2),
       (1, 0, 4, 3),
       (2, 480, 4, 6),
       (1, 0, 5, 4),
       (2, 240, 6, 4),
       (1, 0, 7, 5);

-- --------------------------------------------------------

--
-- Table structure for table `seat_layouts`
--

CREATE TABLE `seat_layouts`
(
    `id`          int(11)                                            NOT NULL,
    `layout_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`layout_data`)),
    `name`        varchar(255)                                       NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `seat_layouts`
--

INSERT INTO `seat_layouts` (`id`, `layout_data`, `name`)
VALUES (1, '{\"rows\": 10, \"columns\": 4, \"seats\": [\"A1\", \"A2\", \"B1\", \"B2\"]}', 'Standard 40 seats'),
       (2, '{\"rows\": 8, \"columns\": 4, \"seats\": [\"C1\", \"C2\", \"D1\", \"D2\"]}', 'Standard 32 seats'),
       (3, '{\"rows\": 12, \"columns\": 4, \"seats\": [\"E1\", \"E2\", \"F1\", \"F2\"]}', 'Luxury 48 seats');

-- --------------------------------------------------------

--
-- Table structure for table `spring_session`
--

CREATE TABLE `spring_session`
(
    `PRIMARY_ID`            char(36)   NOT NULL,
    `SESSION_ID`            char(36)   NOT NULL,
    `CREATION_TIME`         bigint(20) NOT NULL,
    `LAST_ACCESS_TIME`      bigint(20) NOT NULL,
    `MAX_INACTIVE_INTERVAL` int(11)    NOT NULL,
    `EXPIRY_TIME`           bigint(20) NOT NULL,
    `PRINCIPAL_NAME`        varchar(100) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- --------------------------------------------------------

--
-- Table structure for table `spring_session_attributes`
--

CREATE TABLE `spring_session_attributes`
(
    `SESSION_PRIMARY_ID` char(36)     NOT NULL,
    `ATTRIBUTE_NAME`     varchar(200) NOT NULL,
    `ATTRIBUTE_BYTES`    blob         NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- --------------------------------------------------------

--
-- Table structure for table `tickets`
--

CREATE TABLE `tickets`
(
    `ticket_id`       int    primary key  auto_increment,
    `passenger_name`  varchar(255)                      NOT NULL,
    `passenger_phone` varchar(255) DEFAULT NULL,
    `price`           decimal(38, 2)                    NOT NULL,
    `status`          enum ('cancelled','used','valid') NOT NULL,
    `ticket_code`     varchar(255)                      NOT NULL,
    `booking_id`      bigint(20)                        NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `tickets`
--

INSERT INTO `tickets` (`passenger_name`, `passenger_phone`, `price`, `status`, `ticket_code`, `booking_id`, `seat_number`)
VALUES ('Trần Thị Khách', '0987654321', 500000.00, 'valid', 'TICKET123',  1, 'B01'),
       ('Lê Văn Khách', '0976543210', 150000.00, 'valid', 'TICKET124',  2,'A05'),
       ('Phạm Thị Hành Khách', '0965432109', 250000.00, 'valid', 'TICKET125',  3, 'B05'),
       ('Hoàng Văn Khách', '0954321098', 200000.00, 'valid', 'TICKET126',  4, 'B10'),
       ('Lê Văn Khách', '0976543210', 220000.00, 'valid', 'TICKET127',  5, 'A17');

-- --------------------------------------------------------

--
-- Table structure for table `trips`
--

CREATE TABLE `trips`
(
    `trip_id`                bigint(20)                                                              NOT NULL,
    `departure_time`         datetime(6)                                                             NOT NULL,
    `estimated_arrival_time` datetime(6) DEFAULT NULL,
    `price_per_seat`         decimal(10, 2)                                                          NOT NULL,
    `status`                 enum ('arrived','cancelled','delayed','departed','on_time','scheduled') NOT NULL,
    `bus_id`                 bigint(20)  DEFAULT NULL,
    `driver_id`              bigint(20)  DEFAULT NULL,
    `route_id`               bigint(20)  DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `trips`
--

INSERT INTO `trips` (`trip_id`, `departure_time`, `estimated_arrival_time`, `price_per_seat`, `status`, `bus_id`,
                     `driver_id`, `route_id`)
VALUES (1, '2025-07-25 08:00:00.000000', '2025-07-25 20:00:00.000000', 500000.00, 'scheduled', 1, 4, 1),
       (2, '2025-07-25 09:00:00.000000', '2025-07-25 12:00:00.000000', 150000.00, 'scheduled', 2, 8, 2),
       (3, '2025-07-25 10:00:00.000000', '2025-07-25 16:00:00.000000', 250000.00, 'scheduled', 3, 8, 3),
       (4, '2025-07-25 11:00:00.000000', '2025-07-25 15:00:00.000000', 200000.00, 'scheduled', 5, 8, 4),
       (5, '2025-07-25 12:00:00.000000', '2025-07-25 17:00:00.000000', 220000.00, 'scheduled', 6, 8, 5),
       (6, '2025-07-25 13:00:00.000000', '2025-07-25 21:00:00.000000', 300000.00, 'scheduled', 2, 8, 6);

-- --------------------------------------------------------

--
-- Table structure for table `trip_seats`
--

CREATE TABLE `trip_seats`
(
    `seat_number`     varchar(255)                         NOT NULL,
    `trip_id`         bigint(20)                           NOT NULL,
    `locked_at`       datetime(6) DEFAULT NULL,
    `status`          enum ('available','booked','locked') NOT NULL,
    `locking_user_id` bigint(20)  DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `trip_seats`
--

INSERT INTO `trip_seats` (`seat_number`, `trip_id`, `locked_at`, `status`, `locking_user_id`)
VALUES ('A1', 1, NULL, 'available', NULL),
       ('A2', 1, NULL, 'available', NULL),
       ('C1', 2, NULL, 'available', NULL),
       ('C1', 4, NULL, 'available', NULL),
       ('C1', 6, NULL, 'available', NULL),
       ('C2', 2, NULL, 'available', NULL),
       ('C2', 4, NULL, 'available', NULL),
       ('C2', 6, NULL, 'available', NULL),
       ('E1', 3, NULL, 'available', NULL),
       ('E1', 5, NULL, 'available', NULL),
       ('E2', 3, NULL, 'available', NULL),
       ('E2', 5, NULL, 'available', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users`
(
    `id`            bigint(20)   NOT NULL,
    `email`         varchar(255) NOT NULL,
    `password_hash` varchar(255) NOT NULL,
    `refresh_token` varchar(255) DEFAULT NULL,
    `role_id`       int(11)      DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `password_hash`, `refresh_token`, `role_id`)
VALUES (2, 'customer1@busify.com', '$2a$10$exampleHash', NULL, NULL),
       (3, 'operator@busify.com', '$2a$10$exampleHash', NULL, NULL),
       (4, 'driver@busify.com', '$2a$10$exampleHash', NULL, NULL),
       (5, 'operator2@busify.com', '$2a$10$exampleHash', NULL, NULL),
       (6, 'customer2@busify.com', '$2a$10$exampleHash', NULL, NULL),
       (7, 'customer3@busify.com', '$2a$10$exampleHash', NULL, NULL),
       (8, 'driver2@busify.com', '$2a$10$exampleHash', NULL, NULL),
       (9, 'customer4@busify.com', '$2a$10$exampleHash', NULL, NULL);

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
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UKq97166k18hklq6ls46osbrftx` (`booking_code`),
    ADD KEY `idx_booking_customerID_createdAt` (`customer_id`, `created_at`),
    ADD KEY `FKegnh9ksa308rvreolxjjgxfbn` (`agent_accept_booking_id`),
    ADD KEY `FK76g5jpvf8bcqejvp5d2vgrnjb` (`trip_id`);

--
-- Indexes for table `booking_promotions`
--
ALTER TABLE `booking_promotions`
    ADD PRIMARY KEY (`booking_id`, `promotion_id`),
    ADD KEY `FK6ad7p0xekh937qe72cfb7dg5w` (`promotion_id`);

--
-- Indexes for table `buses`
--
ALTER TABLE `buses`
    ADD PRIMARY KEY (`id`),
    ADD KEY `idx_buses_licensePlate` (`license_plate`),
    ADD KEY `FKwmt4p1sd2pys4x800ha06ste` (`operator_id`),
    ADD KEY `FK71mdanlbyn55bepxk8j27n3g6` (`seat_layout_id`);

--
-- Indexes for table `bus_operators`
--
ALTER TABLE `bus_operators`
    ADD PRIMARY KEY (`operator_id`),
    ADD KEY `FKhdysn8wxp8hnchty3yuo3nkii` (`owner_id`);

--
-- Indexes for table `complaints`
--
ALTER TABLE `complaints`
    ADD PRIMARY KEY (`complaints_id`),
    ADD KEY `FKdjr8v1k0sie3eioouxacnfjsx` (`assigned_agent_id`),
    ADD KEY `FK9iay3myxtp9bh1vf9iuj2rich` (`booking_id`),
    ADD KEY `FKhtvrekom2uyukk0u8e95np1se` (`customer_id`);

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
    ADD PRIMARY KEY (`id`),
    ADD KEY `FK8yuomapwk6ker3oiqr5uo8axa` (`operator_id`);

--
-- Indexes for table `locations`
--
ALTER TABLE `locations`
    ADD PRIMARY KEY (`location_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
    ADD PRIMARY KEY (`payment_id`),
    ADD UNIQUE KEY `UK8inpv30544qjykcwa6ck7pusy` (`transaction_code`),
    ADD KEY `FKc52o2b1jkxttngufqp3t7jr3h` (`booking_id`);

--
-- Indexes for table `profiles`
--
ALTER TABLE `profiles`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `promotions`
--
ALTER TABLE `promotions`
    ADD PRIMARY KEY (`promotion_id`),
    ADD UNIQUE KEY `UKjdho73ymbyu46p2hh562dk4kk` (`code`);

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
    ADD PRIMARY KEY (`location_id`, `route_id`),
    ADD KEY `FK63y33daxb1qs5nbnkuicbpkej` (`route_id`);

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
    ADD PRIMARY KEY (`SESSION_PRIMARY_ID`, `ATTRIBUTE_NAME`);

--
-- Indexes for table `tickets`
--
ALTER TABLE `tickets`
    ADD UNIQUE KEY `UKcvl4jbu5fln08ltem9rrmtp8w` (`ticket_code`),
    ADD KEY `FKefja4avuu7g29t78mxifrsynb` (`booking_id`);

--
-- Indexes for table `trips`
--
ALTER TABLE `trips`
    ADD PRIMARY KEY (`trip_id`),
    ADD KEY `idx_trips_departureTime_routeId` (`departure_time`, `route_id`),
    ADD KEY `FK2vg7b2xayoq4ogt2kbsot4juq` (`bus_id`),
    ADD KEY `FKevvmqtfyigychrwa7y8c14sht` (`driver_id`),
    ADD KEY `FKm7ci3blm9wj2k0d94chu18y7s` (`route_id`);

--
-- Indexes for table `trip_seats`
--
ALTER TABLE `trip_seats`
    ADD PRIMARY KEY (`seat_number`, `trip_id`),
    ADD KEY `FKiv2sj369u16d4iyoi6wkvnn11` (`locking_user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`id`),
    ADD KEY `idx_users_email` (`email`),
    ADD KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `audit_logs`
--
ALTER TABLE `audit_logs`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 6;

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 6;

--
-- AUTO_INCREMENT for table `buses`
--
ALTER TABLE `buses`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 7;

--
-- AUTO_INCREMENT for table `bus_operators`
--
ALTER TABLE `bus_operators`
    MODIFY `operator_id` bigint(20) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 7;

--
-- AUTO_INCREMENT for table `complaints`
--
ALTER TABLE `complaints`
    MODIFY `complaints_id` bigint(20) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 4;

--
-- AUTO_INCREMENT for table `locations`
--
ALTER TABLE `locations`
    MODIFY `location_id` bigint(20) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 8;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
    MODIFY `payment_id` bigint(20) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 6;

--
-- AUTO_INCREMENT for table `promotions`
--
ALTER TABLE `promotions`
    MODIFY `promotion_id` bigint(20) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 2;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
    MODIFY `review_id` bigint(20) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 8;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
    MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 2;

--
-- AUTO_INCREMENT for table `routes`
--
ALTER TABLE `routes`
    MODIFY `route_id` bigint(20) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 7;

--
-- AUTO_INCREMENT for table `seat_layouts`
--
ALTER TABLE `seat_layouts`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 4;

--
-- AUTO_INCREMENT for table `trips`
--
ALTER TABLE `trips`
    MODIFY `trip_id` bigint(20) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `audit_logs`
--
ALTER TABLE `audit_logs`
    ADD CONSTRAINT `FKjs4iimve3y0xssbtve5ysyef0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `bookings`
--
ALTER TABLE `bookings`
    ADD CONSTRAINT `FK76g5jpvf8bcqejvp5d2vgrnjb` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`trip_id`),
    ADD CONSTRAINT `FKegnh9ksa308rvreolxjjgxfbn` FOREIGN KEY (`agent_accept_booking_id`) REFERENCES `users` (`id`),
    ADD CONSTRAINT `FKib6gjgj2e9binkktxmm175bmm` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `booking_promotions`
--
ALTER TABLE `booking_promotions`
    ADD CONSTRAINT `FK6ad7p0xekh937qe72cfb7dg5w` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`promotion_id`),
    ADD CONSTRAINT `FKkbk18851fr0cvuyjhxamxrwtu` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`);

--
-- Constraints for table `buses`
--
ALTER TABLE `buses`
    ADD CONSTRAINT `FK71mdanlbyn55bepxk8j27n3g6` FOREIGN KEY (`seat_layout_id`) REFERENCES `seat_layouts` (`id`) ON DELETE SET NULL,
    ADD CONSTRAINT `FKwmt4p1sd2pys4x800ha06ste` FOREIGN KEY (`operator_id`) REFERENCES `bus_operators` (`operator_id`) ON DELETE SET NULL;

--
-- Constraints for table `bus_operators`
--
ALTER TABLE `bus_operators`
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
    ADD CONSTRAINT `FK55e5d3sfwkob62wtprm633alk` FOREIGN KEY (`id`) REFERENCES `users` (`id`);

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
    ADD CONSTRAINT `FKe70h9t86py3fbswj0gw16v0by` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`trip_id`),
    ADD CONSTRAINT `FKkquncb1glvrldaui8v52xfd5q` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`);

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
-- Constraints for table `spring_session_attributes`
--
ALTER TABLE `spring_session_attributes`
    ADD CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE;

--
-- Constraints for table `tickets`
--
ALTER TABLE `tickets`
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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
