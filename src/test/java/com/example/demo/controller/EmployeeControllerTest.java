package com.example.demo.controller;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import com.example.demo.entity.ErrorRequest;
import com.example.demo.entity.Request;
import com.example.demo.entity.Response;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {
	
	@InjectMocks
	private EmployeeController employeeController;
	
	@Mock
	private EmployeeService employeeService;

	@Test
	public void addEmployeeSuccesTest() throws EmployeeNotFoundException {
		Request request=new Request();
		request.setEmpId("EMP123");
		request.setEmpName("John Doe");
		request.setEmpDept("IT");
		request.setEmpSalary(50000);
		request.setOrderId("ORD001");
		
		Response response=new Response();
    	response.setEId("EMP123");
    	response.setDescription("Employee added successfully");
    	response.setStatus("Success");
		
		Mockito.when(employeeService.addEmployee(request)).thenReturn(ResponseEntity.ok(response));
		
		ResponseEntity<Response> re=employeeController.addEmployee(request);
		
		Assertions.assertEquals(request.getEmpId(), re.getBody().getEId());
		
		Assertions.assertDoesNotThrow(() -> {
			employeeController.addEmployee(request);
		});
		
	}
	
	@Test
	public void addEmployeeFailureTest() throws EmployeeNotFoundException {
		Request request=new Request();
		request.setEmpId("EMP123");
		request.setEmpName("John Doe");
		request.setEmpDept("IT");
		request.setEmpSalary(50000);
		request.setOrderId("ORD001");
		
		Mockito.when(employeeService.addEmployee(request)).thenThrow(new EmployeeNotFoundException(new ErrorRequest("500","Database error")));
		
		EmployeeNotFoundException exception=Assertions.assertThrows(EmployeeNotFoundException.class, ()->{
			employeeController.addEmployee(request);
		});
		
		Assertions.assertEquals("Database error", exception.getErrorRequest().getErrorMessage());
 	}
	
	@Test
	public void getAllEmployeesTest() {
		
		Pageable pageable=PageRequest.of(0, 3,Sort.by("empName").ascending());		
		Page<Request> page=new PageImpl<>(
				    Arrays.asList(
						new Request("EMP001","Alice","HR",60000,"xyz"),
						new Request("EMP002","Bob","IT",70000,"abc"),
						new Request("EMP003","Charlie","Finance",80000,"def")
						)
				);
		Mockito.when(employeeService.getAllEmployees(pageable)).thenReturn(page);
		Page<Request> result=employeeController.getAllEmployees(0, 3, "empName", true);
		Assertions.assertEquals(3, result.getContent().size());
	}
}
