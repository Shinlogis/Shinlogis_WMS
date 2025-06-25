package com.shinlogis.locationuser.order.model;

import java.time.LocalDateTime;
import java.util.List;

public class StoreOrder {
	private int storeOrderId;
	private int locationId;
	private LocalDateTime orderDate;
	private int totalPrice;
	private List<StoreOrderItem> items;
	
	public int getStoreOrderId() {
		return storeOrderId;
	}
	public void setStoreOrderId(int storeOrderId) {
		this.storeOrderId = storeOrderId;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<StoreOrderItem> getItems() {
		return items;
	}
	public void setItems(List<StoreOrderItem> items) {
		this.items = items;
	}
	
	
}
