package com.shinlogis.wms.inoutbound.model;

import java.sql.Date;

import com.shinlogis.wms.product.model.Product;

/**
 * 입고 입력 폼 모델 객체입니다
 * @author 김예진
 * @since 2025-06-26
 */
public class InboundForm {
    private Product product;
    private int quantity;
    private Date plannedDate;
    
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Date getPlannedDate() {
		return plannedDate;
	}
	public void setPlannedDate(Date plannedDate) {
		this.plannedDate = plannedDate;
	}
    
    
}
