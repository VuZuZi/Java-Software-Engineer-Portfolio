package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com"})
@EntityScan(basePackages = {"com.model"})
@EnableJpaRepositories(basePackages = {"com.repository"})
public class PortfolioApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortfolioApplication.class, args);
        System.out.println("🚀 Portfolio Platform đã khởi động!");
        System.out.println("📍 http://localhost:8080");
    }
}