package com.shinlogis.wms.headquarters.model;

public class HeadquartersUser {
	private int headquartersUserId;
	private String id;
	private String pw;
	private String email;
	private String status;
	private boolean isChecked;
	
	
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getHeadquartersUserId() {
		return headquartersUserId;
	}
	public void setHeadquartersUserId(int headquartersUserId) {
		this.headquartersUserId = headquartersUserId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String textEmail, String selectEmail) {
		this.email = textEmail + "@" + selectEmail;
	}
}
