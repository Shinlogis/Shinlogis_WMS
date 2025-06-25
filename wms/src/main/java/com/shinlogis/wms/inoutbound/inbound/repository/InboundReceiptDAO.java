package com.shinlogis.wms.inoutbound.inbound.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.inoutbound.model.IOReceipt;

/**
 * 입고예정 전표 DAO입니다.
 *
 * @author 김예진
 */
public class InboundReceiptDAO {
    DBManager dbManager = DBManager.getInstance();



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
        sql.append("SELECT ")
                .append("  r.io_receipt_id, ")
                .append("  r.io_type, ")
                .append("  r.user_id, ")
                .append("  u.id       AS user_id_str, ")
                .append("  r.status, ")
//                .append("  u.email, ")
                .append("  r.created_at, ")
                .append("  r.scheduled_date, ")
                .append("  r.processed_date, ")
                .append("  r.location_id, ")
                .append("  sf.supplier_name, ")
                // 첫 상품명
                .append("  ( ")
                .append("    SELECT s.product_name ")
                .append("    FROM io_detail d ")
                .append("    JOIN snapshot s ")
                .append("      ON s.snapshot_id = d.snapshot_id ")
                .append("    WHERE d.io_receipt_id = r.io_receipt_id ")
                .append("    ORDER BY d.io_detail_id ")
                .append("    LIMIT 1 ")
                .append("  ) AS first_product_name, ")
                // 첫 공급사명
                .append("  ( ")
                .append("    SELECT s.supplier_name ")
                .append("    FROM io_detail d ")
                .append("    JOIN snapshot s ")
                .append("      ON s.snapshot_id = d.snapshot_id ")
                .append("    WHERE d.io_receipt_id = r.io_receipt_id ")
                .append("    ORDER BY d.io_detail_id ")
                .append("    LIMIT 1 ")
                .append("  ) AS supplier_name, ")
                // 아이템 개수
                .append("  ( ")
                .append("    SELECT COUNT(*) ")
                .append("    FROM io_detail d ")
                .append("    WHERE d.io_receipt_id = r.io_receipt_id ")
                .append("  ) AS item_count ")
                .append("FROM io_receipt r ")
                .append("JOIN headquarters_user u ")
                .append("  ON r.user_id = u.headquarters_user_id ")
                .append("JOIN location l ")
                .append("  ON r.location_id = l.location_id ")

                .append("JOIN io_detail df ON df.io_receipt_id = r.io_receipt_id ")
                .append("JOIN snapshot sf ON sf.snapshot_id = df.snapshot_id ")
                .append("WHERE r.io_type = 'IN'");

        // 검색 필터 추가
        List<Object> params = new ArrayList<>();
        if (filters.get("io_receipt_id") != null) {
            sql.append("AND r.io_receipt_id = ? ");
            params.add(filters.get("io_receipt_id"));
        }
        if (filters.get("scheduled_date") != null) {
            sql.append("AND DATE(r.scheduled_date) = ? ");
            params.add(filters.get("scheduled_date"));
        }
        if (filters.get("product_name") != null) {
            sql.append("AND sf.product_name = ? ");
            params.add(filters.get("product_name"));
        }
        if (filters.get("supplier_name") != null) {
            sql.append("AND sf.supplier_name = ? ");
            params.add(filters.get("supplier_name"));
        }
        if (filters.get("status") != "전체" && filters.get("status") != null) {
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
//                u.setEmail(rs.getString("email")); // TODO: setter을 수정 필요
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
     * 입고 전표 select
     *
     * @return
     * @author 김예진
     * @since 2025-06-19
     */
//    public List<IOReceipt> selectInboundReceipts() {
//        Connection connection = null;
//        PreparedStatement pStatement = null;
//        ResultSet resultSet = null;
//
//        ArrayList<IOReceipt> result = new ArrayList<>();
//
//        StringBuffer sql = new StringBuffer();
//        sql.append("SELECT ");
//        sql.append("r.io_receipt_id, r.io_type, r.user_id, u.id, u.email, ");
//        sql.append("r.created_at, r.scheduled_date, r.processed_date, ");
//        sql.append("r.location_id, l.location_name, l.address ");
//        sql.append("FROM io_receipt r ");
//        sql.append("JOIN headquarters_user u ON r.user_id = u.headquarters_user_id ");
//        sql.append("JOIN location l ON r.location_id = l.location_id ");
//        sql.append("WHERE r.io_type = 'IN'");
//
//        connection = dbManager.getConnection();
//        try {
//            pStatement = connection.prepareStatement(sql.toString());
//            resultSet = pStatement.executeQuery(); // 쿼리 수행
//
//            while (resultSet.next()) { // 값이 있으면 반복
//                IOReceipt ioReceipt = new IOReceipt();
//                ioReceipt.setIoReceiptId(resultSet.getInt("io_receipt_id"));
//                ioReceipt.setIoType(resultSet.getString("io_type"));
//
//                // 본사유저
//                HeadquartersUser user = new HeadquartersUser();
//                user.setHeadquartersUserId(resultSet.getInt("user_id"));
//                user.setId(resultSet.getString("id"));
//                user.setEmail(resultSet.getString("email"));
//                ioReceipt.setUser(user);
//
//                ioReceipt.setCreatedAt(resultSet.getDate("created_at"));
//                ioReceipt.setScheduledDate(resultSet.getDate("scheduled_date"));
//                if (resultSet.getDate("processed_date") != null) {
//                    ioReceipt.setProcessedDate(resultSet.getDate("processed_date"));
//                }
//
//                // 지점
//                Location location = new Location();
//                location.setLocationId(resultSet.getInt("location_id"));
//                location.setLocationName(resultSet.getString("location_name"));
//                location.setAddress(resultSet.getString("address"));
//                ioReceipt.setLocation(location);
//
//                result.add(ioReceipt);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            dbManager.release(pStatement, resultSet);
//        }
//        return result;
//    }

    /**
     * 전체 입고 품목 상세 조회
     */
//    public List<IOReceipt> selectIODetails() {
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet resultSet = null;
//        String sql =
//                "SELECT ip.io_detail_id, ip.planned_quantity, ip.damage_quantity, ip.actual_quantity, " +
//                        "       ip.processed_date, ip.status, " +
//                        "       ir.io_receipt_id, ir.scheduled_date, " +
//                        "       s.snapshot_id, s.product_code, s.product_name, " +
//                        "       s.storage_type_code, s.supplier_name, s.price, s.expiry_date, " +
//                        "       dc.damage_code_id, dc.name AS damage_code_name, " +
//                        "       hu.headquarters_user_id, hu.id AS hq_user_id, hu.email AS hq_user_email " +
//                        "FROM io_detail ip " +
//                        "JOIN io_receipt      ir  ON ip.io_receipt_id    = ir.io_receipt_id " +
//                        "JOIN snapshot        s   ON ip.snapshot_id      = s.snapshot_id " +
//                        "JOIN damaged_code    dc  ON ip.damage_code_id   = dc.damage_code_id " +
//                        "JOIN headquarters_user hu ON ip.headquarters_user_id = hu.headquarters_user_id " +
//                        "WHERE ir.io_type = 'IN'";
//
//        List<IOReceipt> list = new ArrayList<>();
//        try {
//            conn = dbManager.getConnection();
//            ps = conn.prepareStatement(sql);
//            resultSet = ps.executeQuery();
//
//            while (resultSet.next()) {
//            	IOReceipt ioReceipt = new IOReceipt();
//				ioReceipt.setIoReceiptId(resultSet.getInt("io_receipt_id"));
//				ioReceipt.setIoType(resultSet.getString("io_type"));
//				
//
//				// 본사유저
//				HeadquartersUser user = new HeadquartersUser();
//				user.setHeadquartersUserId(resultSet.getInt("user_id"));
//				user.setId(resultSet.getString("id"));
//				user.setEmail(resultSet.getString("email"));
//				ioReceipt.setUser(user);
//				
//
//				ioReceipt.setCreatedAt(resultSet.getDate("created_at"));
//				ioReceipt.setScheduledDate(resultSet.getDate("scheduled_date"));					
//				if (resultSet.getDate("proccessed_date") != null) {
//					ioReceipt.setProcessedDate(resultSet.getDate("proccessed_date"));
//				}
//				ioReceipt.setStatus(resultSet.getString("status"));
//
//				// 공급사
//				ioReceipt.setSupplierName(resultSet.getString("supplier_name"));
//				
//
//				list.add(ioReceipt);
//			
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("selectIODetails 실패", e);
//        } finally {
//            dbManager.release(ps, resultSet);
//        }
//
//        return list;
//    }

    /**
     *검색한 상품이 포함된 입출고전표
     * SELECT DISTINCT r.*
     * FROM io_receipt r
     * JOIN io_detail d ON r.io_receipt_id = d.io_receipt_id
     * JOIN snapshot s ON d.snapshot_id = s.snapshot_id
     * WHERE s.product_name LIKE '%1%'
     *    OR s.product_code LIKE '%1%';
     * */

    /**
     * 검색 조건에 맞게 입고 전표를 검색하는 메서드
     *
     * @param filters Map<검색조건, 검색어>
     * @return
     * @auther 김예진
     * @since 2025-06-22
     */
//    public List<IOReceipt> selectInboundReceipts(Map<String, Object> filters) {
//        List<IOReceipt> result = new ArrayList<>();
//        Connection connection = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        // 파라미터 보관할 리스트
//        List<Object> params = new ArrayList<>();
//
//        StringBuffer sql = new StringBuffer();
//        sql.append("SELECT ");
//        sql.append("r.io_receipt_id, r.io_type, r.user_id, u.id, u.email, ");
//        sql.append("r.created_at, r.scheduled_date, r.processed_date, ");
//        sql.append("r.location_id, l.location_name, l.address ");
//        sql.append("FROM io_receipt r ");
//        sql.append("JOIN headquarters_user u ON r.user_id = u.headquarters_user_id ");
//        sql.append("JOIN location l ON r.location_id = l.location_id ");
//        sql.append("WHERE r.io_type = 'IN' ");
//
//        // 파라미터로 받아온 검색 조건에 맞게 동적으로 조건을 추가
//        if (filters.get("io_receipt_id") != null) { // 검색조건 Map filters에 입고예정id io_receipt_id의 값이 있는 경우
//            // 검색 조건에 맞는 조건 추가
//            sql.append("AND r.io_receipt_id = ?");
//            // 파라미터 리스트에 조건 추가
//            params.add(filters.get("io_receipt_id"));
//        }
//        // 입고예정일자 조건 추가
//        if (filters.get("scheduled_date") != null) {
//            sql.append("AND r.scheduled_date = ?");
//            params.add(filters.get("scheduled_date"));
//        }
//        // TODO: 상품코드, 상품명, 공급사명 필터 추가, 이 테이블에 status가 없으면 계산이 너무 복잡해져서 status넣을지 상의
////		if (filters.get("status") != null){
////			sql.append("AND r.scheduled_date = ?");
////		}
//
//        try {
//            connection = dbManager.getConnection();
//            pstmt = connection.prepareStatement(sql.toString());
//
//            // 검색조건 파라미터 바인딩
//            for (int i = 0; i < params.size(); i++) {
//                // 파라미터의 타입이 항상 변하므로 setObject로 바인딩
//                pstmt.setObject(i + 1, params.get(i));
//            }
//
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                IOReceipt ioReceipt = new IOReceipt();
//                ioReceipt.setIoReceiptId(rs.getInt("io_receipt_id"));
//
//                // 본사유저
//                HeadquartersUser user = new HeadquartersUser();
//                user.setHeadquartersUserId(rs.getInt("user_id"));
//                user.setId(rs.getString("id"));
//                user.setEmail(rs.getString("email"));
//
//                ioReceipt.setCreatedAt(rs.getDate("created_at"));
//                ioReceipt.setScheduledDate(rs.getDate("scheduled_date"));
//                ioReceipt.setProcessedDate(rs.getTimestamp("processed_date") != null ? rs.getDate("processed_date") : null);
//
//
//                result.add(ioReceipt);
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        return result;
//    }

    /**
     * 입고 전표 별 첫 번째 상품명, 공급사명, 품목 수량을 조회하는 메서드
     *
     * @return 전표 ID, 첫 상품명, 공급사명, 품목 개수를 포함한 Map 리스트
     * @author 김예진
     * @since 2025-06-20
     */
//    public List<Map<String, Object>> selectFirstProductAndItemCount() {
//        Connection connection = null;
//        PreparedStatement pStatement = null;
//        ResultSet resultSet = null;
//        List<Map<String, Object>> result = new ArrayList<>();
//        StringBuffer sql = new StringBuffer();
//
//        sql.append("SELECT ");
//        sql.append("ir.io_receipt_id, ");
//        sql.append("( ");
//        sql.append("    SELECT s.product_name ");
//        sql.append("    FROM io_detail ipi2 ");
//        sql.append("    JOIN snapshot s ON s.snapshot_id = ipi2.snapshot_id ");
//        sql.append("    WHERE ipi2.io_receipt_id = ir.io_receipt_id ");
//        sql.append("    LIMIT 1 ");
//        sql.append(") AS first_product_name, ");
//        sql.append("( ");
//        sql.append("    SELECT s.supplier_name ");
//        sql.append("    FROM io_detail ipi2 ");
//        sql.append("    JOIN snapshot s ON s.snapshot_id = ipi2.snapshot_id ");
//        sql.append("    WHERE ipi2.io_receipt_id = ir.io_receipt_id ");
//        sql.append("    LIMIT 1 ");
//        sql.append(") AS supplier_name, ");
//        sql.append("( ");
//        sql.append("    SELECT COUNT(*) ");
//        sql.append("    FROM io_detail ipi3 ");
//        sql.append("    WHERE ipi3.io_receipt_id = ir.io_receipt_id ");
//        sql.append(") AS item_count ");
//        sql.append("FROM io_receipt ir ");
//        sql.append("WHERE ir.io_type = 'IN' ");
//
//
//        try {
//            connection = dbManager.getConnection();
//            pStatement = connection.prepareStatement(sql.toString());
//            resultSet = pStatement.executeQuery();
//
//            while (resultSet.next()) {
//                Map<String, Object> row = new HashMap<>();
//                row.put("io_receipt_id", resultSet.getInt("io_receipt_id"));
//                row.put("first_product_name", resultSet.getString("first_product_name"));
//                row.put("supplier_name", resultSet.getString("supplier_name"));
//                row.put("item_count", resultSet.getInt("item_count"));
//                result.add(row);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            dbManager.release(pStatement, resultSet);
//        }
//        return result;
//    }

//    public Map<String, Object> selectFirstProductAndItemCount(int ioReceiptId) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT ")
//                .append("  ir.io_receipt_id, ")
//                .append("  (")
//                .append("    SELECT s.product_name ")
//                .append("    FROM io_detail d ")
//                .append("    JOIN snapshot s ON s.snapshot_id = d.snapshot_id ")
//                .append("    WHERE d.io_receipt_id = ir.io_receipt_id ")
//                .append("    LIMIT 1")
//                .append("  ) AS first_product_name, ")
//                .append("  (")
//                .append("    SELECT s.supplier_name ")
//                .append("    FROM io_detail d ")
//                .append("    JOIN snapshot s ON s.snapshot_id = d.snapshot_id ")
//                .append("    WHERE d.io_receipt_id = ir.io_receipt_id ")
//                .append("    LIMIT 1")
//                .append("  ) AS supplier_name, ")
//                .append("  (")
//                .append("    SELECT COUNT(*) ")
//                .append("    FROM io_detail d ")
//                .append("    WHERE d.io_receipt_id = ir.io_receipt_id")
//                .append("  ) AS item_count ")
//                .append("FROM io_receipt ir ")
//                .append("WHERE ir.io_type = 'IN' ")
//                .append("  AND ir.io_receipt_id = ?");
//
//        Map<String, Object> result = new HashMap<>();
//
//        try (
//                Connection conn = dbManager.getConnection();
//                PreparedStatement pstmt = conn.prepareStatement(sql.toString())
//        ) {
//            // 파라미터 바인딩
//            pstmt.setInt(1, ioReceiptId);
//
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    result.put("io_receipt_id", rs.getInt("io_receipt_id"));
//                    result.put("first_product_name", rs.getString("first_product_name"));
//                    result.put("supplier_name", rs.getString("supplier_name"));
//                    result.put("item_count", rs.getInt("item_count"));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }


}
