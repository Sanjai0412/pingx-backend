package org.example.config;

import jakarta.ws.rs.NameBinding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, METHOD})
public @interface Secured {
    // This is just a marker annotation
    // for Binding JWTAuthFilter with this annotation @Secured
    // Use this annotation to add (middleware)filter on any endpoint/ or a whole class
}
