package com.shinlogis.wms.supplier.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.Exception.SupplierException;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.supplier.model.Supplier;

public class SupplierDAO {
	DBManager dbManager = DBManager.getInstance();
	
	
	//화면에 목록 보여주기
	public List showSuppliers() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Supplier> list = new ArrayList<>();
		
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from supplier where status = '활성' order by supplier_id desc");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Supplier supplier = new Supplier();
				supplier.setSupplierId(rs.getInt("supplier_id"));
				supplier.setName(rs.getString("name"));
				supplier.setAddress(rs.getString("address"));
				
				list.add(supplier);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		
		return list;
	}
	
	//공급사 추가
	public void insert(Supplier supplier) throws SupplierException{
		Connection con = null;
		PreparedStatement pstmt = null;
		
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into supplier (name, address)values(?,?)");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, supplier.getName());
			pstmt.setString(2, supplier.getAddress());
			
			int result = pstmt.executeUpdate();
			
			if(result <1) {
				throw new SupplierException("공급사 추가에 실패했습니다.");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SupplierException("공급사 추가 중 문제 발생", e);
		}finally {
			dbManager.release(pstmt);
		}
		
	}
	
	
	//공급사 삭제(실제로 db에 삭제대신 상태를 '비활성'으로 바꾸기)
	public void deleteSupplier(Supplier supplier) throws SupplierException{
		Connection con = null;
		PreparedStatement pstmt = null;
		
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("update supplier set status ='비활성' where supplier_id = ?");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, supplier.getSupplierId());
			int result = pstmt.executeUpdate();
			
			if(result <1) {
				throw new SupplierException("삭제에 실패하였습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SupplierException("삭제처리 시 문제 발생", e);
		}finally {
			dbManager.release(pstmt);
		}
		
	}

}
