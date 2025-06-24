package com.shinlogis.wms.location.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shinlogis.wms.common.Exception.HeadquartersException;
import com.shinlogis.wms.common.Exception.LocationException;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.common.util.StringUtil;
import com.shinlogis.wms.location.model.LocationUser;

public class LocationUserDAO {

	DBManager dbManager = DBManager.getInstance();
	
	//회원가입
	public void insertLocationUser(LocationUser locationUser) throws LocationException{
		Connection con = null;
		PreparedStatement pstmt = null;
		
		
		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into location_user(id, pw, email, location_id)values (?,?,?,?)");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, locationUser.getId());
			pstmt.setString(2, StringUtil.getSecuredPass(new String(locationUser.getPw())));
			pstmt.setString(3, locationUser.getEmail());
			pstmt.setInt(4, locationUser.getLocation().getLocationId());
			
			System.out.println(locationUser.getLocation().getLocationId());
			
			int result = pstmt.executeUpdate();
			
			if(result < 1) {
				throw new LocationException("회원가입에 실패했습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LocationException("회원가입시 문제 발생",e);
		} finally {
			dbManager.release(pstmt);
		}
	}
	
	
	/**
	 * 중복 아이디를 검증하는 메서드 
	 * @param insertId 사용자가 입력한 id 
	 * @return true: 사용가능한 아이디 / false: 중복 아이디 
	 */
	public boolean checkId(String insertId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from location_user where id = ?");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, insertId); //sql문에 사용자가 입력한 아이디를 매핑 
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				//rs.next()가 존재하는 경우 -> 값이 있다는 것 -> 중복 아이디 (db에 존재하므로 값이 select 된 것)
				return false; // 중복아이디
			} else { 
				//rs.next()가 존재하지 않는 경우 경우 -> 값이 없다는 것 -> 중복 아이디가 아님 (db에 중복되는 값이 없음)
				return true; // 사용 가능한 아이디 
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt, rs);
			
		}
		return false;
		
	}
	
	
	
	
	
	/**
	 * 중복 이메일을 검증하는 메서드 
	 * @param insertId 사용자가 입력한 id 
	 * @return true: 사용가능한 아이디 / false: 중복 아이디 
	 */
	public boolean checkEmail(String insertEmail) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from location_user where email = ?");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, insertEmail); //sql문에 사용자가 입력한 아이디를 매핑 
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				//rs.next()가 존재하는 경우 -> 값이 있다는 것 -> 중복 아이디 (db에 존재하므로 값이 select 된 것)
				return false; // 중복아이디
			} else { 
				//rs.next()가 존재하지 않는 경우 경우 -> 값이 없다는 것 -> 중복 아이디가 아님 (db에 중복되는 값이 없음)
				return true; // 사용 가능한 아이디 
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt, rs);
			
		}
		return false;
		
	}
	
	
		//이메일을 통해 아이디 찾기
		public String findIdByEmail(String email) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			con = dbManager.getConnection();
			
			StringBuffer sql = new StringBuffer();
			sql.append("select * from location_user where email = ?");
			String id = null;
			
			try {
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setString(1, email);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					id = rs.getString("id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbManager.release(pstmt, rs);
			}
			return id;
			
		}
		
		//비밀번호 찾기
		public String findPwd(String id, String email)throws HeadquartersException{
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String pwd =  null;
			
			con = dbManager.getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select * from location_user where id = ? and email = ?");
			
			try {
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setString(1, id);
				pstmt.setString(2, email);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					pwd = StringUtil.getRandomPwd(); //랜덤 비밀번호 발급
					String securedPass = StringUtil.getSecuredPass(pwd); //비밀번호 암호화
					
					rs.close(); //기존 rs 닫기
					pstmt.close(); //기존 pstmt닫기
					
					//비밀번호 업데이트
					String sqlUdate = "update location_user set pw = ? where id = ? and email = ?";
					pstmt = con.prepareStatement(sqlUdate);
					pstmt.setString(1, securedPass);
					pstmt.setString(2, id);
					pstmt.setString(3, email);
					
					int result = pstmt.executeUpdate();
					
					if(result<1) {
						throw new HeadquartersException("비밀번호 찾기에 실패하셨습니다.");
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new HeadquartersException("비밀번호 찾기 중 문제 발생", e);
			}finally {
				dbManager.release(pstmt, rs);
			}
			
			
			return pwd;
		}
		
		//로그인
		public LocationUser Login(LocationUser locationUser) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			LocationUser user = null;
			
			con = dbManager.getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select * from location_user where id = ? and pw = ?");
			
			
			try {
				String securedPass = StringUtil.getSecuredPass(locationUser.getPw()); //암호화된 비밀번호 꺼내기
				
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setString(1, locationUser.getId());
				pstmt.setString(2, securedPass);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					user = new LocationUser();
					user = new LocationUser();
					user.setId(rs.getString("id"));
				} 
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				dbManager.release(pstmt, rs);
			}
			return user;
			
		}
		
		
	
	
	
}
