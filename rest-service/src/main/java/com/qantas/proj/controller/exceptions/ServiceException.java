package com.qantas.proj.controller.exceptions;

public class ServiceException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4529135616934607712L;

	public ServiceException(final String message,Throwable cause) {
		super(message,cause);
		
	}
	
	public ServiceException(final String message) {
		super(message);
		
	}

}
