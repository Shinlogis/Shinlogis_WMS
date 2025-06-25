package com.shinlogis.locationuser.order.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import java.sql.Statement;  


import com.shinlogis.locationuser.order.model.StoreOrder;
import com.shinlogis.locationuser.order.model.StoreOrderItem;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.product.model.Product;

public class OrderDAO {
	DBManager dbManager = DBManager.getInstance();

	//주문서 inser하기 
	public void insertStoreOrder(StoreOrder storeOrder) {
		List<Product> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("insert store_order(order_date,location_id,total_price) values(NOW(),?,?)");
		StringBuffer sql2 = new StringBuffer("insert store_order_item(store_order_id,product_id,quantity) values(?,?,?)");

		Connection connection = null;
		PreparedStatement pstmt = null; //store_order 용도 
		PreparedStatement pstmt2= null; //store_order_item 용도 
		ResultSet rs =null;

		try {
			connection = dbManager.getConnection();
			try {
				pstmt = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				pstmt.setInt(1,storeOrder.getLocationId());
				pstmt.setInt(2,storeOrder.getTotalPrice());
				int result =pstmt.executeUpdate();
				
				if(result>0) {
					//자동생성된 store_order_id 가져오기
			        rs = pstmt.getGeneratedKeys();
			        int storeOrderId = 0;
			        if (rs.next()) {
			            storeOrderId = rs.getInt(1);
			        }
			        
			        //주문서에 포함된 아이템들 디비에 insert 
			        pstmt2 = connection.prepareStatement(sql2.toString());
			        int result2=0; //아이템 insert 실행 횟수 
			        for (StoreOrderItem item : storeOrder.getItems()) {
			        	pstmt2.setInt(1, storeOrderId); 
			        	pstmt2.setInt(2, item.getProductId());
			        	pstmt2.setInt(3, item.getQuantity());
			        	
			        	result2+=pstmt2.executeUpdate();  
			        }
			        
			        if(result2==storeOrder.getItems().size()) {
			        	JOptionPane optionPane = new JOptionPane("주문이 완료되었습니다!", JOptionPane.INFORMATION_MESSAGE);
			        	JDialog dialog = optionPane.createDialog("알림");
			        	dialog.setLocation(715, 320); // 원하는 좌표
			        	dialog.setVisible(true); 
			        	
			        }
				}
				

				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			dbManager.release(pstmt,rs);
		}

	
	}

}
