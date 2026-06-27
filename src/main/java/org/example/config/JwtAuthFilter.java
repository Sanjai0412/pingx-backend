package org.example.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.example.model.ApiResponse;

import java.io.IOException;
import java.security.Key;

@Provider
@Secured
public class JwtAuthFilter implements ContainerRequestFilter {
    public static final String SECRET_KEY = Dotenv.load().get("ACCESS_TOKEN_SECRET");
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the "authorization" value from request header where jwt signed token stored
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            abortWithUnauthorized(requestContext, "Missing or invalidate request header");
            return;
        }
        String token = authorizationHeader.substring(7); // to trim "Bearer " from Auth header

        try{
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String userId = claims.get("userId", String.class);
            String username = claims.get("username", String.class);

            requestContext.setProperty("userId", userId);
            requestContext.setProperty("username", username);
        } catch (Exception e) {
            e.printStackTrace();
            abortWithUnauthorized(requestContext, "Invalid or expired JWT token");
        }
    }
    public void abortWithUnauthorized(ContainerRequestContext requestContext, String message){
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ApiResponse<>(false, message, null))
                        .build());
    }
}
