package com.iit.client_server_cw.resources;

import com.iit.client_server_cw.model.Carts;
import com.iit.client_server_cw.model.Items;
import com.iit.client_server_cw.model.Orders;
import com.iit.client_server_cw.model.Books;

import com.iit.client_server_cw.exception.CartNotFoundException;


import com.iit.client_server_cw.exception.CustomerNotFoundException;
import com.iit.client_server_cw.exception.InvalidInputException;

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

    // customerId → ( orderId → Orders )
    private static final ConcurrentHashMap<Integer, HashMap<Integer,Orders>> orderStore =
        new ConcurrentHashMap<>();

    private static final AtomicInteger counter = new AtomicInteger(0);

   
    @POST
    public Response placeOrder(@PathParam("customerId") int customerId) {
        // 1) Ensure customer exists
        if (!CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException(
                "Customer with ID " + customerId + " not found"
            );
        }

        // 2) Fetch & validate cart
        Carts cart = CartResource.getCartForCustomer(customerId);
        if (cart.getItems().isEmpty()) {
            throw new CartNotFoundException(
                "Cart is empty for customer " + customerId
            );
        }

        // 3) Compute total and validate books
        double total = 0;
        for (Items ci : cart.getItems()) {
            if (ci.getBookQuantity() <= 0) {
                throw new InvalidInputException(
                    "bookQuantity must be positive for bookId " + ci.getBookId()
                );
            }
           Books book = new BookResource().getBookbyId(ci.getBookId());
            // getBookbyId() itself will throw BookNotFoundException if null
            total += ci.getBookQuantity() * book.getPrice();
        }

        // 4) Build the order
        int orderId = counter.incrementAndGet();
        Orders order = new Orders(orderId, cart, LocalDateTime.now(), total);

        // 5) Persist
        orderStore
          .computeIfAbsent(customerId, id -> new HashMap<>())
          .put(orderId, order);

        // 6) Clear the cart
        cart.getItems().clear();

        // 7) Return the created order
        return Response.status(Response.Status.CREATED)
                       .entity(order)
                       .build();
    }

    
    @GET
    public Response getAllOrders(@PathParam("customerId") int customerId) {
        if (!CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException(
                "Customer with ID " + customerId + " not found"
            );
        }
        Map<Integer,Orders> custMap = orderStore.get(customerId);
        List<Orders> list = (custMap == null)
            ? Collections.emptyList()
            : new ArrayList<>(custMap.values());
        return Response.ok(list).build();
    }

    
    @GET
    @Path("/{orderId}")
    public Response getOrderById(
        @PathParam("customerId") int customerId,
        @PathParam("orderId")    int orderId
    ) {
        if (!CustomerResource.customers.containsKey(customerId)) {
            throw new CustomerNotFoundException(
                "Customer with ID " + customerId + " not found"
            );
        }
        Map<Integer,Orders> custMap = orderStore.get(customerId);
        if (custMap == null || !custMap.containsKey(orderId)) {
            throw new NotFoundException(
                "Order " + orderId + " not found for customer " + customerId
            );
        }
        return Response.ok(custMap.get(orderId)).build();
    }
}
