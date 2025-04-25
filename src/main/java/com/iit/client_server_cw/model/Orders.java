/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.model;

/**
 *
 * @author Nipuna Rajapaksa
 */
import java.time.LocalDateTime;

public class Orders {
    
    private int id;
    private Carts carts;
    private LocalDateTime orderDate;
    private double totalAmount;

    public Orders() {
    }   
    

    public Orders(int id, Carts carts, LocalDateTime orderDate, double totalAmount) {
        this.id = id;
        this.carts = carts;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public Carts getCarts() {
        return carts;
    }

    public void setCarts(Carts carts) {
        this.carts = carts;
    } 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }   
    

   

   


    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
}
