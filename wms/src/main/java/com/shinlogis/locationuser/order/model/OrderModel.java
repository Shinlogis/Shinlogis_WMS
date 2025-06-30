package com.shinlogis.locationuser.order.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.shinlogis.locationuser.order.repository.StoreOrderDAO;
import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.product.repository.ProductDAO;

//상품목록 테이블에 값 채워넣기 
public class OrderModel extends AbstractTableModel{
	StoreOrderDAO storeOrderDAO;
	StoreOrder storeOrder;
	ProductDAO productDAO;
	public List<Product> list;

	String [] column= {
			"선택","상품명","가격","수량 입력 "
	};
	public OrderModel() {
		productDAO= new ProductDAO();
		list=productDAO.selectOrderProduct();  //디비에 있는 상품 다 가져와서 list에 넣기 
		
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
		Product product=list.get(row);
		
		switch (col) {
        case 0: return product.isChecked();
        case 1: return product.getProductName(); 
        case 2: return product.getPrice(); 
        case 3: return product.getQuantity() == 0 ? "" : product.getQuantity();
        default: return null;
		}
	}
	
	//값 가져오기 
	//Object value => 사용자의 입력값 
	@Override
	public void setValueAt(Object value, int row, int col) {
	    Product product = list.get(row);
	    if (col == 0) {
	        product.setChecked((Boolean) value);
	    } else if (col == 3) {
	        try {
	            int quantity = Integer.parseInt(value.toString());
	            product.setQuantity(quantity);
	        } catch (NumberFormatException e) {
	        	product.setQuantity(0);
	        }
	    }
	    //변경 알림 
	    fireTableCellUpdated(row, col);
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
	    switch (col) {
	        case 0: return Boolean.class;   // 체크박스 열
	        case 1: return String.class;   // 상품명
	        case 2: return Integer.class;  // 가격
	        case 3: return Integer.class;  // 수량입력 
	        default: return Object.class;   
	    }
	}
	
	//디비에 있는 전체 상품가져오기 
	public List<Product> getAllProducts() {
	    return list;
	}
	public void setList(List<Product> list) {
		this.list = list;
	}
	
	//첫번째 열(체크박스),마지막 열 편집가능하게 하기 (1값을 반환할 경우 편집 가능)
	@Override
	public boolean isCellEditable(int row, int col) {
		
		return col == 0 ||col==3;
	}

	//체크박스 선택된 물건 가져오기 
	public List<Product> getSelectedProducts() {
		return list.stream()
				.filter(p -> p.isChecked() && p.getQuantity() > 0)
				.toList();
		//return null;
	}
	
	//주문서 얻기 
	public StoreOrder getStoreOrder(int location_id) {
		List<Product> selectedList = getSelectedProducts();
		List<StoreOrderItem> orderItems = new ArrayList<>();
		StoreOrder storeOrder=new StoreOrder();

		int totalPrice=0;
		
		for (Product product : selectedList) {
			StoreOrderItem storeOrderItem=new StoreOrderItem();
			
			storeOrderItem.setProduct(product);
			storeOrderItem.setQuantity(product.getQuantity());
			totalPrice+=(product.getQuantity()* product.getPrice());
			
			orderItems.add(storeOrderItem);
		}
		
		storeOrder.setLocationId(location_id);
		storeOrder.setTotalPrice(totalPrice);
		storeOrder.setItems(orderItems);
		System.out.print(orderItems);
		
		return storeOrder;
	}
	
	public void tableChanged() {
		list=productDAO.selectOrderProduct();
		fireTableDataChanged();
	}
}
