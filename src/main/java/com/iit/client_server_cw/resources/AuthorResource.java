/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.resources;

import com.iit.client_server_cw.exception.AuthorNotFoundException;
import com.iit.client_server_cw.exception.InvalidInputException;
import com.iit.client_server_cw.model.Author;
import com.iit.client_server_cw.model.Books;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    private static final ConcurrentHashMap<Integer, Author> authors = new ConcurrentHashMap<>();
    private static final AtomicInteger counter = new AtomicInteger(0);
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAuthor(Author author) {
        validateNewAuthor(author);
        int id = counter.incrementAndGet();
        author.setId(id);
        authors.put(id, author);
        return Response.status(Response.Status.CREATED).entity(author).build();
    }
    @GET
    public List<Author> getAllAuthors() {
        return new ArrayList<>(authors.values());
    }

    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") int id) {
         Author a = authors.get(id);
        if (a == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        return Response.ok(a).build();
    }    

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author updated) {
         Author existing = authors.get(id);
        if (existing == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        validateUpdateAuthor(updated);

        if (updated.getFirstName() != null) {
            existing.setFirstName(updated.getFirstName());
        }
        if (updated.getLastName() != null) {
            existing.setLastName(updated.getLastName());
        }
        if (updated.getBiography() != null) {
            existing.setBiography(updated.getBiography());
        }

        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
         Author removed = authors.remove(id);
        if (removed == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public Response getBooksByAuthor(@PathParam("id") int id) {
       if (!authors.containsKey(id)) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }

        List<Books> all  = new BookResource().getAllBooks();
        List<Books> byAuthor = all.stream()
            .filter(b -> b.getAuthorId() == id)
            .collect(Collectors.toList());

        return Response.ok(byAuthor).build();
    }
    private void validateNewAuthor(Author author) {
        if (author.getFirstName() == null
         || author.getFirstName().trim().isEmpty()) {
            throw new InvalidInputException("Author first name is required");
        }
        if (author.getLastName() == null
         || author.getLastName().trim().isEmpty()) {
            throw new InvalidInputException("Author last name is required");
        }
        // biography is optional
    }

    private void validateUpdateAuthor(Author author) {
        if (author.getFirstName() == null
         && author.getLastName()  == null
         && author.getBiography()== null) {
            throw new InvalidInputException("At least one field (firstName, lastName, biography) must be provided to update");
        }
    }
}
