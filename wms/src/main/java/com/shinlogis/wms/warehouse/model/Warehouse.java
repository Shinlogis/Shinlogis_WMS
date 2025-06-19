package com.shinlogis.wms.warehouse.model;

public class Warehouse {
	private int warehouseId;
	private String warehouseName;
	private String address;
	private int storageTypeId;
	private String warehouseCode;

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getStorageTypeId() {
		return storageTypeId;
	}

	public void setStorageTypeId(int storageTypeId) {
		this.storageTypeId = storageTypeId;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

}
