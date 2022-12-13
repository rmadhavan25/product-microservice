package com.amazonclone.productmicroservice.exceptions;

public class AreaNotFoundException extends RuntimeException {
	

	private static final long serialVersionUID = 1L;

	public AreaNotFoundException(String msg) {
		super(msg);
	}

}
