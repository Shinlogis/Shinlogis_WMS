package com.shinlogis.wms.inOutBound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.damagedCode.model.DamagedCode;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.inOutBound.model.IODetail;
import com.shinlogis.wms.inOutBound.model.IOReceipt;
import com.shinlogis.wms.snapshot.model.Snapshot;

/**
 * 입출고예정 상세 DAO입니다
 * @author 김예진
 */
public class IODetailDAO {
	DBManager dbManager = DBManager.getInstance();

	/**
	 * 입고 품목을 insert하는 메서드
	 * @param item
	 * @return
     * @author 김예진
     * @since 2025-06-29
	 */
    public int insertIoPlanItem(IODetail item){
        int result = 0;
        String sql = "INSERT INTO io_plan_item (io_receipt_id, planned_quantity, product_snapshot, damage_code_id, damage_quantity, actual_quantity) VALUES (?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = dbManager.getConnection();
            try {
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, item.getIoItemId());
				pstmt.setInt(2, item.getPlannedQuantity());
				pstmt.setInt(3, item.getProductSnapshot().getSnapshotId());
				pstmt.setInt(4, item.getDamageCode().getDamageCodeId());
				pstmt.setInt(5, item.getDamageQuantity());
				pstmt.setInt(6, item.getActualQuantity());
				
				result = pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        } finally {
        	dbManager.release(pstmt);
        }

        return result;
    }
    
    /**
     * 입고예정상세를 SELECT하는 메서드
     * @return
     * @author 김예진
     * @since 2025-06-29
     */
    public List<IODetail> selectIODetails() {

        Connection conn =null;
        PreparedStatement ps = null;
        ResultSet rs =null;

        String sql = ""
                + "SELECT ip.io_detail_id, ip.planned_quantity, ip.damage_quantity, ip.actual_quantity, "
                + "       ip.proccessed_date, ip.status, "
                + "       ir.io_receipt_id, ir.scheduled_date, "
                + "       s.snapshot_id, s.product_code, s.product_name, s.storage_type_code, s.supplier_name, s.price, s.expiry_date, "
                + "       dc.damage_code_id, dc.code AS damage_code, "
                + "       hu.headquarters_user_id, hu.id AS hq_user_id, hu.email AS hq_user_email "
                + "FROM io_detail ip "
                + "JOIN io_receipt      ir  ON ip.io_receipt_id    = ir.io_receipt_id "
                + "JOIN snapshot        s   ON ip.snapshot_id      = s.snapshot_id "
                + "JOIN damaged_code    dc  ON ip.damage_code_id   = dc.damage_code_id "
                + "JOIN headquarters_user hu ON ip.headquarters_user_id = hu.headquarters_user_id "
                + "WHERE ir.io_type = 'IN'";

        List<IODetail> list = new ArrayList<>();

        try {
                conn = dbManager.getConnection();
                ps = conn.prepareStatement(sql);
           rs = ps.executeQuery();

                while (rs.next()) {
                    IODetail detail = new IODetail();
                    detail.setIoItemId(rs.getInt("io_detail_id"));

                    IOReceipt receipt = new IOReceipt();
                    receipt.setIoReceiptId(rs.getInt("io_receipt_id"));
                    receipt.setScheduledDate(rs.getDate("scheduled_date"));
                    detail.setIoReceipt(receipt);

                    detail.setPlannedQuantity(rs.getInt("planned_quantity"));
                    detail.setDamageQuantity(rs.getInt("damage_quantity"));
                    detail.setActualQuantity(rs.getInt("actual_quantity"));
                    detail.setProccessedDate(rs.getDate("proccessed_date") != null
                            ? rs.getDate("proccessed_date") : null);
                    detail.setStatus(rs.getString("status"));

                    Snapshot snapshot = new Snapshot();
                    snapshot.setSnapshotId(rs.getInt("snapshot_id"));
                    snapshot.setProductCode(rs.getString("product_code"));
                    snapshot.setProductName(rs.getString("product_name"));
                    snapshot.setStorageTypeCode(rs.getString("storage_type_code"));
                    snapshot.setSupplierName(rs.getString("supplier_name"));
                    snapshot.setPrice(rs.getInt("price"));
                    snapshot.setExpiryDate(rs.getDate("expiry_date"));
                    detail.setProductSnapshot(snapshot);

                    DamagedCode code = new DamagedCode();
                    code.setDamageCodeId(rs.getInt("damage_code_id"));
                    code.setCode(rs.getString("damage_code"));
                    detail.setDamageCode(code);

                    HeadquartersUser hq = new HeadquartersUser();
                    hq.setHeadquartersUserId(rs.getInt("headquarters_user_id"));
                    hq.setId(rs.getString("hq_user_id"));
                    hq.setEmail(rs.getString("hq_user_email"));
//                    detail.(hq);

                    list.add(detail);
                }
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            dbManager.release(ps, rs);
        }

        return list;
    }


}
