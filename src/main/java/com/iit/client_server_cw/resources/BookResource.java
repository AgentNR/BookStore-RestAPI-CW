/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.resources;


import com.iit.client_server_cw.exception.BookNotFoundException;
import com.iit.client_server_cw.exception.InvalidInputException;
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
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found");
        }
        return Response.ok(book).build();
    }
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") int id, Books updatedBook) {
       Books existing = books.get(id);
        if (existing == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found");
        }
        validateUpdate(updatedBook);

        // only overwrite fields that were provided
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

        return Response.ok(existing).build();
    }

     @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
         Books removed = books.remove(id);
        if (removed == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found");
        }
        return Response.noContent().build();
    }
    
    public Books getBookbyId(int bookId){
        
        Books book = books.get(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found");
        }
        return book;
    
    }
     private void validateNewBook(Books book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()|| book.getTitle().equals("")) {
            throw new InvalidInputException("Title is required");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new InvalidInputException("ISBN is required");
        }
        if (book.getPublicationYear() <= 0) {
            throw new InvalidInputException("Publication year must be positive");
        }
        if (book.getPrice() < 0) {
            throw new InvalidInputException("Price cannot be negative");
        }
        if (book.getStock() < 0) {
            throw new InvalidInputException("Stock cannot be negative");
        }
    }

    private void validateUpdate(Books book) {
        // ensure at least one field is present to update
        if (book.getTitle() == null
         && book.getAuthorId() == 0
         && book.getIsbn() == null
         && book.getPublicationYear() == 0
         && book.getPrice() == 0.0
         && book.getStock() == 0) {
            throw new InvalidInputException("At least one field must be provided for update");
        }
        // you can add further per-field validations here if you like
    }
   
    
    
    
    
}
