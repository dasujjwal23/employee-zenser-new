package com.example.demo.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OrderResponseDTO {

	@Id
	@JsonProperty("orderId")
    private String orderId;
	
	@JsonProperty("name")
    private String name;
	
	@JsonProperty("qty")
    private int qty;
	
	@JsonProperty("price")
    private double price;
	
	@JsonProperty("orderDate")
    private Date orderDate;
	
	@JsonProperty("status")
    private String status;
	
	@JsonProperty("estimateDeliveryWindow")
    private int estimateDeliveryWindow;
    
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getEstimateDeliveryWindow() {
		return estimateDeliveryWindow;
	}
	public void setEstimateDeliveryWindow(int estimateDeliveryWindow) {
		this.estimateDeliveryWindow = estimateDeliveryWindow;
	}
	public OrderResponseDTO(String orderId, String name, int qty, double price, Date orderDate, String status,
			int estimateDeliveryWindow) {
		super();
		this.orderId = orderId;
		this.name = name;
		this.qty = qty;
		this.price = price;
		this.orderDate = orderDate;
		this.status = status;
		this.estimateDeliveryWindow = estimateDeliveryWindow;
	}
	@Override
	public String toString() {
		return "OrderResponseDTO [orderId=" + orderId + ", name=" + name + ", qty=" + qty + ", price=" + price
				+ ", orderDate=" + orderDate + ", status=" + status + ", estimateDeliveryWindow="
				+ estimateDeliveryWindow + "]";
	}
	public OrderResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
