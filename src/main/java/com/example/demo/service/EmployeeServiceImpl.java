package com.example.demo.service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.entity.ErrorRequest;
import com.example.demo.entity.OrderResponseDTO;
import com.example.demo.entity.Request;
import com.example.demo.entity.Response;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repository.EmployeeDao;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private OrderServiceClient orderServiceClient;
	
	

	@Override
	public ResponseEntity<Response> addEmployee(Request request) throws EmployeeNotFoundException {
		Request req=null;
		Response response=null;
	    try {		
			/*if(request.getEmpId()==null || request.getEmpId().isEmpty()) {
			  request.setEmpId("EMP"+System.currentTimeMillis());
		    }*/
		    if((request.getEmpName()!=null || !request.getEmpName().isEmpty()) 
		    	&& (request.getEmpDept()!=null || !request.getEmpDept().isEmpty())
		    	&& (request.getEmpSalary()>0)
		      ) {
			     req=employeeDao.save(request);
		    }    
			response=new Response();
			response.setEId(req.getEmpId());
			response.setDescription("Employee added successfully");
			response.setStatus("Success");
	    }catch(Exception e) {
	    	throw new EmployeeNotFoundException(new ErrorRequest("5OO",e.getMessage()));
	    }
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public Page<Request> getAllEmployees(Pageable pageable) {
		Page<Request> pagereq=employeeDao.findAll(pageable);
		return pagereq;
	}

	@Override
	public Request getEmployeeByName(String empName) throws EmployeeNotFoundException {
		Optional<Request> opt=Optional.ofNullable(employeeDao.searchByEmpName(empName));		
		if(opt.isPresent()) {
			return opt.get();
		}
		else {
			throw new EmployeeNotFoundException(new ErrorRequest("5OO","Employee with name "+empName+" not found"));
		}
	}

	@Override
	public ResponseEntity<Response> updateEmployee(Request request, String empId) throws EmployeeNotFoundException {
	    try {
		  return employeeDao.findById(empId).map(existingEmployee -> {
			  if((request.getEmpName()!=null || !request.getEmpName().isEmpty()) 
				    	&& (request.getEmpDept()!=null || !request.getEmpDept().isEmpty())
				    	&& (request.getEmpSalary()>0)
				) {
					existingEmployee.setEmpName(request.getEmpName());
					existingEmployee.setEmpDept(request.getEmpDept());
					existingEmployee.setEmpSalary(request.getEmpSalary());
			  }
			Request updatedEmployee = employeeDao.save(existingEmployee);
			Response response=new Response();
			response.setEId(updatedEmployee.getEmpId());
			response.setDescription("Employee is updated successfully");
			response.setStatus("Success");
			return ResponseEntity.ok(response);
		  }).get();
		}catch(Exception e) {
			throw new EmployeeNotFoundException(new ErrorRequest("5OO",e.getMessage()));
		}
	}

	@Override
	public ResponseEntity<OrderResponseDTO> getOrders(Request request) throws EmployeeNotFoundException, InterruptedException, ExecutionException{
	 try {
		 return orderServiceClient.getOrdersFromOrderService(request).get();
	 }catch(EmployeeNotFoundException e) {
		 System.out.println("Exception in EmployeeServiceImpl getOrders method: "+e.getMessage());
		 throw e;
	  }	
	}
}
