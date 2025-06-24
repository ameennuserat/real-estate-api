package com.graduation.realestateconsulting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**") // Allow all endpoints
                .allowedOrigins("*") // Allow requests from http://example.com
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(false);// not Allow credentials (e.g., cookies)
//                .maxAge(3600); // Cache the preflight response for 1 hour
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
