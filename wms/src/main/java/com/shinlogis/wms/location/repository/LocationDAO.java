package com.shinlogis.wms.location.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.shinlogis.wms.common.Exception.LocationException;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.location.model.Location;

public class LocationDAO {
	DBManager dbManager = DBManager.getInstance();
	
	//회원가입
	public int insertLocation(Location location) throws LocationException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int locationId = 0;
		
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into location (location_name, address)values (?,?)");
		
		try {
			pstmt = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, location.getLocationName());
			pstmt.setString(2, location.getAddress());
			int result = pstmt.executeUpdate();
			
			if(result <1) {
				throw new LocationException("회원가입에 실패했습니다.");
			}
			
			rs = pstmt.getGeneratedKeys();
			
			if(rs.next()) {
				locationId = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LocationException("회원가입 중 문제 발생");
		}finally {
			dbManager.release(pstmt,rs);
		}
		return locationId;
		
	}
	
	
	
	
	
	

}
