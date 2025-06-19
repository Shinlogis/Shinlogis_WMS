package com.shinlogis.wms.storageType.model;

public class StorageType {
	private int storageTypeId;
	private String typeCode;
	private String typeName;
	
	public int getStorageTypeId() {
		return storageTypeId;
	}
	public void setStorageTypeId(int storageTypeId) {
		this.storageTypeId = storageTypeId;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
