package com.shinlogis.locationuser.order.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.locationuser.order.repository.OrderDAO;
import com.shinlogis.wms.product.model.Product;




//상품목록 테이블에 값 채워넣기 
public class OrderModel extends AbstractTableModel{
	OrderDAO orderDAO;
	List<StoreOrder> list;
	
	String [] column= {
			"선택","상품명","가격","수량 입력 "
	};
	public OrderModel() {
		orderDAO= new OrderDAO();
		//list=orderDAO.selectOrderProduct();
		
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
	
	//행열에 값 넣기 
	@Override
	public Object getValueAt(int row, int col) {
		StoreOrder storeOrder=list.get(row);
		
		switch (col) {
//        case 0: return product.isChecked();
//        case 1: return product.getProductName(); 
//        case 2: return product.getPrice(); 
        case 3: return "";
        default: return null;
		}
		
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
	    switch (col) {
	        case 0: return Boolean.class;   // 체크박스 열
	        case 1: return String.class;    // 상품명
	        case 2: return Integer.class;   // 가격
	        default: return Object.class;   // 수량 입력 
	    }
	}
	
	//첫번째 열(체크박스),마지막 열 편집가능하게 하기 (1값을 반환할 경우 편집 가능)
	@Override
	public boolean isCellEditable(int row, int col) {
		
		return col == 0 ||col==getColumnCount() -1;
	}

}
