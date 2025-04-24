/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.resources;


import com.iit.client_server_cw.model.Carts;
import com.iit.client_server_cw.model.Items;
import com.iit.client_server_cw.model.Customer;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    //Here key is the cutomer id and the value is carts object
    private static final ConcurrentHashMap<Integer, Carts> carts = new ConcurrentHashMap<>();
    private static final AtomicInteger counter = new AtomicInteger(0);
    
    @POST
    @Path("/items")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItem(@PathParam("customerId") int customerId, Items item) {
        
        boolean customerExist = new CustomerResource().customerExist(customerId);
        
        if (!customerExist) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer does not exist")
                    .build();
        }       
               
        
        Carts cart = carts.computeIfAbsent(
            customerId,
            id -> new Carts(id, customerId)  
        );
        // Add/update the item
        cart.addItem(item.getBookId(), item.getBookQuantity());
        return Response
                .status(Response.Status.CREATED)
                .entity(cart)
                .build();
        
        // if already present, increase quantity
        
       
    }

    @GET
    public Response getCart(@PathParam("customerId") int customerId) {
        boolean customerExist = new CustomerResource().customerExist(customerId);
        
        if (!customerExist) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer does not exist")
                    .build();
        }    
        Carts cart = carts.get(customerId);
        if (cart != null) {
            return Response.ok(cart).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("Cart for customer " + customerId + " not found")
                       .build();
    }

    @PUT
    @Path("/items/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItem(@PathParam("customerId") int customerId,
                               @PathParam("bookId") int bookId,
                               Items updated) {
        boolean customerExist = new CustomerResource().customerExist(customerId);
        if (!customerExist) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer does not exist"+ customerId)
                    .build();
        }    
       
        Carts cart = carts.get(customerId);
        if (cart == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Cart for customer " + customerId + " not found")
                           .build();
        }
        List<Items> matches = cart.getItems().stream()
                .filter(d -> d.getBookId()== bookId)
                .collect(Collectors.toList());
        
        

        
        if (matches.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Item " + bookId + " not in cart")
                           .build();
        }
        Items matcheditem = matches.get(0);
        int newQuantity = updated.getBookQuantity();
        if (newQuantity == 0) {
            cart.getItems().remove(matcheditem);
            
        }else{
             matcheditem.setBookQuantity(newQuantity);
        }
       
        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response deleteItem(@PathParam("customerId") int customerId,
                               @PathParam("bookId") int bookId) {
        boolean customerExist = new CustomerResource().customerExist(customerId);
        if (!customerExist) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer does not exist"+ customerId)
                    .build();
        }           
       
        Carts cart = carts.get(customerId);
        if (cart != null) {
            boolean removed = cart.getItems().removeIf(i -> i.getBookId()==(bookId));
            if (removed) {
                return Response.noContent().build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Item " + bookId + " not in cart")
                           .build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("Cart for customer " + customerId + " not found")
                       .build();
    }
    
    public static Carts getCartForCustomer(int customerId) {
        return carts.get(customerId);
    }

}
