package com.shinlogis.wms.inoutbound.model;

import java.sql.Date;

import com.shinlogis.wms.damagedCode.model.DamagedCode;
import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.snapshot.model.Snapshot;
import com.shinlogis.wms.warehouse.model.Warehouse;

/**
 * 입출고예정 상세 품목 model입니다.
 * 
 * @author 김예진
 * @since 2025-06-20
 */
public class IODetail {
	private int ioDetailId;
	private IOReceipt ioReceipt;
	private int plannedQuantity;
	private Snapshot productSnapshot;
	private Warehouse warehouse;
	private DamagedCode damagedCode;
	private int damageQuantity;
	private int actualQuantity;
	private Date proccessedDate;
	private String status;
	private HeadquartersUser user;
	
	public int getIoDetailId() {
		return ioDetailId;
	}
	public void setIoDetailId(int ioDetailId) {
		this.ioDetailId = ioDetailId;
	}
	public IOReceipt getIoReceipt() {
		return ioReceipt;
	}
	public void setIoReceipt(IOReceipt ioReceipt) {
		this.ioReceipt = ioReceipt;
	}
	public int getPlannedQuantity() {
		return plannedQuantity;
	}
	public void setPlannedQuantity(int plannedQuantity) {
		this.plannedQuantity = plannedQuantity;
	}
	public Snapshot getProductSnapshot() {
		return productSnapshot;
	}
	public void setProductSnapshot(Snapshot productSnapshot) {
		this.productSnapshot = productSnapshot;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	public DamagedCode getDamagedCode() {
		return damagedCode;
	}
	public void setDamagedCode(DamagedCode damagedCode) {
		this.damagedCode = damagedCode;
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
	public Date getProccessedDate() {
		return proccessedDate;
	}
	public void setProccessedDate(Date proccessedDate) {
		this.proccessedDate = proccessedDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public HeadquartersUser getUser() {
		return user;
	}
	public void setUser(HeadquartersUser user) {
		this.user = user;
	}
	
}
