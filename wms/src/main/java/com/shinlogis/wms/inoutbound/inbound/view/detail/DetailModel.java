package com.shinlogis.wms.inoutbound.inbound.view.detail;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.inoutbound.inbound.repository.DetailDAO;
import com.shinlogis.wms.inoutbound.inbound.repository.ReceiptDAO;
import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.snapshot.model.Snapshot;


/**
 * InboundPlanItemModel 테이블모델 입니다.
 * @author 김예진
 */
public class DetailModel extends AbstractTableModel {
	DetailDAO ioDetailDAO = new DetailDAO();
	ReceiptDAO ioReceiptDAO = new ReceiptDAO();
	Snapshot snapshot = new Snapshot();
	
	List<IODetail> inblundPlanItemList;
	String[] column = { "입고예정ID", "입고예정상세ID", "상품코드", "상품명", "상태", "공급사명",
			"수량", "입고예정일", "처리일", "입고", "수정"};

	public DetailModel() {
		// 전체 목록 로드
		this.inblundPlanItemList = ioDetailDAO.selectIODetails(Collections.emptyMap());
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
		this.inblundPlanItemList = ioDetailDAO.selectIODetails(filters);
		fireTableDataChanged();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 9) { // "입고처리" 버튼 컬럼으로 변경
			IODetail detail = inblundPlanItemList.get(row);
			return detail != null && detail.isProcessable();
		}
		return column == 10; // 수정 컬럼
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
				return ioDetail.getIoReceipt().getIoReceiptId();
			case 1:
				return ioDetail.getIoDetailId();
			case 2:
				return ioDetail.getProductSnapshot().getProductCode();
			case 3:
				return ioDetail.getProductSnapshot().getProductName();
			case 4:
				return ioDetail.getStatus();
			case 5:
				return ioDetail.getProductSnapshot().getSupplierName();
			case 6:
				return ioDetail.getPlannedQuantity();
			case 7:
				return ioDetail.getIoReceipt().getScheduledDate();
			case 8:
				return ioDetail.getProccessedDate(); 
			case 9:
				if (ioDetail != null && ioDetail.isProcessable()) {
					return "검수";
				} else {
					return "완료"; // 버튼 아닌 텍스트로 표시
				}
			case 10:
				return "수정";

		default:
			return "";
		}
	}

}
