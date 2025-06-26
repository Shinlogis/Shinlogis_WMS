package com.shinlogis.wms.inoutbound.outbound.view;

import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.inoutbound.outbound.repository.OutboundReceiptDAO;

public class OutboundReceiptModel extends AbstractTableModel {
	List<IOReceipt> outboundReceiptList;
	String[] column = { "출고예정ID", "출고품목", "출고지점", "출고예정일", "상태", "등록일", "상세보기" };

	public OutboundReceiptModel() {
		OutboundReceiptDAO outboundReceiptDAO = new OutboundReceiptDAO();
		outboundReceiptDAO.countTotal();
		outboundReceiptList = outboundReceiptDAO.selectAllOutbounds();
	}

	public OutboundReceiptModel(String ioReceiptId, String productName, String locationName, Date scheduledDate,
			Date createdAt, String status) {
		OutboundReceiptDAO dao = new OutboundReceiptDAO();
		outboundReceiptList = dao.selectByCondition(ioReceiptId, productName, locationName, scheduledDate, createdAt,
				status);
	}

	@Override
	public int getRowCount() {
		return outboundReceiptList.size();
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
		IOReceipt outboundReceipt = outboundReceiptList.get(row);
		String value = null;

		switch (col) {
		case 0:
			value = Integer.toString(outboundReceipt.getIoReceiptId());
			break;
		case 1:
			int count = outboundReceipt.getItemCount();
			String name = outboundReceipt.getFirstProductName();
			value = (count <= 1) ? name : name + " 외 " + (count - 1) + "건";
			break;
		case 2:
			value = outboundReceipt.getLocation().getLocationName();
			break;
		case 3:
			value = outboundReceipt.getScheduledDate().toString();
			break;
		case 4:
			value = outboundReceipt.getStatus();
			break;
		case 5:
			value = outboundReceipt.getCreatedAt().toString();
			break;
		case 6:
			value = "상세보기";
			break;
		default:
			return null;
		}

		return value;
	}

}
