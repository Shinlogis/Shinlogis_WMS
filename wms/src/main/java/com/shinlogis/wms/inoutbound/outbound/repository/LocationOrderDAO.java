package com.shinlogis.wms.inoutbound.outbound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;

public class LocationOrderDAO {
	DBManager dbManager = DBManager.getInstance();
	
	public List selectAllOrder() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList();
	
		con= dbManager.getConnection();
		
		try {
			StringBuffer sql = new StringBuffer();
			//지점 주문, 지점주문 상세 나눴기 때문에 조인 해놓음
			sql.append("select * "
					+ " from store_order so"
					+ " inner join store_order_item oi"
					+ " on so.store_order_id = oi.store_order_id");
			pstmt= con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				//모델 pull받고 다시오자
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
