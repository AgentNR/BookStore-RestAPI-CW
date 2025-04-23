/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.resources;


import com.iit.client_server_cw.model.Carts;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    
    private static final ConcurrentHashMap<Integer, Carts> carts = new ConcurrentHashMap<>();

    @POST
    @Path("/items")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItem(@PathParam("customerId") String customerId, CartItem item) {
        Carts cart = carts.computeIfAbsent(customerId, id -> new Cart(UUID.randomUUID().toString(), id, new ArrayList<>()));
        // if already present, increase quantity
        cart.getItems().stream()
            .filter(i -> i.getBookId().equals(item.getBookId()))
            .findFirst()
            .ifPresentOrElse(
                i -> i.setQuantity(i.getQuantity() + item.getQuantity()),
                () -> cart.getItems().add(item)
            );
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Response getCart(@PathParam("customerId") String customerId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            return Response.ok(cart).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("Cart for customer " + customerId + " not found")
                       .build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateItem(@PathParam("customerId") String customerId,
                               @PathParam("bookId") String bookId,
                               CartItem updated) {
        Cart cart = carts.get(customerId);
        if (cart == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Cart for customer " + customerId + " not found")
                           .build();
        }
        Optional<CartItem> oi = cart.getItems().stream()
                                    .filter(i -> i.getBookId().equals(bookId))
                                    .findFirst();
        if (oi.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Item " + bookId + " not in cart")
                           .build();
        }
        CartItem item = oi.get();
        item.setQuantity(updated.getQuantity());
        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response deleteItem(@PathParam("customerId") String customerId,
                               @PathParam("bookId") String bookId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            boolean removed = cart.getItems().removeIf(i -> i.getBookId().equals(bookId));
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
}
