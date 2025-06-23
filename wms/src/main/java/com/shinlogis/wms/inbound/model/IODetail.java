package com.shinlogis.wms.inbound.model;

import java.sql.Date;

import com.shinlogis.wms.damagedCode.model.DamagedCode;
import com.shinlogis.wms.snapshot.model.Snapshot;

/**
 * 입출고예정 상세 품목 model입니다.
 * 
 * @author 김예진
 * @since 2025-06-20
 */
public class IODetail {
	private int ioItemId;
	private IOReceipt ioReceipt;
	private int plannedQuantity;
	private Snapshot productSnapshot;
	private DamagedCode damagedCode;
	private int damageQuantity;
	private int actualQuantity;
	private Date proccessedDate;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getProccessedDate() {
		return proccessedDate;
	}

	public void setProccessedDate(Date proccessedDate) {
		this.proccessedDate = proccessedDate;
	}

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
