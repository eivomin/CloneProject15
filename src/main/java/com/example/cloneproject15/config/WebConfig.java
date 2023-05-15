package com.example.cloneproject15.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    public void addCorsMappings(final CorsRegistry registry){
        registry.addMapping("/**")
                .exposedHeaders("ACCESS_KEY", "REFRESH_KEY")
                .allowedOriginPatterns("http://localhost:3000","http://localhost:8080","http://13.125.6.183:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD");
    }
}

