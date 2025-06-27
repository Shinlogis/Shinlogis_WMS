package com.shinlogis.wms.inoutbound.outbound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.damagedCode.model.DamagedCode;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.location.model.Location;
import com.shinlogis.wms.snapshot.model.Snapshot;
import com.shinlogis.wms.warehouse.model.Warehouse;

public class OutboundDetailDAO {
	DBManager dbManager = DBManager.getInstance();

	// outboundDetail의 모든 정보 출력. 초기화면에 사용
	public List<IODetail> selectAllOutboundDetail() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<IODetail> list = new ArrayList<>();

		try {
			con = dbManager.getConnection();
			StringBuffer sql = new StringBuffer();
			// io_detail, io_receipt join
			sql.append("SELECT * FROM io_detail id ")
					.append("INNER JOIN io_receipt ir ON id.io_receipt_id = ir.io_receipt_id ")
					.append("INNER JOIN snapshot s ON id.snapshot_id = s.snapshot_id ")
					.append("INNER JOIN damaged_code dc ON id.damage_code_id = dc.damage_code_id ")
					.append("INNER JOIN headquarters_user hu ON ir.user_id = hu.headquarters_user_id ")
					.append("INNER JOIN warehouse w ON id.warehouse_id = w.warehouse_id ")
					.append("INNER JOIN location l ON ir.location_id = l.location_id ")
					.append(" where ir.io_type = 'out' ").append("ORDER BY id.io_detail_id DESC ");

			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				IODetail outboundDetail = createIODetailFromResultSet(rs);
				list.add(outboundDetail);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return list;
	}

	// 검색 조건별 출고 상세 조회
	public List<IODetail> selectByCondition(String outboundPlanId, String outboundDetailId, String productCode,
			String productSupplier, Date reservatedDate, String targetStore, String container, String status,
			Date processedDate) {
		List<IODetail> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = dbManager.getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM io_detail id ")
					.append("INNER JOIN io_receipt ir ON id.io_receipt_id = ir.io_receipt_id ")
					.append("INNER JOIN snapshot s ON id.snapshot_id = s.snapshot_id ")
					.append("INNER JOIN damaged_code dc ON id.damage_code_id = dc.damage_code_id ")
					.append("INNER JOIN headquarters_user hu ON ir.user_id = hu.headquarters_user_id ")
					.append("INNER JOIN warehouse w ON id.warehouse_id = w.warehouse_id ")
					.append("INNER JOIN location l ON ir.location_id = l.location_id ").append("WHERE 1=1 ")
					.append("AND ir.io_type = 'out'  ");

			List<Object> params = new ArrayList<>();

			// 출고예정ID 검색
			if (outboundPlanId != null && !outboundPlanId.trim().isEmpty()) {
				sql.append("AND ir.io_receipt_id = ? ");
				params.add(Integer.parseInt(outboundPlanId.trim()));
			}

			// 출고상세ID 검색
			if (outboundDetailId != null && !outboundDetailId.trim().isEmpty()) {
				sql.append("AND id.io_detail_id = ? ");
				params.add(Integer.parseInt(outboundDetailId.trim()));
			}

			// 상품코드 검색 (부분 일치)
			if (productCode != null && !productCode.trim().isEmpty()) {
				sql.append("AND s.product_code LIKE ? ");
				params.add("%" + productCode.trim() + "%");
			}

			// 공급사명 검색 (부분 일치)
			if (productSupplier != null && !productSupplier.trim().isEmpty()) {
				sql.append("AND s.supplier_name LIKE ? ");
				params.add("%" + productSupplier.trim() + "%");
			}

			// 출고예정일 검색 (정확한 날짜)
			if (reservatedDate != null) {
				sql.append("AND DATE(ir.scheduled_date) = DATE(?) ");
				params.add(reservatedDate);
			}

			// 출고지점 검색 (부분 일치)
			if (targetStore != null && !targetStore.trim().isEmpty()) {
				sql.append("AND l.location_name LIKE ? ");
				params.add("%" + targetStore.trim() + "%");
			}

			// 보관창고 검색 (부분 일치)
			if (container != null && !container.trim().isEmpty()) {
				sql.append("AND w.warehouse_name LIKE ? ");
				params.add("%" + container.trim() + "%");
			}

			// 상태 검색
			if (status != null && !status.equals("전체")) {
				sql.append("AND id.status = ? ");
				params.add(status);
			}

			// 출고일 검색 (정확한 날짜)
			if (processedDate != null) {
				sql.append("AND DATE(id.processed_date) = DATE(?) ");
				params.add(processedDate);
			}

			sql.append("ORDER BY id.io_detail_id DESC");

			pstmt = con.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				IODetail detail = createIODetailFromResultSet(rs);
				list.add(detail);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println("ID 값이 올바른 숫자 형식이 아닙니다: " + e.getMessage());
		} finally {
			dbManager.release(pstmt, rs);
		}

		return list;
	}

	// 검색 조건에 따른 총 개수 조회
	public int countByCondition(String outboundPlanId, String outboundDetailId, String productCode,
			String productSupplier, Date reservatedDate, String targetStore, String container, String status,
			Date processedDate) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = dbManager.getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT COUNT(*) AS total FROM io_detail id ")
					.append("INNER JOIN io_receipt ir ON id.io_receipt_id = ir.io_receipt_id ")
					.append("INNER JOIN snapshot s ON id.snapshot_id = s.snapshot_id ")
					.append("INNER JOIN damaged_code dc ON id.damage_code_id = dc.damage_code_id ")
					.append("INNER JOIN headquarters_user hu ON ir.user_id = hu.headquarters_user_id ")
					.append("INNER JOIN warehouse w ON id.warehouse_id = w.warehouse_id ")
					.append("INNER JOIN location l ON ir.location_id = l.location_id ").append("WHERE 1=1 ")
					.append("AND ir.io_type = 'out'  ");

			List<Object> params = new ArrayList<>();

			if (outboundPlanId != null && !outboundPlanId.trim().isEmpty()) {
				sql.append("AND ir.io_receipt_id = ? ");
				params.add(Integer.parseInt(outboundPlanId.trim()));
			}

			if (outboundDetailId != null && !outboundDetailId.trim().isEmpty()) {
				sql.append("AND id.io_detail_id = ? ");
				params.add(Integer.parseInt(outboundDetailId.trim()));
			}

			if (productCode != null && !productCode.trim().isEmpty()) {
				sql.append("AND s.product_code LIKE ? ");
				params.add("%" + productCode.trim() + "%");
			}

			if (productSupplier != null && !productSupplier.trim().isEmpty()) {
				sql.append("AND s.supplier_name LIKE ? ");
				params.add("%" + productSupplier.trim() + "%");
			}

			if (reservatedDate != null) {
				sql.append("AND DATE(ir.scheduled_date) = DATE(?) ");
				params.add(reservatedDate);
			}

			if (targetStore != null && !targetStore.trim().isEmpty()) {
				sql.append("AND l.location_name LIKE ? ");
				params.add("%" + targetStore.trim() + "%");
			}

			if (container != null && !container.trim().isEmpty()) {
				sql.append("AND w.warehouse_name LIKE ? ");
				params.add("%" + container.trim() + "%");
			}

			if (status != null && !status.equals("전체")) {
				sql.append("AND id.status = ? ");
				params.add(status);
			}

			if (processedDate != null) {
				sql.append("AND DATE(id.processed_date) = DATE(?) ");
				params.add(processedDate);
			}

			pstmt = con.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("total");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println("ID 값이 올바른 숫자 형식이 아닙니다: " + e.getMessage());
		} finally {
			dbManager.release(pstmt, rs);
		}

		return count;
	}

	// 총 검색결과 수 를 출력하는 메서드
	public int countTotal() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int totalCount = 0;

		try {
			con = dbManager.getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT COUNT(*) AS total FROM io_detail id "
					+ " inner join io_receipt ir "
					+ " on id.io_receipt_id = ir.io_receipt_id "
					+ " where ir.io_type = 'out' ");
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				totalCount = rs.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return totalCount;
	}

	/**
	 * <h2>ResultSet에서 IODetail 객체 생성하는 공통 메서드
	 * 
	 * @author 이세형
	 */
	private IODetail createIODetailFromResultSet(ResultSet rs) throws SQLException {
		IODetail outboundDetail = new IODetail();
		outboundDetail.setIoDetailId(rs.getInt("io_detail_id"));

		// IOReceipt 정보
		IOReceipt or = new IOReceipt();
		or.setIoReceiptId(rs.getInt("ir.io_receipt_id"));
		or.setScheduledDate(rs.getDate("ir.scheduled_date"));

		Location location = new Location();
		location.setLocationName(rs.getString("l.location_name"));
		or.setLocation(location);
		outboundDetail.setIoReceipt(or);

		// 창고 정보
		Warehouse warehouse = new Warehouse();
		warehouse.setWarehouseName(rs.getString("w.warehouse_name"));
		outboundDetail.setWarehouse(warehouse);

		// 계획 수량
		outboundDetail.setPlannedQuantity(rs.getInt("planned_quantity"));

		// 상품 스냅샷 정보
		Snapshot snapshot = new Snapshot();
		snapshot.setProductCode(rs.getString("s.product_code"));
		snapshot.setProductName(rs.getString("s.product_name"));
		snapshot.setSnapshotId(rs.getInt("id.snapshot_id"));
		outboundDetail.setProductSnapshot(snapshot);

		// 손상코드 정보
		DamagedCode dmc = new DamagedCode();
		dmc.setDamageCodeId(rs.getInt("id.damage_code_id"));
		outboundDetail.setDamagedCode(dmc);

		// 수량 정보
		outboundDetail.setDamageQuantity(rs.getInt("id.damage_quantity"));
		outboundDetail.setActualQuantity(rs.getInt("id.actual_quantity"));

		// 사용자 정보
		HeadquartersUser user = new HeadquartersUser();
		user.setHeadquartersUserId(rs.getInt("ir.user_id"));
		outboundDetail.setUser(user);

		// 처리 일자 및 상태
		outboundDetail.setProccessedDate(rs.getDate("id.processed_date"));
		outboundDetail.setStatus(rs.getString("id.status"));

		return outboundDetail;
	}
}