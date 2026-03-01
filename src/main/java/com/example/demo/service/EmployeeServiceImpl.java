package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.example.demo.entity.ResponseEmp;
import com.example.demo.entity.TransactionResponse;
import com.example.demo.entity.TransctionRequest;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repository.EmployeeDao;
import com.example.demo.repository.TransactionDao;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private OrderServiceClient orderServiceClient;
	
	@Autowired
	private TransactionDao transactionDao;
	
	Logger logger=LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	@Override
	public ResponseEntity<Response> addEmployee(Request request) throws EmployeeNotFoundException {
		Request req=null;
		Response response=null;
		ResponseEntity<ResponseEmp> res=null;
	    try {		
			if(request.getEmpId()==null || request.getEmpId().isEmpty()) {
			  request.setEmpId("EMP"+System.currentTimeMillis());
		    }
		    if((request.getEmpName()!=null || !request.getEmpName().isEmpty()) 
		    	&& (request.getEmpDept()!=null || !request.getEmpDept().isEmpty())
		    	&& (request.getEmpSalary()>0)
		      ) {
		    	 res=this.orderServiceClient.processTOKafka(request).get();
		    	 if(res.getStatusCode()==HttpStatus.CREATED || res.getStatusCode()==HttpStatus.OK) {
		    	     System.out.println("Response received from Kafka for empId: "+res.getStatusCode());
		    		 System.out.println("Message sent to Kafka successfully to get joining Date: "+res.getBody().getJoingdate());
		    		 req=employeeDao.save(request);
		    	 }else {
		    		 throw new Exception("Failed to save data to database: "+request.getEmpId());
		    	 }
		    }else {
		    	throw new Exception("Invalid employee data. Please provide valid employee details.");
		    }
			response=new Response();
			response.setEId(req.getEmpId());
			response.setDescription("Employee added successfully");
			response.setStatus("Success");
	    }catch(Exception e) {
	    	logger.error("Exception in EmployeeServiceImpl addEmployee method: "+e.getMessage());
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

	@Override
	public ResponseEntity<List<TransactionResponse>> getTransactionhistory(String contentType, String uuid,
			TransctionRequest trequest) throws Exception {
		try {
			String dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
			String startDate=trequest.getStartDate();
			
			if(trequest.getAccountNumber()==null || trequest.getAccountNumber().isEmpty()) {
				throw new EmployeeNotFoundException(new ErrorRequest("500", "Valid Account number is required"));
			}
			
			if(!startDate.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"))
			{
				throw new EmployeeNotFoundException(new ErrorRequest("400", "Invalid start date format. Expected format: " + dateTimePattern));
			}
			String endDate=trequest.getEndDate();
			
			if(!endDate.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"))
			{
				throw new EmployeeNotFoundException(new ErrorRequest("400", "Invalid end date format. Expected format: " + dateTimePattern));
			}
			List<TransactionResponse> transactions = transactionDao.getAllTransactionhistory(trequest);
			
			return ResponseEntity.status(HttpStatus.OK).body(transactions);
		} catch (Exception e) {
			if(e instanceof EmployeeNotFoundException) {
				logger.error("EmployeeNotFoundException in EmployeeServiceImpl getTransactionhistory method: " + e.getMessage());
			} else {
				logger.error("Exception in EmployeeServiceImpl getTransactionhistory method: " + e.getMessage());
				throw e;
				//throw new EmployeeNotFoundException(new ErrorRequest("500", "Error while fetching transaction history: " + e.getMessage()));
			}
			throw e;
		}
	}
}
 