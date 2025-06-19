package com.shinlogis.wms.inOutBound.model;

import java.util.Date;

public class IOReceipt {
	private int ioReceiptId;
	private String ioType;
	private int userId;
	private Date createdAt;
	private Date scheduledDate;
	private Date processedDate;
	private String status;
	private int locationId;
	
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
}
