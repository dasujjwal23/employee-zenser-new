package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
	
	private static final String EMPLOYEE_API="EmpAPI";
	
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
}
