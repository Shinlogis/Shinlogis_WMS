package com.shinlogis.wms.location.model;

public class LocationUser {
	private int locationUserId;
    private String id;
    private String pw;
    private String email;
    private Location location;
    
	
	public int getLocationUserId() {
		return locationUserId;
	}
	public void setLocationUserId(int locationUserId) {
		this.locationUserId = locationUserId;
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
	public void setEmail(String textEmail, String combEmail) {
		this.email = textEmail + "@" + combEmail;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
}
