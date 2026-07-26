package org.example;

import org.example.config.AppBinder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import java.net.URI;

public class App {

    private static final String HOST = System.getenv().getOrDefault("HOST", "0.0.0.0");

    private static final String PORT = System.getenv().getOrDefault("PORT", "8080");

    private static final String BASE_PATH = "/api/";

    private static String getBaseUrl() {
        return "http://" + HOST + ":" + PORT + BASE_PATH;
    }

    public static HttpServer startServer() {
        ResourceConfig rc = new ResourceConfig()
                .packages("org.example.controller", "org.example.config")
                .register(new AppBinder())
                .register(MultiPartFeature.class);

        return GrizzlyHttpServerFactory.createHttpServer(
                URI.create(getBaseUrl()), rc);
    }

    public static void main(String[] args) {
        HttpServer server = startServer();

        System.out.println("Twitter backend started at " + getBaseUrl());
        System.out.println("Hit stop in your IDE to shut it down");
    }
}