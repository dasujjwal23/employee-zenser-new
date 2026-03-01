package com.example.demo.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.example.demo.entity.OrderResponseDTO;
import com.example.demo.entity.Request;
import com.example.demo.entity.Response;
import com.example.demo.entity.TransactionResponse;
import com.example.demo.entity.TransctionRequest;
import com.example.demo.exception.EmployeeNotFoundException;


public interface EmployeeService {

	public ResponseEntity<Response> addEmployee(Request request) throws EmployeeNotFoundException;

	public Page<Request> getAllEmployees(Pageable pageable);

	public Request getEmployeeByName(String empName) throws EmployeeNotFoundException;

	public ResponseEntity<Response> updateEmployee(Request request, String empId) throws EmployeeNotFoundException;

	public ResponseEntity<OrderResponseDTO> getOrders(Request request) throws EmployeeNotFoundException, InterruptedException, ExecutionException;

	public ResponseEntity<List<TransactionResponse>> getTransactionhistory(String contentType, String uuid,
			TransctionRequest trequest) throws EmployeeNotFoundException,Exception, InterruptedException, ExecutionException;

}
