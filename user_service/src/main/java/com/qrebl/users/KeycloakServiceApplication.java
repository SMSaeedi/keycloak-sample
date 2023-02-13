package com.qrebl.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class KeycloakServiceApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(KeycloakServiceApplication.class, args);
        System.out.println("Done");
    }
}