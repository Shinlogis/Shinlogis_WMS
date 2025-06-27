package com.shinlogis.wms.inoutbound.inbound.view.receipt;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.inoutbound.inbound.repository.ReceiptDAO;
import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.snapshot.repository.SnapshotDAO;

/**
 * InboundReceiptModel 클래스입니다.
 * 
 * @author 김예진
 * @since 2025-06-19
 */
public class ReceiptModel extends AbstractTableModel {

	ReceiptDAO ioReceiptDAO = new ReceiptDAO();
	SnapshotDAO snapshotDAO = new SnapshotDAO();
	List<IOReceipt> inboundList;
	List<Map<String, Object>> infoList;
	
	String[] column = { "입고예정ID", "입고 품목", "공급사명", "입고예정일", "상태", "등록일", "처리일", "상세보기"};

	/**
	 * 생성자
	 */
	public ReceiptModel() {
		inboundList = ioReceiptDAO.selectInboundReceiptsWithItemInfo(Collections.emptyMap());
	}

	/** 행 인덱스로 IOReceipt 가져오기 */
	public IOReceipt getIOReceiptAt(int rowIndex) {
		if (rowIndex >= 0 && rowIndex < inboundList.size()) {
			return inboundList.get(rowIndex);
		}
		return null;
	}

	/** 버튼 클릭 가능 여부 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 7; // 상세보기 버튼
	}

	/** Map 필터로 전체 데이터 설정 */
	public void setData(Map<String,Object> filters) {
		this.inboundList = ioReceiptDAO.selectInboundReceiptsWithItemInfo(filters);
		fireTableDataChanged();
	}

	/** 페이징용 서브리스트 설정 */
	public void setData(List<IOReceipt> pageList) {
		this.inboundList = pageList;
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return inboundList != null ? inboundList.size() : 0;
	}

	@Override
	public int getColumnCount() {
		return column.length;
	}

	@Override
	public String getColumnName(int col) {
		return column[col];
	}

	/** 입고예정 테이블의 값 반환 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		IOReceipt ioReceipt = inboundList.get(rowIndex);

		switch (columnIndex) {
			case 0: return ioReceipt.getIoReceiptId();
			case 1: return ioReceipt.getFirstProductName() + " 포함 " + ioReceipt.getItemCount() + "건";
			case 2: return ioReceipt.getSupplierName();
			case 3: return ioReceipt.getScheduledDate();
			case 4: return ioReceipt.getStatus();
			case 5: return ioReceipt.getCreatedAt();
			case 6: return ioReceipt.getProcessedDate() != null ? ioReceipt.getProcessedDate() : "처리 전";
			case 7: return "선택";
			default: return "";
		}
	}
}
