package com.shinlogis.wms.headquarters.repository;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
		sql.append("insert into headquarters_user (id, pw, email) values (?,?,?)");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, headquartersUser.getId());
			pstmt.setString(2, StringUtil.getSecuredPass(new String(headquartersUser.getPw())));
			pstmt.setString(3, headquartersUser.getEmail());
			
			int result = pstmt.executeUpdate();
			
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

}
