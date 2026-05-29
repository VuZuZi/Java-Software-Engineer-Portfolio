package com.backend.portfolio;

import com.backend.portfolio.entity.User;
import com.backend.portfolio.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataTest {

    @Bean
    CommandLineRunner testDb(UserRepository repo) {
        return args -> {
            User u = new User();
            u.setEmail("test@gmail.com");
            u.setPassword("123"); // test thôi

            repo.save(u);

            System.out.println("✅ DB OK: " + repo.findAll().size());
        };
    }
}