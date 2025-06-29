package com.shinlogis.wms.inoutbound.outbound.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shinlogis.wms.common.util.DBManager;

public class OutboundDetailSimpleDTO {
	private String productCode;
	private String productName;
	private String warehouseName;
	private int plannedQuantity;

	// 생성자, getter, setter
	public OutboundDetailSimpleDTO(String productCode, String productName, String warehouseName, int plannedQuantity) {
		this.productCode = productCode;
		this.productName = productName;
		this.warehouseName = warehouseName;
		this.plannedQuantity = plannedQuantity;
	}

	public String getProductCode() {
		return productCode;
	}

	public String getProductName() {
		return productName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public int getPlannedQuantity() {
		return plannedQuantity;
	}

	public OutboundDetailSimpleDTO selectSimpleByIoDetailId(int ioDetailId) {
		DBManager dbManager = DBManager.getInstance();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OutboundDetailSimpleDTO dto = null;

		try {
			con = dbManager.getConnection();
			String sql = "SELECT s.product_code, s.product_name, w.warehouse_name, id.planned_quantity "
					+ "FROM io_detail id " + "INNER JOIN snapshot s ON id.snapshot_id = s.snapshot_id "
					+ "INNER JOIN warehouse w ON id.warehouse_id = w.warehouse_id " + "WHERE id.io_detail_id = ?";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ioDetailId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new OutboundDetailSimpleDTO(rs.getString("product_code"), rs.getString("product_name"),
						rs.getString("warehouse_name"), rs.getInt("planned_quantity"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.release(pstmt, rs);
		}

		return dto;
	}
}