package com.shinlogis.wms.inoutbound.outbound.repository;

import java.sql.Connection;
import java.sql.Date;
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
import com.shinlogis.wms.inoutbound.outbound.model.OrderDetail;
import com.shinlogis.wms.product.model.Product;

public class OrderDetailDAO {
	DBManager dbManager = DBManager.getInstance();
		/**
	 * <h2>주문상세 셀렉트 메서드 입니다.
	 *  @author 세형
	 * */
	public void selectAll() {
		
	}
	
	/**
	 * <h2>주문 상세 인서트 메서드입니다.
	 * @author 예닮
	 * */
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
	
	//주문상세가져오기 
	public List<OrderDetail> select(int pk) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		
		List<OrderDetail> list= new ArrayList();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ");
		sql.append("store_order_item soi  join product p ");
		sql.append("on soi.product_id = p.product_id ");
		sql.append("where soi.store_order_id =?");
		
		
		try {
			connection = dbManager.getConnection();
			try {
				pstmt = connection.prepareStatement(sql.toString());
				
				pstmt.setInt(1,pk); 
	        	rs=pstmt.executeQuery();
	        	
	        	while(rs.next()) {
	        		OrderDetail orderDetail= new OrderDetail();
	        		Product p = new Product();
	        		
	        		p.setPrice(rs.getInt("price"));
	        		p.setProductName(rs.getString("product_name"));
	        		
	        		orderDetail.setProduct(p);
	        		orderDetail.setStatus(rs.getString("status"));
	        		orderDetail.setQuantity(rs.getInt("quantity"));
	        		orderDetail.setOrderId(rs.getInt("store_order_id"));
	        		orderDetail.setItemId(rs.getInt("item_id"));
	        		
	        		list.add(orderDetail);
	        	}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}finally {
			dbManager.release(pstmt,rs);
		}
		return list;
	}
}
