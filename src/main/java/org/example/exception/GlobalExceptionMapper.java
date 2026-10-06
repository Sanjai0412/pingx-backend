package org.example.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.model.ApiResponse;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    // Global exception mapper with all domain exceptions in one place
    @Override
    public Response toResponse(Throwable exception) {

        if (exception instanceof IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(ApiResponse.error(exception.getMessage(), "BAD_REQUEST"))
                    .build();
        }
        if (exception instanceof NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(ApiResponse.error(exception.getMessage(), "NOT_FOUND"))
                    .build();
        }
        if (exception instanceof ConflictException) {
            return Response.status(Response.Status.CONFLICT)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(ApiResponse.error(exception.getMessage(), "CONFLICT"))
                    .build();
        }
        // Log the exception to the server console
        exception.printStackTrace();

        // Return a JSON response with status 500
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(ApiResponse.error("Server Error", "INTERNAL_SERVER_ERROR"))
                .build();
    }
}
