package org.example.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.example.model.ApiResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

@Provider
@Secured
public class JwtAuthFilter implements ContainerRequestFilter {
    public static final String ACCESS_TOKEN_SECRET = System.getenv("ACCESS_TOKEN_SECRET");

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the access token from http cookie
        System.out.println("Path: " + requestContext.getUriInfo().getPath());

        Cookie cookie = requestContext.getCookies().get("accessToken");
        System.out.println("Cookie: " + cookie);
        if (cookie == null) {
            abortWithUnauthorized(requestContext, "Access token is missing");
            return;
        }

        String token = cookie.getValue(); // access token

        try {
            SecretKey key = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String userId = claims.get("userId", String.class);
            String username = claims.get("username", String.class);

            System.out.println("UserId: " + userId);
            requestContext.setProperty("userId", userId);
            requestContext.setProperty("username", username);
        } catch (Exception e) {
            abortWithUnauthorized(requestContext, "Invalid or expired JWT token");
        }
    }

    public void abortWithUnauthorized(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity(ApiResponse.error(message, "INVALID_TOKEN"))
                        .build());
    }
}
