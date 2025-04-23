/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.model;

/**
 *
 * @author Nipuna Rajapaksa
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carts {
   
    private int id;
    private int customerId;
   private Map<Integer,Integer> items = new HashMap<>();

    public Carts(int id, int customerId) {
        this.id = id;
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Map<Integer, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Integer> items) {
        this.items = items;
    }

   
   
  
    
}

