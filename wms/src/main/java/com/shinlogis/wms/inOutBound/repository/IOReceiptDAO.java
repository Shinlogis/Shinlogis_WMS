package com.shinlogis.wms.inOutBound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.inOutBound.model.IOReceipt;
import com.shinlogis.wms.location.model.Location;

/**
 * 입출력예정 전표 DAO입니다.
 */
public class IOReceiptDAO {
	DBManager dbManager = DBManager.getInstance();

	/**
	 * 입출고 전표 테이블에서 입고 항목 select
	 * @return
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
		sql.append("r.created_at, r.scheduled_date, r.processed_date, r.status, ");
		sql.append("r.location_id, l.location_name, l.address ");
		sql.append("FROM io_receipt r ");
		sql.append("JOIN headquarters_user u ON r.user_id = u.headquarters_user_id ");
		sql.append("JOIN location l ON r.location_id = l.location_id ");
		sql.append("WHERE r.io_type = 'IN'");
		// 입력한 검색어에 맞게 WHERE 조건 추가
		
		connection = dbManager.getConnection();
		try {
			pStatement = connection.prepareStatement(sql.toString());
			resultSet = pStatement.executeQuery(); // 쿼리 수행
			
			while(resultSet.next()) { // 값이 있으면 반복
				IOReceipt ioReceipt = new IOReceipt();
				ioReceipt.setIoReceiptId(resultSet.getInt("io_receipt_id"));
				ioReceipt.setIoType(resultSet.getString("io_type"));
				
				// 본사유저
				HeadquartersUser user = new HeadquartersUser();
				user.setHeadquartersUserId(resultSet.getInt("user_id"));
				user.setId(resultSet.getString("id"));
				user.setEmail(resultSet.getString("email"));
				ioReceipt.setUser(user);
				
				ioReceipt.setCreatedAt(resultSet.getDate("created_at"));
				ioReceipt.setScheduledDate(resultSet.getDate("scheduled_date"));
				if (resultSet.getDate("processed_date") != null) {
					ioReceipt.setProcessedDate(resultSet.getDate("processed_date"));					
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
}
