package com.dreamtech.toolshopapi.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    /***  P R I V A T E  ***/

    private static String RESOURCE;
    private static final Properties PROPERTIES = new Properties();

    private ConfigManager() {}

    static {
        String env = System.getProperty("env", "dit");
        RESOURCE = "application-" + env + ".properties";

        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream(RESOURCE)) {

            if (input == null) {
                throw new IllegalStateException("Resource not found: " + RESOURCE);
            }

            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /***  P U B L I C  ***/

    public static String get(String key) {
        String envKey = key.toUpperCase().replace('.', '_');
        String value = System.getProperty(key);

        if (value == null || value.isBlank()) {
            value = System.getenv(envKey);
        }
        if (value == null || value.isBlank()) {
            value = PROPERTIES.getProperty(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing configuration for: " + key + " in resource: " + RESOURCE);
        }

        return value;
    }

}