package com.shinlogis.locationuser.order.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.locationuser.order.repository.StoreOrderDAO;
import com.shinlogis.locationuser.order.repository.StoreOrderItemDAO;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.product.repository.ProductDAO;

public class StoreOrderModel extends AbstractTableModel {
	StoreOrderDAO storeOrderDAO;
	StoreOrder storeOrder;
	ProductDAO productDAO;
	public List<StoreOrder> list;

	String [] column= {
			"주문ID","주문 품목","주문일시","총 가격"
	};
	public StoreOrderModel() {
		storeOrderDAO=new StoreOrderDAO();
		list=storeOrderDAO.selectAll();
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
		//Product product=list.get(row);
		StoreOrder storeOrder = list.get(row);
		
		switch (col) {
        case 0: return storeOrder.getStoreOrderId();
        case 1: if (storeOrder.getItems()==null) {
        	return 0;
        }else if(storeOrder.getItems().size() == 1)return storeOrder.getItems().get(0).getProduct().getProductName();
        else {
            return storeOrder.getItems().get(0).getProduct().getProductName() + " 외 " + (storeOrder.getItems().size() - 1) + "건";
        }
        case 2: return storeOrder.getOrderDate();
        case 3: return storeOrder.getTotalPrice();
        default: return null;
		}
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
	    switch (col) {
	        case 0: return Integer.class;    //주문ID
	        case 1: return String.class;     //주문품목 
	        case 2: return Integer.class;    //주문일시 
	        case 3: return Integer.class;    //총가격 
	        default: return Object.class;   
	    }
	}
	
	//데이터 갱신 
	public void setList(List<StoreOrder> newList) {
	    this.list = newList;
	    fireTableDataChanged(); // JTable에 데이터 변경 알림
	}
	
	


}
