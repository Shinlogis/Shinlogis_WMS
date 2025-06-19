package com.shinlogis.wms.inOutBound.view;

import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.shinlogis.wms.inOutBound.model.IOReceipt;
import com.shinlogis.wms.inOutBound.repository.IOReceiptDAO;

/**
 * InboundReceiptModel
 * 
 * @author 김예진
 * @since 2025-06-19
 */
public class InboundReceiptModel extends AbstractTableModel {

	IOReceiptDAO ioReceiptDAO = new IOReceiptDAO();
	List<IOReceipt> inboundList;
	String[] column = { "입고예정ID", "입고 품목", "공급사명", "입고예정일", "상태", "등록일",
			"처리일", "상세보기"};

	public InboundReceiptModel() {
		inboundList = ioReceiptDAO.selectAllInbound();
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
	 * 입고예정 테이블의 값을 불러오는 메서드
	 * 컬럼과 맞게 수정해야함
	 * 
	 * @author 김예진
	 * @since 2025-06-19
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String value = null;
		IOReceipt ioReceipt = inboundList.get(rowIndex); // 레코드 값 불러오기

		switch (columnIndex) {
		case 0:
			return ioReceipt.getIoReceiptId();
		case 1:
			return "품목추가";
		case 2:
			return "공급사추가";
		case 3:
			return ioReceipt.getScheduledDate();
		case 4:
			return ioReceipt.getStatus();
		case 5:
			return ioReceipt.getCreatedAt(); 
		case 6:
			return ioReceipt.getProcessedDate() != null ? ioReceipt.getProcessedDate() : "처리 전";
		case 7:
			return ioReceipt.getLocation().getLocationName(); // Location 객체에서 ID만 추출
		case 8:
			return "선택";
		default:
			return "";
		}
	}

}
