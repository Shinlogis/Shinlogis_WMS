use wms;
set sql_safe_updates = 0;
SET FOREIGN_KEY_CHECKS = 0;
 DROP TABLE IF EXISTS `damaged_code`;               
 DROP TABLE IF EXISTS `headquarters_user`;          
 DROP TABLE IF EXISTS `inventory`;                  
 DROP TABLE IF EXISTS `io_detail`;                    
 DROP TABLE IF EXISTS `io_receipt`;               
 DROP TABLE IF EXISTS `location`;                  
 DROP TABLE IF EXISTS `location_user`;              
 DROP TABLE IF EXISTS `product`;                    
 DROP TABLE IF EXISTS `snapshot`;                   
 DROP TABLE IF EXISTS `storage_type`;              
 DROP TABLE IF EXISTS `supplier`;                   
 DROP TABLE IF EXISTS `warehouse`;
 DROP TABLE IF EXISTS `store_order`;
 DROP TABLE IF EXISTS `store_order_item`;
SET FOREIGN_KEY_CHECKS = 1;

-- [지점 정보 테이블]
CREATE TABLE location (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    location_name VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(100),
    status VARCHAR(10) NOT NULL DEFAULT '활성',
    CHECK (status IN ('활성', '탈퇴'))
);

-- [본사 소속 사용자 테이블]
CREATE TABLE headquarters_user (
    headquarters_user_id INT AUTO_INCREMENT PRIMARY KEY,
    id VARCHAR(100) NOT NULL UNIQUE,
    pw VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE, 
    status VARCHAR(10) NOT NULL DEFAULT '활성',
    CHECK (status IN ('활성', '탈퇴'))
    
);

-- [지점 소속 사용자 테이블]
CREATE TABLE location_user (
    location_user_id INT AUTO_INCREMENT PRIMARY KEY,
    id VARCHAR(100) NOT NULL UNIQUE,
    pw VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    location_id INT NOT NULL,
    FOREIGN KEY (location_id) REFERENCES location(location_id),
    status VARCHAR(10) NOT NULL DEFAULT '활성',
    CHECK (status IN ('활성', '탈퇴'))
);

-- [공급사 정보 테이블]
CREATE TABLE supplier (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(100),
    status VARCHAR(10) NOT NULL DEFAULT '활성',
    CHECK (status IN ('활성', '비활성'))
);

-- [보관 유형 테이블]
CREATE TABLE storage_type (
    storage_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_code VARCHAR(100) NOT NULL,
    type_name VARCHAR(100) NOT NULL
);

-- [상품 정보 테이블]
CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_img VARCHAR(100),
    product_code VARCHAR(100) NOT NULL UNIQUE,
    product_name VARCHAR(100) NOT NULL,
    storage_type_id INT NOT NULL,
    supplier_id INT NOT NULL,
    price INT NOT NULL,
    CHECK (price >= 0),
    FOREIGN KEY (storage_type_id) REFERENCES storage_type(storage_type_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id)
);


-- [창고 테이블]
CREATE TABLE warehouse (
    warehouse_id INT  AUTO_INCREMENT PRIMARY KEY,
    warehouse_name VARCHAR(100),
    address VARCHAR(100),
    storage_type_id INT NOT NULL,
    warehouse_code VARCHAR(100) NOT NULL UNIQUE,
    FOREIGN KEY (storage_type_id) REFERENCES storage_type(storage_type_id)
);

-- [재고 테이블]
CREATE TABLE inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    quantity INT NOT NULL,
    warehouse_id INT NOT NULL,
    expiry_date DATETIME,
    product_id INT NOT NULL,
    CHECK (quantity >= 0),
    FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- [상품 스냅샷 테이블]
CREATE TABLE snapshot (
    snapshot_id INT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(100) NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    storage_type_code VARCHAR(100) NOT NULL,
    supplier_name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    expiry_date DATETIME,
    CHECK (price >= 0)
);

-- [파손코드 테이블]
CREATE TABLE damaged_code (
    damage_code_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL
);


-- [입출고 전표 테이블]
CREATE TABLE io_receipt (
    io_receipt_id INT AUTO_INCREMENT PRIMARY KEY,
    io_type VARCHAR(20) NOT NULL,
    user_id INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    scheduled_date DATETIME NOT NULL,
    status VARCHAR(100) NOT NULL DEFAULT '예정',
    processed_date DATETIME,
    location_id INT,
    active BOOLEAN DEFAULT TRUE,
    CHECK (io_type IN ('IN', 'OUT')),
    FOREIGN KEY (user_id) REFERENCES headquarters_user(headquarters_user_id),
    FOREIGN KEY (location_id) REFERENCES location(location_id)
);

-- [입출고 상세 항목 테이블]
CREATE TABLE io_detail (
    io_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    io_receipt_id INT NOT NULL,
    planned_quantity INT NOT NULL DEFAULT 0,
    snapshot_id INT NOT NULL,
    damage_code_id INT,
    damage_quantity INT,
    actual_quantity INT NOT NULL,
    processed_date DATETIME,
    warehouse_id INT,
    headquarters_user_id INT NULL,
    active BOOLEAN DEFAULT TRUE,
    status VARCHAR(100) NOT NULL DEFAULT '예정',
    CHECK (planned_quantity >= 0),
    CHECK (damage_quantity >= 0),
    CHECK (actual_quantity >= 0),
    CHECK (status IN ('예정', '진행 중', '완료', '보류')),
    FOREIGN KEY (io_receipt_id) REFERENCES io_receipt(io_receipt_id),
    FOREIGN KEY (snapshot_id) REFERENCES snapshot(snapshot_id),
    FOREIGN KEY (damage_code_id) REFERENCES damaged_code(damage_code_id),
     foreign key (headquarters_user_id) references headquarters_user(headquarters_user_id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id)
);


-- [주문서 테이블]
CREATE TABLE store_order (
    store_order_id INT AUTO_INCREMENT PRIMARY KEY,
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    location_id INT NOT NULL,
    total_price int not null,
   status varchar(10) not null default '대기' check (status in ('대기', '완료')),
    
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES location(location_id)
);

-- [주문서 상세 테이블]
CREATE TABLE store_order_item (
    item_id INT AUTO_INCREMENT PRIMARY KEY, -- 상세 ID (PK)
    store_order_id INT NOT NULL,            -- 주문 ID (FK)
    product_id INT NOT NULL,                -- 상품 ID (FK)
   status varchar(10) not null default '대기' check(status in ('대기', '완료')),

    quantity INT CHECK(quantity > 0),                           -- 수량



    CONSTRAINT fk_store_order FOREIGN KEY (store_order_id) REFERENCES store_order(store_order_id),

    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(product_id)
);

INSERT INTO location (location_id, location_name, address) VALUES (1, '강남점', '서울시 강남구 1번지');

INSERT INTO location (location_id, location_name, address) VALUES (2, '마포점', '서울시 마포구 2번지');

INSERT INTO location (location_id, location_name, address) VALUES (3, '김포점', '김포시 3번지');

INSERT INTO location (location_id, location_name, address) VALUES (4, '시흥점', '시흥시 4번지');

INSERT INTO location (location_id, location_name, address) VALUES (5, '부평점', '부평시 5번지');

INSERT INTO location (location_id, location_name, address) VALUES (6, '구로점', '서울시 구로구 6번지');

INSERT INTO location (location_id, location_name, address) VALUES (7, '부천점', '부천시 7번지');

INSERT INTO location (location_id, location_name, address) VALUES (8, '양천점', '서울시 양천구 8번지');

INSERT INTO location (location_id, location_name, address) VALUES (9, '노원점', '서울시 노원구 9번지');

INSERT INTO location (location_id, location_name, address) VALUES (10, '강서점', '서울시 강서구 10번지');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (1, 'hquser1', 'pw1234', 'hquser1@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (2, 'hquser2', 'pw1234', 'hquser2@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (3, 'hquser3', 'pw1234', 'hquser3@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (4, 'hquser4', 'pw1234', 'hquser4@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (5, 'hquser5', 'pw1234', 'hquser5@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (6, 'hquser6', 'pw1234', 'hquser6@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (7, 'hquser7', 'pw1234', 'hquser7@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (8, 'hquser8', 'pw1234', 'hquser8@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (9, 'hquser9', 'pw1234', 'hquser9@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (10, 'hquser10', 'pw1234', 'hquser10@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (11, 'hquser11', 'pw1234', 'hquser11@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (12, 'hquser12', 'pw1234', 'hquser12@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (13, 'hquser13', 'pw1234', 'hquser13@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (14, 'hquser14', 'pw1234', 'hquser14@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (15, 'hquser15', 'pw1234', 'hquser15@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (16, 'hquser16', 'pw1234', 'hquser16@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (17, 'hquser17', 'pw1234', 'hquser17@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (18, 'hquser18', 'pw1234', 'hquser18@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (19, 'hquser19', 'pw1234', 'hquser19@example.com');

INSERT INTO headquarters_user (headquarters_user_id, id, pw, email) VALUES (20, 'hquser20', 'pw1234', 'hquser20@example.com');

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (1, 'user1', 'pw1234', 'user1@example.com', 2);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (2, 'user2', 'pw1234', 'user2@example.com', 3);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (3, 'user3', 'pw1234', 'user3@example.com', 4);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (4, 'user4', 'pw1234', 'user4@example.com', 5);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (5, 'user5', 'pw1234', 'user5@example.com', 6);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (6, 'user6', 'pw1234', 'user6@example.com', 7);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (7, 'user7', 'pw1234', 'user7@example.com', 8);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (8, 'user8', 'pw1234', 'user8@example.com', 9);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (9, 'user9', 'pw1234', 'user9@example.com', 10);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (10, 'user10', 'pw1234', 'user10@example.com', 1);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (11, 'user11', 'pw1234', 'user11@example.com', 2);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (12, 'user12', 'pw1234', 'user12@example.com', 3);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (13, 'user13', 'pw1234', 'user13@example.com', 4);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (14, 'user14', 'pw1234', 'user14@example.com', 5);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (15, 'user15', 'pw1234', 'user15@example.com', 6);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (16, 'user16', 'pw1234', 'user16@example.com', 7);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (17, 'user17', 'pw1234', 'user17@example.com', 8);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (18, 'user18', 'pw1234', 'user18@example.com', 9);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (19, 'user19', 'pw1234', 'user19@example.com', 10);

INSERT INTO location_user (location_user_id, id, pw, email, location_id) VALUES (20, 'user20', 'pw1234', 'user20@example.com', 1);

INSERT INTO supplier (supplier_id, name, address) VALUES (1, '맛나제과', '경기도 성남시 분당구 1길');

INSERT INTO supplier (supplier_id, name, address) VALUES (2, '푸드킹', '경기도 성남시 분당구 2길');

INSERT INTO supplier (supplier_id, name, address) VALUES (3, '오렌지수산', '경기도 성남시 분당구 3길');

INSERT INTO supplier (supplier_id, name, address) VALUES (4, '해피축산', '경기도 성남시 분당구 4길');

INSERT INTO supplier (supplier_id, name, address) VALUES (5, '청과직송', '경기도 성남시 분당구 5길');

INSERT INTO supplier (supplier_id, name, address) VALUES (6, '건강푸드', '경기도 성남시 분당구 6길');

INSERT INTO supplier (supplier_id, name, address) VALUES (7, '농장직송', '경기도 성남시 분당구 7길');

INSERT INTO supplier (supplier_id, name, address) VALUES (8, '웰빙식품', '경기도 성남시 분당구 8길');

INSERT INTO supplier (supplier_id, name, address) VALUES (9, '수산왕', '경기도 성남시 분당구 9길');

INSERT INTO supplier (supplier_id, name, address) VALUES (10, '바다찬물산', '경기도 성남시 분당구 10길');

INSERT INTO storage_type (storage_type_id, type_code, type_name) VALUES (1, 'ROOM', '상온');

INSERT INTO storage_type (storage_type_id, type_code, type_name) VALUES (2, 'COLD', '냉장');

INSERT INTO storage_type (storage_type_id, type_code, type_name) VALUES (3, 'FROZEN', '냉동');

INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (1, 'images/tomato.png', 'P001', '토마토', 1, 2, 4108);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (2, 'images/orange.png', 'P002', '오렌지', 1, 3, 5879);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (3, 'images/tuna.png', 'P003', '참치캔', 1, 4, 3200);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (4, 'images/chickenbreast.png', 'P004', '닭가슴살캔', 1, 5, 3500);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (5, 'images/potato.png', 'P005', '감자', 1, 6, 6010);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (6, 'images/onion.png', 'P006', '양파', 1, 7, 4546);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (7, 'images/napacabbage.png', 'P007', '배추', 1, 8, 7674);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (8, 'images/carrot.png', 'P008', '당근', 1, 9, 4426);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (9, 'images/ramen.png', 'P009', '라면', 1, 10, 15800);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (10, 'images/rice.png', 'P010', '쌀', 1, 1, 32000);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (11, 'images/canned_corn.png', 'P011', '옥수수캔', 1, 2, 2100);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (12, 'images/cereal.png', 'P012', '시리얼', 1, 3, 4900);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (13, 'images/soy_sauce.png', 'P013', '간장', 1, 4, 5200);

INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (14, 'images/spinach.png', 'P014', '시금치', 2, 6, 2500);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (15, 'images/strawberry.png', 'P015', '딸기', 2, 7, 5200);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (16, 'images/lettuce.png', 'P016', '상추', 2, 8, 2100);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (17, 'images/yogurt.png', 'P017', '요거트', 2, 9, 3800);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (18, 'images/ham.png', 'P018', '햄', 2, 10, 4300);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (19, 'images/cheese.png', 'P019', '치즈', 2, 1, 4900);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (20, 'images/chilled_chickenbreast.png', 'P020', '닭가슴살', 2, 2, 7500);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (21, 'images/bellpepper.png', 'P021', '파프리카', 2, 3, 3300);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (22, 'images/bokchoy.png', 'P022', '청경채', 2, 4, 2900);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (23, 'images/cucumber.png', 'P023', '오이', 2, 5, 2600);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (24, 'images/kale.png', 'P024', '케일', 2, 6, 3100);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (25, 'images/pear.png', 'P025', '배', 2, 7, 4700);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (26, 'images/blueberry.png', 'P026', '블루베리', 2, 8, 5800);


INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (27, 'images/frozen_dumplings.png', 'P027', '냉동만두', 3, 2, 6800);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (28, 'images/frozen_pizza.png', 'P028', '냉동피자', 3, 3, 8900);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (29, 'images/icecream.png', 'P029', '아이스크림', 3, 4, 1200);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (30, 'images/frozen_chicken.png', 'P030', '냉동닭다리', 3, 5, 9700);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (31, 'images/frozen_porkbelly.png', 'P031', '냉동삼겹살', 3, 6, 15500);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (32, 'images/frozen_seafoodmix.png', 'P032', '냉동해물믹스', 3, 7, 7200);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (33, 'images/frozen_blueberry.png', 'P033', '냉동블루베리', 3, 8, 5600);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (34, 'images/frozen_friedrice.png', 'P034', '냉동볶음밥', 3, 9, 4500);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (35, 'images/frozen_frenchfries.png', 'P035', '냉동감자튀김', 3, 10, 3900);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (36, 'images/frozen_tteokbokki.png', 'P036', '냉동떡볶이', 3, 1, 4200);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (37, 'images/frozen_udon.png', 'P037', '냉동우동', 3, 2, 3100);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (38, 'images/frozen_squid.png', 'P038', '냉동오징어', 3, 3, 6300);
INSERT INTO product (product_id, product_img, product_code, product_name, storage_type_id, supplier_id, price) VALUES (39, 'images/frozen_croissant.png', 'P039', '냉동크로와상', 3, 4, 5700);


INSERT INTO warehouse (warehouse_id, warehouse_name, address, storage_type_id, warehouse_code) VALUES (1, '인천1창고', '인천시 남동구 1번지', 2, 'W001');

INSERT INTO warehouse (warehouse_id, warehouse_name, address, storage_type_id, warehouse_code) VALUES (2, '서울2창고', '인천시 남동구 2번지', 3, 'W002');

INSERT INTO warehouse (warehouse_id, warehouse_name, address, storage_type_id, warehouse_code) VALUES (3, '부산3창고', '인천시 남동구 3번지', 1, 'W003');

INSERT INTO warehouse (warehouse_id, warehouse_name, address, storage_type_id, warehouse_code) VALUES (4, '대전4창고', '인천시 남동구 4번지', 2, 'W004');

INSERT INTO warehouse (warehouse_id, warehouse_name, address, storage_type_id, warehouse_code) VALUES (5, '광주5창고', '인천시 남동구 5번지', 3, 'W005');

INSERT INTO warehouse (warehouse_id, warehouse_name, address, storage_type_id, warehouse_code) VALUES (6, '인천6창고', '인천시 남동구 6번지', 1, 'W006');

INSERT INTO warehouse (warehouse_id, warehouse_name, address, storage_type_id, warehouse_code) VALUES (7, '서울7창고', '인천시 남동구 7번지', 2, 'W007');

INSERT INTO warehouse (warehouse_id, warehouse_name, address, storage_type_id, warehouse_code) VALUES (8, '부산8창고', '인천시 남동구 8번지', 3, 'W008');

INSERT INTO warehouse (warehouse_id, warehouse_name, address, storage_type_id, warehouse_code) VALUES (9, '대전9창고', '인천시 남동구 9번지', 1, 'W009');

INSERT INTO warehouse (warehouse_id, warehouse_name, address, storage_type_id, warehouse_code) VALUES (10, '광주10창고', '인천시 남동구 10번지', 2, 'W010');

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (1, 32, 4, '2025-12-22 00:00:00', 6);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (2, 25, 9, '2025-12-01 00:00:00', 8);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (3, 43, 7, '2025-12-10 00:00:00', 2);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (4, 11, 3, '2025-12-09 00:00:00', 8);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (5, 69, 4, '2025-12-05 00:00:00', 2);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (6, 29, 4, '2025-12-10 00:00:00', 3);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (7, 64, 4, '2025-12-28 00:00:00', 8);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (8, 13, 5, '2025-12-11 00:00:00', 1);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (9, 39, 6, '2025-12-15 00:00:00', 5);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (10, 90, 3, '2025-12-19 00:00:00', 4);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (11, 95, 8, '2025-12-22 00:00:00', 1);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (12, 58, 1, '2025-12-15 00:00:00', 3);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (13, 36, 4, '2025-12-03 00:00:00', 8);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (14, 61, 4, '2025-12-03 00:00:00', 1);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (15, 14, 4, '2025-12-12 00:00:00', 8);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (16, 93, 8, '2025-12-09 00:00:00', 10);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (17, 29, 4, '2025-12-10 00:00:00', 10);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (18, 22, 3, '2025-12-21 00:00:00', 10);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (19, 45, 6, '2025-12-17 00:00:00', 2);

INSERT INTO inventory (inventory_id, quantity, warehouse_id, expiry_date, product_id) VALUES (20, 88, 6, '2025-12-18 00:00:00', 10);

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (1, 'P001', '오렌지', 'COLD', '푸드킹', 6499, '2025-12-25 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (2, 'P002', '참치', 'FROZEN', '오렌지수산', 9339, '2025-12-22 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (3, 'P003', '닭가슴살', 'ROOM', '해피축산', 6057, '2025-12-25 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (4, 'P004', '감자', 'COLD', '청과직송', 1475, '2025-12-22 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (5, 'P005', '양파', 'FROZEN', '건강푸드', 4920, '2025-12-18 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (6, 'P006', '배추', 'ROOM', '농장직송', 8596, '2025-12-28 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (7, 'P007', '당근', 'COLD', '웰빙식품', 5915, '2025-12-10 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (8, 'P008', '연어', 'FROZEN', '수산왕', 2604, '2025-12-01 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (9, 'P009', '소고기', 'ROOM', '바다찬물산', 7269, '2025-12-07 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (10, 'P010', '토마토', 'COLD', '맛나제과', 3042, '2025-12-15 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (11, 'P011', '오렌지', 'FROZEN', '푸드킹', 4883, '2025-12-12 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (12, 'P012', '참치', 'ROOM', '오렌지수산', 1426, '2025-12-25 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (13, 'P013', '닭가슴살', 'COLD', '해피축산', 7222, '2025-12-21 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (14, 'P014', '감자', 'FROZEN', '청과직송', 4716, '2025-12-08 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (15, 'P015', '양파', 'ROOM', '건강푸드', 7882, '2025-12-24 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (16, 'P016', '배추', 'COLD', '농장직송', 7661, '2025-12-16 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (17, 'P017', '당근', 'FROZEN', '웰빙식품', 8224, '2025-12-10 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (18, 'P018', '연어', 'ROOM', '수산왕', 2067, '2025-12-13 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (19, 'P019', '소고기', 'COLD', '바다찬물산', 9651, '2025-12-09 00:00:00');

INSERT INTO snapshot (snapshot_id, product_code, product_name, storage_type_code, supplier_name, price, expiry_date) VALUES (20, 'P020', '토마토', 'FROZEN', '맛나제과', 4560, '2025-12-09 00:00:00');

INSERT INTO damaged_code (damage_code_id, code, name) VALUES (1, 'D000', '정상');

INSERT INTO damaged_code (damage_code_id, code, name) VALUES (2, 'D001', '포장불량');

INSERT INTO damaged_code (damage_code_id, code, name) VALUES (3, 'D002', '파손');

INSERT INTO damaged_code (damage_code_id, code, name) VALUES (4, 'D003', '누수');

INSERT INTO damaged_code (damage_code_id, code, name) VALUES (5, 'D004', '기타');

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (1, 'OUT', 2, '2025-06-20 01:00:00', '2025-06-22 00:00:00', '예정', NULL, 2);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (2, 'IN', 3, '2025-06-20 02:00:00', '2025-06-23 00:00:00', '예정', NULL, 3);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (3, 'OUT', 4, '2025-06-20 03:00:00', '2025-06-24 00:00:00', '예정', NULL, 4);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (4, 'IN', 5, '2025-06-20 04:00:00', '2025-06-25 00:00:00', '예정', '2025-06-25 12:00:00', 5);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (5, 'OUT', 6, '2025-06-20 05:00:00', '2025-06-26 00:00:00', '예정', NULL, 6);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (6, 'IN', 7, '2025-06-20 06:00:00', '2025-06-27 00:00:00', '예정', NULL, 7);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (7, 'OUT', 8, '2025-06-20 07:00:00', '2025-06-28 00:00:00', '예정', NULL, 8);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (8, 'IN', 9, '2025-06-20 08:00:00', '2025-06-29 00:00:00', '예정', '2025-06-29 12:00:00', 9);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (9, 'OUT', 10, '2025-06-20 09:00:00', '2025-06-21 00:00:00', '예정', NULL, 10);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (10, 'IN', 11, '2025-06-20 00:00:00', '2025-06-21 00:00:00', '예정', NULL, 1);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (11, 'OUT', 12, '2025-06-20 01:00:00', '2025-06-22 00:00:00', '예정', NULL, 2);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (12, 'IN', 13, '2025-06-20 02:00:00', '2025-06-23 00:00:00', '예정', '2025-06-23 12:00:00', 3);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (13, 'OUT', 14, '2025-06-20 03:00:00', '2025-06-24 00:00:00', '예정', NULL, 4);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (14, 'IN', 15, '2025-06-20 04:00:00', '2025-06-25 00:00:00', '예정', NULL, 5);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (15, 'OUT', 16, '2025-06-20 05:00:00', '2025-06-26 00:00:00', '예정', NULL, 6);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (16, 'IN', 17, '2025-06-20 06:00:00', '2025-06-27 00:00:00', '예정', '2025-06-27 12:00:00', 7);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (17, 'OUT', 18, '2025-06-20 07:00:00', '2025-06-28 00:00:00', '예정', NULL, 8);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (18, 'IN', 19, '2025-06-20 08:00:00', '2025-06-29 00:00:00', '예정', NULL, 9);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (19, 'OUT', 20, '2025-06-20 09:00:00', '2025-06-21 00:00:00', '예정', NULL, 10);

INSERT INTO io_receipt (io_receipt_id, io_type, user_id, created_at, scheduled_date, status, processed_date, location_id) VALUES (20, 'IN', 1, '2025-06-20 00:00:00', '2025-06-21 00:00:00', '예정', '2025-06-21 12:00:00', 1);

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity,  processed_date, status) VALUES ( 1, 88, 14, 2, 0, 16, NULL, '보류');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity,  processed_date, status) VALUES ( 1, 79, 5, 3, 1, 75,  '2025-06-22 12:00:00', '완료');



INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 2, 20, 1, 2, 1, 37, 16, '2025-06-23 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 2, 14, 1, 1, 0, 83, 5, NULL, '예정');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 3, 60, 9, 1, 1, 92, 15, NULL, '보류');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES (3, 78, 5, 5, 4, 30, 2, NULL, '보류');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES (4, 54, 4, 2, 4, 13, 4, NULL, '보류');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES (4, 84, 19, 1, 1, 64, 1, '2025-06-25 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES (5, 98, 1, 5, 4, 61, 5, NULL, '보류');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 5, 84, 9, 5, 2, 41, 17, '2025-06-26 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 6, 97, 12, 2, 4, 82, 4, '2025-06-27 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 6, 39, 18, 3, 5, 77, 4, NULL, '보류');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 7, 43, 15, 3, 1, 67, 12, NULL, '보류');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 7, 58, 11, 1, 0, 12, 3, '2025-06-28 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 8, 76, 2, 1, 0, 62, 4, '2025-06-29 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 8, 60, 10, 5, 5, 23, 13, NULL, '진행 중');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 9, 10, 12, 2, 1, 90, 16, NULL, '진행 중');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 9, 42, 10, 1, 1, 7, 4, NULL, '진행 중');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 10, 22, 9, 5, 5, 63, 3, '2025-06-21 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 10, 55, 3, 1, 5, 79, 12, NULL, '예정');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 11, 84, 17, 5, 1, 70, 3, NULL, '예정');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 11, 35, 8, 4, 2, 40, 7, '2025-06-22 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 12, 31, 20, 4, 2, 59, 16, NULL, '진행 중');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 12, 60, 9, 1, 1, 99, 19, '2025-06-23 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 13, 81, 4, 5, 0, 76, 7, '2025-06-24 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 13, 50, 12, 4, 1, 13, 10, NULL, '예정');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 14, 99, 4, 5, 4, 81, 12, '2025-06-25 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 14, 71, 4, 2, 0, 13, 2, NULL, '예정');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 15, 71, 15, 3, 0, 18, 3, '2025-06-26 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 15, 47, 20, 2, 4, 85, 19, '2025-06-26 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 16, 81, 3, 5, 5, 72, 7, NULL, '예정');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 16, 81, 12, 2, 3, 23, 18, '2025-06-27 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 17, 23, 6, 3, 2, 18, 15, NULL, '예정');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 17, 36, 3, 5, 3, 78, 17, '2025-06-28 12:00:00', '완료');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 18, 12, 14, 1, 2, 26, 6, NULL, '진행 중');

INSERT INTO io_detail ( io_receipt_id, planned_quantity, snapshot_id, damage_code_id, damage_quantity, actual_quantity, headquarters_user_id, processed_date, status) VALUES ( 18, 13, 1, 1, 0, 24, 9, NULL, '진행 중');

INSERT INTO store_order (order_date, location_id, total_price, status) VALUES
(NOW(), 1, 50000, '대기'),
(NOW(), 2, 75000, '완료'),
(NOW(), 3, 25000, '대기'),
(NOW(), 1, 120000, '완료'),
(NOW(), 4, 95000, '대기'),
(NOW(), 2, 31000, '완료'),
(NOW(), 5, 42000, '대기'),
(NOW(), 3, 67000, '완료'),
(NOW(), 4, 88000, '대기'),
(NOW(), 1, 55000, '완료');

INSERT INTO store_order_item (store_order_id, product_id, status, quantity) VALUES
(1, 1, '대기', 2),
(1, 4, '대기', 1),
(2, 7, '완료', 3),
(3, 10, '대기', 2),
(4, 2, '완료', 4),
(5, 8, '대기', 1),
(6, 5, '완료', 2),
(7, 3, '대기', 2),
(8, 9, '완료', 3),
(9, 6, '대기', 2);



-- [io_receipt 상태 계산]
-- 진행 중이 하나라도 있으면 '진행 중'
UPDATE io_receipt
SET status = '진행 중'
WHERE io_receipt_id IN (
    SELECT io_receipt_id
    FROM io_detail
    WHERE status = '진행 중'
    GROUP BY io_receipt_id
);

-- 모두 완료면 '완료'
UPDATE io_receipt
SET status = '완료'
WHERE io_receipt_id IN (
    SELECT io_receipt_id
    FROM io_detail
    GROUP BY io_receipt_id
    HAVING SUM(CASE WHEN status != '완료' THEN 1 ELSE 0 END) = 0
);

-- 모두 예정이면 '예정'
UPDATE io_receipt
SET status = '예정'
WHERE io_receipt_id IN (
    SELECT io_receipt_id
    FROM io_detail
    GROUP BY io_receipt_id
    HAVING SUM(CASE WHEN status != '예정' THEN 1 ELSE 0 END) = 0
);

-- [입고상세에 창고 연결]
-- ROOM 저장창고 연결 (예: warehouse_id = 3)
UPDATE io_detail d
JOIN snapshot s ON d.snapshot_id = s.snapshot_id
SET d.warehouse_id = 3
WHERE s.storage_type_code = 'ROOM';

-- COLD 저장창고 연결 (예: warehouse_id = 1)
UPDATE io_detail d
JOIN snapshot s ON d.snapshot_id = s.snapshot_id
SET d.warehouse_id = 1
WHERE s.storage_type_code = 'COLD';

-- FROZEN 저장창고 연결 (예: warehouse_id = 2)
UPDATE io_detail d
JOIN snapshot s ON d.snapshot_id = s.snapshot_id
SET d.warehouse_id = 2
WHERE s.storage_type_code = 'FROZEN';