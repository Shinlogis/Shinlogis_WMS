package com.shinlogis.locationuser.order.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.locationuser.order.repository.StoreOrderDAO;
import com.shinlogis.locationuser.order.repository.StoreOrderItemDAO;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.product.repository.ProductDAO;

public class StoreOrderItemModel extends AbstractTableModel {
	StoreOrderItemDAO storeOrderItemDAO;
	public List<StoreOrderItem> list;

	String[] column = { "주문상세ID", "주문 품목", "수량", "총 가격", "처리상태" };

	public StoreOrderItemModel(int pk) {
		StoreOrderItemDAO storeOrderItemDao = new StoreOrderItemDAO();
		list = storeOrderItemDao.select(pk);

	}

	@Override
	public int getColumnCount() {
		return column.length;
	}

	public String getColumnName(int col) {
		return column[col];
	}

	@Override
	public int getRowCount() {
		return list.size();

	}

	// 행열에 값 넣기
	@Override
	public Object getValueAt(int row, int col) {
		// Product product=list.get(row);
		StoreOrderItem storeOrderItem = list.get(row);

		switch (col) {
		case 0:
			return storeOrderItem.getStoreOrderId();
		case 1:
			return storeOrderItem.getProduct().getProductName();
		case 2:
			return storeOrderItem.getQuantity();
		case 3:
			return storeOrderItem.getQuantity() * storeOrderItem.getProduct().getPrice();
		case 4:
			return storeOrderItem.getStatus();
		default:
			return null;
		}
	}

	// 데이터 갱신
	public void setList(List<StoreOrderItem> newList) {
		this.list = newList;
		fireTableDataChanged(); // JTable에 데이터 변경 알림
	}

}
