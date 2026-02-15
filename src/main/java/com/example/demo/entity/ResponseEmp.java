package com.example.demo.entity;

public class ResponseEmp {

	private String eId;
	private String joingdate;
	
	public String geteId() {
		return eId;
	}
	public void seteId(String eId) {
		this.eId = eId;
	}
	public String getJoingdate() {
		return joingdate;
	}
	public void setJoingdate(String joingdate) {
		this.joingdate = joingdate;
	}
	 
	public ResponseEmp() {
		super();
	}
	
	public ResponseEmp(String eId, String joingdate) {
		this.eId = eId;
		this.joingdate = joingdate;
	}
	
	public String toString() {
		return "ResponseEmp [eId=" + eId + ", joingdate=" + joingdate + "]";
	}
}
