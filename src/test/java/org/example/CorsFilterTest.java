package org.example;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.example.config.CorsFilter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CorsFilterTest {

    @Test
    public void testPreflightRequestAborts() throws IOException {
        CorsFilter filter = new CorsFilter();
        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);

        when(requestContext.getMethod()).thenReturn("OPTIONS");
        when(requestContext.getHeaderString("Origin")).thenReturn("https://pingx-sanjaii04.vercel.app");

        filter.filter(requestContext);

        verify(requestContext, times(1)).abortWith(any(Response.class));
    }

    @Test
    public void testNonPreflightRequestDoesNotAbort() throws IOException {
        CorsFilter filter = new CorsFilter();
        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);

        when(requestContext.getMethod()).thenReturn("GET");
        when(requestContext.getHeaderString("Origin")).thenReturn("https://pingx-sanjaii04.vercel.app");

        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any(Response.class));
    }

    @Test
    public void testHeadersAdded() throws IOException {
        CorsFilter filter = new CorsFilter();
        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        ContainerResponseContext responseContext = mock(ContainerResponseContext.class);
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

        when(requestContext.getHeaderString("Origin")).thenReturn("https://pingx-sanjaii04.vercel.app");
        when(responseContext.getHeaders()).thenReturn(headers);

        filter.filter(requestContext, responseContext);

        assertEquals("https://pingx-sanjaii04.vercel.app", headers.getFirst("Access-Control-Allow-Origin"));
        assertEquals("true", headers.getFirst("Access-Control-Allow-Credentials"));
        assertEquals("GET, POST, PUT, DELETE, OPTIONS, HEAD", headers.getFirst("Access-Control-Allow-Methods"));
        assertEquals("Origin, Content-Type, Accept, Authorization", headers.getFirst("Access-Control-Allow-Headers"));
    }
}
