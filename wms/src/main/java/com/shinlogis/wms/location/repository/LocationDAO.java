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
	public int getLocation(Location location) throws LocationException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int locationId = 0;
		
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from location where location_name = ?");
		
		try {
			pstmt = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, location.getLocationName());
			rs = pstmt.executeQuery();
			
			
			if(rs.next()) {
				location.setLocationId(rs.getInt("location_id"));
				location.setLocationName(rs.getString("location_name"));
				location.setAddress(rs.getString("address"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt,rs);
		}
		return locationId;
		
	}
	
	
	
	
	
	

}
