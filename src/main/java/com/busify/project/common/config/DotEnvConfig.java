package com.busify.project.common.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class to load environment variables from .env file
 * This ensures that .env file variables are available to Spring Boot early in
 * the startup process
 */
public class DotEnvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            // Load .env file from the project root
            Dotenv dotenv = Dotenv.configure()
                    .directory("./") // Look for .env in project root
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            // Create a map of environment variables
            Map<String, Object> envProperties = new HashMap<>();
            dotenv.entries().forEach(entry -> {
                envProperties.put(entry.getKey(), entry.getValue());
            });

            // Add to Spring environment early in the process
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            environment.getPropertySources().addFirst(
                    new MapPropertySource("dotenv", envProperties));

            System.out.println("✅ Successfully loaded .env file with " + envProperties.size() + " variables");

        } catch (Exception e) {
            System.err.println("⚠️ Warning: Could not load .env file: " + e.getMessage());
            System.err.println("💡 Make sure .env file exists in the project root directory");
        }
    }
}