package org.example;

import org.example.config.AppBinder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class App
{
    public static final String BASE_URL = "http://localhost:8080/api/";

    public static HttpServer startServer(){
        // For Dependency Injection (DI)
        final ResourceConfig rc = new ResourceConfig()
                .packages("org.example.controller", "org.example.config")
                .register(new AppBinder());

        // embedded Grizzly server
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URL), rc);
    }
    public static void main( String[] args )
    {
        final HttpServer server = startServer();
        System.out.println("Twitter backend started at " + BASE_URL);
        System.out.println("Hit stop in your IDE to shut it down");
    }
}
