package com.shinlogis.wms.inOutBound.model;

public class IOPlanItem {
	private int ioItemId;
	private int ioReceiptId;
	private int plannedQuantity;
	private int productSnapshot;
	private int damageCodeId;
	private int damageQuantity;
	private int actualQuantity;

	public int getIoItemId() {
		return ioItemId;
	}

	public void setIoItemId(int ioItemId) {
		this.ioItemId = ioItemId;
	}

	public int getIoReceiptId() {
		return ioReceiptId;
	}

	public void setIoReceiptId(int ioReceiptId) {
		this.ioReceiptId = ioReceiptId;
	}

	public int getPlannedQuantity() {
		return plannedQuantity;
	}

	public void setPlannedQuantity(int plannedQuantity) {
		this.plannedQuantity = plannedQuantity;
	}

	public int getProductSnapshot() {
		return productSnapshot;
	}

	public void setProductSnapshot(int productSnapshot) {
		this.productSnapshot = productSnapshot;
	}

	public int getDamageCodeId() {
		return damageCodeId;
	}

	public void setDamageCodeId(int damageCodeId) {
		this.damageCodeId = damageCodeId;
	}

	public int getDamageQuantity() {
		return damageQuantity;
	}

	public void setDamageQuantity(int damageQuantity) {
		this.damageQuantity = damageQuantity;
	}

	public int getActualQuantity() {
		return actualQuantity;
	}

	public void setActualQuantity(int actualQuantity) {
		this.actualQuantity = actualQuantity;
	}
}
