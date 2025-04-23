/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.resources;

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
    public Response getAuthorById(@PathParam("id") String id) {
        Author a = authors.get(id);
        if (a != null) {
            return Response.ok(a).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("Author with ID " + id + " not found")
                       .build();
    }    

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author updated) {
        if (!authors.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Author with ID " + id + " not found")
                           .build();
        }
        Author existing = authors.get(id);
        if (updated.getFirstName()!= null) {
            existing.setFirstName(updated.getFirstName());
        }
        if (updated.getLastName()!= null) {
            existing.setLastName(updated.getLastName());
        }
        if (updated.getBiography() != null) {
            existing.setBiography(updated.getBiography());
        }
        authors.put(id, existing);
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") String id) {
        if (authors.remove(id) != null) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("Author with ID " + id + " not found")
                       .build();
    }

    @GET
    @Path("/{id}/books")
    public Response getBooksByAuthor(@PathParam("id") String id) {
        if (!authors.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Author with ID " + id + " not found")
                           .build();
        }
        
        List<Books> all = new BookResource().getAllBooks();
        List<Books> byAuthor = all.stream()
                                 .filter(b -> id.equals(b.getAuthorId()))
                                 .collect(Collectors.toList());
        return Response.ok(byAuthor).build();
    }
}
