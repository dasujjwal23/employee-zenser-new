package com.example.demo.entity;

public class ErrorRequest {

	public String errorCode;
	public String errorMessage;
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public ErrorRequest() {
		super();
	}
	
	public ErrorRequest(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public String toString() {
		return "ErrorRequest [errorCode=" + errorCode + ", errorMessage=" + errorMessage + "]";
	}
}
