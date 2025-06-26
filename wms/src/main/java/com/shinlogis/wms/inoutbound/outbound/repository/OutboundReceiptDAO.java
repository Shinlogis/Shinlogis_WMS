package com.shinlogis.wms.inoutbound.outbound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.location.model.Location;

/**
 * <h2>출고예정 전표DAO
 * <li>outboundReceipt 관련 DAO
 * 
 * @author 이세형
 */
public class OutboundReceiptDAO {
	DBManager dbManager = DBManager.getInstance();

//	public OutBoundReceiptDAO(){}

	/**
	 * <h2>총 데이터 테이블에 출력
	 * 
	 * @author 이세형
	 */
	public List selectAllOutbounds() {// IoReceipt의 모든 레코드 가져오는 메서드
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList();

		con = dbManager.getConnection();

		try {
			StringBuffer sql = new StringBuffer();
			// sql문 필요한거 join. 필요한만큼 계속 조인해서 쓸거임.
			sql.append("select ir.*, l.location_name," + " (select s.product_name" + " from io_detail id"
					+ " inner join snapshot s on id.snapshot_id = s.snapshot_id"
					+ " where id.io_receipt_id = ir.io_receipt_id" + " order by id.io_detail_id"
					+ " limit 1) as first_product_name," + " (select count(*)" + " from io_detail id2"
					+ " where id2.io_receipt_id = ir.io_receipt_id) as item_count" + " from io_receipt ir"
					+ " inner join location l on ir.location_id = l.location_id" + " order by ir.io_receipt_id desc");
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				IOReceipt outboundReceipt = new IOReceipt();
				outboundReceipt.setIoReceiptId(rs.getInt("io_receipt_id"));// pk넣어주기
				outboundReceipt.setIoType(rs.getString("io_type"));
				HeadquartersUser user = new HeadquartersUser();
				// headquarters유저의 pk가져옴, user에 할당
				user.setHeadquartersUserId(rs.getInt("user_id"));
				// user객체에서 받아온user
				outboundReceipt.setUser(user);

				Location location = new Location();
				location.setLocationId(rs.getInt("ir.location_id"));
				location.setLocationName(rs.getString("l.location_name"));
				outboundReceipt.setLocation(location);

				outboundReceipt.setCreatedAt(rs.getDate("created_at"));
				outboundReceipt.setScheduledDate(rs.getDate("scheduled_date"));
				outboundReceipt.setProcessedDate(rs.getDate("processed_date"));
				outboundReceipt.setStatus(rs.getString("status"));

				outboundReceipt.setFirstProductName(rs.getString("first_product_name"));
				outboundReceipt.setItemCount(rs.getInt("item_count"));

				list.add(outboundReceipt);
			}

//			System.out.println("1번" + rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return list;
	}

	public void insertOutbound() {

	}

	/**
	 * <h2>총 데이터 수 표헌
	 * 
	 * @author 이세형
	 */
	public int countTotal() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int totalCount = 0;

		try {
			con = dbManager.getConnection();
			// total로서 sql문을 받아온다.
			String sql = "SELECT COUNT(*) AS total FROM io_receipt";
			pstmt = con.prepareStatement(sql);
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
	 * <h2>검색기능 구현
	 * 
	 * @author 이세형
	 */
	public List<IOReceipt> selectByCondition(String ioReceiptId, String productName, String locationName,
			Date scheduledDate, Date createdAt, String status) {
		List<IOReceipt> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = dbManager.getConnection();

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ir.*, l.location_name, ");
			sql.append("  (SELECT s.product_name FROM io_detail id ");
			sql.append("   JOIN snapshot s ON id.snapshot_id = s.snapshot_id ");
			sql.append(
					"   WHERE id.io_receipt_id = ir.io_receipt_id ORDER BY id.io_detail_id LIMIT 1) AS first_product_name, ");
			sql.append(
					"  (SELECT COUNT(*) FROM io_detail id2 WHERE id2.io_receipt_id = ir.io_receipt_id) AS item_count ");
			sql.append("FROM io_receipt ir ");
			sql.append("JOIN location l ON ir.location_id = l.location_id ");
			sql.append("WHERE 1=1 ");

			List<Object> params = new ArrayList<>();

			if (ioReceiptId != null && !ioReceiptId.isEmpty()) {
				sql.append("AND ir.io_receipt_id = ? ");
				params.add(Integer.parseInt(ioReceiptId));
			}
			if (productName != null && !productName.isEmpty()) {
				sql.append("AND EXISTS (SELECT 1 FROM io_detail id ");
				sql.append("JOIN snapshot s ON id.snapshot_id = s.snapshot_id ");
				sql.append("WHERE id.io_receipt_id = ir.io_receipt_id AND s.product_name LIKE ?) ");
				params.add("%" + productName + "%");
			}
			if (locationName != null && !locationName.isEmpty()) {
				sql.append("AND l.location_name LIKE ? ");
				params.add("%" + locationName + "%");
			}
			if (scheduledDate != null) {
				sql.append("AND DATE(ir.scheduled_date) = ? ");
				params.add(new java.sql.Date(scheduledDate.getTime())); // 🔥 수정
			}
			if (createdAt != null) {
				sql.append("AND DATE(ir.created_at) = ? ");
				params.add(new java.sql.Date(createdAt.getTime())); // 🔥 수정
			}
			if (status != null && !"전체".equals(status)) {
				sql.append("AND ir.status = ? ");
				params.add(status);
			}

			pstmt = con.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				IOReceipt receipt = new IOReceipt();
				receipt.setIoReceiptId(rs.getInt("io_receipt_id"));
				receipt.setIoType(rs.getString("io_type"));
				HeadquartersUser user = new HeadquartersUser();
				user.setHeadquartersUserId(rs.getInt("user_id"));
				receipt.setUser(user);

				Location loc = new Location();
				loc.setLocationId(rs.getInt("location_id"));
				loc.setLocationName(rs.getString("location_name"));
				receipt.setLocation(loc);

				receipt.setScheduledDate(rs.getDate("scheduled_date"));
				receipt.setCreatedAt(rs.getDate("created_at"));
				receipt.setProcessedDate(rs.getDate("processed_date"));
				receipt.setStatus(rs.getString("status"));

				receipt.setFirstProductName(rs.getString("first_product_name"));
				receipt.setItemCount(rs.getInt("item_count"));

				list.add(receipt);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return list;
	}

	/**
	 * <h2> 검색기반 카운트 구현
	 * 
	 * @author 이세형
	 * */
	
	public int countByCondition(String ioReceiptId, String productName, String locationName, Date scheduledDate,
			Date createdAt, String status) {
		int totalCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = dbManager.getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT COUNT(*) as total ");
			sql.append("FROM io_receipt ir ");
			sql.append("JOIN location l ON ir.location_id = l.location_id ");
			sql.append("WHERE 1=1 ");

			List<Object> params = new ArrayList<>();

			if (ioReceiptId != null && !ioReceiptId.isEmpty()) {
				sql.append("AND ir.io_receipt_id = ? ");
				params.add(Integer.parseInt(ioReceiptId));
			}
			if (productName != null && !productName.isEmpty()) {
				sql.append("AND EXISTS (SELECT 1 FROM io_detail id ");
				sql.append("JOIN snapshot s ON id.snapshot_id = s.snapshot_id ");
				sql.append("WHERE id.io_receipt_id = ir.io_receipt_id AND s.product_name LIKE ?) ");
				params.add("%" + productName + "%");
			}
			if (locationName != null && !locationName.isEmpty()) {
				sql.append("AND l.location_name LIKE ? ");
				params.add("%" + locationName + "%");
			}
			if (scheduledDate != null) {
				sql.append("AND DATE(ir.scheduled_date) = ? ");
				params.add(scheduledDate);
			}
			if (createdAt != null) {
				sql.append("AND DATE(ir.created_at) = ? ");
				params.add(createdAt);
			}
			if (status != null && !"전체".equals(status)) {
				sql.append("AND ir.status = ? ");
				params.add(status);
			}

			pstmt = con.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				totalCount = rs.getInt("total");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return totalCount;
	}

}
//			출고예정, 출고예정ID
//		sql.append("select io_receipt_id");
//		sql.append(" from io_receipt;");

//			//출고예정, 출고품목
//			sql.append("select s.product_name");
//			sql.append(" from snapshot s");
//			sql.append(" inner join io_detail id");
//			sql.append(" on s.snapshot_id = id.snapshot_id;");
//			System.out.println("2번" + rs);
//			
//			//출고예정, 출고지점
//			sql.append("select l.location_name");
//			sql.append(" from location l");
//			sql.append(" inner join io_receipt ir");
//			sql.append(" on l.location_id = ir.location_id;");
//			
//			//출고예정, 출고예정일
//			sql.append("select scheduled_date");
//			sql.append(" from io_receipt;");
//			
//			//출고예정, 상태
//			sql.append("select status");
//			sql.append(" from io_receipt");
//			
//			//출고예정, 등록일
//			sql.append("select DATE(created_at) as date_only");
//			sql.append(" from io_receipt");