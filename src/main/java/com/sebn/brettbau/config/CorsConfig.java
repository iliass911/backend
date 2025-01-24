package com.sebn.brettbau.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Adjust the path pattern as needed
                        .allowedOrigins("http://localhost:3000") // Specify allowed origins
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed HTTP methods
                        .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "X-Requesting-Module") // Specify allowed headers
                        .allowCredentials(true) // Allow credentials (cookies, authorization headers, etc.)
                        .maxAge(3600); // Optional: Set how long the response from a pre-flight request can be cached
            }
        };
    }
}
