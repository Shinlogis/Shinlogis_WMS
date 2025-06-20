package com.shinlogis.wms.inOutBound.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.inOutBound.model.IOPlanItem;
import com.shinlogis.wms.inOutBound.repository.IOPlanItemDAO;
import com.shinlogis.wms.inOutBound.repository.IOReceiptDAO;
import com.shinlogis.wms.snapshot.model.Snapshot;


/**
 * InboundPlanItemModel 테이블모델 입니다.
 * @author 김예진
 */
public class InboundPlanItemModel extends AbstractTableModel {
	IOPlanItemDAO ioPlanItemDAO = new IOPlanItemDAO();
	IOReceiptDAO ioReceiptDAO = new IOReceiptDAO();
	Snapshot snapshot = new Snapshot();
	
	List<IOPlanItem> inblundPlanItemList;
	String[] column = { "입고예정ID", "입고예정상세ID", "상품코드", "상품명", "상태", "공급사명",
			"수량", "입고예정일", "입고처리"};

	public InboundPlanItemModel() {
		inblundPlanItemList = ioPlanItemDAO.selectIOPlanItems();
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
		IOPlanItem ioPlanItem = inblundPlanItemList.get(rowIndex); // 레코드 값 불러오기
		
		
		switch (columnIndex) {
		case 0:
			return ioPlanItem.getIoReceipt().getIoReceiptId();
		case 1:
			return ioPlanItem.getIoItemId();
		case 2:
			return ioPlanItem.getProductSnapshot().getProductCode();
		case 3:
			return ioPlanItem.getProductSnapshot().getProductName();
		case 4:
			return "상태";
		case 5:
			return ioPlanItem.getProductSnapshot().getSupplierName();
		case 6:
			return ioPlanItem.getPlannedQuantity();
		case 7:
			return ioPlanItem.getProccessedDate();
		case 8:
			return "검수하기";
		default:
			return "";
		}
	}
}
