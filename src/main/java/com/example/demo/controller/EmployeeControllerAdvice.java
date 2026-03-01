package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.entity.ErrorRequest;
import com.example.demo.exception.EmployeeNotFoundException;

@RestControllerAdvice
public class EmployeeControllerAdvice {

	Logger logger=LoggerFactory.getLogger(EmployeeControllerAdvice.class);
	
	@ExceptionHandler(EmployeeNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorRequest handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
		//logger.error("Exception observed at Employee Details: " + ex.getMessage());
		return ex.getErrorRequest();
	}
	
	@ExceptionHandler(MissingRequestHeaderException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleGenericException(MissingRequestHeaderException ex) {
		logger.error("Missing required header: " + ex.getHeaderName());
	    Map<String, String> errorResponse = new HashMap<>();
	    
	    errorResponse.put("error", "400");
	    errorResponse.put("message", "Missing required header: " + ex.getHeaderName());
	    return errorResponse;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, String> handleGenericException(Exception ex) {
		logger.error("An unexpected error occurred: " + ex.getMessage());
	    Map<String, String> errorResponse = new HashMap<>();
	    
	    errorResponse.put("error", "500");
	    errorResponse.put("message", ex.getMessage());
	    return errorResponse;
	}
}
