package com.shinlogis.wms.warehouse.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.storageType.model.StorageType;
import com.shinlogis.wms.warehouse.model.Warehouse;

public class WarehouseDAO {
	DBManager dbManager = DBManager.getInstance();

	/**
	 * 모든 창고를 조회하는 메서드
	 * 
	 * @author 김예진
	 * @since 2025-06-24
	 * @return
	 */
	public List<Warehouse> selectAll() {
		List<Warehouse> list = new ArrayList<>();

		String sql = "SELECT warehouse_id, warehouse_name, address, storage_type_id, warehouse_code FROM warehouse";

		try (Connection conn = dbManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Warehouse wh = new Warehouse();
				wh.setWarehouseId(rs.getInt("warehouse_id"));
				wh.setWarehouseName(rs.getString("warehouse_name"));
				wh.setAddress(rs.getString("address"));
				wh.setWarehouseCode(rs.getString("warehouse_code"));

				StorageType storageType = new StorageType();
				storageType.setStorageTypeId(rs.getInt("storage_type_id"));
				storageType.setTypeCode(rs.getString("type_name"));
				storageType.setTypeName(rs.getString("type_code"));
				wh.setStorageType(storageType);


				list.add(wh);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 창고코드로 창고를 조회하는 메서드
	 * 
	 * @author 김예진
	 * @since 2025-06-24
	 * @return
	 */
	public List<Warehouse> selectByCode(String code) {
		List<Warehouse> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select warehouse_id, warehouse_code, warehouse_name, address"
				   + "	, st.storage_type_id, type_name, type_code "
				   + "from warehouse w "
				   + "join storage_type st "
				   + "on w.storage_type_id  = st.storage_type_id "
		           + "where warehouse_code = ?";

		try {
			conn = dbManager.getConnection();

			ps = conn.prepareStatement(sql);
			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				Warehouse wh = new Warehouse();
				wh.setWarehouseId(rs.getInt("warehouse_id"));
				wh.setWarehouseName(rs.getString("warehouse_name"));
				wh.setAddress(rs.getString("address"));

				StorageType storageType = new StorageType();
				storageType.setStorageTypeId(rs.getInt("storage_type_id"));
				storageType.setTypeCode(rs.getString("type_code"));
				storageType.setTypeName(rs.getString("type_name"));
				wh.setStorageType(storageType);

				list.add(wh);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	/**
	 * 검색조건에 따라 창고 목록을 조회하는 메서드
	 * 
	 * @author 김지민
	 * @since 2025-06-26
	 * @return
	 */
	
	public List<Warehouse> selectWarehouseList(String code, String name, String location) {
	    List<Warehouse> list = new ArrayList<>();

	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT w.warehouse_id, w.warehouse_code, w.warehouse_name, w.address, ");
	    sql.append("st.storage_type_id, st.type_name, st.type_code ");
	    sql.append("FROM warehouse w ");
	    sql.append("JOIN storage_type st ON w.storage_type_id = st.storage_type_id ");
	    sql.append("WHERE 1=1 ");

	    if (code != null && !code.isEmpty()) {
	        sql.append("AND w.warehouse_code LIKE ? ");
	    }
	    if (name != null && !name.isEmpty()) {
	        sql.append("AND w.warehouse_name LIKE ? ");
	    }
	    if (location != null && !location.isEmpty()) {
	        sql.append("AND w.address LIKE ? ");
	    }
	    
	    sql.append("ORDER BY w.warehouse_code ");
	    
	    try (Connection conn = dbManager.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

	        int idx = 1;
	        if (code != null && !code.isEmpty()) {
	            ps.setString(idx++, "%" + code + "%");
	        }
	        if (name != null && !name.isEmpty()) {
	            ps.setString(idx++, "%" + name + "%");
	        }
	        if (location != null && !location.isEmpty()) {
	            ps.setString(idx++, "%" + location + "%");
	        }

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Warehouse wh = new Warehouse();
	                wh.setWarehouseId(rs.getInt("warehouse_id"));
	                wh.setWarehouseCode(rs.getString("warehouse_code"));
	                wh.setWarehouseName(rs.getString("warehouse_name"));
	                wh.setAddress(rs.getString("address"));

	                StorageType st = new StorageType();
	                st.setStorageTypeId(rs.getInt("storage_type_id"));
	                st.setTypeName(rs.getString("type_name"));
	                st.setTypeCode(rs.getString("type_code"));
	                wh.setStorageType(st);

	                list.add(wh);
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	public int deleteWarehousesByCodes(List<String> codes) {
	    Connection conn = null;
	    PreparedStatement pstmtSelect = null;
	    PreparedStatement pstmtDeleteInventory = null;
	    PreparedStatement pstmtDeleteIoDetail = null;
	    PreparedStatement pstmtDeleteWarehouse = null;
	    ResultSet rs = null;
	    int totalDeleted = 0;

	    try {
	        conn = dbManager.getConnection();
	        conn.setAutoCommit(false);

	        // 1) warehouse_code -> warehouse_id 조회
	        StringBuilder selectSql = new StringBuilder("SELECT warehouse_id FROM warehouse WHERE warehouse_code IN (");
	        for (int i = 0; i < codes.size(); i++) {
	            selectSql.append("?");
	            if (i != codes.size() - 1) {
	                selectSql.append(",");
	            }
	        }
	        selectSql.append(")");

	        pstmtSelect = conn.prepareStatement(selectSql.toString());
	        for (int i = 0; i < codes.size(); i++) {
	            pstmtSelect.setString(i + 1, codes.get(i));
	        }
	        rs = pstmtSelect.executeQuery();

	        List<Integer> warehouseIds = new ArrayList<>();
	        while (rs.next()) {
	            warehouseIds.add(rs.getInt("warehouse_id"));
	        }

	        if (warehouseIds.isEmpty()) {
	            conn.rollback();
	            return 0;
	        }

	        // 2) inventory 삭제
	        StringBuilder deleteInventorySql = new StringBuilder("DELETE FROM inventory WHERE warehouse_id IN (");
	        for (int i = 0; i < warehouseIds.size(); i++) {
	            deleteInventorySql.append("?");
	            if (i != warehouseIds.size() - 1) {
	                deleteInventorySql.append(",");
	            }
	        }
	        deleteInventorySql.append(")");

	        pstmtDeleteInventory = conn.prepareStatement(deleteInventorySql.toString());
	        for (int i = 0; i < warehouseIds.size(); i++) {
	            pstmtDeleteInventory.setInt(i + 1, warehouseIds.get(i));
	        }
	        pstmtDeleteInventory.executeUpdate();

	        // 3) io_detail 삭제 추가
	        StringBuilder deleteIoDetailSql = new StringBuilder("DELETE FROM io_detail WHERE warehouse_id IN (");
	        for (int i = 0; i < warehouseIds.size(); i++) {
	            deleteIoDetailSql.append("?");
	            if (i != warehouseIds.size() - 1) {
	                deleteIoDetailSql.append(",");
	            }
	        }
	        deleteIoDetailSql.append(")");

	        pstmtDeleteIoDetail = conn.prepareStatement(deleteIoDetailSql.toString());
	        for (int i = 0; i < warehouseIds.size(); i++) {
	            pstmtDeleteIoDetail.setInt(i + 1, warehouseIds.get(i));
	        }
	        pstmtDeleteIoDetail.executeUpdate();

	        // 4) warehouse 삭제
	        StringBuilder deleteWarehouseSql = new StringBuilder("DELETE FROM warehouse WHERE warehouse_id IN (");
	        for (int i = 0; i < warehouseIds.size(); i++) {
	            deleteWarehouseSql.append("?");
	            if (i != warehouseIds.size() - 1) {
	                deleteWarehouseSql.append(",");
	            }
	        }
	        deleteWarehouseSql.append(")");

	        pstmtDeleteWarehouse = conn.prepareStatement(deleteWarehouseSql.toString());
	        for (int i = 0; i < warehouseIds.size(); i++) {
	            pstmtDeleteWarehouse.setInt(i + 1, warehouseIds.get(i));
	        }
	        totalDeleted = pstmtDeleteWarehouse.executeUpdate();

	        conn.commit();

	    } catch (Exception e) {
	        if (conn != null) {
	            try {
	                conn.rollback();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        e.printStackTrace();
	    } finally {
	        dbManager.release(pstmtSelect, rs);
	        dbManager.release(pstmtDeleteInventory);
	        dbManager.release(pstmtDeleteIoDetail);
	        dbManager.release(pstmtDeleteWarehouse);
	        if (conn != null) {
	            try {
	                conn.setAutoCommit(true);
	                conn.close();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }

	    return totalDeleted;
	}
}