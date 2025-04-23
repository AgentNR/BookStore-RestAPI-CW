///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.iit.client_server_cw.model;
//
///**
// *
// * @author Nipuna Rajapaksa
// */
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Orders {
//    
//    private String id;
//    private String customerId;
//    private List<Books> items = new ArrayList<>();
//    private LocalDateTime orderDate;
//    private double totalAmount;
//
//    public Order() {}
//
//    public Order(String id, String customerId, List<OrderItem> items,
//                 LocalDateTime orderDate, double totalAmount) {
//        this.id = id;
//        this.customerId = customerId;
//        this.items = items;
//        this.orderDate = orderDate;
//        this.totalAmount = totalAmount;
//    }
//
//    public String getId() { return id; }
//    public void setId(String id) { this.id = id; }
//
//    public String getCustomerId() { return customerId; }
//    public void setCustomerId(String customerId) { this.customerId = customerId; }
//
//    public List<OrderItem> getItems() { return items; }
//    public void setItems(List<OrderItem> items) { this.items = items; }
//
//    public LocalDateTime getOrderDate() { return orderDate; }
//    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
//
//    public double getTotalAmount() { return totalAmount; }
//    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
//    
//}
