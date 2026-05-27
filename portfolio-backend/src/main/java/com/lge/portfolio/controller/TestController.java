package com.lge.portfolio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/login-success")
    public String loginSuccess(@RequestParam String token) {
        return "JWT: " + token;
    }
}