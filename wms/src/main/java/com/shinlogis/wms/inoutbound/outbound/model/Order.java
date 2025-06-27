package com.shinlogis.wms.inoutbound.outbound.model;

import java.util.List;

import com.shinlogis.locationuser.order.model.StoreOrderItem;
import com.shinlogis.wms.location.model.Location;

/**
 *<h2>StoreOrder을 출고에서 사용하기 좋은 형태로 첨삭
 *<li>대부분 소스코드는 model.StoreOrder인용 @author 예닮
 *
 *@author 이세형 
 */
public class Order {
	private int storeOrderId; 
	private Location location;
	private String orderDate;
	private int totalPrice;
	private List<OrderDetail> items;
	private int count;
	
	public int getStoreOrderId() {
		return storeOrderId;
	}
	public void setStoreOrderId(int storeOrderId) {
		this.storeOrderId = storeOrderId;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<OrderDetail> getItems() {
		return items;
	}
	public void setItems(List<OrderDetail> items) {
		this.items = items;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
