package com.shinlogis.wms.warehouse.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
}
