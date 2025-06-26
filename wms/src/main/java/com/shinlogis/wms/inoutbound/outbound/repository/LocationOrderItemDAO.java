package com.shinlogis.wms.inoutbound.outbound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.locationuser.order.model.StoreOrder;
import com.shinlogis.locationuser.order.model.StoreOrderItem;
import com.shinlogis.wms.common.util.DBManager;

public class LocationOrderItemDAO {
	DBManager dbManager = DBManager.getInstance();
	
	public List selectAllOrderItem() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList();
		
		con = dbManager.getConnection();
		try {
			StringBuffer sql = new StringBuffer();
			//지점 주문, 지점주문 상세 나눴기 때문에 조인 해놓음
			sql.append("select *"
					+ " from store_oreder_item oi"
					+ " inner join store_order so"
					+ " on so.store_order_id = oi.store_order_id");
			
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				StoreOrderItem item = new StoreOrderItem();
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return list;
	}
}
