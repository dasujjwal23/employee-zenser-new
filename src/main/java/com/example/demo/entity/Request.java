package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Request {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JsonProperty("empId")
	private String empId;
	
	@JsonProperty("empName")
	private String empName;
	
	@JsonProperty("empDept")
	private String empDept;
	
	@JsonProperty("empSalary")
	private double empSalary;
	
	@JsonProperty("orderId")
    private String orderId;
	
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
		
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpDept() {
		return empDept;
	}
	public void setEmpDept(String empDept) {
		this.empDept = empDept;
	}
	public double getEmpSalary() {
		return empSalary;
	}
	public void setEmpSalary(double empSalary) {
		this.empSalary = empSalary;
	}
	
   public Request() {
	   super();
   }
	
   public Request(String empId, String empName, String empDept, double empSalary, String orderId) {
	   		this.empId = empId;
	   		this.empName = empName;
	   	    this.empDept = empDept;
	   	    this.empSalary = empSalary;
	   	    this.orderId = orderId;
   }
   
   public String toString() {
	   return "Request [empId=" + empId + ", empName=" + empName + ", empDept=" + empDept + ", empSalary=" + empSalary + ", orderId=" + orderId + "]";
   }
}
