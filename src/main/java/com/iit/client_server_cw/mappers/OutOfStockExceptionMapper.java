/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.mappers;

/**
 *
 * @author Nipuna Rajapaksa
 */

import com.iit.client_server_cw.exception.AuthorNotFoundException;
import com.iit.client_server_cw.exception.OutOfStockException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Provider
public class OutOfStockExceptionMapper implements ExceptionMapper<OutOfStockException> {
    private static final Logger logger =
        LoggerFactory.getLogger(OutOfStockExceptionMapper.class);

    @Override
    public Response toResponse(OutOfStockException ex) {
        logger.error("Out of stock: {}", ex.getMessage());
        return Response.status(Response.Status.CONFLICT)
                       .entity(ex.getMessage())
                       .type(MediaType.TEXT_PLAIN)
                       .build();
    }
}