package com.shinlogis.wms.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.shinlogis.locationuser.order.model.StoreOrderItem;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.damagedCode.model.DamagedCode;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.snapshot.model.Snapshot;

public class ProductDAO {
//    id, product_code, product_name, storage_type_id, supplier_id, price
	DBManager dbManager = DBManager.getInstance();

	//전체상품검색 
	public List<Product> selectOrderProduct() {
		List<Product> list = new ArrayList<>();
		String sql = "select * from product";

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = dbManager.getConnection();
			try {
				pstmt = connection.prepareStatement(sql);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					Product product = new Product();
				
					product.setProductId(rs.getInt("product_id"));
					product.setProductCode(rs.getString("product_code"));
					product.setProductName(rs.getString("product_name"));
					product.setPrice(rs.getInt("price"));

					list.add(product);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			dbManager.release(pstmt, rs);
		}

		return list;
	}
	
	//상품명으로 검색 
	public List<Product> selectSearchProduct(String keyword) {
		List<Product> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select * from product WHERE (? = '' OR product_name LIKE CONCAT('%', ?, '%'));");

		Connection connection = null;
		PreparedStatement pstmt = null; 
		PreparedStatement pstmt2= null; 
		ResultSet rs =null;

		try {
			connection = dbManager.getConnection();
			try {
				pstmt = connection.prepareStatement(sql.toString());
				pstmt.setString(1,keyword);
				pstmt.setString(2,keyword);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					Product product = new Product();
				
					product.setProductId(rs.getInt("product_id"));
					product.setProductCode(rs.getString("product_code"));
					product.setProductName(rs.getString("product_name"));
					product.setPrice(rs.getInt("price"));

					list.add(product);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			dbManager.release(pstmt, rs);
		}
		return list;
	}

}
