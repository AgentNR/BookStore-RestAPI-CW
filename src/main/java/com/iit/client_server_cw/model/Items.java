/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.model;




/**
 *
 * @author Nipuna Rajapaksa
 */
public class Items {
    
    private int cartId;    
    private int bookId;
    private int bookQuantity;
    
    public Items(){}

    public Items(int bookId, int bookQuantity) {
        this.bookId = bookId;
        this.bookQuantity = bookQuantity;
    }   
    

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBookQuantity() {
        return bookQuantity;
    }

    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }
    
    
    
    
}
