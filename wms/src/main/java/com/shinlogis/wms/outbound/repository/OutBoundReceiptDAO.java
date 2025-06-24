package com.shinlogis.wms.outbound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shinlogis.wms.common.util.DBManager;

public class OutBoundReceiptDAO {
	DBManager dbManager = DBManager.getInstance();
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public OutBoundReceiptDAO() {
		connect();
	}

	public void connect() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from io_receipt");
		con = dbManager.getConnection();
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			System.out.println("DB연결됐습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
