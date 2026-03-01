package com.example.demo.utils;

import java.util.List;

import com.example.demo.entity.TransactionResponse;
import com.example.demo.entity.TransctionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MaskAccountNumber {
	

	  public static String maskAccountNumber(String accountNumber) {
		if (accountNumber == null || accountNumber.length() < 4) {
			return accountNumber; // Return as is if the account number is too short
		}
		
		int length = accountNumber.length();
		String maskedPart = "x".repeat(4); // Masking with asterisks
		String visiblePart = accountNumber.substring(length - 4); // Last 4 digits
		
		return maskedPart + visiblePart; // Combine masked part with visible part
	}
	  
	  
	  public static String maskRequest(TransctionRequest trequest)
	  {
		  String jsonString=null;
		  try {		  
			      ObjectMapper objectMapper=new ObjectMapper();
				  jsonString = objectMapper.writeValueAsString(trequest);
				  String maskedAccountNumber = maskAccountNumber(trequest.getAccountNumber());
				  jsonString = jsonString.replace(trequest.getAccountNumber(), maskedAccountNumber);
			  return jsonString;
		  }catch(Exception e) {
			  return "Error while masking account number: "+e.getMessage();
		  }		  
	  }
	  
	  public static String maskResponse(List<TransactionResponse> tresponses)
	  {
		   String jsonString=null;
		   String result="";
		   try {
			      ObjectMapper objectMapper=new ObjectMapper();
			      for(TransactionResponse res : tresponses) {
					  jsonString = objectMapper.writeValueAsString(res);
					  String maskedAccountNumber = maskAccountNumber(res.getAccountNumber());
					  jsonString = jsonString.replace(res.getAccountNumber(), maskedAccountNumber);
					  result=result+jsonString;
				  }	  
			  return result;
		  }catch(Exception e) {
			  return "Error while masking account number: "+e.getMessage();
		  }		  
	  }
}
