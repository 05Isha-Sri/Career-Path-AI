package com.careerpath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point of the Spring Boot application.
 * The @SpringBootApplication annotation enables auto-configuration,
 * component scanning (searching for controllers, services, entities),
 * and properties configuration.
 */
@SpringBootApplication
public class CareerPathApplication {

    public static void main(String[] args) {
        // Runs the application and starts the embedded Tomcat server on port 8080
        SpringApplication.run(CareerPathApplication.class, args);
    }
}
