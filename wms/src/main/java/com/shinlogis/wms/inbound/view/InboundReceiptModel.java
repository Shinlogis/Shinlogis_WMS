package com.shinlogis.wms.inbound.view;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.inbound.repository.InboundReceiptDAO;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.snapshot.repository.SnapshotDAO;

/**
 * InboundReceiptModel 클래스입니다.
 * 
 * @author 김예진
 * @since 2025-06-19
 */
public class InboundReceiptModel extends AbstractTableModel {

	InboundReceiptDAO ioReceiptDAO = new InboundReceiptDAO();
	SnapshotDAO snapshotDAO = new SnapshotDAO();
	List<IOReceipt> inboundList;
	List<Map<String, Object>> infoList;
	
	String[] column = { "입고예정ID", "입고 품목", "공급사명", "입고예정일", "상태", "등록일", "처리일", "상세보기"};

	/**
	 * 생성자
	 */
	public InboundReceiptModel() {
		// 초기에는 필터가 없으므로 Collections.emptyMap()를 넘겨 빈 필터로 전체를 불러옴
		inboundList = ioReceiptDAO.selectInboundReceiptsWithItemInfo(Collections.emptyMap());
	}
	
	public IOReceipt getIOReceiptAt(int rowIndex) {
		if (rowIndex >= 0 && rowIndex < inboundList.size()) {
			return inboundList.get(rowIndex);
		} else {
			return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 7;
	}

	/**
	 * 값을 새로 설정하는 메서드
	 * @auther 김예진
	 * @since 2025-06-22
	 * @param filters
	 */
	public void setData(Map<String,Object> filters) {
		this.inboundList = ioReceiptDAO.selectInboundReceiptsWithItemInfo(filters);
		fireTableDataChanged();
	}
	

	@Override
	public int getRowCount() {
		return inboundList.size();
	}

	@Override
	public int getColumnCount() {
		return column.length;
	}

	@Override
	public String getColumnName(int col) {
		return column[col];
	}
	
	/**
	 * 입고예정 id와 일치하는 입고예정 품목 정보를 가져오는 메서드
	 * @param receiptId
	 * @return
	 */
	private Map<String, Object> findProductInfo(int receiptId) {
		for (Map<String, Object> map : infoList) {
			if (((Integer) map.get("io_receipt_id")) == receiptId) {
				return map;
			}
		}
		return null;
	}



	/**
	 * 입고예정 테이블의 값을 불러오는 메서드
	 * 컬럼과 맞게 수정해야함
	 * 
	 * @author 김예진
	 * @since 2025-06-19
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String value = null;
		IOReceipt ioReceipt = inboundList.get(rowIndex); // 입고예정 레코드 값 불러오기
//		infoList = ioReceiptDAO.selectFirstProductAndItemCount();
//		Map<String, Object> productInfo = findProductInfo(ioReceipt.getIoReceiptId());
		
		switch (columnIndex) {
		case 0:
			return ioReceipt.getIoReceiptId();
		case 1:
			return ioReceipt.getFirstProductName() + " 포함 " + ioReceipt.getItemCount() + "건";
		case 2:
			return ioReceipt.getSupplierName();
		case 3:
			return ioReceipt.getScheduledDate();
		case 4:
			return ioReceipt.getStatus();
		case 5:
			return ioReceipt.getCreatedAt(); 
		case 6:
			return ioReceipt.getProcessedDate() != null ? ioReceipt.getProcessedDate() : "처리 전";
		case 7:
			return "선택";
		default:
			return "";
		}
	}

}
