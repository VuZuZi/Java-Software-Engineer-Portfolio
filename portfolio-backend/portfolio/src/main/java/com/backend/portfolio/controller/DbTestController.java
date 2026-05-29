package com.backend.portfolio.controller;

import com.backend.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class DbTestController {

    private final UserRepository userRepository;

    @GetMapping("/db")
    public Map<String, Object> db() {

        return Map.of(
                "database", "CONNECTED",
                "users", userRepository.count()
        );
    }
}
