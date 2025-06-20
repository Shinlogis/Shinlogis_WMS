package com.shinlogis.wms.headquarters.repository;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shinlogis.wms.common.Exception.HeadquartersException;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.common.util.StringUtil;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;

public class HeadquartersDAO {
	
	DBManager dbManager = DBManager.getInstance();
	
	//회원가입 insert
	public void insert(HeadquartersUser headquartersUser) throws HeadquartersException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into headquarters_user(id, pw, email) values (?,?,?)");
		
		System.out.println(sql.toString());
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, headquartersUser.getId());
			pstmt.setString(2, StringUtil.getSecuredPass(new String(headquartersUser.getPw())));
			pstmt.setString(3, headquartersUser.getEmail());
			
			int result = pstmt.executeUpdate();
			System.out.println("result is "+result);
			
			if(result < 1) {
				throw new HeadquartersException("회원가입 실패하였습니다.");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new HeadquartersException("회원가입 시 문제 발생");
		}finally {
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
		sql.append("select * from headquarters_user where id = ?");
		
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
	
	//이메일을 통해 아이디 찾기
	public String findIdByEmail(String emailText, String emailComb) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		con = dbManager.getConnection();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from headquarters_user where email = ?");
		String id = null;
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, emailText + "@" + emailComb);
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

}
