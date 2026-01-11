package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Request {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String empId;
	private String empName;
	private String empDept;
	private double empSalary;
		
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
	
   public Request(String empId, String empName, String empDept, double empSalary) {
	   		this.empId = empId;
	   		this.empName = empName;
	   	    this.empDept = empDept;
	   	    this.empSalary = empSalary;
   }
   
   public String toString() {
	   return "Request [empId=" + empId + ", empName=" + empName + ", empDept=" + empDept + ", empSalary=" + empSalary + "]";
   }
}
