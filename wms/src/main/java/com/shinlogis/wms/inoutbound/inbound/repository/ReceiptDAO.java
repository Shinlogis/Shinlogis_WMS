package com.shinlogis.wms.inoutbound.inbound.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.inoutbound.model.InboundForm;

/**
 * 입고예정 전표 DAO입니다.
 *
 * @author 김예진
 */
public class ReceiptDAO {
	DBManager dbManager = DBManager.getInstance();

	/**
	 * 폼에 작성한 내용으로 입고예정을 등록하는 메서드
	 * @auther 김예진
	 * @param date
	 * @param user
	 * @return
	 */
	public Map<String, Object> insertReceipt(Date date, HeadquartersUser user) {
		Map<String, Object> resultMap = new HashMap<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		int id = 0; // 초기화 필요

		StringBuffer sql = new StringBuffer();
		sql.append("insert into io_receipt (io_type, user_id, created_at, scheduled_date) values(?, ?, now(), ?)");

		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, "IN");
			pstmt.setInt(2, user.getHeadquartersUserId());
			pstmt.setDate(3, date);

			pstmt.executeUpdate();
			resultMap.put("result", result);

			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
				resultMap.put("id", id);
				System.out.println("예정id " + id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt);
		}
		return resultMap;
	}

	/**
	 * 입고 전표, 전표에 맞는 상품 정보를 가져오는 메서드
	 *
	 * @param filters
	 * @return
	 * @auther 김예진
	 * @since 2025-06-22
	 */
	public List<IOReceipt> selectInboundReceiptsWithItemInfo(Map<String, Object> filters) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<IOReceipt> result = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append("  r.io_receipt_id, ").append("  r.io_type, ").append("  r.user_id, ")
				.append("  u.id AS user_id_str, ").append("  r.status, ").append("  r.created_at, ")
				.append("  r.scheduled_date, ").append("  r.processed_date, ").append("  r.location_id, ")

				// 첫 번째 상품명
				.append("  ( ").append("    SELECT s.product_name ").append("    FROM io_detail d ")
				.append("    JOIN snapshot s ON s.snapshot_id = d.snapshot_id ")
				.append("    WHERE d.io_receipt_id = r.io_receipt_id AND d.active = true ").append("    ORDER BY d.io_detail_id ")
				.append("    LIMIT 1 ").append("  ) AS first_product_name, ")

				// 첫 번째 공급사명
				.append("  ( ").append("    SELECT s.supplier_name ").append("    FROM io_detail d ")
				.append("    JOIN snapshot s ON s.snapshot_id = d.snapshot_id ")
				.append("    WHERE d.io_receipt_id = r.io_receipt_id  AND d.active = true ").append("    ORDER BY d.io_detail_id ")
				.append("    LIMIT 1 ").append("  ) AS supplier_name, ")

				// 아이템 개수
				.append("  ( ").append("    SELECT COUNT(*) ").append("    FROM io_detail d ")
				.append("    WHERE d.io_receipt_id = r.io_receipt_id  AND d.active = true ").append("  ) AS item_count ")

				.append("FROM io_receipt r ").append("JOIN headquarters_user u ON r.user_id = u.headquarters_user_id ")
				.append(" JOIN io_detail d ON r.io_receipt_id = d.io_receipt_id ")
				.append("WHERE r.io_type = 'IN' AND r.active = true AND d.active = true ");

		// 검색 필터 추가
		List<Object> params = new ArrayList<>();

		if (filters.get("io_receipt_id") != null) {
			sql.append("AND r.io_receipt_id = ?  ");
			params.add(filters.get("io_receipt_id"));
		}

		if (filters.get("scheduled_date") != null) {
			sql.append("AND DATE(r.scheduled_date) = ? ");
			params.add(filters.get("scheduled_date"));
		}

		if (filters.get("product_name") != null) {
			sql.append("AND EXISTS ( ").append("  SELECT 1 FROM io_detail d ")
					.append("  JOIN snapshot s ON s.snapshot_id = d.snapshot_id ")
					.append("  WHERE d.io_receipt_id = r.io_receipt_id ").append("  AND s.product_name = ? ")
					.append(") ");
			params.add(filters.get("product_name"));
		}

		if (filters.get("supplier_name") != null) {
			sql.append("AND EXISTS ( ").append("  SELECT 1 FROM io_detail d ")
					.append("  JOIN snapshot s ON s.snapshot_id = d.snapshot_id ")
					.append("  WHERE d.io_receipt_id = r.io_receipt_id ").append("  AND s.supplier_name = ? ")
					.append(") ");
			params.add(filters.get("supplier_name"));
		}

		if (filters.get("status") != null && !"전체".equals(filters.get("status"))) {
			sql.append("AND r.status = ? ");
			params.add(filters.get("status"));
		}

		try {
			conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql.toString());

			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				IOReceipt r = new IOReceipt();
				r.setIoReceiptId(rs.getInt("io_receipt_id"));
				r.setIoType(rs.getString("io_type"));

				HeadquartersUser u = new HeadquartersUser();
				u.setHeadquartersUserId(rs.getInt("user_id"));
				u.setId(rs.getString("user_id_str"));
				r.setUser(u);

				r.setCreatedAt(rs.getDate("created_at"));
				r.setScheduledDate(rs.getDate("scheduled_date"));

				Date p = rs.getDate("processed_date");
				r.setProcessedDate(p != null ? p : null);

				r.setStatus(rs.getString("status"));

				// 인포 정보
				r.setFirstProductName(rs.getString("first_product_name"));
				r.setSupplierName(rs.getString("supplier_name"));
				r.setItemCount(rs.getInt("item_count"));

				result.add(r);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			dbManager.release(ps, rs);
		}

		return result;
	}

	/**
	 * 입고상세의 상태에 따라 입고예정의 상태를 결정하는 메서드
	 * @author 김예진
	 * @since 2025-06-27
	 * @param ioReceiptId
	 * @return
	 */
	public String determineReceiptStatus(int ioReceiptId) {
	    String sql = "SELECT status FROM io_detail WHERE io_receipt_id = ?";
	    List<String> statuses = new ArrayList<>();
	    Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

	    try{
	    	conn = dbManager.getConnection();
	    	ps = conn.prepareStatement(sql);
    		ps.setInt(1, ioReceiptId);
	        rs = ps.executeQuery();
	        while (rs.next()) {
	            statuses.add(rs.getString("status"));
	        }
	        
	        boolean allPlanned = statuses.stream().allMatch(s -> s.equals("예정"));
	        boolean allCompleted = statuses.stream().allMatch(s -> s.equals("완료"));
	        boolean anyInProgress = statuses.stream().anyMatch(s -> s.equals("진행 중"));
	        
	        if (allPlanned) return "예정";
	        else if (allCompleted) return "완료";
	        else if (anyInProgress) return "진행 중";
	        else return "진행 중"; // 예정 + 완료 섞인 경우
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "예정"; // 기본 
	    } finally {
			dbManager.release(ps, rs);
		}

	}
	
	/**
	 * 입고예정의 상태를 업데이트하는 메서드
	 * @author 김예진
	 * @since 2025-06-27
	 * @param ioReceiptId
	 */
	public void updateReceiptStatus(int ioReceiptId, String status) {
	    Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

	    String sql = "UPDATE io_receipt SET status = ? WHERE io_receipt_id = ?";
	    try {
	    	conn = dbManager.getConnection();
	    	ps = conn.prepareStatement(sql);
	    	ps.setString(1, status);
	    	ps.setInt(2, ioReceiptId);
	    	ps.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
			dbManager.release(ps, rs);
		}
	}

	/**
	 * 입고예정의 active를 비활성화
	 *
	 * @author 김예진
	 * @param ioReceiptId
	 * @return
	 */
	public int deactivateIoReceipt(int ioReceiptId) {
		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;

		String sql = "UPDATE io_receipt SET active = false WHERE io_receipt_id = ?";

		try {
			conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, ioReceiptId);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(ps);
		}
		return result;
	}

	/**
	 * 특정 입고예정 ID에 해당하는 입고상세 ID 목록을 반환
	 * @auther 김예진
	 * @since 2025-06-28
	 * @param receiptId 입고상세 ID
	 * @return 상위 입고예정 ID
	 */
	public List<Integer> findDetailIsdByReceiptId(int receiptId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Integer> detailIds = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT d.io_detail_id ");
		sql.append("FROM io_receipt r ");
		sql.append("JOIN io_detail d ON r.io_receipt_id = d.io_receipt_id ");
		sql.append("WHERE r.io_receipt_id = ? AND d.active = true");

		try {
			conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, receiptId);
			rs = ps.executeQuery();

			while (rs.next()) { 
				detailIds.add(rs.getInt("io_detail_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(ps, rs);
		}

		return detailIds;
	}



}
