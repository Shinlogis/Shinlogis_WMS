package com.shinlogis.wms.outbound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;

public class OutboundDetailDAO {
	DBManager dbManager = DBManager.getInstance();

	//outboundDetail의 모든 정보 출력.
	public List selectAllOutboundDetail() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList();

		con = dbManager.getConnection();

		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from io_detail id");
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

}
