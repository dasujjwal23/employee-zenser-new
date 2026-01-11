package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Request;
import com.example.demo.entity.Response;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.service.EmployeeService;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping("/api/v1/addEmployee")
	public ResponseEntity<Response> addEmployee(@RequestBody Request request) throws EmployeeNotFoundException {
		try {
			return employeeService.addEmployee(request);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/api/v1/getAllEmployees")
	public Page<Request> getAllEmployees(
			@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "empName") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
			) {
		Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(page, size,sort);
		return employeeService.getAllEmployees(pageable);
	}
	
	@GetMapping("/api/v1/getEmployeeByName/{empName}")
	public Request getEmployeeByName(@PathVariable String empName) throws EmployeeNotFoundException {
		return employeeService.getEmployeeByName(empName); // Placeholder return statement
	}
	
	@PutMapping("/api/v1/updateEmployee/{empId}")
	public ResponseEntity<Response> updateEmployee(@RequestBody Request request,@PathVariable String empId) throws EmployeeNotFoundException {
		try {
			return employeeService.updateEmployee(request, empId);
		} catch (Exception e) {
			throw e;
		}
	}
	
}
 