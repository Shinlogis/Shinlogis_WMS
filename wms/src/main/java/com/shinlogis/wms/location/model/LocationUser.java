package com.shinlogis.wms.location.model;

public class LocationUser {
	private int headquartersUserId;
    private String id;
    private String pw;
    private String email;
    
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
	public void setEmail(String email) {
		this.email = email;
	}
}
