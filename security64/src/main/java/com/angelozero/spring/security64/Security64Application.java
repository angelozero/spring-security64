package com.angelozero.spring.security64;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class Security64Application {

    public static void main(String[] args) {
        SpringApplication.run(Security64Application.class, args);
    }

}
