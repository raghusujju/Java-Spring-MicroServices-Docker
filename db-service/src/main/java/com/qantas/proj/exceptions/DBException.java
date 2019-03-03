package com.qantas.proj.exceptions;

import java.util.Date;

public class DBException extends Throwable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Date timestamp = new Date();
	private final String message;
	private final String debugMessage;
	public DBException(final String message, final Throwable cause) {
		this.message=message;
		this.debugMessage=cause.getMessage();
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public String getMessage() {
		return message;
	}
	public String getDebugMessage() {
		return debugMessage;
	}
	
	
}
