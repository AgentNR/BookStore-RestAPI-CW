/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.resources;

import com.iit.client_server_cw.model.Orders;



import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    // Here customerId is the key and Order objet is the value
    private static final ConcurrentHashMap<Integer,  Orders> orders = new ConcurrentHashMap<>();

    @POST
    public Response createOrder(@PathParam("customerId") String customerId,
                                Orders order) {
        String id = UUID.randomUUID().toString();
        order.setId(id);
        order.setCustomerId(customerId);
        order.setOrderDate(LocalDateTime.now());
        // compute total
        double total = order.getItems().stream()
                            .mapToDouble(i -> i.getPrice() * i.getQuantity())
                            .sum();
        order.setTotalAmount(total);

        orders.computeIfAbsent(customerId, k -> new ConcurrentHashMap<>())
              .put(id, order);

        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public Response getOrders(@PathParam("customerId") String customerId) {
        var custOrders = orders.get(customerId);
        if (custOrders != null) {
            return Response.ok(new ArrayList<>(custOrders.values())).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("No orders for customer " + customerId)
                       .build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrder(@PathParam("customerId") String customerId,
                             @PathParam("orderId") String orderId) {
        var custOrders = orders.get(customerId);
        if (custOrders != null && custOrders.containsKey(orderId)) {
            return Response.ok(custOrders.get(orderId)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("Order " + orderId + " not found for customer " + customerId)
                       .build();
    }
}
