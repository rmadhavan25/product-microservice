package com.amazonclone.productmicroservice.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.amazonclone.productmicroservice.models.ErrorMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ShippableAreaAlreadyExistsException.class)
	public ResponseEntity<ErrorMessage> ShippableAreaAlreadyExistsException(ShippableAreaAlreadyExistsException saaee,WebRequest request){
		ErrorMessage errorMessage = new ErrorMessage(403,new Date(),saaee.getMessage(),"");
		return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorMessage> ProductNotFoundException(ProductNotFoundException pnfe,WebRequest request){
		ErrorMessage errorMessage = new ErrorMessage(404,new Date(),pnfe.getMessage(),"");
		return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.NOT_FOUND);
	}
}
