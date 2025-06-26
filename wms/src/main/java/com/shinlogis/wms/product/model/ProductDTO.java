package com.shinlogis.wms.product.model;

public class ProductDTO {

	private String productCode;
	private String productName;
	private String supplierName;     // supplier.name
	private String storageTypeName;  // storage_type.type_name
	private int price;

	public ProductDTO() {
	}

	public ProductDTO(String productCode, String productName, String supplierName, String storageTypeName, int price) {
		this.productCode = productCode;
		this.productName = productName;
		this.supplierName = supplierName;
		this.storageTypeName = storageTypeName;
		this.price = price;
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

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getStorageTypeName() {
		return storageTypeName;
	}

	public void setStorageTypeName(String storageTypeName) {
		this.storageTypeName = storageTypeName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ProductDTO [productCode=" + productCode + ", productName=" + productName +
		       ", supplierName=" + supplierName + ", storageTypeName=" + storageTypeName + ", price=" + price + "]";
	}
}
