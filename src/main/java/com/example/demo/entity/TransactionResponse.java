package com.example.demo.entity;

public class TransactionResponse {

	private int sid;
	private String accountNumber;
	private String senderName;
	private String receiverName;
	private String payment;
	
	public int getSid() {
		return sid;
	}
	
	public void setSid(int sid) {
		this.sid = sid;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getSenderName() {
		return senderName;
	}
	
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	public String getReceiverName() {
		return receiverName;
	}
	
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	
	public String getPayment() {
		return payment;
	}
	
	public void setPayment(String payment) {
		this.payment = payment;
	}
	
	public TransactionResponse(int sid, String accountNumber, String senderName, String receiverName, String payment) {
		super();
		this.sid = sid;
		this.accountNumber = accountNumber;
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.payment = payment;
	}
	
	public TransactionResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		return "TransactionResponse [sid=" + sid + ", accountNumber=" + accountNumber + ", senderName=" + senderName
				+ ", receiverName=" + receiverName + ", payment=" + payment + "]";
	}
}
