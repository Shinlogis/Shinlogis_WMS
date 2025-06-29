package com.shinlogis.wms.common.Exception;

public class SupplierException extends RuntimeException{
	
	public SupplierException(String msg) {
		super(msg);
	}
	
	public SupplierException(Throwable e) {
		super(e);
	}

	public SupplierException(String msg, Throwable e) {
		super(msg, e);
	}
}
