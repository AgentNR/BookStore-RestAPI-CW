/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.resources;


import com.iit.client_server_cw.model.Books;
import java.awt.PageAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
/**
 *
 * @author Nipuna Rajapaksa
 */
@Path("/books")
public class BookResource {
    
    private static final Map<Integer , Books> books = new ConcurrentHashMap<>();
    private static final AtomicInteger counter = new AtomicInteger(0);
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBooks(Books book){
        
        int bookId  = counter.incrementAndGet();
        book.setId(bookId);
        books.put(bookId, book);
        
         return Response.status(Response.Status.CREATED)
                 .entity(book).build();                
        
       
    
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Books> getAllBooks(){
        
        return new ArrayList<>(books.values());  
    
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") int id){
        
        Books book = books.get(id);
        
        
        if (book != null) {
            
            return Response.status(Response.Status.ACCEPTED).entity(book).build();
            
        }
         return Response.status(Response.Status.NOT_ACCEPTABLE).entity("The Books is not avaialble").build();
    }
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") int id, Books updatedBook) {
        if (books.containsKey(id)) {
            Books existing = books.get(id);

            if (updatedBook.getTitle() != null) {
                existing.setTitle(updatedBook.getTitle());
            }
            if (updatedBook.getAuthorId() != 0) {
                existing.setAuthorId(updatedBook.getAuthorId());
            }
            if (updatedBook.getIsbn() != null) {
                existing.setIsbn(updatedBook.getIsbn());
            }
            if (updatedBook.getPublicationYear() != 0) {
                existing.setPublicationYear(updatedBook.getPublicationYear());
            }
            if (updatedBook.getPrice() != 0.0) {
                existing.setPrice(updatedBook.getPrice());
            }
            if (updatedBook.getStock() != 0) {
                existing.setStock(updatedBook.getStock());
            }

            books.put(id, existing);
            return Response.ok(existing).build();

        } else {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Book with ID " + id + " not found")
                           .build();
        }
    }

     @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        if (books.remove(id) != null) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Book with ID " + id + " not found")
                           .build();
        }
    }
    
    public Books getBookbyId(int bookId){
        
        return books.get(bookId);
    
    }
   
    
    
    
    
}
