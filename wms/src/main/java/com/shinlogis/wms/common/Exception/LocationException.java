package com.shinlogis.wms.common.Exception;

public class LocationException extends RuntimeException{

	public LocationException(String msg) {
		super(msg);
	}
	
	public LocationException(Throwable e) {
		super(e);
	}
	
	public LocationException(String msg, Throwable e) {
		super(msg, e);
	}
}
