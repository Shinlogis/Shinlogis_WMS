package com.shinlogis.wms.common.Exception;

public class OrderInsertException extends RuntimeException {
	
	public OrderInsertException(String msg) {
		super(msg);
	}
	public OrderInsertException(Throwable e) {
		super(e);
	}
	public OrderInsertException(String msg,Throwable e) {
		super(msg,e);
	}
	
}
