package com.iit.client_server_cw.resources;

import com.iit.client_server_cw.model.Carts;
import com.iit.client_server_cw.model.Items;
import com.iit.client_server_cw.model.Orders;
import com.iit.client_server_cw.model.Books;            // your book model
import com.iit.client_server_cw.resources.CartResource;
import com.iit.client_server_cw.resources.CustomerResource;
   

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    /**
     * key   = customerId
     * value =  Order 
     */
    // customerId → ( orderId → Order )
    private static final ConcurrentHashMap<Integer, HashMap<Integer,Orders>> orderStore =
           new ConcurrentHashMap<>();
    
    private static final AtomicInteger counter = new AtomicInteger(0);
    

    /**
     * POST /customers/{customerId}/orders
     * Creates a new order from the customer’s cart.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response placeOrder(@PathParam("customerId") int customerId) {
       
        boolean customerExist = new CustomerResource().customerExist(customerId);
        
        if (!customerExist) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer does not exist")
                    .build();
        }     

        
        Carts cart = CartResource.getCartForCustomer(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Cart is empty for customer " + customerId)
                           .build();
        }

        
        double total = 0;
        for (Items ci : cart.getItems()) {
            
             Books book = new BookResource().getBookbyId(ci.getBookId());
             
             if (book == null) {
                 return Response.status(Response.Status.BAD_REQUEST)
                       .entity("Book with ID " + ci.getBookId() + " not found")
                       .build();
                
            }
            
            
           
            total += ci.getBookQuantity() * book.getPrice();
        }

       
        int orderId  = counter.incrementAndGet();
        Orders order = new Orders(orderId, cart, LocalDateTime.now(), total);

        // Atomically put this order into the customer's sub‐map:
        orderStore
          .computeIfAbsent(customerId, id -> new HashMap<>())
          .put(orderId, order);

        // Clear the cart…
        cart.getItems().clear();

        return Response.status(Response.Status.CREATED)
                       .entity(order)
                       .build();
    
    }

    /**
     * GET /customers/{customerId}/orders
     * Returns all orders for the given customer.
     */
    @GET
    public Response getAllOrders(@PathParam("customerId") int customerId) {
        
         boolean customerExist = new CustomerResource().customerExist(customerId);
        
        if (!customerExist) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer does not exist")
                    .build();
        }     
        
        
        if (!orderStore.containsKey(customerId)) {
            return Response.ok(Collections.emptyList()).build();
        }
        Collection<Orders> orders = orderStore
            .get(customerId)
            .values();
        return Response.ok(new ArrayList<>(orders)).build();
    
    }

    /**
     * GET /customers/{customerId}/orders/{orderId}
     * Returns a specific order if it exists.
     */
    @GET
    @Path("/{orderId}")
    public Response getOrderById(
        @PathParam("customerId") int customerId,
        @PathParam("orderId")    String orderId
    ) {
         boolean customerExist = new CustomerResource().customerExist(customerId);
        
        if (!customerExist) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer does not exist")
                    .build();
        }   
        var custMap = orderStore.get(customerId);
        if (custMap != null && custMap.containsKey(orderId)) {
            return Response.ok(custMap.get(orderId)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("Order " + orderId + " not found for customer " + customerId)
                       .build();
    }
}
