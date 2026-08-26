package org.example.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.Set;

@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Set<String> ALLOWED_ORIGINS = Set.of(
            "http://localhost:5173",
            "https://pingx-sanjaii04.vercel.app"
    );

    private boolean isAllowedOrigin(String origin) {
        return origin != null && ALLOWED_ORIGINS.contains(origin);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String origin = requestContext.getHeaderString("Origin");

        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())
                && isAllowedOrigin(origin)) {

            requestContext.abortWith(
                    Response.ok()
                            .header("Access-Control-Allow-Origin", origin)
                            .header("Access-Control-Allow-Credentials", "true")
                            .header(
                                    "Access-Control-Allow-Methods",
                                    "GET, POST, PUT, DELETE, OPTIONS, HEAD"
                            )
                            .header(
                                    "Access-Control-Allow-Headers",
                                    "Origin, Content-Type, Accept, Authorization"
                            )
                            .build()
            );
        }
    }

    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext
    ) throws IOException {

        String origin = requestContext.getHeaderString("Origin");

        if (isAllowedOrigin(origin)) {
            responseContext.getHeaders().putSingle(
                    "Access-Control-Allow-Origin",
                    origin
            );

            responseContext.getHeaders().putSingle(
                    "Access-Control-Allow-Credentials",
                    "true"
            );
        }
    }
}