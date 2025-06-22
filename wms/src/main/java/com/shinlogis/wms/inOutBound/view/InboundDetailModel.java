package com.shinlogis.wms.inOutBound.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.inOutBound.model.IODetail;
import com.shinlogis.wms.inOutBound.repository.IODetailDAO;
import com.shinlogis.wms.inOutBound.repository.IOReceiptDAO;
import com.shinlogis.wms.snapshot.model.Snapshot;


/**
 * InboundPlanItemModel 테이블모델 입니다.
 * @author 김예진
 */
public class InboundDetailModel extends AbstractTableModel {
	IODetailDAO ioDetailDAO = new IODetailDAO();
	IOReceiptDAO ioReceiptDAO = new IOReceiptDAO();
	Snapshot snapshot = new Snapshot();
	
	List<IODetail> inblundPlanItemList;
	String[] column = { "입고예정ID", "입고예정상세ID", "상품코드", "상품명", "상태", "공급사명",
			"수량", "입고예정일", "입고처리", "수정"};

	public InboundDetailModel() {
		// 전체 목록 로드
		this.inblundPlanItemList = ioDetailDAO.selectIODetails();
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


	@Override
	public boolean isCellEditable(int row, int column) {
		// 예: 마지막 컬럼(수정 버튼)이면 편집 가능
		return column == 9;
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
				return ioDetail.getIoItemId();
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
				return "검수하기";
			case 9:
				return "수정";

		default:
			return "";
		}
	}

}
