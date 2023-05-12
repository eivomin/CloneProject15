package com.example.cloneproject15;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CloneProject15Application {

    public static void main(String[] args) {
        SpringApplication.run(CloneProject15Application.class, args);
    }

}
