package com.iit.client_server_cw.resources;

import com.iit.client_server_cw.model.Customer;
import com.iit.client_server_cw.exception.CustomerNotFoundException;
import com.iit.client_server_cw.exception.InvalidInputException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    // In‐memory store: customerId → Customer
    public static final ConcurrentHashMap<Integer, Customer> customers = new ConcurrentHashMap<>();
    private static final AtomicInteger counter       = new AtomicInteger(0);

    /** Create a new customer */
    @POST
    public Response createCustomer(Customer cust) {
        validateNewCustomer(cust);

        int id = counter.incrementAndGet();
        cust.setId(id);
        customers.put(id, cust);

        return Response.status(Response.Status.CREATED)
                       .entity(cust)
                       .build();
    }

    /** List all customers */
    @GET
    public List<Customer> getAll() {
        return new ArrayList<>(customers.values());
    }

    /** Fetch one by ID */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        Customer cs = customers.get(id);
        if (cs == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
        return Response.ok(cs).build();
    }

    /** Update an existing customer */
    @PUT
    @Path("/{id}")
    public Response update(
        @PathParam("id") int id,
        Customer        updated
    ) {
        Customer existing = customers.get(id);
        if (existing == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
        validateUpdateCustomer(updated);

        if (updated.getFirstName() != null) existing.setFirstName(updated.getFirstName());
        if (updated.getLastName()  != null) existing.setLastName(updated.getLastName());
        if (updated.getEmail()     != null) existing.setEmail(updated.getEmail());
        if (updated.getPassword()  != null) existing.setPassword(updated.getPassword());

        return Response.ok(existing).build();
    }

    /** Delete a customer */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        Customer removed = customers.remove(id);
        if (removed == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
        return Response.noContent().build();
    }

    
    private void validateNewCustomer(Customer cust) {
        if (cust.getFirstName() == null || cust.getFirstName().trim().isEmpty()) {
            throw new InvalidInputException("firstName is required");
        }
        if (cust.getLastName() == null || cust.getLastName().trim().isEmpty()) {
            throw new InvalidInputException("lastName is required");
        }
        if (cust.getEmail() == null || !cust.getEmail().contains("@")) {
            throw new InvalidInputException("A valid email is required");
        }
        if (cust.getPassword() == null || cust.getPassword().length() < 6) {
            throw new InvalidInputException("Password must be at least 6 characters");
        }
    }

    private void validateUpdateCustomer(Customer cust) {
        if (cust.getFirstName() == null 
         && cust.getLastName()  == null
         && cust.getEmail()     == null
         && cust.getPassword()  == null) {
            throw new InvalidInputException(
                "At least one of firstName, lastName, email or password must be provided to update"
            );
        }
    }
    public boolean customerExist(int customerId){
        
        Customer cs = customers.get(customerId);
        if (cs != null) {
            return true;
            
        }
        return false;
        
    }
}
