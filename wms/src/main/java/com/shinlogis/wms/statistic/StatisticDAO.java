package com.shinlogis.wms.statistic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.snapshot.model.Snapshot;

public class StatisticDAO {
	DBManager dbManager = DBManager.getInstance();

	/**
	 * 오늘 최다 출고 지점 5곳과 각 출고상세 건수를 반환
	 * @author 김예진
	 * @since 2025-06-27
	 * @return
	 */
	public List<Map<String, Object>> getTop5OutboundLocationsToday() {
		List<Map<String, Object>> resultMap = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select w.warehouse_name as name, count(*) as cnt ");
		sql.append("from warehouse w ");
		sql.append("join io_detail id ");
		sql.append("  on w.warehouse_id = id.warehouse_id ");
		sql.append("join io_receipt ir  ");
		sql.append("  on id.io_receipt_id = ir.io_receipt_id ");
		sql.append("where id.processed_date > CURRENT_DATE()  and ir.io_type = \"OUT\"");
		sql.append("group by w.warehouse_name ");
		sql.append("order by cnt desc ");
		sql.append("limit 5");
		
		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Map<String, Object> map = new HashMap<>();
				map.put("name", rs.getString("name"));
				map.put("cnt", rs.getInt("cnt"));
				resultMap.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return resultMap;
	}
	
	/**
	 * 오늘의 총 입고예정 수를 반환
	 * @author 김예진
	 * @since 2025-06-27
	 * @return
	 */
	public int getTotalInboundToday(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) as cnt ");
		sql.append("FROM io_detail id ");
		sql.append("JOIN io_receipt ir ");
		sql.append("  ON id.io_receipt_id = ir.io_receipt_id ");
		sql.append("WHERE ir.scheduled_date = CURRENT_DATE() AND ir.io_type = \"IN\"");

		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
	            total = rs.getInt("cnt");
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return total; 
	}
	
	/**
	 * 오늘의 총 입고완료 수를 반환
	 * @author 김예진
	 * @since 2025-06-27
	 * @return
	 */
	public int getTotalInboundCompleted(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) as cnt ");
		sql.append("FROM io_detail id ");
		sql.append("JOIN io_receipt ir ");
		sql.append("  ON id.io_receipt_id = ir.io_receipt_id ");
		sql.append("WHERE ir.scheduled_date = CURRENT_DATE() AND ir.io_type = \"IN\" and id.processed_date is not null");

		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
	            total = rs.getInt("cnt");
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return total; 
	}
	
	/**
	 * 오늘의 총 출고예정 수를 반환
	 * @author 김예진
	 * @since 2025-06-27
	 * @return
	 */
	public int getTotalOutboundToday(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) as cnt ");
		sql.append("FROM io_detail id ");
		sql.append("JOIN io_receipt ir ");
		sql.append("  ON id.io_receipt_id = ir.io_receipt_id ");
		sql.append("WHERE ir.scheduled_date = CURRENT_DATE() AND ir.io_type = \"OUT\"");

		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
	            total = rs.getInt("cnt");
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return total; 
	}
	
	/**
	 * 오늘의 총 출고완료 수를 반환
	 * @author 김예진
	 * @since 2025-06-27
	 * @return
	 */
	public int getTotalOutboundCompleted(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int total = 0;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) as cnt ");
		sql.append("FROM io_detail id ");
		sql.append("JOIN io_receipt ir ");
		sql.append("  ON id.io_receipt_id = ir.io_receipt_id ");
		sql.append("WHERE ir.scheduled_date = CURRENT_DATE() AND ir.io_type = \"OUT\" and id.processed_date is not null");

		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
	            total = rs.getInt("cnt");
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return total; 
	}

	/**
	 * 오늘 출고한 상품의 총 금액을 반환
	 * @return
	 */
	public int getTotalOutboundCompletedPrice() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int totalAmount = 0;

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT IFNULL(SUM(s.price * id.actual_quantity), 0) AS total_price ");
		sql.append("FROM io_detail id ");
		sql.append("JOIN io_receipt ir ON id.io_receipt_id = ir.io_receipt_id ");
		sql.append("JOIN snapshot s ON id.snapshot_id = s.snapshot_id ");
		sql.append("WHERE ir.io_type = 'OUT' ");
		sql.append("AND DATE(ir.processed_date) = CURRENT_DATE() ");
		sql.append("AND id.status = '완료' ");
		sql.append("AND id.active = TRUE ");
		sql.append("AND ir.active = TRUE");

		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				totalAmount = rs.getInt("total_price");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return totalAmount;
	}



	/**
	 * 최근 7일 간 일자별 입고 수량
	 * @author 김예진
	 * @since 2025-06-27
	 * @return
	 */
	public List<Map<String, Object>> get7daysInboundQuantity() {
		List<Map<String, Object>> resultMap = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("    DATE(ir.processed_date) AS \"입고일자\", ");
		sql.append("    SUM(id.actual_quantity) AS \"입고수량\" ");
		sql.append("FROM io_detail id ");
		sql.append("JOIN io_receipt ir ");
		sql.append("    ON id.io_receipt_id = ir.io_receipt_id ");
		sql.append("WHERE ir.processed_date IS NOT NULL ");
		sql.append("    AND ir.io_type = \"IN\" ");
		sql.append("    AND ir.processed_date >= CURRENT_DATE() - INTERVAL 6 DAY ");
		sql.append("GROUP BY DATE(ir.processed_date) ");
		sql.append("ORDER BY 입고일자 ASC");
		
		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Map<String, Object> map = new HashMap<>();
				map.put("입고일자", rs.getString("입고일자"));
				map.put("입고수량", rs.getInt("입고수량"));
				resultMap.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return resultMap;
	}
	
	/**
	 * 최근 7일 간 일자별 출고 수량
	 * @author 김예진
	 * @since 2025-06-27
	 * @return
	 */
	public List<Map<String, Object>> get7daysOutboundQuantity() {
		List<Map<String, Object>> resultMap = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("    DATE(ir.processed_date) AS \"출고일자\", ");
		sql.append("    SUM(id.actual_quantity) AS \"출고수량\" ");
		sql.append("FROM io_detail id ");
		sql.append("JOIN io_receipt ir ");
		sql.append("    ON id.io_receipt_id = ir.io_receipt_id ");
		sql.append("WHERE ir.processed_date IS NOT NULL ");
		sql.append("    AND ir.io_type = \"OUT\" ");
		sql.append("    AND ir.processed_date >= CURRENT_DATE() - INTERVAL 6 DAY ");
		sql.append("GROUP BY DATE(ir.processed_date) ");
		sql.append("ORDER BY 출고일자 ASC");
		
		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Map<String, Object> map = new HashMap<>();
				map.put("출고일자", rs.getString("출고일자"));
				map.put("출고수량", rs.getInt("출고수량"));
				resultMap.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return resultMap;
	}
	
	/**
	 *  최근 7일 간 일자별 입출고 수량
	 *  @author 김예진
	 *  @since 2025-06-27
	 * @return
	 */
	public List<Map<String, Object>> get7daysInOutQuantity() {
	    List<Map<String, Object>> inbound = get7daysInboundQuantity();
	    List<Map<String, Object>> outbound = get7daysOutboundQuantity();

	    Map<String, Map<String, Object>> merged = new LinkedHashMap<>();

	    // 입고 데이터 먼저 넣기
	    for (Map<String, Object> in : inbound) {
	        String date = (String) in.get("입고일자");
	        merged.putIfAbsent(date, new HashMap<>());
	        Map<String, Object> map = merged.get(date);
	        map.put("날짜", date);
	        map.put("입고수량", in.get("입고수량"));
	        map.put("출고수량", 0); // 초기값 0
	    }

	    // 출고 데이터 병합
	    for (Map<String, Object> out : outbound) {
	        String date = (String) out.get("출고일자");
	        merged.putIfAbsent(date, new HashMap<>());
	        Map<String, Object> map = merged.get(date);
	        map.put("날짜", date);
	        // 입고수량이 없으면 0으로 초기화
	        if (!map.containsKey("입고수량")) map.put("입고수량", 0);
	        map.put("출고수량", out.get("출고수량"));
	    }

	    // LinkedHashMap 값을 리스트로 변환
	    return new ArrayList<>(merged.values());
	}

	/**
	 * 최근 완료된 입고
	 * @return
	 */
	public List<IODetail> getRecentCompletedInboundDetails() {
		List<IODetail> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT id.io_receipt_id, id.io_detail_id, s.product_name, id.actual_quantity, id.processed_date ");
		sql.append("FROM io_detail id ");
		sql.append("JOIN io_receipt ir ON id.io_receipt_id = ir.io_receipt_id ");
		sql.append("JOIN snapshot s ON id.snapshot_id = s.snapshot_id ");
		sql.append("WHERE ir.io_type = 'IN' ");
		sql.append("AND id.status = '완료' ");
		sql.append("AND id.active = TRUE ");
		sql.append("AND ir.active = TRUE ");
		sql.append("ORDER BY id.processed_date DESC ");
		sql.append("LIMIT 5");

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				IODetail detail = new IODetail();
				IOReceipt receipt = new IOReceipt();
				Snapshot snapshot = new Snapshot();

				receipt.setIoReceiptId(rs.getInt("io_receipt_id"));
				detail.setIoReceipt(receipt);
				detail.setIoDetailId(rs.getInt("io_detail_id"));
				snapshot.setProductName(rs.getString("product_name"));
				detail.setProductSnapshot(snapshot);
				detail.setActualQuantity(rs.getInt("actual_quantity"));
				detail.setProccessedDate(rs.getDate("processed_date"));

				list.add(detail);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return list;
	}


	/**
	 * 최근 출고 완료
	 * @return
	 */
	public List<IODetail> getRecentCompletedOutboundDetails() {
		List<IODetail> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT id.io_receipt_id, id.io_detail_id, s.product_name, id.actual_quantity, id.processed_date ");
		sql.append("FROM io_detail id ");
		sql.append("JOIN io_receipt ir ON id.io_receipt_id = ir.io_receipt_id ");
		sql.append("JOIN snapshot s ON id.snapshot_id = s.snapshot_id ");
		sql.append("WHERE ir.io_type = 'OUT' ");
		sql.append("AND id.status = '완료' ");
		sql.append("AND id.active = TRUE ");
		sql.append("AND ir.active = TRUE ");
		sql.append("ORDER BY id.processed_date DESC ");
		sql.append("LIMIT 5");

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				IODetail detail = new IODetail();
				IOReceipt receipt = new IOReceipt();
				Snapshot snapshot = new Snapshot();

				receipt.setIoReceiptId(rs.getInt("io_receipt_id"));
				detail.setIoReceipt(receipt);
				detail.setIoDetailId(rs.getInt("io_detail_id"));
				snapshot.setProductName(rs.getString("product_name"));
				detail.setProductSnapshot(snapshot);
				detail.setActualQuantity(rs.getInt("actual_quantity"));
				detail.setProccessedDate(rs.getDate("processed_date"));

				list.add(detail);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return list;
	}


}
