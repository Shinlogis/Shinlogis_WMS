INSERT INTO location (location_name, address) VALUES
('지점1', '서울시 강남구 1번지');
INSERT INTO location (location_name, address) VALUES
('지점2', '서울시 강남구 2번지');
INSERT INTO location (location_name, address) VALUES
('지점3', '서울시 강남구 3번지');
INSERT INTO location (location_name, address) VALUES
('지점4', '서울시 강남구 4번지');
INSERT INTO location (location_name, address) VALUES
('지점5', '서울시 강남구 5번지');
INSERT INTO location (location_name, address) VALUES
('지점6', '서울시 강남구 6번지');
INSERT INTO location (location_name, address) VALUES
('지점7', '서울시 강남구 7번지');
INSERT INTO location (location_name, address) VALUES
('지점8', '서울시 강남구 8번지');
INSERT INTO location (location_name, address) VALUES
('지점9', '서울시 강남구 9번지');
INSERT INTO location (location_name, address) VALUES
('지점10', '서울시 강남구 10번지');
INSERT INTO headquarters_user (id, pw, email) VALUES
('hquser1', 'pw1234', 'hquser1@example.com');
INSERT INTO headquarters_user (id, pw, email) VALUES
('hquser2', 'pw1234', 'hquser2@example.com');
INSERT INTO headquarters_user (id, pw, email) VALUES
('hquser3', 'pw1234', 'hquser3@example.com');
INSERT INTO headquarters_user (id, pw, email) VALUES
('hquser4', 'pw1234', 'hquser4@example.com');
INSERT INTO headquarters_user (id, pw, email) VALUES
('hquser5', 'pw1234', 'hquser5@example.com');
INSERT INTO headquarters_user (id, pw, email) VALUES
('hquser6', 'pw1234', 'hquser6@example.com');
INSERT INTO headquarters_user (id, pw, email) VALUES
('hquser7', 'pw1234', 'hquser7@example.com');
INSERT INTO headquarters_user (id, pw, email) VALUES
('hquser8', 'pw1234', 'hquser8@example.com');
INSERT INTO headquarters_user (id, pw, email) VALUES
('hquser9', 'pw1234', 'hquser9@example.com');
INSERT INTO headquarters_user (id, pw, email) VALUES
('hquser10', 'pw1234', 'hquser10@example.com');
INSERT INTO location (location_user_id, id, pw, email) VALUES
(1, 'lcuser1', 'pw1234', 'lcuser1@example.com');
INSERT INTO location (location_user_id, id, pw, email) VALUES
(2, 'lcuser2', 'pw1234', 'lcuser2@example.com');
INSERT INTO location (location_user_id, id, pw, email) VALUES
(3, 'lcuser3', 'pw1234', 'lcuser3@example.com');
INSERT INTO location (location_user_id, id, pw, email) VALUES
(4, 'lcuser4', 'pw1234', 'lcuser4@example.com');
INSERT INTO location (location_user_id, id, pw, email) VALUES
(5, 'lcuser5', 'pw1234', 'lcuser5@example.com');
INSERT INTO location (location_user_id, id, pw, email) VALUES
(6, 'lcuser6', 'pw1234', 'lcuser6@example.com');
INSERT INTO location (location_user_id, id, pw, email) VALUES
(7, 'lcuser7', 'pw1234', 'lcuser7@example.com');
INSERT INTO location (location_user_id, id, pw, email) VALUES
(8, 'lcuser8', 'pw1234', 'lcuser8@example.com');
INSERT INTO location (location_user_id, id, pw, email) VALUES
(9, 'lcuser9', 'pw1234', 'lcuser9@example.com');
INSERT INTO location (location_user_id, id, pw, email) VALUES
(10, 'lcuser10', 'pw1234', 'lcuser10@example.com');
INSERT INTO supplier (name, address) VALUES
('공급사1', '경기도 성남시 분당구 1길');
INSERT INTO supplier (name, address) VALUES
('공급사2', '경기도 성남시 분당구 2길');
INSERT INTO supplier (name, address) VALUES
('공급사3', '경기도 성남시 분당구 3길');
INSERT INTO supplier (name, address) VALUES
('공급사4', '경기도 성남시 분당구 4길');
INSERT INTO supplier (name, address) VALUES
('공급사5', '경기도 성남시 분당구 5길');
INSERT INTO supplier (name, address) VALUES
('공급사6', '경기도 성남시 분당구 6길');
INSERT INTO supplier (name, address) VALUES
('공급사7', '경기도 성남시 분당구 7길');
INSERT INTO supplier (name, address) VALUES
('공급사8', '경기도 성남시 분당구 8길');
INSERT INTO supplier (name, address) VALUES
('공급사9', '경기도 성남시 분당구 9길');
INSERT INTO supplier (name, address) VALUES
('공급사10', '경기도 성남시 분당구 10길');
INSERT INTO storage_type (type_code, type_name) VALUES
('COLD', '냉장');
INSERT INTO storage_type (type_code, type_name) VALUES
('FROZEN', '냉동');
INSERT INTO storage_type (type_code, type_name) VALUES
('ROOM', '상온');
INSERT INTO product (product_code, product_name, storage_type_id, supplier_id, price) VALUES
('P001', '상품1', 1, 3, 3776);
INSERT INTO product (product_code, product_name, storage_type_id, supplier_id, price) VALUES
('P002', '상품2', 2, 1, 5770);
INSERT INTO product (product_code, product_name, storage_type_id, supplier_id, price) VALUES
('P003', '상품3', 2, 2, 9774);
INSERT INTO product (product_code, product_name, storage_type_id, supplier_id, price) VALUES
('P004', '상품4', 2, 10, 8687);
INSERT INTO product (product_code, product_name, storage_type_id, supplier_id, price) VALUES
('P005', '상품5', 3, 9, 1119);
INSERT INTO product (product_code, product_name, storage_type_id, supplier_id, price) VALUES
('P006', '상품6', 3, 4, 4681);
INSERT INTO product (product_code, product_name, storage_type_id, supplier_id, price) VALUES
('P007', '상품7', 2, 7, 4199);
INSERT INTO product (product_code, product_name, storage_type_id, supplier_id, price) VALUES
('P008', '상품8', 3, 2, 5872);
INSERT INTO product (product_code, product_name, storage_type_id, supplier_id, price) VALUES
('P009', '상품9', 2, 9, 3716);
INSERT INTO product (product_code, product_name, storage_type_id, supplier_id, price) VALUES
('P010', '상품10', 2, 10, 1966);
INSERT INTO warehouse (warehouse_name, address, storage_type_id, warehouse_code) VALUES
('창고1', '인천시 남동구 1번지', 2, 'W001');
INSERT INTO warehouse (warehouse_name, address, storage_type_id, warehouse_code) VALUES
('창고2', '인천시 남동구 2번지', 1, 'W002');
INSERT INTO warehouse (warehouse_name, address, storage_type_id, warehouse_code) VALUES
('창고3', '인천시 남동구 3번지', 1, 'W003');
INSERT INTO warehouse (warehouse_name, address, storage_type_id, warehouse_code) VALUES
('창고4', '인천시 남동구 4번지', 2, 'W004');
INSERT INTO warehouse (warehouse_name, address, storage_type_id, warehouse_code) VALUES
('창고5', '인천시 남동구 5번지', 1, 'W005');
INSERT INTO warehouse (warehouse_name, address, storage_type_id, warehouse_code) VALUES
('창고6', '인천시 남동구 6번지', 3, 'W006');
INSERT INTO warehouse (warehouse_name, address, storage_type_id, warehouse_code) VALUES
('창고7', '인천시 남동구 7번지', 3, 'W007');
INSERT INTO warehouse (warehouse_name, address, storage_type_id, warehouse_code) VALUES
('창고8', '인천시 남동구 8번지', 2, 'W008');
INSERT INTO warehouse (warehouse_name, address, storage_type_id, warehouse_code) VALUES
('창고9', '인천시 남동구 9번지', 1, 'W009');
INSERT INTO warehouse (warehouse_name, address, storage_type_id, warehouse_code) VALUES
('창고10', '인천시 남동구 10번지', 2, 'W010');
INSERT INTO inventory (quantity, warehouse_id, expiry_date, product_id) VALUES
(334, 1, '2025-11-13 00:00:00', 6);
INSERT INTO inventory (quantity, warehouse_id, expiry_date, product_id) VALUES
(395, 10, '2025-08-31 00:00:00', 4);
INSERT INTO inventory (quantity, warehouse_id, expiry_date, product_id) VALUES
(461, 7, '2025-11-12 00:00:00', 1);
INSERT INTO inventory (quantity, warehouse_id, expiry_date, product_id) VALUES
(334, 10, '2025-07-31 00:00:00', 1);
INSERT INTO inventory (quantity, warehouse_id, expiry_date, product_id) VALUES
(473, 9, '2025-10-12 00:00:00', 10);
INSERT INTO inventory (quantity, warehouse_id, expiry_date, product_id) VALUES
(22, 4, '2025-10-01 00:00:00', 7);
INSERT INTO inventory (quantity, warehouse_id, expiry_date, product_id) VALUES
(11, 8, '2025-12-14 00:00:00', 3);
INSERT INTO inventory (quantity, warehouse_id, expiry_date, product_id) VALUES
(334, 9, '2025-07-19 00:00:00', 3);
INSERT INTO inventory (quantity, warehouse_id, expiry_date, product_id) VALUES
(56, 3, '2025-10-25 00:00:00', 10);
INSERT INTO inventory (quantity, warehouse_id, expiry_date, product_id) VALUES
(44, 3, '2025-10-19 00:00:00', 5);
INSERT INTO snapshot (product_code, product_name, price, expiry_date) VALUES
('P001', '상품1', 4236, '2025-12-27 00:00:00');
INSERT INTO snapshot (product_code, product_name, price, expiry_date) VALUES
('P002', '상품2', 2730, '2025-12-22 00:00:00');
INSERT INTO snapshot (product_code, product_name, price, expiry_date) VALUES
('P003', '상품3', 3457, '2025-08-31 00:00:00');
INSERT INTO snapshot (product_code, product_name, price, expiry_date) VALUES
('P004', '상품4', 1295, '2025-09-15 00:00:00');
INSERT INTO snapshot (product_code, product_name, price, expiry_date) VALUES
('P005', '상품5', 7660, '2025-09-22 00:00:00');
INSERT INTO snapshot (product_code, product_name, price, expiry_date) VALUES
('P006', '상품6', 8779, '2025-08-30 00:00:00');
INSERT INTO snapshot (product_code, product_name, price, expiry_date) VALUES
('P007', '상품7', 5085, '2025-10-07 00:00:00');
INSERT INTO snapshot (product_code, product_name, price, expiry_date) VALUES
('P008', '상품8', 2677, '2025-12-03 00:00:00');
INSERT INTO snapshot (product_code, product_name, price, expiry_date) VALUES
('P009', '상품9', 3593, '2025-08-17 00:00:00');
INSERT INTO snapshot (product_code, product_name, price, expiry_date) VALUES
('P010', '상품10', 7919, '2025-11-10 00:00:00');
INSERT INTO damaged_code (code, name) VALUES
('D000', '정상');
INSERT INTO damaged_code (code, name) VALUES
('D001', '라벨 파손');
INSERT INTO damaged_code (code, name) VALUES
('D002', '포장 파손');
INSERT INTO damaged_code (code, name) VALUES
('D003', '내용물 누수');
INSERT INTO damaged_code (code, name) VALUES
('D004', '유통기한 임박');
INSERT INTO damaged_code (code, name) VALUES
('D005', '상품 오염');
INSERT INTO damaged_code (code, name) VALUES
('D006', '기타');
INSERT INTO io_receipt (io_type, user_id, created_at, scheduled_date, processed_date, status, location_id) VALUES
('OUT', 9, '2025-06-19 00:36:48', '2025-06-24 00:36:48', NULL, '보류', 7);
INSERT INTO io_receipt (io_type, user_id, created_at, scheduled_date, processed_date, status, location_id) VALUES
('IN', 6, '2025-06-19 00:36:48', '2025-06-20 00:36:48', NULL, '보류', 2);
INSERT INTO io_receipt (io_type, user_id, created_at, scheduled_date, processed_date, status, location_id) VALUES
('IN', 10, '2025-06-19 00:36:48', '2025-06-21 00:36:48', NULL, '진행 중', 3);
INSERT INTO io_receipt (io_type, user_id, created_at, scheduled_date, processed_date, status, location_id) VALUES
('OUT', 6, '2025-06-19 00:36:48', '2025-06-20 00:36:48', NULL, '진행 중', 2);
INSERT INTO io_receipt (io_type, user_id, created_at, scheduled_date, processed_date, status, location_id) VALUES
('IN', 5, '2025-06-19 00:36:48', '2025-06-20 00:36:48', NULL, '진행 중', 7);
INSERT INTO io_receipt (io_type, user_id, created_at, scheduled_date, processed_date, status, location_id) VALUES
('OUT', 8, '2025-06-19 00:36:48', '2025-06-23 00:36:48', NULL, '예정', 3);
INSERT INTO io_receipt (io_type, user_id, created_at, scheduled_date, processed_date, status, location_id) VALUES
('IN', 10, '2025-06-19 00:36:48', '2025-06-21 00:36:48', NULL, '예정', 10);
INSERT INTO io_receipt (io_type, user_id, created_at, scheduled_date, processed_date, status, location_id) VALUES
('OUT', 9, '2025-06-19 00:36:48', '2025-06-21 00:36:48', NULL, '완료', 7);
INSERT INTO io_receipt (io_type, user_id, created_at, scheduled_date, processed_date, status, location_id) VALUES
('IN', 7, '2025-06-19 00:36:48', '2025-06-24 00:36:48', NULL, '예정', 2);
INSERT INTO io_receipt (io_type, user_id, created_at, scheduled_date, processed_date, status, location_id) VALUES
('IN', 6, '2025-06-19 00:36:48', '2025-06-24 00:36:48', NULL, '완료', 4);

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date)
VALUES (1, 'P001', '상품1', 'ROOM', '공급사1', 7225, '2025-11-30 00:00:00');
INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date)
VALUES (2, 'P002', '상품2', 'ROOM', '공급사2', 4790, '2025-10-07 00:00:00');
INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date)
VALUES (3, 'P003', '상품3', 'COLD', '공급사3', 3696, '2025-07-25 00:00:00');
INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date)
VALUES (4, 'P004', '상품4', 'COLD', '공급사4', 9356, '2025-11-23 00:00:00');
INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date)
VALUES (5, 'P005', '상품5', 'COLD', '공급사5', 2565, '2025-11-24 00:00:00');
INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date)
VALUES (6, 'P006', '상품6', 'FROZEN', '공급사6', 8896, '2025-08-12 00:00:00');
INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date)
VALUES (7, 'P007', '상품7', 'COLD', '공급사7', 1734, '2025-10-26 00:00:00');
INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date)
VALUES (8, 'P008', '상품8', 'COLD', '공급사8', 7250, '2025-09-22 00:00:00');
INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date)
VALUES (9, 'P009', '상품9', 'COLD', '공급사9', 4975, '2025-11-21 00:00:00');
INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date)
VALUES (10, 'P010', '상품10', 'ROOM', '공급사10', 7355, '2025-11-16 00:00:00');

INSERT INTO io_plan_item (io_receipt_id, planned_quantity, product_snapshot, damage_code_id, damage_quantity, actual_quantity)
VALUES (3, 71, 1, 4, 3, 20);
INSERT INTO io_plan_item (io_receipt_id, planned_quantity, product_snapshot, damage_code_id, damage_quantity, actual_quantity)
VALUES (3, 20, 2, 3, 0, 54);
INSERT INTO io_plan_item (io_receipt_id, planned_quantity, product_snapshot, damage_code_id, damage_quantity, actual_quantity)
VALUES (4, 82, 3, 4, 4, 60);
INSERT INTO io_plan_item (io_receipt_id, planned_quantity, product_snapshot, damage_code_id, damage_quantity, actual_quantity)
VALUES (6, 59, 4, 2, 2, 47);
INSERT INTO io_plan_item (io_receipt_id, planned_quantity, product_snapshot, damage_code_id, damage_quantity, actual_quantity)
VALUES (9, 58, 5, 2, 0, 77);
INSERT INTO io_plan_item (io_receipt_id, planned_quantity, product_snapshot, damage_code_id, damage_quantity, actual_quantity)
VALUES (10, 69, 6, 3, 1, 82);
INSERT INTO io_plan_item (io_receipt_id, planned_quantity, product_snapshot, damage_code_id, damage_quantity, actual_quantity)
VALUES (7, 26, 7, 3, 1, 53);
INSERT INTO io_plan_item (io_receipt_id, planned_quantity, product_snapshot, damage_code_id, damage_quantity, actual_quantity)
VALUES (1, 82, 8, 4, 0, 84);
INSERT INTO io_plan_item (io_receipt_id, planned_quantity, product_snapshot, damage_code_id, damage_quantity, actual_quantity)
VALUES (8, 28, 9, 3, 0, 79);
INSERT INTO io_plan_item (io_receipt_id, planned_quantity, product_snapshot, damage_code_id, damage_quantity, actual_quantity)
VALUES (4, 47, 10, 5, 0, 41);
