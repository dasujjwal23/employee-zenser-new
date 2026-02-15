package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.ErrorRequest;
import com.example.demo.entity.OrderResponseDTO;
import com.example.demo.entity.Request;
import com.example.demo.entity.ResponseEmp;
import com.example.demo.exception.EmployeeNotFoundException;


import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;


@Component
public class OrderServiceClient {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Value("${restaurant.application.name}")
	private String restaurantServiceClient;
	
	@Value("${restaurant.application.uri}")
	private String restaurantServiceClienturi;
	
	@Value("${loan.kafka.hostname}")
	private String kafkaServiceClient;
	
	@Value("${loan.kafka.uri}")
	private String kafkaServiceClienturi;
	
			
	private static final String EMPLOYEE_API="EmpAPI";
	private static final String KAFKA_API="KafkaAPI";
	
	
	//http://RESTAURANT-SERVICE/restaurant/orders/status/" + orderId

	@CircuitBreaker(name = EMPLOYEE_API, fallbackMethod = "getOrdersFromOrderServiceFallback")
	@TimeLimiter(name = EMPLOYEE_API)
	@Bulkhead(name = EMPLOYEE_API, type = Bulkhead.Type.THREADPOOL)
	public CompletableFuture<ResponseEntity<OrderResponseDTO>> getOrdersFromOrderService(Request request) throws EmployeeNotFoundException {
		
		List<ServiceInstance> services=null;
		ResponseEntity<OrderResponseDTO> response=null;
		try {
			//service=discoveryClient.getApplication(restaurantServiceClient).getInstances().get(0);
			services=discoveryClient.getInstances(restaurantServiceClient);
		  if(services.size()!=0 && request.getOrderId()!=null && !request.getOrderId().isEmpty()) {
			  System.out.println("Restaurant Service Instance is :"+services.get(0).getUri());
			  response=restTemplate.getForEntity("http://"+services.get(0).getServiceId()+restaurantServiceClienturi+request.getOrderId(),OrderResponseDTO.class);
		  }	else {
			  throw new EmployeeNotFoundException(new ErrorRequest("500","No instances available for Restaurant Service or Invalid Order Id"));
		  }
		}catch(Exception e) {
			System.out.println("Exception occurred :"+e.getMessage());
			throw new EmployeeNotFoundException(new ErrorRequest("500","Exception occured while calling Restaurant Service:"+e.getMessage()));
		}
		return CompletableFuture.completedFuture(response);
	}
	
	public CompletableFuture<ResponseEntity<OrderResponseDTO>> getOrdersFromOrderServiceFallback(Request request, Throwable t) throws EmployeeNotFoundException {
		throw new EmployeeNotFoundException(new ErrorRequest("500","Exception occured while invoking call to  Restaurant Service:"+t.getMessage()));

	}
   
	@CircuitBreaker(name = KAFKA_API, fallbackMethod = "getOrdersFromKafkaServiceFallback")
	@TimeLimiter(name = KAFKA_API)
	@Bulkhead(name = KAFKA_API, type = Bulkhead.Type.THREADPOOL)
	public CompletableFuture<ResponseEntity<ResponseEmp>> processTOKafka(Request request) throws Exception {
		
		ResponseEntity<ResponseEmp> response=null;
		try {
		  if(request!=null && request.getOrderId()!=null && !request.getOrderId().isEmpty()) {
			  System.out.println("Calling Kafka Service at URL: " + kafkaServiceClient+kafkaServiceClienturi);
			  response=restTemplate.postForEntity(kafkaServiceClient+kafkaServiceClienturi, request, ResponseEmp.class);
		  }	else {
			  String errorMessage = "Invalid Request for Kafka Service: Order ID is missing or empty";
			  //throw new EmployeeNotFoundException(new ErrorRequest("500","Invalid Request for Kafka Service"));
			  throw new Exception(errorMessage);
		  }	
		}catch(Exception e) {
		    /*if(e instanceof EmployeeNotFoundException ex) {
		       System.out.println("Exception inside occurred :"+ex);
			   throw ex;
		       //throw new EmployeeNotFoundException(new ErrorRequest("500","Exception occured while calling Kafka Emp Service:"+ex));
		    }*/
			System.out.println("Exception occurred :"+e);
			//throw new EmployeeNotFoundException(new ErrorRequest("500","Exception occured while calling Kafka Service:"+e.getMessage()));
			throw e;
		}
		return CompletableFuture.completedFuture(response);
	}
	
	public CompletableFuture<ResponseEntity<OrderResponseDTO>> getOrdersFromKafkaServiceFallback(Request request, Throwable t) throws Throwable {
		System.out.println("Exception in Kafka Service Fallback :"+t.getMessage());
//		String mydata = t.toString();
//		Pattern pattern = Pattern.compile("errorMessage=(.*?)]");
//		Matcher matcher = pattern.matcher(mydata);
//		System.out.println("Error message extracted from exception: " + (matcher.find() ? matcher.group(1) : "No error message found in exception"));
		//throw new EmployeeNotFoundException(new ErrorRequest("500","Exception occured while invoking call to Kafka Loan Service:"+ matcher.find()!=null ? matcher.group(1) : "No error message found in exception"));
		//throw new EmployeeNotFoundException(new ErrorRequest("500","Exception occured while invoking call to Kafka Loan Service:"+t.getMessage()));
		throw t;
	}
}
