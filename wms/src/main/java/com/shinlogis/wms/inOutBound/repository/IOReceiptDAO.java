package com.shinlogis.wms.inOutBound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.inOutBound.model.IOReceipt;
import com.shinlogis.wms.location.model.Location;

/**
 * 입출력예정 전표 DAO입니다.
 *  @author 김예진
 */
public class IOReceiptDAO {
	DBManager dbManager = DBManager.getInstance();

	/**
	 * 입고 전표 select
	 * 
	 * @return
	 * @author 김예진
	 * @since 2025-06-19
	 */
	public List<IOReceipt> selectAllInbound() {
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;

		ArrayList<IOReceipt> result = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("r.io_receipt_id, r.io_type, r.user_id, u.id, u.email, ");
		sql.append("r.created_at, r.scheduled_date, r.proccessed_date, r.status, ");
		sql.append("r.location_id, l.location_name, l.address ");
		sql.append("FROM io_receipt r ");
		sql.append("JOIN headquarters_user u ON r.user_id = u.headquarters_user_id ");
		sql.append("JOIN location l ON r.location_id = l.location_id ");
		sql.append("WHERE r.io_type = 'IN'");

		// 입력한 검색어에 맞게 WHERE 조건 추가해야 함

		connection = dbManager.getConnection();
		try {
			pStatement = connection.prepareStatement(sql.toString());
			resultSet = pStatement.executeQuery(); // 쿼리 수행

			while (resultSet.next()) { // 값이 있으면 반복
				IOReceipt ioReceipt = new IOReceipt();
				ioReceipt.setIoReceiptId(resultSet.getInt("io_receipt_id"));
				ioReceipt.setIoType(resultSet.getString("io_type"));

				// 본사유저
				HeadquartersUser user = new HeadquartersUser();
				user.setHeadquartersUserId(resultSet.getInt("user_id"));
				user.setId(resultSet.getString("id"));
				//user.setEmail(resultSet.getString("email"));
				ioReceipt.setUser(user);

				ioReceipt.setCreatedAt(resultSet.getDate("created_at"));
				ioReceipt.setScheduledDate(resultSet.getDate("scheduled_date"));
				if (resultSet.getDate("proccessed_date") != null) {
					ioReceipt.setProcessedDate(resultSet.getDate("proccessed_date"));
				}
				ioReceipt.setStatus(resultSet.getString("status"));

				// 지점
				Location location = new Location();
				location.setLocationId(resultSet.getInt("location_id"));
				location.setLocationName(resultSet.getString("location_name"));
				location.setAddress(resultSet.getString("address"));
				ioReceipt.setLocation(location);

				result.add(ioReceipt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pStatement, resultSet);
		}
		return result;
	}

	/**
	 * 입고 전표 별 첫 번째 상품명, 공급사명, 품목 수량을 조회하는 메서드
	 * 
	 * @return 전표 ID, 첫 상품명, 공급사명, 품목 개수를 포함한 Map 리스트
	 * @author 김예진
	 * @since 2025-06-20
	 */
	public List<Map<String, Object>> selectFirstProductAndItemCountByReceipt() {
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		List<Map<String, Object>> result = new ArrayList<>();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT ");
		sql.append("ir.io_receipt_id, ");
		sql.append("( ");
		sql.append("    SELECT s.product_name ");
		sql.append("    FROM io_plan_item ipi2 ");
		sql.append("    JOIN snapshot s ON s.snapshot_id = ipi2.product_snapshot ");
		sql.append("    WHERE ipi2.io_receipt_id = ir.io_receipt_id ");
		sql.append("    LIMIT 1 ");
		sql.append(") AS first_product_name, ");
		sql.append("( ");
		sql.append("    SELECT s.supplier_name ");
		sql.append("    FROM io_plan_item ipi2 ");
		sql.append("    JOIN snapshot s ON s.snapshot_id = ipi2.product_snapshot ");
		sql.append("    WHERE ipi2.io_receipt_id = ir.io_receipt_id ");
		sql.append("    LIMIT 1 ");
		sql.append(") AS supplier_name, ");
		sql.append("( ");
		sql.append("    SELECT COUNT(*) ");
		sql.append("    FROM io_plan_item ipi3 ");
		sql.append("    WHERE ipi3.io_receipt_id = ir.io_receipt_id ");
		sql.append(") AS item_count ");
		sql.append("FROM io_receipt ir ");
		sql.append("WHERE ir.io_type = 'IN' ");


		try {
			connection = dbManager.getConnection();
			pStatement = connection.prepareStatement(sql.toString());
			resultSet = pStatement.executeQuery();

			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<>();
				row.put("io_receipt_id", resultSet.getInt("io_receipt_id"));
				row.put("first_product_name", resultSet.getString("first_product_name"));
				row.put("supplier_name", resultSet.getString("supplier_name"));
				row.put("item_count", resultSet.getInt("item_count"));
				result.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pStatement, resultSet);
		}
		return result;
	}
	

}
