package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransctionRequest {

	@JsonProperty("tid")
	private String tid;
	
	@JsonProperty("accountNumber")
	private String accountNumber;
	
	@JsonProperty("startDate")
	private String startDate;
	
	@JsonProperty("endDate")
	private String endDate;
	
	public String getTid() {
		return tid;
	}
	
	public void setTid(String tid) {
		this.tid = tid;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public TransctionRequest(String tid, String accountNumber, String startDate, String endDate) {
		super();
		this.tid = tid;
		this.accountNumber = accountNumber;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public TransctionRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		return "TransctionRequest [tid=" + tid + ", accountNumber=" + accountNumber + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}
}
