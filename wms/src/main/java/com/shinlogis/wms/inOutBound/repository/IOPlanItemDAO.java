package com.shinlogis.wms.inOutBound.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shinlogis.wms.common.util.DBManager;
import com.shinlogis.wms.damagedCode.model.DamagedCode;
import com.shinlogis.wms.inOutBound.model.IOPlanItem;
import com.shinlogis.wms.inOutBound.model.IOReceipt;
import com.shinlogis.wms.snapshot.model.Snapshot;

/**
 * 입출고예정 상세 DAO입니다
 * @author 김예진
 */
public class IOPlanItemDAO {
	DBManager dbManager = DBManager.getInstance();

	/**
	 * 입고 품목을 insert하는 메서드
	 * @param item
	 * @return
     * @author 김예진
     * @since 2025-06-29
	 */
    public int insertIoPlanItem(IOPlanItem item){
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
     * @param receiptId 입고예정Id
     * @return
     * @author 김예진
     * @since 2025-06-29
     */
    public List<IOPlanItem> selectIOPlanItems() {
        List<IOPlanItem> list = new ArrayList<>();
        String sql = "select * "
        		+ "FROM io_plan_item ip "
        		+ "join io_receipt ir  "
        		+ "on IP.io_receipt_id = IR.io_receipt_id "
        		+ "JOIN snapshot s  "
        		+ "ON ip.product_snapshot = s.snapshot_id "
        		+ "join damaged_code dc "
        		+ "on IP.damage_code_id = DC.damage_code_id "
        		+ "join headquarters_user hu "
        		+ "on HU.headquarters_user_id = IP.headquarters_user_id "
        		+ "where ir.io_type = 'IN'";

        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getConnection();
            try {
                pstmt = connection.prepareStatement(sql);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    IOPlanItem item = new IOPlanItem();
                    item.setIoItemId(rs.getInt("io_item_id"));
                    System.out.println(rs.getInt("io_item_id"));
                    
                    // 입고예정
                    IOReceipt ioReceipt = new IOReceipt();
                    ioReceipt.setIoReceiptId(rs.getInt("io_receipt_id"));
                    item.setIoReceipt(ioReceipt);

                    // 입고 예정 수량
                    item.setPlannedQuantity(rs.getInt("planned_quantity"));
                    
                    // 상품 스냅샷
                    Snapshot snapshot = new Snapshot();
                    snapshot.setSnapshotId(rs.getInt("product_snapshot"));
                    snapshot.setProductCode(rs.getString("product_code"));
                    snapshot.setProductName(rs.getString("product_name"));
                    snapshot.setStorageTypeCode(rs.getString("storage_type_code"));
                    snapshot.setSupplierName(rs.getString("supplier_name"));
                    snapshot.setPrice(rs.getInt("price"));
                    snapshot.setExpiryDate(rs.getDate("expiry_date"));
                    
                    item.setProductSnapshot(snapshot);
                    
                    DamagedCode code = new DamagedCode();
                    code.setDamageCodeId(rs.getInt("damage_code_id"));
                    item.setDamageCode(code);

                    item.setDamageQuantity(rs.getInt("damage_quantity"));
                    item.setActualQuantity(rs.getInt("actual_quantity"));
                    item.setProccessedDate(rs.getDate("proccessed_date"));
                    
                    list.add(item);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            dbManager.release(pstmt, rs);
        }

        return list;
    }

}
