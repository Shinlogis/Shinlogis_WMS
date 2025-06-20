package com.shinlogis.wms.snapshot.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.snapshot.model.Snapshot;

/**
 * 스냅샷 DAO입니다
 * @author 김예진
 */
public class SnapshotDAO {
	DBManager dbManager = DBManager.getInstance();

	/**
	 * 스냅샷 목록을 select하는 메서드
	 * 
	 * @author 김예진
	 * @since 2025-06-20
	 * @return
	 */
	public List<Snapshot> selectSnapshots() {
		List<Snapshot> list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM SNAPSHOT";

		try {
			connection = dbManager.getConnection();
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Snapshot snapshot = new Snapshot();
				snapshot.setSnapshotId(rs.getInt("snapshot_id"));
				snapshot.setProductCode(rs.getString("product_code"));
				snapshot.setProductName(rs.getString("product_name"));
				snapshot.setStorageTypeCode(rs.getString("storage_type_code"));
				snapshot.setSupplierName(rs.getString("supplier_name"));
				snapshot.setExpiryDate(rs.getDate("expiry_date"));
				
				list.add(snapshot);
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return list;
	}
	
	/**
	 * 스냅샷을 id로 select하는 메서드
	 * @author 김예진
	 * @since 2025-06-20
	 * @return
	 */
	public Snapshot selectSnapshotById(int id) {
		Snapshot snapshot = new Snapshot();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM SNAPSHOT WHERE SNAPSHOT_ID = " + id;

		try {
			connection = dbManager.getConnection();
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				snapshot.setSnapshotId(rs.getInt("snapshot_id"));
				snapshot.setProductCode(rs.getString("product_code"));
				snapshot.setProductName(rs.getString("product_name"));
				snapshot.setStorageTypeCode(rs.getString("storage_type_code"));
				snapshot.setSupplierName(rs.getString("supplier_name"));
				snapshot.setExpiryDate(rs.getDate("expiry_date"));
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return snapshot;
	}
	
}
