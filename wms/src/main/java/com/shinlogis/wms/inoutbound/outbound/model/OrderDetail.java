package com.shinlogis.wms.inoutbound.outbound.model;

import com.shinlogis.wms.product.model.Product;
/**
 *<h2>StoreOrderItem을 출고에서 사용하기 좋은 형태로 첨삭
 *<li>대부분 소스코드는 model.StoreOrder인용 @author 예닮
 *
 *@author 이세형 
 */
public class OrderDetail {
	private int itemId;
	private int orderId;
	private int quantity;
	private Product product;
	private String status;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
