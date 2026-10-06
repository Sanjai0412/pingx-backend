package org.example.config;

public class AppConfig {

    public static final String HOST = getEnv("HOST", "0.0.0.0");
    public static final int PORT = getEnvInt("PORT", 8080);
    public static final String DB_URL = getEnv("DB_URL", "");
    public static final String ACCESS_TOKEN_SECRET = getEnv("ACCESS_TOKEN_SECRET", "");
    public static final int MAX_QUOTE_DEPTH = getEnvInt("MAX_QUOTE_DEPTH", 3);

    public static final String CLOUDINARY_CLOUD_NAME = getEnv("MY_CLOUD_NAME", "");
    public static final String CLOUDINARY_API_KEY = getEnv("MY_API_KEY", "");
    public static final String CLOUDINARY_API_SECRET = getEnv("MY_API_SECRET", "");

    public static String getEnv(String name, String defaultValue) {
        String val = System.getenv(name);
        return (val != null && !val.trim().isEmpty()) ? val.trim() : defaultValue;
    }

    public static int getEnvInt(String name, int defaultValue) {
        String val = System.getenv(name);
        if (val == null || val.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
