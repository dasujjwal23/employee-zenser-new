package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.example.demo.entity.Request;
import com.example.demo.entity.Response;
import com.example.demo.exception.EmployeeNotFoundException;


public interface EmployeeService {

	public ResponseEntity<Response> addEmployee(Request request) throws EmployeeNotFoundException;

	public Page<Request> getAllEmployees(Pageable pageable);

	public Request getEmployeeByName(String empName) throws EmployeeNotFoundException;

	public ResponseEntity<Response> updateEmployee(Request request, String empId) throws EmployeeNotFoundException;

}
