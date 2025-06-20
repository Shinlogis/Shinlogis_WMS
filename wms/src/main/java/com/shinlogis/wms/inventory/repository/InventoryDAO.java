package com.shinlogis.wms.inventory.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.inventory.model.InventoryDTO;

public class InventoryDAO {
    DBManager dbManager = DBManager.getInstance();

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
        sql.append("group by w.warehouse_code, w.warehouse_name, p.product_code, p.product_name, s.name, i.expiry_date");
        
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
}