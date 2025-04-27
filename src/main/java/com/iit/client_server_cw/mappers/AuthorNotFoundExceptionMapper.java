/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.mappers;
import com.iit.client_server_cw.exception.AuthorNotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author User
 */
@Provider
public class AuthorNotFoundExceptionMapper implements ExceptionMapper< AuthorNotFoundException>{
    private static final Logger logger = LoggerFactory.getLogger(AuthorNotFoundExceptionMapper.class.getName());
    
    @Override
    public Response toResponse(AuthorNotFoundException ex) {
        logger.error("Author not found: {}", ex.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .entity(ex.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
    
    
    
}