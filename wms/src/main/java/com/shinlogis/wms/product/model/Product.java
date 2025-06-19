package com.shinlogis.wms.product.model;

public class Product {
	private int productId;
	private String productCode;
	private String productName;
	private int storageTypeId;
	private int supplierId;
	private int price;
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getStorageTypeId() {
		return storageTypeId;
	}
	public void setStorageTypeId(int storageTypeId) {
		this.storageTypeId = storageTypeId;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}
