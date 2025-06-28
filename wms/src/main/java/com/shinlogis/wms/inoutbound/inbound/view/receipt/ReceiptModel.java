package com.shinlogis.wms.inoutbound.inbound.view.receipt;

import java.util.ArrayList;
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

	private ReceiptDAO ioReceiptDAO = new ReceiptDAO();

	private List<IOReceipt> fullList = new ArrayList<>();  // 전체 리스트
	private List<IOReceipt> pageList = new ArrayList<>();  // 현재 페이지에 보여줄 리스트
	private List<Boolean> selected = new ArrayList<>();    // 체크박스 상태

	private String[] column = {
			"선택", "입고예정ID", "입고 품목", "공급사명", "입고예정일", "상태", "등록일", "처리일", "상세보기"
	};

	/** 생성자 */
	public ReceiptModel() {
		setFullData(Collections.emptyMap());
	}

	/** 전체 데이터 필터링 후 세팅 */
	public void setFullData(Map<String, Object> filters) {
		this.fullList = ioReceiptDAO.selectInboundReceiptsWithItemInfo(filters);
		setCurrentPageData(new ArrayList<>(fullList)); // 기본은 전부 보기
	}

	/** 현재 페이지 데이터 설정 */
	public void setCurrentPageData(List<IOReceipt> pageList) {
		this.pageList = pageList;
		initSelected();
		fireTableDataChanged();
	}

	/** 체크박스 초기화 */
	private void initSelected() {
		this.selected = new ArrayList<>(pageList.size());
		for (int i = 0; i < pageList.size(); i++) selected.add(false);
	}

	/** 선택된 입고전표 리스트 반환 */
	public List<IOReceipt> getSelectedReceipts() {
		List<IOReceipt> result = new ArrayList<>();
		for (int i = 0; i < selected.size(); i++) {
			if (selected.get(i)) result.add(pageList.get(i));
		}
		return result;
	}

	/** 현재 페이지에서 선택된 io_receipt_id 리스트 반환 */
	public List<Integer> getSelectedReceiptIds() {
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < selected.size(); i++) {
			if (selected.get(i)) result.add(pageList.get(i).getIoReceiptId());
		}
		return result;
	}

	/** 특정 행의 IOReceipt 반환 */
	public IOReceipt getIOReceiptAt(int rowIndex) {
		if (rowIndex >= 0 && rowIndex < pageList.size()) {
			return pageList.get(rowIndex);
		}
		return null;
	}

	public List<IOReceipt> getFullList() {
		return fullList;
	}

	@Override
	public int getRowCount() {
		return pageList.size();
	}

	@Override
	public int getColumnCount() {
		return column.length;
	}

	@Override
	public String getColumnName(int col) {
		return column[col];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) return Boolean.class;
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 0 || columnIndex == 8; // 체크박스 + 상세보기
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		IOReceipt ioReceipt = pageList.get(rowIndex);

		switch (columnIndex) {
			case 0: return selected.get(rowIndex);
			case 1: return ioReceipt.getIoReceiptId();
			case 2:
				if (ioReceipt.getItemCount() <= 1) {
					return ioReceipt.getFirstProductName();
				} else {
					return ioReceipt.getFirstProductName() + " 외 " + (ioReceipt.getItemCount() - 1) + "건";
				}
			case 3: return ioReceipt.getSupplierName();
			case 4: return ioReceipt.getScheduledDate();
			case 5: return ioReceipt.getStatus();
			case 6: return ioReceipt.getCreatedAt();
			case 7: return ioReceipt.getProcessedDate() != null ? ioReceipt.getProcessedDate() : "처리 전";
			case 8: return "선택";
			default: return "";
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == 0 && rowIndex < selected.size()) {
			selected.set(rowIndex, (Boolean) aValue);
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}
}

