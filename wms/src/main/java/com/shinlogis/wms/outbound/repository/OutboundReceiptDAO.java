package com.shinlogis.wms.outbound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.inbound.model.IOReceipt;
import com.shinlogis.wms.location.model.Location;

public class OutboundReceiptDAO {
	DBManager dbManager = DBManager.getInstance();

//	public OutBoundReceiptDAO(){}
	
	public List selectAllOutbounds() {//IoReceipt의 모든 레코드 가져오는 메서드
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList();
		
		con = dbManager.getConnection();
		

		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from io_receipt ir "
					+ " inner join location l"
					+ " on ir.location_id = l.location_id"
					+ " order by io_receipt_id desc");
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				IOReceipt outboundReceipt = new IOReceipt();
				outboundReceipt.setIoReceiptId(rs.getInt("io_receipt_id"));//pk넣어주기
				outboundReceipt.setIoType(rs.getString("io_type"));
				
				HeadquartersUser user = new HeadquartersUser();
				//headquarters유저의 pk가져옴, user에 할당
				user.setHeadquartersUserId(rs.getInt("user_id"));
				//user객체에서 받아온user
				outboundReceipt.setUser(user);
				
				Location location = new Location();
				location.setLocationId(rs.getInt("ir.location_id"));
				location.setLocationName(rs.getString("l.location_name"));
				outboundReceipt.setLocation(location);
				
				outboundReceipt.setCreatedAt(rs.getDate("created_at"));
				outboundReceipt.setScheduledDate(rs.getDate("scheduled_date"));
				outboundReceipt.setProcessedDate(rs.getDate("processed_date"));
				outboundReceipt.setStatus(rs.getString("status"));
				
				list.add(outboundReceipt);

			}
			System.out.println(list);
			
//			System.out.println("1번" + rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt,rs);
		}
		return list;
	}

}
//출고예정, 출고예정ID
//sql.append("select io_receipt_id");
//sql.append(" from io_receipt;");


//			//출고예정, 출고품목
//			sql.append("select s.product_name");
//			sql.append(" from snapshot s");
//			sql.append(" inner join io_detail id");
//			sql.append(" on s.snapshot_id = id.snapshot_id;");
//			System.out.println("2번" + rs);
//			
//			//출고예정, 출고지점
//			sql.append("select l.location_name");
//			sql.append(" from location l");
//			sql.append(" inner join io_receipt ir");
//			sql.append(" on l.location_id = ir.location_id;");
//			
//			//출고예정, 출고예정일
//			sql.append("select scheduled_date");
//			sql.append(" from io_receipt;");
//			
//			//출고예정, 상태
//			sql.append("select status");
//			sql.append(" from io_receipt");
//			
//			//출고예정, 등록일
//			sql.append("select DATE(created_at) as date_only");
//			sql.append(" from io_receipt");