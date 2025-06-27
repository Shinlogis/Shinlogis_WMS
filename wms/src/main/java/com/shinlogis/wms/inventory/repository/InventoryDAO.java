package com.shinlogis.wms.inventory.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.inventory.model.InventoryDTO;

public class InventoryDAO {
	DBManager dbManager = DBManager.getInstance();

<<<<<<< Updated upstream
	public List<InventoryDTO> selectInventoryDetails() {
		List<InventoryDTO> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		sql.append("select w.warehouse_code, w.warehouse_name, p.product_code, p.product_name, ");
		sql.append("s.name aS supplier_name, i.expiry_date, sum(i.quantity) as total_quantity ");
		sql.append("from inventory i ");
		sql.append("join warehouse w on i.warehouse_id = w.warehouse_id ");
		sql.append("join product p on i.product_id = p.product_id ");
		sql.append("join supplier s on p.supplier_id = s.supplier_id ");
		sql.append(
				"group by w.warehouse_code, w.warehouse_name, p.product_code, p.product_name, s.name, i.expiry_date");

		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				InventoryDTO dto = new InventoryDTO();
				dto.setWarehouseCode(rs.getString("warehouse_code"));
				dto.setWarehouseName(rs.getString("warehouse_name"));
				dto.setProductCode(rs.getString("product_code"));
				dto.setProductName(rs.getString("product_name"));
				dto.setSupplierName(rs.getString("supplier_name"));
				dto.setExpiryDate(rs.getDate("expiry_date"));
				dto.setTotalQuantity(rs.getInt("total_quantity"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return list;
	}

	// TODO 입고처리된 새로운 상품을 재고에 저장
	public int addToInventory() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO inventory (quantity, warehouse_id, expiry_date, product_id VALUES (?, ?, NOW(), ?)");

		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
//			pstmt.setInt(1, );
		} catch (SQLException e) {
			e.printStackTrace();
		}

//		result = pstmt.executeUpdate();

		return 0;
	}

	// TODO 창고에 재고로 존재하는 상품은, 기존의 재고를 업데이트하는 식으로 재고에 저장
}
=======
    // 조회: 중복 항목 병합 (inventory_id 제외)
    public List<InventoryDTO> selectInventoryDetails(InventoryDTO inventoryDTO) {
        List<InventoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT MIN(i.inventory_id) AS inventory_id, ");
        sql.append("w.warehouse_code, w.warehouse_name, ");
        sql.append("p.product_code, p.product_name, ");
        sql.append("s.name AS supplier_name, i.expiry_date, ");
        sql.append("SUM(i.quantity) AS total_quantity ");
        sql.append("FROM inventory i ");
        sql.append("JOIN warehouse w ON i.warehouse_id = w.warehouse_id ");
        sql.append("JOIN product p ON i.product_id = p.product_id ");
        sql.append("JOIN supplier s ON p.supplier_id = s.supplier_id ");
        sql.append("WHERE 1=1 ");

        if (inventoryDTO.getWarehouseCode() != null && !inventoryDTO.getWarehouseCode().isEmpty()) {
            sql.append("AND w.warehouse_code = ? ");
        }
        if (inventoryDTO.getProductCode() != null && !inventoryDTO.getProductCode().isEmpty()) {
            sql.append("AND p.product_code = ? ");
        }
        if (inventoryDTO.getWarehouseName() != null && !inventoryDTO.getWarehouseName().isEmpty()) {
            sql.append("AND w.warehouse_name LIKE ? ");
        }
        if (inventoryDTO.getProductName() != null && !inventoryDTO.getProductName().isEmpty()) {
            sql.append("AND p.product_name LIKE ? ");
        }
        if (inventoryDTO.getSupplierName() != null && !inventoryDTO.getSupplierName().isEmpty()) {
            sql.append("AND s.name LIKE ? ");
        }
        if (inventoryDTO.getExpiryDate() != null) {
            sql.append("AND i.expiry_date = ? ");
        }

        sql.append("GROUP BY w.warehouse_code, w.warehouse_name, ");
        sql.append("p.product_code, p.product_name, s.name, i.expiry_date");

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql.toString());

            int idx = 1;
            if (inventoryDTO.getWarehouseCode() != null && !inventoryDTO.getWarehouseCode().isEmpty()) {
                pstmt.setString(idx++, inventoryDTO.getWarehouseCode());
            }
            if (inventoryDTO.getProductCode() != null && !inventoryDTO.getProductCode().isEmpty()) {
                pstmt.setString(idx++, inventoryDTO.getProductCode());
            }
            if (inventoryDTO.getWarehouseName() != null && !inventoryDTO.getWarehouseName().isEmpty()) {
                pstmt.setString(idx++, "%" + inventoryDTO.getWarehouseName() + "%");
            }
            if (inventoryDTO.getProductName() != null && !inventoryDTO.getProductName().isEmpty()) {
                pstmt.setString(idx++, "%" + inventoryDTO.getProductName() + "%");
            }
            if (inventoryDTO.getSupplierName() != null && !inventoryDTO.getSupplierName().isEmpty()) {
                pstmt.setString(idx++, "%" + inventoryDTO.getSupplierName() + "%");
            }
            if (inventoryDTO.getExpiryDate() != null) {
                pstmt.setDate(idx++, inventoryDTO.getExpiryDate());
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                InventoryDTO dto = new InventoryDTO();
                dto.setInventoryId(rs.getInt("inventory_id"));  // 반드시 세팅
                dto.setWarehouseCode(rs.getString("warehouse_code"));
                dto.setWarehouseName(rs.getString("warehouse_name"));
                dto.setProductCode(rs.getString("product_code"));
                dto.setProductName(rs.getString("product_name"));
                dto.setSupplierName(rs.getString("supplier_name"));
                dto.setExpiryDate(rs.getDate("expiry_date"));
                dto.setTotalQuantity(rs.getInt("total_quantity"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }

    // 병합 기준 재고 수정: 같은 항목 모두 수량 및 유통기한 변경
    public boolean updateMergedInventory(String warehouseCode, String productCode, Date oldExpiryDate, Date newExpiryDate, int newQuantity) {
        Connection conn = null;
        PreparedStatement deletePstmt = null;
        PreparedStatement insertPstmt = null;

        String deleteSql = "DELETE FROM inventory "
                         + "WHERE warehouse_id = (SELECT warehouse_id FROM warehouse WHERE warehouse_code = ?) "
                         + "AND product_id = (SELECT product_id FROM product WHERE product_code = ?) "
                         + "AND expiry_date = ?";

        String insertSql = "INSERT INTO inventory (warehouse_id, product_id, expiry_date, quantity) "
                         + "VALUES ((SELECT warehouse_id FROM warehouse WHERE warehouse_code = ?), "
                         + "        (SELECT product_id FROM product WHERE product_code = ?), ?, ?)";

        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 1. 기존 행 삭제
            deletePstmt = conn.prepareStatement(deleteSql);
            deletePstmt.setString(1, warehouseCode);
            deletePstmt.setString(2, productCode);
            deletePstmt.setDate(3, oldExpiryDate);
            deletePstmt.executeUpdate();

            // 2. 새 병합 행 삽입
            insertPstmt = conn.prepareStatement(insertSql);
            insertPstmt.setString(1, warehouseCode);
            insertPstmt.setString(2, productCode);
            insertPstmt.setDate(3, newExpiryDate);
            insertPstmt.setInt(4, newQuantity);
            insertPstmt.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            dbManager.release(deletePstmt, null);
            dbManager.release(insertPstmt, null);
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
    public boolean deleteInventory(int inventoryId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM inventory WHERE inventory_id = ?";

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, inventoryId);

            int affectedRows = pstmt.executeUpdate();
            System.out.println("deleteInventory - affectedRows: " + affectedRows);
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbManager.release(pstmt, null);
        }
    }

    // 병합 기준 재고 삭제
    public boolean deleteInventoryMerged(InventoryDTO dto) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM inventory "
                   + "WHERE warehouse_id = (SELECT warehouse_id FROM warehouse WHERE warehouse_code = ?) "
                   + "AND product_id = (SELECT product_id FROM product WHERE product_code = ?) "
                   + "AND expiry_date = ?";

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getWarehouseCode());
            pstmt.setString(2, dto.getProductCode());
            pstmt.setDate(3, dto.getExpiryDate());

            int affectedRows = pstmt.executeUpdate();
            System.out.println("deleteInventoryMerged - affectedRows: " + affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbManager.release(pstmt, null);
        }
    }
}
>>>>>>> Stashed changes
