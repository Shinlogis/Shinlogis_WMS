package com.shinlogis.wms.snapshot.model;

import java.util.Date;

import com.shinlogis.wms.product.model.Product;
import com.shinlogis.wms.storageType.model.StorageType;
import com.shinlogis.wms.warehouse.model.Warehouse;

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
    private StorageType storageType;
	
	
	public StorageType getStorageType() {
		return storageType;
	}
	public void setStorageType(StorageType storageType) {
		this.storageType = storageType;
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
	
	/**
	 * 스냅샷의 상품과 저장창고의 보관타입이 일치하는지를 반환
	 * @author 김예진
	 * @since 2025-06-25
	 * @param product
	 * @param warehouse
	 * @return
	 */
	public boolean isStorageTypeMatched(Snapshot snapshot, Warehouse warehouse) {
	    return snapshot.getStorageType() != null &&
	           warehouse.getStorageType() != null &&
	           snapshot.getStorageType().getTypeCode().equals(warehouse.getStorageType().getTypeCode());
	}
}
