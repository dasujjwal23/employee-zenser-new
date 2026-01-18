package com.example.demo.entity;

public class Response {

	public String eId;
	public String description;
	public String status;
	
	public String getEId() {
		return eId;
	}
	public void setEId(String eId) {
		this.eId = eId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Response() {
		super();
	}
	
	public Response(String eId, String description, String status) {
		this.eId = eId;
		this.description = description;
		this.status = status;
	}
	
	public String toString() {
		return "Response [eId=" + eId + ", description=" + description + ", status=" + status + "]";
	}
}
