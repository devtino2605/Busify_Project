-- Dữ liệu cho bảng audit_logs (Không cần thay đổi)
INSERT INTO `audit_logs` (`id`, `action`, `details`, `target_entity`, `target_id`, `timestamp`, `user_id`)
VALUES (1, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK123\"}', 'BOOKING', 1, '2025-07-24 12:00:00.000000', 2),
       (2, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK124\"}', 'BOOKING', 2, '2025-07-24 13:00:00.000000', 6),
       (3, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK125\"}', 'BOOKING', 3, '2025-07-24 14:00:00.000000', 7),
       (4, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK126\"}', 'BOOKING', 4, '2025-07-24 15:00:00.000000', 9),
       (5, 'CREATE_BOOKING', '{\"booking_code\": \"BOOK127\"}', 'BOOKING', 5, '2025-07-24 16:00:00.000000', 6);

-- Dữ liệu cho bảng bookings (Sửa lại cột `guest_phone_number` thành `guest_phone`)
INSERT INTO `bookings` (`id`, `booking_code`, `created_at`, `guest_address`, `guest_email`, `guest_full_name`,
                        `guest_phone`, `seat_number`, `status`, `total_amount`, `updated_at`, `agent_accept_booking_id`,
                        `customer_id`, `trip_id`)
VALUES (1, 'BOOK123', '2025-07-24 12:00:00.000000', '456 Nguyễn Trãi, TP.HCM', 'customer1@busify.com', 'Trần Thị Khách',
        '0987654321', 'B01', 'confirmed', 500000.00, '2025-07-24 12:00:00.000000', NULL, 2, 1),
       (2, 'BOOK124', '2025-07-24 13:00:00.000000', '34 Hai Bà Trưng, Huế', 'customer2@busify.com', 'Lê Văn Khách',
        '0976543210', 'A05', 'confirmed', 150000.00, '2025-07-24 13:00:00.000000', NULL, 6, 2),
       (3, 'BOOK125', '2025-07-24 14:00:00.000000', '56 Nguyễn Huệ, Hà Nội', 'customer3@busify.com',
        'Phạm Thị Hành Khách', '0965432109', 'B05', 'pending', 250000.00, '2025-07-24 14:00:00.000000', NULL, 7, 3),
       (4, 'BOOK126', '2025-07-24 15:00:00.000000', '90 Lý Thường Kiệt, Cần Thơ', 'customer4@busify.com',
        'Hoàng Văn Khách', '0954321098', 'B10', 'confirmed', 200000.00, '2025-07-24 15:00:00.000000', NULL, 9, 4),
       (5, 'BOOK127', '2025-07-24 16:00:00.000000', '34 Hai Bà Trưng, Huế', 'customer2@busify.com', 'Lê Văn Khách',
        '0976543210', 'A17', 'confirmed', 220000.00, '2025-07-24 16:00:00.000000', NULL, 6, 5);
        
-- Dữ liệu cho bảng booking_promotions (Không cần thay đổi)
INSERT INTO `booking_promotions` (`booking_id`, `promotion_id`)
VALUES (1, 1);

-- Dữ liệu cho bảng buses (Không cần thay đổi)
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

-- Dữ liệu cho bảng bus_operators (Không cần thay đổi)
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

-- Dữ liệu cho bảng complaints (Không cần thay đổi)
INSERT INTO `complaints` (`complaints_id`, `created_at`, `description`, `status`, `title`, `updated_at`,
                          `assigned_agent_id`, `booking_id`, `customer_id`)
VALUES (1, '2025-07-25 22:00:00.000000', 'Ghế không thoải mái', 'New', 'Khiếu nại về ghế', '2025-07-25 22:00:00.000000',
        NULL, 1, 2),
       (2, '2025-07-25 14:00:00.000000', 'Xe đến muộn 30 phút', 'pending', 'Khiếu nại về thời gian',
        '2025-07-25 14:00:00.000000', NULL, 2, 6),
       (3, '2025-07-25 15:00:00.000000', 'Wifi không hoạt động', 'New', 'Khiếu nại về wifi',
        '2025-07-25 15:00:00.000000', NULL, 3, 7);

-- Dữ liệu cho bảng employees (Không cần thay đổi)
INSERT INTO `employees` (`driver_license_number`, `employee_type`, `id`, `operator_id`)
VALUES ('DL123456', 'DRIVER', 4, 1),
       ('DL678901', 'DRIVER', 8, 2);

-- Dữ liệu cho bảng locations (Không cần thay đổi)
INSERT INTO `locations` (`location_id`, `address`, `city`, `latitude`, `longitude`, `name`)
VALUES (1, 'Bến xe Miền Đông', 'TP.HCM', 10.782, 106.693, 'Bến xe Miền Đông'),
       (2, 'Bến xe Giáp Bát', 'Hà Nội', 20.987, 105.841, 'Bến xe Giáp Bát'),
       (3, 'Bến xe Đà Lạt', 'Đà Lạt', 11.94, 108.437, 'Bến xe Đà Lạt'),
       (4, 'Bến xe Huế', 'Huế', 16.467, 107.595, 'Bến xe Huế'),
       (5, 'Bến xe Cần Thơ', 'Cần Thơ', 10.045, 105.746, 'Bến xe Cần Thơ'),
       (6, 'Bến xe Vũng Tàu', 'Vũng Tàu', 10.346, 107.084, 'Bến xe Vũng Tàu'),
       (7, 'Bến xe Nha Trang', 'Nha Trang', 12.25, 109.194, 'Bến xe Nha Trang');

-- Dữ liệu cho bảng seat_layouts (Loại bỏ bản ghi trùng lặp)
INSERT INTO `seat_layouts` (`id`, `layout_data`, `name`)
VALUES (1, '{\"rows\": 10, \"columns\": 4, \"seats\": [\"A1\", \"A2\", \"B1\", \"B2\"]}', 'Standard 40 seats'),
       (2, '{\"rows\": 8, \"columns\": 4, \"seats\": [\"C1\", \"C2\", \"D1\", \"D2\"]}', 'Standard 32 seats'),
       (3, '{\"rows\": 12, \"columns\": 4, \"seats\": [\"E1\", \"E2\", \"F1\", \"F2\"]}', 'Luxury 48 seats');

-- Dữ liệu cho bảng payments (Thêm cột `payment_gateway_id`)
INSERT INTO `payments` (`payment_id`, `amount`, `paid_at`, `payment_gateway_id`, `payment_method`, `status`, `transaction_code`, `booking_id`)
VALUES (1, 500000.00, '2025-07-24 12:05:00.000000', NULL, 'CREDIT_CARD', 'completed', 'TX123456', 1),
       (2, 150000.00, '2025-07-24 13:05:00.000000', NULL, 'CREDIT_CARD', 'completed', 'TX123457', 2),
       (3, 250000.00, NULL,                         NULL, 'BANK_TRANSFER', 'pending', 'TX123458', 3),
       (4, 200000.00, '2025-07-24 15:05:00.000000', NULL, 'CASH', 'completed', 'TX123459', 4),
       (5, 220000.00, '2025-07-24 16:05:00.000000', NULL, 'CREDIT_CARD', 'completed', 'TX123460', 5);

-- Dữ liệu cho bảng profiles (Không cần thay đổi)
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

-- Dữ liệu cho bảng promotions (Không cần thay đổi)
INSERT INTO `promotions` (`promotion_id`, `code`, `discount_type`, `discount_value`, `end_date`, `start_date`, `status`,
                          `usage_limit`)
VALUES (1, 'DISCOUNT10', 'PERCENTAGE', 10.00, '2025-08-01', '2025-07-24', 'active', 100);

-- Dữ liệu cho bảng reviews (Không cần thay đổi)
INSERT INTO `reviews` (`review_id`, `comment`, `created_at`, `rating`, `customer_id`, `trip_id`)
VALUES (1, 'Chuyến đi tuyệt vời!', '2025-07-25 21:00:00.000000', 5, 2, 1),
       (2, 'Chuyến đi thoải mái, tài xế thân thiện', '2025-07-25 13:00:00.000000', 4, 6, 2),
       (3, 'Xe sạch sẽ nhưng đến muộn 15 phút', '2025-07-25 17:00:00.000000', 3, 7, 3),
       (4, 'Dịch vụ tốt, sẽ quay lại!', '2025-07-25 16:00:00.000000', 5, 9, 4),
       (5, 'Wifi hơi yếu nhưng tổng thể ổn', '2025-07-25 18:00:00.000000', 4, 6, 5),
       (6, 'Tài xế lái an toàn, rất hài lòng', '2025-07-25 22:00:00.000000', 5, 7, 2),
       (7, 'Ghế ngồi êm, chuyến đi suôn sẻ', '2025-07-25 23:00:00.000000', 4, 9, 5);

-- Dữ liệu cho bảng routes (Không cần thay đổi)
INSERT INTO `routes` (`route_id`, `default_duration_minutes`, `default_price`, `name`, `end_location_id`,
                      `start_location_id`)
VALUES (1, 720, 500000.00, 'TP.HCM - Hà Nội', 2, 1),
       (2, 180, 150000.00, 'TP.HCM - Đà Lạt', 3, 1),
       (3, 360, 250000.00, 'Huế - Đà Nẵng', 1, 4),
       (4, 240, 200000.00, 'Cần Thơ - Vũng Tàu', 6, 5),
       (5, 300, 220000.00, 'Nha Trang - TP.HCM', 1, 7),
       (6, 480, 300000.00, 'Hà Nội - Huế', 4, 2);

-- Dữ liệu cho bảng route_stops (Không cần thay đổi)
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

-- Dữ liệu cho bảng tickets (Loại bỏ bản ghi trùng lặp)
INSERT INTO `tickets` (`passenger_name`, `passenger_phone`, `price`, `status`, `ticket_code`, `booking_id`, `seat_number`)
VALUES ('Trần Thị Khách', '0987654321', 500000.00, 'valid', 'TICKET123',  1, 'B01'),
       ('Lê Văn Khách', '0976543210', 150000.00, 'valid', 'TICKET124',  2,'A05'),
       ('Phạm Thị Hành Khách', '0965432109', 250000.00, 'valid', 'TICKET125',  3, 'B05'),
       ('Hoàng Văn Khách', '0954321098', 200000.00, 'valid', 'TICKET126',  4, 'B10'),
       ('Lê Văn Khách', '0976543210', 220000.00, 'valid', 'TICKET127',  5, 'A17');

-- Dữ liệu cho bảng trips (Không cần thay đổi)
INSERT INTO `trips` (`trip_id`, `departure_time`, `estimated_arrival_time`, `price_per_seat`, `status`, `bus_id`,
                     `driver_id`, `route_id`)
VALUES (1, '2025-07-25 08:00:00.000000', '2025-07-25 20:00:00.000000', 500000.00, 'scheduled', 1, 4, 1),
       (2, '2025-07-25 09:00:00.000000', '2025-07-25 12:00:00.000000', 150000.00, 'scheduled', 2, 8, 2),
       (3, '2025-07-25 10:00:00.000000', '2025-07-25 16:00:00.000000', 250000.00, 'scheduled', 3, 8, 3),
       (4, '2025-07-25 11:00:00.000000', '2025-07-25 15:00:00.000000', 200000.00, 'scheduled', 5, 8, 4),
       (5, '2025-07-25 12:00:00.000000', '2025-07-25 17:00:00.000000', 220000.00, 'scheduled', 6, 8, 5),
       (6, '2025-07-25 13:00:00.000000', '2025-07-25 21:00:00.000000', 300000.00, 'scheduled', 2, 8, 6);

-- Dữ liệu cho bảng trip_seats (Không cần thay đổi)
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
