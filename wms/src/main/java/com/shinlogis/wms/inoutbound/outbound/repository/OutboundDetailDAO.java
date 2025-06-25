package com.shinlogis.wms.inoutbound.outbound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.damagedCode.model.DamagedCode;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.location.model.Location;
import com.shinlogis.wms.snapshot.model.Snapshot;
import com.shinlogis.wms.warehouse.model.Warehouse;

public class OutboundDetailDAO {
	DBManager dbManager = DBManager.getInstance();
	// outboundDetail의 모든 정보 출력.
	public List selectAllOutboundDetail() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList();
		
		con = dbManager.getConnection();

		try {
			StringBuffer sql = new StringBuffer();
			//io_detail, io_receipt join
			sql.append("select *"
					+ " from io_detail id"
					+ " inner join io_receipt ir on id.io_receipt_id = ir.io_receipt_id"
					+ " inner join snapshot s on id.snapshot_id = s.snapshot_id"	
					+ " inner join damaged_code dc on id.damage_code_id = dc.damage_code_id"
					+ " inner join headquarters_user hu on id.headquarters_user_id = hu.headquarters_user_id"
					+ " inner join warehouse w on id.warehouse_id = w.warehouse_id"
					+ " inner join location l on ir.location_id = l.location_id"
					+ " order by id.io_detail_id desc");
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();// 얘가 상하좌우 커서 움직일 수 있게 해주는 애라서 select 문에 추가가 되는거구나..
			while (rs.next()) {
				IODetail outboundDetail = new IODetail();
				outboundDetail.setIoDetailId(rs.getInt("io_detail_id"));
				
				IOReceipt or = new IOReceipt();
				or.setIoReceiptId(rs.getInt("ir.io_receipt_id"));
				or.setScheduledDate((rs.getDate("ir.scheduled_date")));
				Location location = new Location();
				location.setLocationName(rs.getString("l.location_name"));
				or.setLocation(location);
				Warehouse warehouse = new Warehouse();
				warehouse.setWarehouseName(rs.getString("w.warehouse_name"));
				outboundDetail.setWarehouse(warehouse);
				outboundDetail.setIoReceipt(or);
				
				outboundDetail.setPlannedQuantity(rs.getInt("planned_quantity"));
				
				Snapshot snapshot = new Snapshot();
				snapshot.setProductCode(rs.getString("s.product_code"));
				snapshot.setProductName(rs.getString("s.product_name"));
				snapshot.setSnapshotId(rs.getInt("id.snapshot_id"));
				
				outboundDetail.setProductSnapshot(snapshot);
				
				DamagedCode dmc = new DamagedCode();
				dmc.setDamageCodeId(rs.getInt("id.damage_code_id"));
				outboundDetail.setDamagedCode(dmc);
				
				outboundDetail.setDamageQuantity(rs.getInt("id.damage_quantity"));
				outboundDetail.setActualQuantity(rs.getInt("id.actual_quantity"));
				
				HeadquartersUser user = new HeadquartersUser();
				user.setHeadquartersUserId(rs.getInt("id.headquarters_user_id"));
				outboundDetail.setUser(user);
				
				outboundDetail.setProccessedDate(rs.getDate("id.processed_date"));
				outboundDetail.setStatus(rs.getString("id.status"));
				
				list.add(outboundDetail);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(pstmt, rs);
		}

		return list;
	}

	public int countTotal() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int totalCount = 0;

		try {
			con = dbManager.getConnection();
			// total로서 sql문을 받아온다.
			String sql = "SELECT COUNT(*) AS total FROM io_detail";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				totalCount = rs.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}
		return totalCount;
	}
	
}
