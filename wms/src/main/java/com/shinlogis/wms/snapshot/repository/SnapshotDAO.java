package com.shinlogis.wms.snapshot.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.inoutbound.model.InboundForm;
import com.shinlogis.wms.inoutbound.model.OutboundForm;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.snapshot.model.Snapshot;
import com.shinlogis.wms.storageType.model.StorageType;
import com.shinlogis.wms.supplier.model.Supplier;

/**
 * 스냅샷 DAO입니다
 * @author 김예진
 */
public class SnapshotDAO {
	DBManager dbManager = DBManager.getInstance();

	/**
	 * 스냅샷 목록을 select하는 메서드
	 * 
	 * @author 김예진
	 * @since 2025-06-20
	 * @return
	 */
	public List<Snapshot> selectSnapshots() {
		List<Snapshot> list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM SNAPSHOT";

		try {
			connection = dbManager.getConnection();
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Snapshot snapshot = new Snapshot();
				snapshot.setSnapshotId(rs.getInt("snapshot_id"));
				snapshot.setProductCode(rs.getString("product_code"));
				snapshot.setProductName(rs.getString("product_name"));
				
				StorageType storageType = new StorageType();
				storageType.setStorageTypeId(rs.getInt("storage_type_id"));
				snapshot.setStorageType(storageType);
				
				snapshot.setSupplierName(rs.getString("supplier_name"));
				snapshot.setExpiryDate(rs.getDate("expiry_date"));
				
				list.add(snapshot);
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return list;
	}
	
	/**
	 * 스냅샷을 id로 select하는 메서드
	 * @author 김예진
	 * @since 2025-06-20
	 * @return
	 */
	public Snapshot selectSnapshotById(int id) {
		Snapshot snapshot = new Snapshot();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM SNAPSHOT WHERE SNAPSHOT_ID = " + id;

		try {
			connection = dbManager.getConnection();
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				snapshot.setSnapshotId(rs.getInt("snapshot_id"));
				snapshot.setProductCode(rs.getString("product_code"));
				snapshot.setProductName(rs.getString("product_name"));

				StorageType storageType = new StorageType();
				storageType.setTypeCode(rs.getString("storage_type_code"));
				snapshot.setStorageType(storageType);
				
				snapshot.setSupplierName(rs.getString("supplier_name"));
				snapshot.setExpiryDate(rs.getDate("expiry_date"));
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return snapshot;
	}
	
	/**
	 * 출고폼으로부터 스냅샷을 만드는 메서드
	 * 
	 * @author 이세형
	 * @since 2025-06-29
	 * @param form
	 * @return
	 */
	public int createSnapshotFromForm(OutboundForm form) {
		int snapshotId = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into snapshot (product_code, product_name, storage_type_code, supplier_name, price) values(?, ?, ?, ?, ?)");
		
		try {
			connection = dbManager.getConnection();
			pstmt = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, form.getProduct().getProductCode());
			pstmt.setString(2, form.getProduct().getProductName());
			pstmt.setString(3, form.getProduct().getStorageType().getTypeCode());
			pstmt.setString(4, form.getProduct().getSupplier().getName());
			pstmt.setInt(5, form.getProduct().getPrice());
			pstmt.executeUpdate();			
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				snapshotId = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return snapshotId;
	}
	/**
	 * 입고폼으로부터 스냅샷을 만드는 메서드
	 * 
	 * @author 김예진
	 * @since 2025-06-26
	 * @param form
	 * @return
	 */
	public int createSnapshotFromForm(InboundForm form) {
		int snapshotId = 0;
	    Connection connection = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into snapshot (product_code, product_name, storage_type_code, supplier_name, price) values(?, ?, ?, ?, ?)");
		
		try {
			connection = dbManager.getConnection();
			pstmt = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, form.getProduct().getProductCode());
			pstmt.setString(2, form.getProduct().getProductName());
			pstmt.setString(3, form.getProduct().getStorageType().getTypeCode());
			pstmt.setString(4, form.getProduct().getSupplier().getName());
			pstmt.setInt(5, form.getProduct().getPrice());
			pstmt.executeUpdate();			
			rs = pstmt.getGeneratedKeys();
	        if (rs.next()) {
	            snapshotId = rs.getInt(1);
	        }
	        System.out.println("스냅샷id "+snapshotId);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        dbManager.release(pstmt, rs);
	    }

	    return snapshotId;
	}
	
	/**
	 * id에 해당하는 스냅샷의 정보를 상품코드에 일치하는 상품으로 업데이트하는 메서드
	 * @author 김예진
	 * @param id 업데이트할 스냅샷 id
	 * @param productCode 갱신하려는 상품 코드
	 * @return
	 */
	public int updateSnapshotByCode(int id, String productCode) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 1. productCode의 상품 조회
		Product product = new Product(); // 조회한 상품의 정보를 저장할 product
		String sql1 = "select * from product p "
				+ "join supplier s "
				+ "on p.supplier_id = s.supplier_id "
				+ "join storage_type st "
				+ "on p.storage_type_id = st.storage_type_id "
				+ "where product_code = ?";
		try {
			connection = dbManager.getConnection();
			pstmt = connection.prepareStatement(sql1);
			pstmt.setString(1, productCode);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				product.setProductId(rs.getInt("product_id"));
				product.setProductCode(rs.getString("product_code"));
				product.setProductName(rs.getString("product_name"));
				
				Supplier supplier = new Supplier();
				supplier.setSupplierId(rs.getInt("supplier_id"));
				supplier.setName(rs.getString("name"));
				supplier.setAddress(rs.getString("address"));
				product.setSupplier(supplier);
				
				StorageType storageType = new StorageType();
				storageType.setStorageTypeId(rs.getInt("storage_type_id"));
				storageType.setTypeCode(rs.getString("type_code"));
				storageType.setTypeName(rs.getString("type_name"));
				product.setStorageType(storageType);
				
				product.setPrice(rs.getInt("price"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		// 2. 위의 product 정보로 snapshot을 update
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		int result = 0;
		String sql2 = "update snapshot "
				+ "set product_code = ?, product_name = ?, storage_type_code=?, supplier_name=?, price=? "
				+ "where snapshot_id = ?";
		
		try {
			pstmt2 = connection.prepareStatement(sql2);
			pstmt2.setString(1, product.getProductCode());
			pstmt2.setString(2, product.getProductName());
			pstmt2.setString(3, product.getStorageType().getTypeCode());
			pstmt2.setString(4, product.getSupplier().getName());
			pstmt2.setInt(5, product.getPrice());
			pstmt2.setInt(6, id);
			
			result = pstmt2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return result;
	}	
	
	
	
	
}
