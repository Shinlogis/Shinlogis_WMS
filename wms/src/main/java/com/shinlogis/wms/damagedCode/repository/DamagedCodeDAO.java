package com.shinlogis.wms.damagedCode.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;

public class DamagedCodeDAO {
	DBManager dbManager = DBManager.getInstance();
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	/**
	 * 파손코드명을 받아오는 메서드
	 * 
	 * @author 김예진
	 * @since 2025-06-24
	 * @return
	 */
	public List<String> selectAllNames() {
		List<String> names = new ArrayList<>();
		String sql = "SELECT name FROM damaged_code";

		try {
			conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				names.add(rs.getString("name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(ps, rs);
		}

		return names;
	}

}
