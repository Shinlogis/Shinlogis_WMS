package com.shinlogis.wms.inoutbound.inbound.view.detail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.inoutbound.inbound.repository.DetailDAO;
import com.shinlogis.wms.inoutbound.inbound.repository.ReceiptDAO;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.snapshot.model.Snapshot;

/**
 * DetailModel 테이블모델입니다. 체크박스 및 페이징 기능 포함.
 * @author 김예진
 */
public class DetailModel extends AbstractTableModel {
	private DetailDAO ioDetailDAO = new DetailDAO();
	private ReceiptDAO ioReceiptDAO = new ReceiptDAO();
	private Snapshot snapshot = new Snapshot();

	private List<IODetail> fullList = new ArrayList<>();     // 전체 리스트
	private List<IODetail> pageList = new ArrayList<>();     // 현재 페이지에 보여줄 리스트
	private List<Boolean> selected = new ArrayList<>();      // 체크박스 상태 리스트

	private String[] column = {
		"선택", "입고예정ID", "입고예정상세ID", "상품코드", "상품명", "상태", "공급사명",
		"수량", "입고예정일", "처리일", "입고", "수정"
	};

	public DetailModel() {
		setData(Collections.emptyMap());
	}

	/** 특정 행의 IODetail 객체 반환 */
	public IODetail getIODetailAt(int rowIndex) {
		if (rowIndex >= 0 && rowIndex < pageList.size()) {
			return pageList.get(rowIndex);
		} else {
			return null;
		}
	}

	/** 검색 필터에 따라 전체 데이터 새로 불러오기 */
	public void setData(Map<String, Object> filters) {
		this.fullList = ioDetailDAO.selectIODetails(filters);
		this.pageList = new ArrayList<>(fullList);
		initSelected();
		fireTableDataChanged();
	}

	public void setFullData(Map<String, Object> filters) {
		this.fullList = ioDetailDAO.selectIODetails(filters);
	}

	public List<IODetail> getFullData() {
		return this.fullList;
	}

	public void setCurrentPageData(List<IODetail> pageData) {
		this.pageList = pageData;
		initSelected();
		fireTableDataChanged();
	}

	/** 선택 초기화 */
	private void initSelected() {
		this.selected = new ArrayList<>(pageList.size());
		for (int i = 0; i < pageList.size(); i++) selected.add(false);
	}

	/** 현재 페이지에서 선택된 상세 리스트 반환 */
	public List<IODetail> getSelectedDetails() {
		List<IODetail> result = new ArrayList<>();
		for (int i = 0; i < selected.size(); i++) {
			if (selected.get(i)) result.add(pageList.get(i));
		}
		return result;
	}

	/** 현재 페이지에서 선택된 io_detail_id 리스트 반환 */
	public List<Integer> getSelectedDetailIds() {
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < selected.size(); i++) {
			if (selected.get(i)) result.add(pageList.get(i).getIoDetailId());
		}
		return result;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 0) return true; // 체크박스
		if (column == 10) { // 입고처리 버튼
			IODetail detail = getIODetailAt(row);
			return detail != null && detail.isProcessable();
		}
		return column == 11; // 수정 버튼
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
		if (columnIndex == 7) return Integer.class;
		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		IODetail ioDetail = pageList.get(rowIndex);
		switch (columnIndex) {
			case 0: return selected.get(rowIndex);
			case 1: return ioDetail.getIoReceipt().getIoReceiptId();
			case 2: return ioDetail.getIoDetailId();
			case 3: return ioDetail.getProductSnapshot().getProductCode();
			case 4: return ioDetail.getProductSnapshot().getProductName();
			case 5: return ioDetail.getStatus();
			case 6: return ioDetail.getProductSnapshot().getSupplierName();
			case 7: return ioDetail.getPlannedQuantity();
			case 8: return ioDetail.getIoReceipt().getScheduledDate();
			case 9: return ioDetail.getProccessedDate();
			case 10: return ioDetail.isProcessable() ? "검수" : "완료";
			case 11: return "수정";
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
