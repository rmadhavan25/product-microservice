package com.amazonclone.productmicroservice.exceptions;

public class ShippableAreaAlreadyExistsException extends RuntimeException {
	

	private static final long serialVersionUID = 1L;

	public ShippableAreaAlreadyExistsException(String msg) {
		super(msg);
	}

}
