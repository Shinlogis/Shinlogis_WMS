package com.shinlogis.wms.inoutbound.outbound.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.locationuser.order.model.StoreOrderItem;
import com.shinlogis.wms.inoutbound.outbound.model.OrderDetail;
import com.shinlogis.wms.inoutbound.outbound.repository.OrderDetailDAO;

/**
 * <h2>출고페이지에서 주문상세조회에 뿌려줄 모델
 * <li>DAO 및 메서드일체 인용 @author 예닮
 * 
 * @author 이세형
 * */
public class OrderDetailModel extends AbstractTableModel{
	
	OrderDetailDAO OrderDetailDAO;
	public List<OrderDetail> list;

	String[] column = { "주문상세ID", "주문 품목", "수량", "총 가격", "처리상태" };

	public OrderDetailModel(int pk) {
		OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
		list = orderDetailDAO.select(pk);

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
		OrderDetail orderDetail = list.get(row);

		switch (col) {
		case 0:
			return orderDetail.getOrderId();
		case 1:
			return orderDetail.getProduct().getProductName();
		case 2:
			return orderDetail.getQuantity();
		case 3:
			return orderDetail.getQuantity() * orderDetail.getProduct().getPrice();
		case 4:
			return orderDetail.getStatus();
		default:
			return null;
		}
	}

	// 데이터 갱신
	public void setList(List<OrderDetail> newList) {
		this.list = newList;
		fireTableDataChanged(); // JTable에 데이터 변경 알림
	}



}
