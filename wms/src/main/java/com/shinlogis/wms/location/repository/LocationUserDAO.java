package com.shinlogis.wms.location.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shinlogis.wms.common.Exception.HeadquartersException;
import com.shinlogis.wms.common.Exception.LocationException;
import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.common.util.StringUtil;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.location.model.Location;
import com.shinlogis.wms.location.model.LocationUser;

public class LocationUserDAO {

	DBManager dbManager = DBManager.getInstance();

	// 회원가입
	public void insertLocationUser(LocationUser locationUser) throws LocationException {
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

			if (result < 1) {
				throw new LocationException("회원가입에 실패했습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LocationException("회원가입시 문제 발생", e);
		} finally {
			dbManager.release(pstmt);
		}
	}

	/**
	 * 중복 아이디를 검증하는 메서드
	 * 
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
			pstmt.setString(1, insertId); // sql문에 사용자가 입력한 아이디를 매핑

			rs = pstmt.executeQuery();

			if (rs.next()) {
				// rs.next()가 존재하는 경우 -> 값이 있다는 것 -> 중복 아이디 (db에 존재하므로 값이 select 된 것)
				return false; // 중복아이디
			} else {
				// rs.next()가 존재하지 않는 경우 경우 -> 값이 없다는 것 -> 중복 아이디가 아님 (db에 중복되는 값이 없음)
				return true; // 사용 가능한 아이디
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);

		}
		return false;

	}

	/**
	 * 중복 이메일을 검증하는 메서드
	 * 
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
			pstmt.setString(1, insertEmail); // sql문에 사용자가 입력한 아이디를 매핑

			rs = pstmt.executeQuery();

			if (rs.next()) {
				// rs.next()가 존재하는 경우 -> 값이 있다는 것 -> 중복 아이디 (db에 존재하므로 값이 select 된 것)
				return false; // 중복아이디
			} else {
				// rs.next()가 존재하지 않는 경우 경우 -> 값이 없다는 것 -> 중복 아이디가 아님 (db에 중복되는 값이 없음)
				return true; // 사용 가능한 아이디
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);

		}
		return false;

	}

	// 수정시 본인 이메일은 중복방지 제외
	public boolean checkEmailById(String email, int id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		con = dbManager.getConnection();

		StringBuffer sql = new StringBuffer();
		// 본인 아이디와 같지 않은 이메일 중복 체크
		sql.append("select * from location_user where email = ? and location_user_id != ?");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, email);
			pstmt.setInt(2, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				// rs.next()가 존재하는 경우 -> 값이 있다는 것 -> 중복 아이디 (db에 존재하므로 값이 select 된 것)
				return false; // 중복아이디
			} else {
				// rs.next()가 존재하지 않는 경우 경우 -> 값이 없다는 것 -> 중복 아이디가 아님 (db에 중복되는 값이 없음)
				return true; // 사용 가능한 아이디
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);

		}
		return false;

	}

	// 이메일을 통해 아이디 찾기
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

			if (rs.next()) {
				id = rs.getString("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return id;

	}

	// 비밀번호 찾기
	public String findPwd(String id, String email) throws HeadquartersException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String pwd = null;

		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from location_user where id = ? and email = ?");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				pwd = StringUtil.getRandomPwd(); // 랜덤 비밀번호 발급
				String securedPass = StringUtil.getSecuredPass(pwd); // 비밀번호 암호화

				rs.close(); // 기존 rs 닫기
				pstmt.close(); // 기존 pstmt닫기

				// 비밀번호 업데이트
				String sqlUdate = "update location_user set pw = ? where id = ? and email = ?";
				pstmt = con.prepareStatement(sqlUdate);
				pstmt.setString(1, securedPass);
				pstmt.setString(2, id);
				pstmt.setString(3, email);

				int result = pstmt.executeUpdate();

				if (result < 1) {
					throw new HeadquartersException("비밀번호 찾기에 실패하셨습니다.");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new HeadquartersException("비밀번호 찾기 중 문제 발생", e);
		} finally {
			dbManager.release(pstmt, rs);
		}

		return pwd;
	}

	// 로그인
	public LocationUser Login(LocationUser locationUser) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LocationUser user = null;

		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from location_user lu inner join location l");
		sql.append(" on lu.location_id = l.location_id where id = ? and pw = ? and lu.status = '활성'");

		try {
			String securedPass = StringUtil.getSecuredPass(locationUser.getPw()); // 암호화된 비밀번호 꺼내기

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, locationUser.getId());
			pstmt.setString(2, securedPass);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				user = new LocationUser();
				user.setLocationUserId(rs.getInt("location_user_id"));
				user.setId(rs.getString("id"));
				user.setPw(rs.getString("pw"));
				String[] email = rs.getString("email").split("@");
				user.setEmail(email[0], email[1]);

				Location location = new Location();
				location.setLocationId(rs.getInt("location_id"));
				location.setLocationName(rs.getString("location_name"));
				location.setAddress(rs.getString("address"));

				user.setLocation(location);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return user;

	}

	// 회원 1명 정보
	public LocationUser selectById(int id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LocationUser locationUser = null;

		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from location_user where location_user_id = ?");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				locationUser = new LocationUser();
				String[] email = rs.getString("email").split("@");

				locationUser.setLocationUserId(rs.getInt("location_user_id"));
				locationUser.setId(rs.getString("id"));
				locationUser.setPw(rs.getString("pw"));
				locationUser.setEmail(email[0], email[1]);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return locationUser;

	}

	// 수정하기
	public void edit(LocationUser locationUser) throws LocationException {
		Connection con = null;
		PreparedStatement pstmt = null;

		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();

		try {
			if (locationUser.getPw() == null || locationUser.getPw().isEmpty()) {
				sql.append("update location_user set email = ? where location_user_id = ?");
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setString(1, locationUser.getEmail());
				pstmt.setInt(2, locationUser.getLocationUserId());
			} else {
				sql.append("update location_user set pw =?, email = ? where location_user_id = ?");
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setString(1, StringUtil.getSecuredPass(locationUser.getPw()));
				pstmt.setString(2, locationUser.getEmail());
				pstmt.setInt(3, locationUser.getLocationUserId());
			}

			int result = pstmt.executeUpdate();

			if (result < 1) {
				throw new LocationException("비밀번호 변경에 실패하였습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LocationException("비밀번호 변경 시 문제 발생", e);
		} finally {
			dbManager.release(pstmt);
		}

	}

	// 탈퇴하기
	public void delete(LocationUser locationUser) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;

		con = dbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("update location_user set status = '탈퇴' where location_user_id = ?");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, locationUser.getLocationUserId());
			result = pstmt.executeUpdate();

			if (result < 1) {
				throw new HeadquartersException("회원탈퇴에 실패했습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new HeadquartersException("회원 탈퇴 시 문제 발생", e);
		} finally {
			dbManager.release(pstmt);
		}

	}

}
