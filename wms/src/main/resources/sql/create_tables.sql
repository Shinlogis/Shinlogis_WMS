-- [지점 정보 테이블]
CREATE TABLE location (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    location_name VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(100)
);

-- [본사 소속 사용자 테이블]
CREATE TABLE headquarters_user (
    headquarters_user_id INT AUTO_INCREMENT PRIMARY KEY,
    id VARCHAR(100) NOT NULL UNIQUE,
    pw VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- [지점 소속 사용자 테이블]
CREATE TABLE location_user (
    location_user_id INT AUTO_INCREMENT PRIMARY KEY,
    id VARCHAR(100) NOT NULL UNIQUE,
    pw VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    location_id INT NOT NULL,
    FOREIGN KEY (location_id) REFERENCES location(location_id)
);

-- [공급사 정보 테이블]
CREATE TABLE supplier (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(100)
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
    product_code VARCHAR(100) NOT NULL UNIQUE,
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
    processed_date DATETIME,
    status VARCHAR(100) NOT NULL,
    location_id INT NOT NULL,
    CHECK (io_type IN ('IN', 'OUT')),
    CHECK (status IN ('예정', '진행 중', '완료', '보류')),
    FOREIGN KEY (user_id) REFERENCES headquarters_user(headquarters_user_id),
    FOREIGN KEY (location_id) REFERENCES location(location_id)
);

-- [입출고 상세 항목 테이블]
CREATE TABLE io_plan_item (
    io_item_id INT AUTO_INCREMENT PRIMARY KEY,
    io_receipt_id INT NOT NULL,
    planned_quantity INT NOT NULL DEFAULT 0,
    product_snapshot INT NOT NULL,
    damage_code_id INT NOT NULL,
    damage_quantity INT NOT NULL,
    actual_quantity INT NOT NULL,
    CHECK (planned_quantity >= 0),
    CHECK (damage_quantity >= 0),
    CHECK (actual_quantity >= 0),
    FOREIGN KEY (io_receipt_id) REFERENCES io_receipt(io_receipt_id),
    FOREIGN KEY (product_snapshot) REFERENCES snapshot(snapshot_id),
    FOREIGN KEY (damage_code_id) REFERENCES damaged_code(damage_code_id)
);