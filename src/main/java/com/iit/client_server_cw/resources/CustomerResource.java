/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.resources;


import com.iit.client_server_cw.model.Customer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private static final ConcurrentHashMap<Integer, Customer> customers = new ConcurrentHashMap<>();
    
    private static final AtomicInteger counter = new AtomicInteger(0);
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Customer cust) {
        int id = counter.incrementAndGet();
        cust.setId(id);
        customers.put(id, cust);
        return Response.status(Response.Status.CREATED).entity(cust).build();
    }  

    @GET
    public List<Customer> getAll() {
        return new ArrayList<>(customers.values());
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        Customer c = customers.get(id);
        if (c != null) return Response.ok(c).build();
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("Customer with ID " + id + " not found")
                       .build();
    }
   

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Customer updated) {
        if (!customers.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Customer with ID " + id + " not found")
                           .build();
        }
        Customer existing = customers.get(id);
        if (updated.getFirstName() != null) existing.setFirstName(updated.getFirstName());
        if (updated.getLastName() != null) existing.setLastName(updated.getLastName());
        if (updated.getEmail() != null) existing.setEmail(updated.getEmail());
        if (updated.getPassword() != null) existing.setPassword(updated.getPassword());
        customers.put(id, existing);
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        if (customers.remove(id) != null) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("Customer with ID " + id + " not found")
                       .build();
    }
}
