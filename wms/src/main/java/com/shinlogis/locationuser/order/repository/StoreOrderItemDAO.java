package com.shinlogis.locationuser.order.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.shinlogis.locationuser.order.model.StoreOrder;
import com.shinlogis.locationuser.order.model.StoreOrderItem;
import com.shinlogis.wms.common.Exception.OrderInsertException;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.product.model.Product;

public class StoreOrderItemDAO {
	DBManager dbManager = DBManager.getInstance();
	
	//주문상세 insert하기 
	public void insert(StoreOrderItem storeOrderItem) throws  OrderInsertException{
		
		StringBuffer sql = new StringBuffer("insert store_order_item(store_order_id,product_id,quantity) values(?,?,?)");

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;

		try {
			connection = dbManager.getConnection();
			try {
				pstmt = connection.prepareStatement(sql.toString());
				int result=0;
				
				pstmt.setInt(1, storeOrderItem.getStoreOrderId()); 
	        	pstmt.setInt(2, storeOrderItem.getProduct().getProductId());
	        	pstmt.setInt(3, storeOrderItem.getQuantity());
	        	pstmt.executeUpdate();
				
			        
		       
			} catch (SQLException e) {
				e.printStackTrace();
				throw new OrderInsertException("주문 상세 등록 실패했습니다",e);
			}
		}finally {
			dbManager.release(pstmt,rs);
		}
	}
}
