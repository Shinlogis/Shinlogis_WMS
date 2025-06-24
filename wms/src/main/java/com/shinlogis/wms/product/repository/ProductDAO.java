package com.shinlogis.wms.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.damagedCode.model.DamagedCode;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.snapshot.model.Snapshot;

public class ProductDAO {
//    id, product_code, product_name, storage_type_id, supplier_id, price
	DBManager dbManager = DBManager.getInstance();

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

}
