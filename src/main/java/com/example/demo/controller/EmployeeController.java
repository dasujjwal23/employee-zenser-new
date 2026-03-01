package com.example.demo.controller;


import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.ErrorRequest;
import com.example.demo.entity.OrderResponseDTO;
import com.example.demo.entity.Request;
import com.example.demo.entity.Response;
import com.example.demo.entity.TransactionResponse;
import com.example.demo.entity.TransctionRequest;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.service.EmployeeService;
import com.example.demo.utils.MaskAccountNumber;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	Logger logger=LoggerFactory.getLogger(EmployeeController.class);
	
	
	@GetMapping("/welcome")
	public String greeting(@RequestHeader(value="username", required=true) String email) { 
		return "Welcome to Employee Service"; 
	}
	
	@PostMapping("/api/v1/addEmployee")
	public ResponseEntity<Response> addEmployee(
			@RequestHeader(value="username", required=true) String email,
			@RequestBody Request request) throws EmployeeNotFoundException {
		System.out.println("Email from header in EmployeeController :"+email);
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
	
	@PostMapping("/api/v1/getOrders")
	public ResponseEntity<OrderResponseDTO> getOrdes(@RequestBody Request request) throws EmployeeNotFoundException, InterruptedException, ExecutionException
	{
		try {
		  return this.employeeService.getOrders(request);
		}catch(EmployeeNotFoundException e) {
			throw e;
		}
	}
	
	@PostMapping("/api/v1/getTransactionhistory")
	public ResponseEntity<List<TransactionResponse>> getTransactions(
			@RequestHeader(value="contentType", required=true) String contentType,
			@RequestHeader(value="uuid", required=true) String uuid,
			@RequestBody TransctionRequest trequest) throws Exception
	{
		ResponseEntity<List<TransactionResponse>> response=null;
		trequest.setTid(UUID.randomUUID().toString());
		try {
		  if(contentType==null || contentType.isEmpty()) {
			 if(!contentType.equals("application/json")) {
				  throw new EmployeeNotFoundException(new ErrorRequest("500","Invalid Request: contentType and uuid in headers"));
			  }
			  throw new EmployeeNotFoundException(new ErrorRequest("500","Invalid Request: Missing or empty contentType or uuid in headers"));
		  }
		  
		  if(uuid==null || uuid.isEmpty()) {
			  throw new EmployeeNotFoundException(new ErrorRequest("500","Invalid Request: Missing or empty uuid in headers"));
		  }
		  logger.info("Received request for getTransactionhistory with contentType: {}, uuid: {}, and request: {}", contentType, uuid, MaskAccountNumber.maskRequest(trequest));
		  
		  response=this.employeeService.getTransactionhistory(contentType,uuid,trequest);
		  logger.info("Response from getTransactionhistory: {}", MaskAccountNumber.maskResponse(response.getBody()));
		  return response;
		}catch(Exception e) {			
			if(e instanceof EmployeeNotFoundException) {
				logger.error("EmployeeNotFoundException in EmployeeController getTransactions method: "+e.getMessage());
				throw e;
			}
//			if(e instanceof Exception) {
//				logger.error("Exception in EmployeeController getTransactions method: "+e.getMessage());
//				throw new EmployeeNotFoundException(new ErrorRequest("500","Exception occurred while fetching transaction history: "+e.getMessage()));
//			}
			throw e;
		}
	}
}
 