package com.shinlogis.locationuser.order.model;

public class StoreOrderItem {
	private int itemId;
	private int storeOrderId;
	private int productId;
	private int quantity;
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getStoreOrderId() {
		return storeOrderId;
	}
	public void setStoreOrderId(int storeOrderId) {
		this.storeOrderId = storeOrderId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
