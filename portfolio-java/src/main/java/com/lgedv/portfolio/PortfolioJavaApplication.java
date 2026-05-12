package com.lgedv.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortfolioJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortfolioJavaApplication.class, args);
        System.out.println("🚀 Portfolio application started successfully!");
        System.out.println("📱 Access at: http://localhost:8080");
    }
}