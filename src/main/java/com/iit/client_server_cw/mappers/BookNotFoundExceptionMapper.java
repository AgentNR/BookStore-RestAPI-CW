/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.mappers;

/**
 *
 * @author Nipuna Rajapaksa
 */
import com.iit.client_server_cw.exception.BookNotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 1) BookNotFoundException → 404 Not Found
@Provider
public class BookNotFoundExceptionMapper implements ExceptionMapper<BookNotFoundException> {
    private static final Logger logger =
        LoggerFactory.getLogger(BookNotFoundExceptionMapper.class);

    @Override
    public Response toResponse(BookNotFoundException ex) {
        logger.error("Book not found: {}", ex.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                       .entity(ex.getMessage())
                       .type(MediaType.TEXT_PLAIN)
                       .build();
    }
}