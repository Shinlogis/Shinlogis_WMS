package com.shinlogis.wms.inOutBound.model;

import com.shinlogis.wms.damagedCode.model.DamagedCode;
import com.shinlogis.wms.snapshot.model.Snapshot;

public class IOPlanItem {
	private int ioItemId;
	private IOReceipt ioReceipt;
	private int plannedQuantity;
	private Snapshot productSnapshot;
	private DamagedCode damagedCode;
	private int damageQuantity;
	private int actualQuantity;

	public Snapshot getProductSnapshot() {
		return productSnapshot;
	}

	public void setProductSnapshot(Snapshot productSnapshot) {
		this.productSnapshot = productSnapshot;
	}

	public DamagedCode getDamagedCode() {
		return damagedCode;
	}

	public void setDamagedCode(DamagedCode damagedCode) {
		this.damagedCode = damagedCode;
	}

	public int getPlannedQuantity() {
		return plannedQuantity;
	}

	public void setPlannedQuantity(int plannedQuantity) {
		this.plannedQuantity = plannedQuantity;
	}

	public int getIoItemId() {
		return ioItemId;
	}

	public void setIoItemId(int ioItemId) {
		this.ioItemId = ioItemId;
	}

	public IOReceipt getIoReceipt() {
		return ioReceipt;
	}

	public void setIoReceipt(IOReceipt ioReceipt) {
		this.ioReceipt = ioReceipt;
	}

	public DamagedCode getDamageCode() {
		return damagedCode;
	}

	public void setDamageCode(DamagedCode damageCode) {
		this.damagedCode = damageCode;
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
