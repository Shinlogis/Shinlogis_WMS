package com.shinlogis.locationuser.order.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	public void insert(StoreOrder storeOrder) throws OrderInsertException{
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
	    String sql= "select last_insert_id() as orderId"; 
	    
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
	
	
	//주문목록 전체 가져오기 
	public List<StoreOrder> selectAll(){
	    Connection connection = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    List<StoreOrder> list=new ArrayList() ;

	    try {
	        connection = dbManager.getConnection();
	        try {
	        	StringBuffer sql = new StringBuffer();
				//지점 주문, 지점주문 상세 나눴기 때문에 조인 해놓음
	        	
				sql.append("select soi.store_order_id as store_order_id,count(p.product_id) as cnt,so.order_date as order_date,total_price");
				sql.append(" from store_order so inner join store_order_item soi inner join product p ");
				sql.append(" on so.store_order_id =soi.store_order_id and soi.product_id= p.product_id");
				sql.append(" GROUP by soi.store_order_id,so.order_date ");
				sql.append(" order by so.store_order_id asc ");


				pstmt= connection.prepareStatement(sql.toString());
				rs = pstmt.executeQuery();
				

				while (rs.next()) {
					StoreOrder storeOrder= new StoreOrder();
					storeOrder.setStoreOrderId(rs.getInt("store_order_id"));
					storeOrder.setCnt(rs.getInt("cnt"));
					//storeOrder.setQuantity(rs.getInt("quantity"));
					storeOrder.setOrderDate(rs.getString("order_date"));
					storeOrder.setTotalPrice(rs.getInt("total_price"));
					
					sql.delete(0, sql.length());
					sql.append("select distinct(p.product_id) as product_id, product_name,price");
					sql.append(" from product p join store_order_item soi"); 
					sql.append(" on p.product_id =soi.product_id");
					sql.append(" and p.product_id in (");
					sql.append(" select product_id from store_order_item where store_order_id=?)");
					
					PreparedStatement pstmt2= null;
					pstmt2= connection.prepareStatement(sql.toString());
					 
					pstmt2.setInt(1, rs.getInt("store_order_id"));
					ResultSet rs2 =pstmt2.executeQuery();
					
					List<StoreOrderItem> itemList=new ArrayList();
					while(rs2.next()) {
						StoreOrderItem item= new StoreOrderItem();
						Product product = new Product();
						product.setProductId(rs2.getInt("product_id"));
						product.setProductName(rs2.getString("product_name"));
						product.setPrice(rs2.getInt("price"));
						item.setProduct(product);
						itemList.add(item);
						
					}
					
					storeOrder.setItems(itemList);
					list.add(storeOrder);
					dbManager.release(pstmt2, rs2);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    } finally {
	    	dbManager.release(pstmt,rs);
	    }
	    
	    return list;
	}
}
