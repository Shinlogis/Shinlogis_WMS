package com.shinlogis.wms.inoutbound.outbound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.Exception.OrderInsertException;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.inoutbound.outbound.model.Order;
import com.shinlogis.wms.inoutbound.outbound.model.OrderDetail;
import com.shinlogis.wms.location.model.Location;
import com.shinlogis.wms.product.model.Product;
/**
 * <h2>지점 주문 DAO
 * <li>쿼리문 및 변수들은 StoreOrder 대부분 인용 @author 예닮
 * 
 * @author 이세형
 */
public class OrderDAO {
	DBManager dbManager = DBManager.getInstance();

	//주문서 inser하기 
	public void insert(Order order) {
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
//				pstmt.setInt(1,order.getLocation().getLocationId());//이거 어떡할거임
				pstmt.setInt(2,order.getTotalPrice());
				
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
	public List<Order> selectAll(){
	    Connection connection = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    List<Order> list=new ArrayList() ;

	    try {
	        connection = dbManager.getConnection();
	        try {
	        	StringBuffer sql = new StringBuffer();
				//지점 주문, 지점주문 상세 나눴기 때문에 조인 해놓음
	        	
	        	sql.append("SELECT so.store_order_id, ");
	        	sql.append("       so.order_date, ");
	        	sql.append("       so.total_price, ");
	        	sql.append("       so.location_id, ");
	        	sql.append("       l.location_name, ");
	        	sql.append("       COUNT(p.product_id) AS count ");
	        	sql.append("FROM store_order so ");
	        	sql.append("INNER JOIN store_order_item soi ON so.store_order_id = soi.store_order_id ");
	        	sql.append("INNER JOIN product p ON soi.product_id = p.product_id ");
	        	sql.append("INNER JOIN location l ON so.location_id = l.location_id ");
	        	sql.append("GROUP BY so.store_order_id, so.order_date, so.total_price, so.location_id ");
	        	sql.append("ORDER BY so.store_order_id DESC");



				pstmt= connection.prepareStatement(sql.toString());
				rs = pstmt.executeQuery();
				

				while (rs.next()) {
					Order order= new Order();
					order.setStoreOrderId(rs.getInt("store_order_id"));
					order.setCount(rs.getInt("count"));
					order.setOrderDate(rs.getString("order_date"));
					
					Location location = new Location();
					location.setLocationId(rs.getInt("so.location_id"));
					location.setLocationName(rs.getString("l.location_name"));
					order.setLocation(location);
//					order.getLocation().setLocationName(rs.getString("l.location_name"));
					
					sql.delete(0, sql.length());
					sql.append("select p.product_id as product_id, product_name,price,soi.quantity as quantity,store_order_id");
					sql.append(" from product p join store_order_item soi"); 
					sql.append(" on p.product_id =soi.product_id");
					sql.append(" where soi.store_order_id=?");
					
					PreparedStatement pstmt2= null;
					pstmt2= connection.prepareStatement(sql.toString());
					 
					pstmt2.setInt(1, rs.getInt("store_order_id"));
					ResultSet rs2 =pstmt2.executeQuery();
					
					List<OrderDetail> itemList=new ArrayList();
					while(rs2.next()) {
						OrderDetail item= new OrderDetail();
						Product product = new Product();
						product.setProductId(rs2.getInt("product_id"));
						product.setProductName(rs2.getString("product_name"));
						product.setPrice(rs2.getInt("price"));
						item.setProduct(product);
						item.setQuantity(rs2.getInt("quantity"));
						item.setOrderId(rs2.getInt("store_order_id"));
						itemList.add(item);
					}
					
					order.setItems(itemList);
					list.add(order);
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
	
	//날짜로 주문목록 검색 
	public List<Order> selectByDate(String date) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<Order> list = new ArrayList<>();
		

		try {
			connection = dbManager.getConnection();
			try {
				StringBuffer sql = new StringBuffer();
				sql.append("select * ");
				sql.append(" from store_order so inner join store_order_item soi inner join product p");
				sql.append(" on so.store_order_id =soi.store_order_id and soi.product_id= p.product_id");
//				sql.append(" GROUP by soi.store_order_id,so.order_date ");
				sql.append(" having DATE(so.order_date) = ?");
				sql.append(" order by so.store_order_id desc ");
				pstmt = connection.prepareStatement(sql.toString());
				pstmt.setString(1, date);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					Order order= new Order();
					order.setStoreOrderId(rs.getInt("store_order_id"));
					order.setCount(rs.getInt(""));
					order.setOrderDate(rs.getString("order_date"));
					order.setTotalPrice(rs.getInt("total_price"));
					order.getLocation().setLocationId(rs.getInt("location_id"));
					
					sql.delete(0, sql.length());
					sql.append("select p.product_id as product_id, product_name,price,soi.quantity as quantity,store_order_id");
					sql.append(" from product p join store_order_item soi"); 
					sql.append(" on p.product_id =soi.product_id");
					sql.append(" where soi.store_order_id=?");
					
					PreparedStatement pstmt2= null;
					pstmt2= connection.prepareStatement(sql.toString());
					 
					pstmt2.setInt(1, rs.getInt("store_order_id"));
					ResultSet rs2 =pstmt2.executeQuery();
					
					List<OrderDetail> itemList=new ArrayList();
					while(rs2.next()) {
						OrderDetail item= new OrderDetail();
						Product product = new Product();
						product.setProductId(rs2.getInt("product_id"));
						product.setProductName(rs2.getString("product_name"));
						product.setPrice(rs2.getInt("price"));
						item.setProduct(product);
						item.setQuantity(rs2.getInt("quantity"));
						item.setOrderId(rs2.getInt("store_order_id"));
						itemList.add(item);
					}
					
					order.setItems(itemList);
					list.add(order);
					dbManager.release(pstmt2, rs2);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			dbManager.release(pstmt, rs);
		}

		return list;
	}
	public int countTotal() {
	    String sql = "SELECT COUNT(*) AS cnt FROM store_order";
	    Connection connection = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    int count = 0;

	    try {
	        connection = dbManager.getConnection();
	        pstmt = connection.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            count = rs.getInt("cnt");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        dbManager.release(pstmt, rs);
	    }

	    return count;
	}

	
}
