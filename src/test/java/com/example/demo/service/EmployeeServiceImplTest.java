package com.example.demo.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.example.demo.entity.Request;
import com.example.demo.entity.Response;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repository.EmployeeDao;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {
	
	@InjectMocks
	private EmployeeServiceImpl employeeServiceImpl;

	@Mock
	private EmployeeDao employeeDao;
	
   @Test
	public void addEmployeeSuccessTest() throws EmployeeNotFoundException {
    	
    	Request request=new Request();
    	request.setEmpId("EMP123");
    	request.setEmpName("John Doe");
    	request.setEmpDept("IT");
    	request.setEmpSalary(50000);
    	
    	Response response=new Response();
    	response.setEId("EMP123");
    	response.setDescription("Employee added successfully");
    	response.setStatus("Success");
    	
    	Mockito.when(employeeDao.save(request)).thenReturn(request);
    	
    	ResponseEntity<Response> re=employeeServiceImpl.addEmployee(request);
    	
    	Assertions.assertEquals(request.getEmpId(), re.getBody().getEId());
    	Assertions.assertEquals("Employee added successfully", re.getBody().getDescription());
		
	}
    
    @Test
    public void addEmployeeFailureTest() {
		
		Request request=new Request();
		request.setEmpId("EMP123");
		request.setEmpName("John Doe");
		request.setEmpDept("IT");
		request.setEmpSalary(50000);
		
		Mockito.when(employeeDao.save(request)).thenThrow(new RuntimeException("Database error"));
		
		EmployeeNotFoundException exception=Assertions.assertThrows(EmployeeNotFoundException.class, ()->{
			employeeServiceImpl.addEmployee(request);
		});
		
		Assertions.assertEquals("5OO", exception.getErrorRequest().getErrorCode());
		Assertions.assertEquals("Database error", exception.getErrorRequest().getErrorMessage());
    }
}
