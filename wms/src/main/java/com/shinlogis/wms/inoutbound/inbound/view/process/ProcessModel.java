package com.shinlogis.wms.inoutbound.inbound.view.process;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.inoutbound.inbound.repository.DetailDAO;
import com.shinlogis.wms.inoutbound.inbound.repository.ReceiptDAO;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.snapshot.model.Snapshot;


/**
 * InboundProcessModel 테이블모델 입니다.
 * @author 김예진
 */
public class ProcessModel extends AbstractTableModel {
	DetailDAO ioDetailDAO = new DetailDAO();
	ReceiptDAO ioReceiptDAO = new ReceiptDAO();
	Snapshot snapshot = new Snapshot();
	
	List<IODetail> inblundPlanItemList;
	String[] column = { "입고상세ID", "상품코드", "상품명", "예정입고수량", "파손코드", "파손수량",
			"실제입고수량", "저장창고", "처리일자", "담당자"};

	public ProcessModel() {
		// 전체 목록 로드
		this.inblundPlanItemList = ioDetailDAO.selectProcess(Collections.emptyMap());
	}

	/**
	 * rowIndex의 io_detail 데이터를 가져오는 메서드입니다
	 * @author 김예진
	 * @since 2025-06-22
	 * @param rowIndex
	 * @return
	 */
	public IODetail getIODetailAt(int rowIndex) {
		if (rowIndex >= 0 && rowIndex < inblundPlanItemList.size()) {
			return inblundPlanItemList.get(rowIndex);
		} else {
			return null;
		}
	}

	/**
	 * 값을 새로 설정하는 메서드
	 * @author 김예진
	 * @since 2025-06-23
	 * @param filters
	 */
	public void setData(Map<String,Object> filters) {
		this.inblundPlanItemList = ioDetailDAO.selectProcess(filters);
		fireTableDataChanged();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		// 편집 가능 컬럼들 반환 
		return column == 10 || column == 9;
	}

	@Override
	public int getRowCount() {
		return inblundPlanItemList.size();
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		String value = null;
		IODetail ioDetail = inblundPlanItemList.get(rowIndex); // 레코드 값 불러오기
		
		switch (columnIndex) {
			case 0:
				return ioDetail.getIoDetailId();
			case 1:
				return ioDetail.getProductSnapshot().getProductCode();
			case 2:
				return ioDetail.getProductSnapshot().getProductName();
			case 3:
				return ioDetail.getPlannedQuantity();
			case 4:
				return ioDetail.getDamagedCode().getName();
			case 5:
				return ioDetail.getDamageQuantity();
			case 6:
				return ioDetail.getActualQuantity();
			case 7:
				return ioDetail.getWarehouse().getWarehouseCode();
			case 8:
				return ioDetail.getProccessedDate();
			case 9:
				return ioDetail.getUser().getId();
		default:
			return "";
		}
	}

}
