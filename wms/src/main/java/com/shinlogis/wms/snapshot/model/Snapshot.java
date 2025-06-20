package com.shinlogis.wms.snapshot.model;

import java.util.Date;

/**
 * 스냅샷 모델입니다.
 * @author 김예진
 */
public class Snapshot {
	private int snapshotId;
    private String productCode;
    private String productName;
    private String supplierName;
    private int price;
    private Date expiryDate;
	private String storageTypeCode;
	
	public String getStorageTypeCode() {
		return storageTypeCode;
	}
	public void setStorageTypeCode(String storageTypeCode) {
		this.storageTypeCode = storageTypeCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public int getSnapshotId() {
		return snapshotId;
	}
	public void setSnapshotId(int snapshotId) {
		this.snapshotId = snapshotId;
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
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
}
