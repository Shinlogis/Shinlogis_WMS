package com.shinlogis.wms.supplier.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		sql.append("select * from supplier order by supplier_id desc");
		
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

}
