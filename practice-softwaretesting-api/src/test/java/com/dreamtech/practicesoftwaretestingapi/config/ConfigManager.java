package com.dreamtech.practicesoftwaretestingapi.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class ConfigManager {

    private static final Properties PROPERTIES = new Properties();

    private ConfigManager() {}

    static {
        String env = System.getProperty("env", "dit");
        String resource = "application." + env + ".properties";

        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream(resource)) {

            if (input == null) {
                throw new IllegalStateException("Configuration not found: " + resource);
            }

            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

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
            throw new IllegalStateException("Missing configuration: " + key);
        }

        return value;
    }

}
