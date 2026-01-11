package com.example.demo.exception;

import com.example.demo.entity.ErrorRequest;

public class EmployeeNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ErrorRequest errorRequest;
	
	public EmployeeNotFoundException(ErrorRequest errorRequest) {
		super();
		this.errorRequest = errorRequest;
	}
	public ErrorRequest getErrorRequest() {
		return errorRequest;
	}
	public void setErrorRequest(ErrorRequest errorRequest) {
		this.errorRequest = errorRequest;
	}
	
	public String toString() {
		return "EmployeeNotFoundException [errorRequest=" + errorRequest + "]";
	}
	
}
