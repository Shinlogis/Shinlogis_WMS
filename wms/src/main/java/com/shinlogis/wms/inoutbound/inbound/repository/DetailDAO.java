package com.shinlogis.wms.inoutbound.inbound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.damagedCode.model.DamagedCode;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.inoutbound.model.InboundForm;
import com.shinlogis.wms.snapshot.model.Snapshot;
import com.shinlogis.wms.storageType.model.StorageType;
import com.shinlogis.wms.warehouse.model.Warehouse;

/**
 * 입고 상세 DAO입니다
 * 
 * @author 김예진
 */
public class DetailDAO {
	DBManager dbManager = DBManager.getInstance();

	/**
	 * 입고 품목을 insert하는 메서드
	 * 
	 * @param item
	 * @return
	 * @author 김예진
	 * @since 2025-06-29
	 */
	public int insertDetail(int receiptId, int snapshotId, InboundForm form, HeadquartersUser user) {
		int result = 0;
		String sql = "INSERT INTO IO_DETAIL (io_receipt_id, planned_quantity, snapshot_id, actual_quantity, headquarters_user_id ) VALUES (?, ?, ?, ?, ?)";

		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = dbManager.getConnection();
			try {
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, receiptId);
				pstmt.setInt(2, form.getQuantity());
				pstmt.setInt(3, snapshotId);
				pstmt.setInt(4, 0);
				pstmt.setInt(5, user.getHeadquartersUserId());

				result = pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			dbManager.release(pstmt);
		}

		return result;
	}

	/**
	 * 입고상세를 SELECT하는 메서드
	 * 
	 * @return
	 * @author 김예진
	 * @since 2025-06-29
	 */
	public List<IODetail> selectIODetails(Map<String, Object> filters) {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ip.io_detail_id, ip.planned_quantity, ip.damage_quantity, ip.actual_quantity, ")
				.append("ip.processed_date, ip.status,").append("ir.io_receipt_id, ir.scheduled_date, ")
				.append("s.snapshot_id, s.product_code, s.product_name, s.storage_type_code, s.supplier_name, s.price, s.expiry_date, ")
				.append("dc.damage_code_id, dc.code AS damage_code, ")
				.append("hu.headquarters_user_id, hu.id AS hq_user_id, hu.email AS hq_user_email, ")
				.append("w.warehouse_id, w.warehouse_name, w.address, w.storage_type_id, w.warehouse_code, ")
				.append("s.storage_type_code ")
				.append("FROM io_detail ip ")
				.append("JOIN io_receipt ir  ON ip.io_receipt_id = ir.io_receipt_id ")
				.append("LEFT JOIN snapshot s ON ip.snapshot_id = s.snapshot_id ")
				.append("LEFT JOIN damaged_code dc  ON ip.damage_code_id = dc.damage_code_id ")
				.append("LEFT JOIN headquarters_user hu ON ir.user_id = hu.headquarters_user_id ")
				.append("LEFT JOIN warehouse w ON ip.warehouse_id = w.warehouse_id ")
				.append("LEFT JOIN storage_type st ON st.storage_type_id = w.storage_type_id ")
				.append("WHERE ir.io_type = 'IN'");
//		System.out.println(sql);
		// 검색 필터 추가
		List<Object> params = new ArrayList<>();
		if (filters.get("io_detail_id") != null) {
			sql.append("AND ip.io_detail_id = ? ");
			params.add(filters.get("io_detail_id"));
		}
		if (filters.get("io_receipt_id") != null) {
			sql.append("AND ir.io_receipt_id = ? ");
			params.add(filters.get("io_receipt_id"));
		}
		if (filters.get("product_code") != null) {
			sql.append("AND s.product_code = ? ");
			params.add(filters.get("product_code"));
		}
		if (filters.get("product_name") != null) {
			sql.append("AND s.product_name = ? ");
			params.add(filters.get("product_name"));
		}
		if (filters.get("supplier_name") != null) {
			sql.append("AND s.supplier_name = ? ");
			params.add(filters.get("supplier_name"));
		}
		if (filters.get("status") != "전체" && filters.get("status") != null) {
			sql.append("AND ip.status = ? ");
			params.add(filters.get("status"));
		}
		if (filters.get("scheduled_date") != null) {
			sql.append("AND DATE(ir.scheduled_date) = ? ");
			params.add(filters.get("scheduled_date"));
		}

//		System.out.println(sql.toString());

		List<IODetail> list = new ArrayList<>();

		try {
			conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}
			rs = ps.executeQuery();

			while (rs.next()) {
				IODetail detail = new IODetail();
				detail.setIoDetailId(rs.getInt("io_detail_id"));

				IOReceipt receipt = new IOReceipt();
				receipt.setIoReceiptId(rs.getInt("io_receipt_id"));
				receipt.setScheduledDate(rs.getDate("scheduled_date"));
				detail.setIoReceipt(receipt);

				detail.setPlannedQuantity(rs.getInt("planned_quantity"));
				detail.setDamageQuantity(rs.getInt("damage_quantity"));
				detail.setActualQuantity(rs.getInt("actual_quantity"));
				detail.setProccessedDate(rs.getDate("processed_date") != null ? rs.getDate("processed_date") : null);
				detail.setStatus(rs.getString("status"));

				Snapshot snapshot = new Snapshot();
				snapshot.setSnapshotId(rs.getInt("snapshot_id"));
				snapshot.setProductCode(rs.getString("product_code"));
				snapshot.setProductName(rs.getString("product_name"));

				StorageType storageType = new StorageType();
				storageType.setTypeCode(rs.getString("storage_type_code"));
				snapshot.setStorageType(storageType);

				snapshot.setSupplierName(rs.getString("supplier_name"));
				snapshot.setPrice(rs.getInt("price"));
				snapshot.setExpiryDate(rs.getDate("expiry_date"));
				detail.setProductSnapshot(snapshot);

				DamagedCode code = new DamagedCode();
				code.setDamageCodeId(rs.getInt("damage_code_id"));
				code.setCode(rs.getString("damage_code"));
				detail.setDamagedCode(code);

				Warehouse warehouse = new Warehouse();
				warehouse.setWarehouseId(rs.getInt("warehouse_id"));
				warehouse.setWarehouseCode(rs.getString("warehouse_code"));
				warehouse.setWarehouseName(rs.getString("warehouse_name"));
				warehouse.setAddress(rs.getString("address"));
				warehouse.setStorageType(storageType);

				detail.setWarehouse(warehouse);

				HeadquartersUser hq = new HeadquartersUser();
				hq.setHeadquartersUserId(rs.getInt("headquarters_user_id"));
				hq.setId(rs.getString("hq_user_id"));
//				hq.setEmail(rs.getString("hq_user_email")); // TODO: setter을 수정하거나 db에서 받아온 email을 분리해서 저장하는 방법이 필요
//                    detail.(hq);

				list.add(detail);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			dbManager.release(ps, rs);
		}

		return list;
	}

	/**
	 * 입고 상세의 예정수량을 변경하는 메서드
	 * 
	 * @auther 김예진
	 * @since 2025-06-24
	 * @param quantity
	 * @return
	 */
	public int updatePlanQuantity(int id, int quantity) {

		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;

		String sql = "update io_detail set planned_quantity = ? where io_detail_id = ?";
		try {
			conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, quantity);
			ps.setInt(2, id);

			result = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(ps);
		}

		return 0;
	}

	/**
	 * 입고상세의 상태를 변경
	 * 
	 * @author 김예진
	 * @since 2025-06-24
	 * @param id
	 * @param quantity
	 * @return
	 */
	public int updateStatus(int id, String status) {

		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;

		String sql = "update io_detail set status = ? where io_detail_id = ?";
		try {
			conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, status);
			ps.setInt(2, id);

			result = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(ps);
		}

		return 0;
	}

	/**
	 * 입고완료를 select하는 메서드
	 * 
	 * @return
	 * @author 김예진
	 * @since 2025-06-29
	 */
	public List<IODetail> selectProcess(Map<String, Object> filters) {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ip.io_detail_id, ip.planned_quantity, ip.damage_quantity, ip.actual_quantity, ")
				.append("ip.processed_date, ip.status,").append("ir.io_receipt_id, ir.scheduled_date, ")
				.append("s.snapshot_id, s.product_code, s.product_name, s.storage_type_code, s.supplier_name, s.price, s.expiry_date, ")
				.append("dc.damage_code_id, dc.code AS damage_code, dc.name AS damage_code_name,")
				.append("hu.headquarters_user_id, hu.id AS hq_user_id, hu.email AS hq_user_email, ")
				.append("w.warehouse_id, w.warehouse_name, w.address, w.storage_type_id, w.warehouse_code, ")
				.append("s.storage_type_code ")
				.append("FROM io_detail ip ")
				.append("JOIN io_receipt ir  ON ip.io_receipt_id = ir.io_receipt_id ")
				.append("LEFT JOIN snapshot s ON ip.snapshot_id = s.snapshot_id ")
				.append("LEFT JOIN damaged_code dc  ON ip.damage_code_id = dc.damage_code_id ")
				.append("LEFT JOIN headquarters_user hu ON ir.user_id = hu.headquarters_user_id ")
				.append("LEFT JOIN warehouse w ON ip.warehouse_id = w.warehouse_id ")
				.append("LEFT JOIN storage_type st ON st.storage_type_id = w.storage_type_id ")
				.append("WHERE ir.io_type = 'IN' AND ip.processed_date IS NOT NULL ");
//		System.out.println(sql);
		// 검색 필터 추가
		List<Object> params = new ArrayList<>();
		if (filters.get("io_detail_id") != null) {
			sql.append("AND ip.io_detail_id = ? ");
			params.add(filters.get("io_detail_id"));
		}
		if (filters.get("io_receipt_id") != null) {
			sql.append("AND ir.io_receipt_id = ? ");
			params.add(filters.get("io_receipt_id"));
		}
		if (filters.get("product_code") != null) {
			sql.append("AND s.product_code = ? ");
			params.add(filters.get("product_code"));
		}
		if (filters.get("product_name") != null) {
			sql.append("AND s.product_name = ? ");
			params.add(filters.get("product_name"));
		}
		if (filters.get("supplier_name") != null) {
			sql.append("AND s.supplier_name = ? ");
			params.add(filters.get("supplier_name"));
		}
		if (filters.get("damage_code_name") != null && !"전체".equals(filters.get("damage_code_name"))) {
		    sql.append("AND dc.name = ? ");
		    params.add(filters.get("damage_code_name"));
		}
		if (filters.get("processed_date") != null) {
			sql.append("AND DATE(ir.processed_date) = ? ");
			params.add(filters.get("processed_date"));
		}

//		System.out.println(sql.toString());

		List<IODetail> list = new ArrayList<>();

		try {
			conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}
			rs = ps.executeQuery();

			while (rs.next()) {
				IODetail detail = new IODetail();
				detail.setIoDetailId(rs.getInt("io_detail_id"));

				IOReceipt receipt = new IOReceipt();
				receipt.setIoReceiptId(rs.getInt("io_receipt_id"));
				receipt.setScheduledDate(rs.getDate("scheduled_date"));
				detail.setIoReceipt(receipt);

				detail.setPlannedQuantity(rs.getInt("planned_quantity"));
				detail.setDamageQuantity(rs.getInt("damage_quantity"));
				detail.setActualQuantity(rs.getInt("actual_quantity"));
				detail.setProccessedDate(rs.getDate("processed_date") != null ? rs.getDate("processed_date") : null);
				detail.setStatus(rs.getString("status"));

				StorageType storageType = new StorageType();
				storageType.setTypeCode(rs.getString("storage_type_code"));

				Snapshot snapshot = new Snapshot();
				snapshot.setSnapshotId(rs.getInt("snapshot_id"));
				snapshot.setProductCode(rs.getString("product_code"));
				snapshot.setProductName(rs.getString("product_name"));
				snapshot.setStorageType(storageType);
				snapshot.setSupplierName(rs.getString("supplier_name"));
				snapshot.setPrice(rs.getInt("price"));
				snapshot.setExpiryDate(rs.getDate("expiry_date"));
				detail.setProductSnapshot(snapshot);

				DamagedCode code = new DamagedCode();
				code.setDamageCodeId(rs.getInt("damage_code_id"));
				code.setCode(rs.getString("damage_code"));
				code.setName(rs.getString("damage_code_name"));
				detail.setDamagedCode(code);

				Warehouse warehouse = new Warehouse();
				warehouse.setWarehouseId(rs.getInt("warehouse_id"));
				warehouse.setWarehouseCode(rs.getString("warehouse_code"));
				warehouse.setWarehouseName(rs.getString("warehouse_name"));
				warehouse.setAddress(rs.getString("address"));

				warehouse.setStorageType(storageType);

				detail.setWarehouse(warehouse);

				HeadquartersUser hq = new HeadquartersUser();
				hq.setHeadquartersUserId(rs.getInt("headquarters_user_id"));
				hq.setId(rs.getString("hq_user_id"));
//				hq.setEmail(rs.getString("hq_user_email")); // TODO: setter을 수정하거나 db에서 받아온 email을 분리해서 저장하는 방법이 필요
				detail.setUser(hq);

				list.add(detail);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			dbManager.release(ps, rs);
		}

		return list;
	}

	/**
	 * 입고상세의 파손, 저장창고를 입력해서 검수 후 입고처리로 update
	 * 
	 * @author 김예진
	 * @since 2025-06-25
	 * @param detailId
	 * @param damageCodeId
	 * @param damageQuantity
	 * @param warehouseId
	 * @return
	 */
	public int processInboundDetail(int detailId, int damageCodeId, int damageQuantity, int plannedQuantity,
			int warehouseId) {
		Connection conn = null;
		PreparedStatement ps = null;

		StringBuffer sql = new StringBuffer();
		sql.append(
				"update io_detail set damage_code_id = ?, damage_quantity = ?, warehouse_id = ?, processed_date = NOW(), actual_quantity = ? where io_detail_id = ?");

		try {
			conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, damageCodeId);
			ps.setInt(2, damageQuantity);
			ps.setInt(3, warehouseId);
			ps.setInt(4, plannedQuantity - damageQuantity);
			ps.setInt(5, detailId);

			return ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
