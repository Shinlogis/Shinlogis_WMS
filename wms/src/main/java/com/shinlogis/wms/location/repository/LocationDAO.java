package com.shinlogis.wms.location.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.Exception.LocationException;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.location.model.Location;

public class LocationDAO {
	DBManager dbManager = DBManager.getInstance();
	
	//회원가입
	public List<Location> getLocation() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Location> list = new ArrayList<>();
		
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from location");
		
		try {
			pstmt = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				Location location = new Location();
				location.setLocationId(rs.getInt("location_id"));
				location.setLocationName(rs.getString("location_name"));
				location.setAddress(rs.getString("address"));
				location.setStatus(rs.getString("status"));
				
				list.add(location);
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt,rs);
		}
		return list;
		
	}
	
	//업데이트 
	public void update(int locationId, String status) throws LocationException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("update location SET status = ? WHERE location_id = ?");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, status);
			pstmt.setInt(2, locationId);
			
			int result=pstmt.executeUpdate();
			if(result<1) {
				throw new  LocationException("지점 업데이트에 실패하였습니다");
			}
			
			
		} catch (SQLException e) {
			throw new LocationException("지점 업데이트에 실패하였습니다");
		}finally {
			dbManager.release(pstmt,rs);
		}
		
		
	}
	
	
	
	

}
