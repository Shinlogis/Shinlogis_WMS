package com.shinlogis.wms.product.model;

import com.shinlogis.wms.storageType.model.StorageType;
import com.shinlogis.wms.supplier.model.Supplier;

public class Product {
	private int productId;
	private String productCode;
	private String productName;
	private StorageType storageType;
	private Supplier supplier;
	private int price;
	private boolean isChecked;
	private String thumbnailPath;

	public String getThumbnailPath() {
	    return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
	    this.thumbnailPath = thumbnailPath;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
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

	
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public StorageType getStorageType() {
		return storageType;
	}
	public void setStorageType(StorageType storageType) {
		this.storageType = storageType;
	}

	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	private int quantity;

	public int getQuantity() {
	    return quantity;
	}
	public void setQuantity(int quantity) {
	    this.quantity = quantity;
	}
}
