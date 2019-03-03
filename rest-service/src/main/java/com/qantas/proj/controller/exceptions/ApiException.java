package com.qantas.proj.controller.exceptions;

import java.util.Date;

public class ApiException {

	private final String statusCode;
	private final String message;
	private final Date time;
	
	public ApiException(final String statusCode, final String message) {
		this.statusCode=statusCode;
		this.message=message;
		time=new Date();
	}

	public String getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}

	public Date getTime() {
		return time;
	}
	
	
	
	
}
