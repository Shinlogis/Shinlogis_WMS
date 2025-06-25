package com.shinlogis.wms.inoutbound.outbound.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.wms.inoutbound.model.IOReceipt;
import com.shinlogis.wms.inoutbound.outbound.repository.OutboundReceiptDAO;

public class OutboundReceiptModel extends AbstractTableModel{
	List<IOReceipt> outboundReceiptList;
	String[] column = {"출고예정ID","출고품목","출고지점","출고예정일","상태","등록일","상세보기"};
	
	public OutboundReceiptModel() {
		OutboundReceiptDAO outboundReceiptDAO = new OutboundReceiptDAO();
		outboundReceiptDAO.countTotal();
		outboundReceiptList = outboundReceiptDAO.selectAllOutbounds();
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
		/*임시로 지정해둠 나중에 구해서 붙일것*/
		outboundReceipt.setFirstProductName("대표물품(수정)");
		switch(col) {
		case 0 : value = Integer.toString(outboundReceipt.getIoReceiptId());break;
		case 1 : value = outboundReceipt.getFirstProductName() + "외" + (outboundReceipt.getItemCount()-1) + "건";break;
		case 2 : value = outboundReceipt.getLocation().getLocationName();break;
		case 3 :	value = outboundReceipt.getScheduledDate().toString();break;
		case 4 : value = outboundReceipt.getStatus();break;
		case 5 : value = outboundReceipt.getCreatedAt().toString();break;
		case 6 : value = "상세보기";break;
		
		}
		
		return value;
	}

}
