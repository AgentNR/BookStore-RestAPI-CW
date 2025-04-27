package com.iit.client_server_cw.resources;

import com.iit.client_server_cw.model.Carts;
import com.iit.client_server_cw.model.Items;

import com.iit.client_server_cw.exception.CartNotFoundException;
import com.iit.client_server_cw.exception.CustomerNotFoundException;
import com.iit.client_server_cw.exception.InvalidInputException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    private static final ConcurrentHashMap<Integer, Carts> carts = new ConcurrentHashMap<>();
    private static final AtomicInteger counter = new AtomicInteger(0);

    @POST
    @Path("/items")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(@PathParam("customerId") int customerId, Items item) {
        boolean customerExist = new CustomerResource().customerExist(customerId);
        if ((!customerExist)) {
            throw new CustomerNotFoundException("Customer " + customerId + " not found");
        }

        
        if (item.getBookId() <= 0 || item.getBookQuantity() <= 0) {
            throw new InvalidInputException("bookId and bookQuantity must be positive");
        }

       
        Carts cart = carts.computeIfAbsent(
            customerId,
            id -> new Carts(id, customerId)
        );

        
        cart.addItem(item.getBookId(), item.getBookQuantity());

        return Response.status(Response.Status.CREATED)
                       .entity(cart)
                       .build();
    }

    @GET
    public Response getCart(@PathParam("customerId") int customerId) {
        boolean customerExist = new CustomerResource().customerExist(customerId);
        if (!customerExist) {
            throw new CustomerNotFoundException("Customer " + customerId + " not found");
        }
        Carts cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer " + customerId + " not found");
        }
        return Response.ok(cart).build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateItem(@PathParam("customerId") int customerId,@PathParam("bookId") int bookId,
        Items updated) {
        boolean customerExist = new CustomerResource().customerExist(customerId);
        if (!customerExist) {
            throw new CustomerNotFoundException("Customer " + customerId + " not found");
        }
        if (updated.getBookQuantity() < 0) {
            throw new InvalidInputException("bookQuantity cannot be negative");
        }
        Carts cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer " + customerId + " not found");
        }

        List<Items> matches = cart.getItems().stream()
            .filter(i -> i.getBookId() == bookId)
            .collect(Collectors.toList());

        if (matches.isEmpty()) {
            throw new CartNotFoundException("Item " + bookId + " not in cart");
        }

        Items line = matches.get(0);
        if (updated.getBookQuantity() == 0) {
            cart.getItems().remove(line);
        } else {
            line.setBookQuantity(updated.getBookQuantity());
        }

        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response deleteItem(@PathParam("customerId") int customerId,
        @PathParam("bookId")int bookId) {
        boolean customerExist = new CustomerResource().customerExist(customerId);
        if (!customerExist) {
            throw new CustomerNotFoundException("Customer " + customerId + " not found");
        }
        Carts cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer " + customerId + " not found");
        }

        boolean removed = cart.getItems().removeIf(i -> i.getBookId() == bookId);
        if (!removed) {
            throw new CartNotFoundException("Item " + bookId + " not in cart");
        }

        return Response.noContent().build();
    }

    
    public static Carts getCartForCustomer(int customerId) {
        Carts cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer " + customerId + " not found");
        }
        return cart;
    }
}
