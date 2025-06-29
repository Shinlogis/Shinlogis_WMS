package com.shinlogis.wms.inoutbound.outbound.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.inoutbound.model.IODetail;
import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.inoutbound.outbound.repository.OutboundDetailDAO;

public class OutboundDetailModel extends AbstractTableModel {
	List<IODetail> outboundDetailList;

	String[] column = { "출고예정ID", "출고상세ID", "상품코드", "상품명", "출고예정수량", "출고예정일", "출고지점", "보관창고", "상태", "실제출고수량", "출고일",
			"출고확정" };

	public OutboundDetailModel() {
		IOReceipt outboundReceipt = new IOReceipt();
		OutboundDetailDAO outboundDetailDAO = new OutboundDetailDAO();
		outboundDetailList = outboundDetailDAO.selectAllOutboundDetail();
	}

	public OutboundDetailModel(List<IODetail> filteredList) {
		this.outboundDetailList = filteredList;
	}

	@Override
	public int getRowCount() {
		return outboundDetailList.size();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 11;
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
	public Object getValueAt(int row, int col) {
		IODetail outboundDetail = outboundDetailList.get(row);
		String value = null;
		switch (col) {
		case 0:
			value = Integer.toString(outboundDetail.getIoReceipt().getIoReceiptId());
			break;
		case 1:
			value = Integer.toString(outboundDetail.getIoDetailId());
			break;
		case 2:
			value = outboundDetail.getProductSnapshot().getProductCode();
			break;
		case 3:
			value = outboundDetail.getProductSnapshot().getProductName();
			break;
		case 4:
			value = Integer.toString(outboundDetail.getPlannedQuantity());
			break;
		case 5:
			value = outboundDetail.getIoReceipt().getScheduledDate().toString();
			break;
		case 6:
			value = outboundDetail.getIoReceipt().getLocation().getLocationName();
			break;
		case 7:
			value = outboundDetail.getWarehouse().getWarehouseName();
			break;
		case 8:
			value = outboundDetail.getStatus();
			break;
		case 9:
			if ("완료".equals(outboundDetail.getStatus())) {
				value = Integer.toString(outboundDetail.getPlannedQuantity());
				break;
			} else {
				value = "0";
				break;
			}
		case 10:
			if (outboundDetail.getProccessedDate() != null) {
				value = outboundDetail.getProccessedDate().toString();
				break;
			} else {
				value = "출고 전";
				break;
			}
		case 11:
			if (!"완료".equals(outboundDetail.getStatus())) {
				value = "출고확정";
				break;
			} else {
				value = "완료";
				break;
			}
		default:
			return null;
		}

		return value;
	}
	
	//상세 검색을 통해 해당 row에 해당하는 id값을 받아오기 위해 따로 정의한 메서드
	public IODetail getIODetailAt(int row) {
	    return outboundDetailList.get(row);
	}

	
}
