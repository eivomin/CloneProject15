package com.example.cloneproject15;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableJpaAuditing
@SpringBootApplication
public class CloneProject15Application {

    public static void main(String[] args) {
        SpringApplication.run(CloneProject15Application.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080", "http://localhost:3000", "http://127.0.0.1:3000", "http://13.125.6.183:8080", "http://192.168.25.2:3000", "https://team15clone.vercel.app/", "https://hang-hae99-clone-week-o69l.vercel.app/")
                        .allowedOriginPatterns("*")
                        .exposedHeaders("ACCESS_KEY", "REFRESH_KEY", "Authorization", "Set-Cookie", "USER_ROLE")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH")
                        //.allowedHeaders()
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }


}
