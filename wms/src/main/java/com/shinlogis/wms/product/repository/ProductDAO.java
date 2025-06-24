package com.shinlogis.wms.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
<<<<<<< HEAD

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.storageType.model.StorageType;
import com.shinlogis.wms.supplier.model.Supplier;

public class ProductDAO {
	DBManager dbManager = DBManager.getInstance();
=======
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
>>>>>>> d2d0fd5ea70d958520a8e5a136a94c00c488661d

	/**
	 * 상품코드가 일치하는 product를 select하는 메서드
	 * 
	 * @param productCode
	 * @return
	 * @author 김예진
	 * @since 2025-06-23
	 */
	
	public Product selectByCode(String productCode) {
		Connection connection = null;
		PreparedStatement pstmt = null;		
		ResultSet resultSet = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from product p join storage_type st on p.storage_type_id = st.storage_type_id join supplier s 	on p.supplier_id = s.supplier_id where product_code = ?");
		
		try {
			connection = dbManager.getConnection();
			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setString(1, productCode);
			
			resultSet = pstmt.executeQuery();
			while(resultSet.next()) {
				Product product = new Product();
				product.setProductId(resultSet.getInt("product_id"));
				product.setProductCode(resultSet.getString("product_code"));
				product.setProductName(resultSet.getString("product_name"));
				
				// 보관타입
				StorageType storageType = new StorageType();
				storageType.setStorageTypeId(resultSet.getInt("storage_type_id"));
				storageType.setTypeCode(resultSet.getString("type_code"));
				storageType.setTypeName(resultSet.getString("type_name"));
				product.setStorageType(storageType);
				
				// 공급사
				Supplier supplier = new Supplier();
				supplier.setSupplierId(resultSet.getInt("supplier_id"));
				supplier.setName(resultSet.getString("name"));
				supplier.setAddress(resultSet.getString("address"));
				product.setSupplier(supplier);
				
				return product;	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
