package com.qantas.proj.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ApiException> handleServiceException(ServiceException exception){
		ApiException apiException = new ApiException(HttpStatus.FORBIDDEN.toString(), exception.getMessage());
		return new ResponseEntity<ApiException>(apiException,HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<ApiException> handleServiceException(HttpClientErrorException exception){
		ApiException apiException = new ApiException(exception.getStatusCode().toString(), exception.getMessage());
		return new ResponseEntity<ApiException>(apiException,exception.getStatusCode());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiException> handleGenericException(Exception exception){
		ApiException apiException = new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), exception.getMessage());
		return new ResponseEntity<ApiException>(apiException,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
