package com.shinlogis.wms.common.Exception;

public class HeadquartersException extends RuntimeException{

	public HeadquartersException(String msg) {
		super(msg);
	}
	
	public HeadquartersException(Throwable e) {
		super(e);
	}
	
	public HeadquartersException(String msg, Throwable e) {
		super(msg, e);
	}
}
