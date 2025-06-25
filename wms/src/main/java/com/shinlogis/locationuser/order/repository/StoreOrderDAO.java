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
import com.shinlogis.wms.common.Exception.OrderInsertException;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.product.model.Product;

public class StoreOrderDAO {
	DBManager dbManager = DBManager.getInstance();

	//주문서 inser하기 
	public void insertStoreOrder(StoreOrder storeOrder) throws OrderInsertException{
		List<Product> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("insert store_order(order_date,location_id,total_price) values(NOW(),?,?)");

		Connection connection = null;
		PreparedStatement pstmt = null; 
		ResultSet rs =null;
		int result=0;

		try {
			connection = dbManager.getConnection();
			try {
				pstmt = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				pstmt.setInt(1,storeOrder.getLocationId());
				pstmt.setInt(2,storeOrder.getTotalPrice());
				
				result =pstmt.executeUpdate();
				if(result<1) {
					throw new OrderInsertException("주문 실패했습니다");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new OrderInsertException("주문 실패했습니다",e);
			}
		} finally {
			dbManager.release(pstmt,rs);
		}
	}
	
	// 주문PK (id) 얻어오기
	public int getRecentId(){
	    String sql= "select last_insert_id() as  orderId"; 
	    
	    Connection connection = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    int orderId = 0;

	    try {
	        connection = dbManager.getConnection();
	        try {
				pstmt = connection.prepareStatement(sql);
				rs = pstmt.executeQuery();

		        if(rs.next()){
		            orderId = rs.getInt("orderId");
		        }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
	    } finally {
	    	dbManager.release(pstmt,rs);
	    }
	    
	    return orderId;
	}
	
	
	//주문서 가져오기 
	public int selectStoreOrder(){
	    String sql= "select last_insert_id() as  orderId"; 
	    
	    Connection connection = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    int orderId = 0;

	    try {
	        connection = dbManager.getConnection();
	        try {
				pstmt = connection.prepareStatement(sql);
				rs = pstmt.executeQuery();

		        if(rs.next()){
		            orderId = rs.getInt("orderId");
		        }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
	    } finally {
	    	dbManager.release(pstmt,rs);
	    }
	    
	    return orderId;
	}
}
