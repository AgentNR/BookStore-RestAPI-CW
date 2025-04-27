/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iit.client_server_cw.mappers;

/**
 *
 * @author Nipuna Rajapaksa
 */

import com.iit.client_server_cw.exception.InvalidInputException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class InvalidInputExceptionMapper implements ExceptionMapper<InvalidInputException> {
    private static final Logger logger =
        LoggerFactory.getLogger(InvalidInputExceptionMapper.class);

    @Override
    public Response toResponse(InvalidInputException ex) {
        logger.error("Invalid input: {}", ex.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(ex.getMessage())
                       .type(MediaType.TEXT_PLAIN)
                       .build();
    }
}
