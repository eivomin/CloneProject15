package com.example.cloneproject15.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    public void addCorsMappings(final CorsRegistry registry){
        registry.addMapping("/**")
                .exposedHeaders("ACCESS_KEY")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedOrigins("http://localhost:8080", "http://localhost:3000",
                        "http://127.0.0.1:3000","http://13.125.6.183:8080", "http://192.168.25.2:3000")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
