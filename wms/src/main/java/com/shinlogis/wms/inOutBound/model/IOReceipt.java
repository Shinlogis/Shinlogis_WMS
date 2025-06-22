package com.shinlogis.wms.inOutBound.model;

import java.util.Date;

import com.shinlogis.wms.headquarters.model.HeadquartersUser;
import com.shinlogis.wms.location.model.Location;

/**
 * 입출고예정 모델입니다.
 * @author 김예진
 */
public class IOReceipt {
	private int ioReceiptId;
	private String ioType;
	private HeadquartersUser user;
	private Date createdAt;
	private Date scheduledDate;
	private Date processedDate;
	private String status;
	private Location location;
	private String supplierName;

	private String firstProductName;
	private int itemCount;

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public String getFirstProductName() {
		return firstProductName;
	}

	public void setFirstProductName(String firstProductName) {
		this.firstProductName = firstProductName;
	}



	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public HeadquartersUser getUser() {
		return user;
	}

	public void setUser(HeadquartersUser user) {
		this.user = user;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getIoReceiptId() {
		return ioReceiptId;
	}

	public void setIoReceiptId(int ioReceiptId) {
		this.ioReceiptId = ioReceiptId;
	}

	public String getIoType() {
		return ioType;
	}

	public void setIoType(String ioType) {
		this.ioType = ioType;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
