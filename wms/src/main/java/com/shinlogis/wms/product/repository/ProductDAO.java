package com.shinlogis.wms.product.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.product.model.ProductDTO;
import com.shinlogis.wms.storageType.model.StorageType;
import com.shinlogis.wms.supplier.model.Supplier;

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
	
	/**
	 * 상품코드, 상품명, 공급사명, 보관타입, 가격범위 등 다양한 조건을 select하는 메서드
	 * 
	 * @param productCode
	 * @return
	 * @author 김지민
	 * @since 2025-06-25
	 */
	
	public List<ProductDTO> selectProductList(String productCode, String productName, String supplierName, String storageTypeName, int minPrice, int maxPrice) {
	    List<ProductDTO> list = new ArrayList<>();

	    StringBuffer sql = new StringBuffer();
	    sql.append("SELECT p.*, s.name AS supplier_name, s.address, st.type_name ");
	    sql.append("FROM product p ");
	    sql.append("JOIN supplier s ON p.supplier_id = s.supplier_id ");
	    sql.append("JOIN storage_type st ON p.storage_type_id = st.storage_type_id ");
	    sql.append("WHERE 1=1 ");

	    if (productCode != null && !productCode.isEmpty()) {
	        sql.append("AND p.product_code LIKE CONCAT('%', ?, '%') ");
	    }
	    if (productName != null && !productName.isEmpty()) {
	        sql.append("AND p.product_name LIKE CONCAT('%', ?, '%') ");
	    }
	    if (supplierName != null && !supplierName.isEmpty()) {
	        sql.append("AND s.name LIKE CONCAT('%', ?, '%') ");
	    }
	    if (storageTypeName != null && !storageTypeName.isEmpty()) {
	        sql.append("AND st.type_name = ? ");
	    }
	    sql.append("AND p.price BETWEEN ? AND ?");

	    Connection connection = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        connection = dbManager.getConnection();
	        pstmt = connection.prepareStatement(sql.toString());

	        int paramIndex = 1;
	        if (productCode != null && !productCode.isEmpty()) {
	            pstmt.setString(paramIndex++, productCode);
	        }
	        if (productName != null && !productName.isEmpty()) {
	            pstmt.setString(paramIndex++, productName);
	        }
	        if (supplierName != null && !supplierName.isEmpty()) {
	            pstmt.setString(paramIndex++, supplierName);
	        }
	        if (storageTypeName != null && !storageTypeName.isEmpty()) {
	            pstmt.setString(paramIndex++, storageTypeName);
	        }
	        pstmt.setInt(paramIndex++, minPrice);
	        pstmt.setInt(paramIndex++, maxPrice);

	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            ProductDTO dto = new ProductDTO();
	            dto.setProductCode(rs.getString("product_code"));
	            dto.setProductName(rs.getString("product_name"));
	            dto.setPrice(rs.getInt("price"));
	            dto.setSupplierName(rs.getString("supplier_name"));
	            dto.setStorageTypeName(rs.getString("type_name"));
	            list.add(dto);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        dbManager.release(pstmt, rs);
	    }

	    return list;
	}
	/**
	 * 다중 삭제하는 메서드
	 * @author 김지민
	 * @since 2025-06-27
	 */
	public int deleteProductsByCodes(List<String> codes) {
	    String deleteInventorySQL = "DELETE FROM inventory WHERE product_id = (SELECT product_id FROM product WHERE product_code = ?)";
	    String deleteOrderItemSQL = "DELETE FROM store_order_item WHERE product_id = (SELECT product_id FROM product WHERE product_code = ?)";
	    String deleteProductSQL = "DELETE FROM product WHERE product_code = ?";

	    int totalDeleted = 0;

	    try (Connection conn = dbManager.getConnection()) {
	        conn.setAutoCommit(false); // 트랜잭션 시작

	        try (
	            PreparedStatement deleteInventoryStmt = conn.prepareStatement(deleteInventorySQL);
	            PreparedStatement deleteOrderItemStmt = conn.prepareStatement(deleteOrderItemSQL);
	            PreparedStatement deleteProductStmt = conn.prepareStatement(deleteProductSQL);
	        ) {
	            for (String code : codes) {
	                // 1. inventory에서 먼저 삭제
	                deleteInventoryStmt.setString(1, code);
	                deleteInventoryStmt.executeUpdate();

	                // 2. store_order_item에서 삭제
	                deleteOrderItemStmt.setString(1, code);
	                deleteOrderItemStmt.executeUpdate();

	                // 3. product에서 삭제
	                deleteProductStmt.setString(1, code);
	                int affectedRows = deleteProductStmt.executeUpdate();
	                totalDeleted += affectedRows;
	            }

	            conn.commit();
	        } catch (SQLException e) {
	            conn.rollback();
	            e.printStackTrace();
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return totalDeleted;
	}
}
