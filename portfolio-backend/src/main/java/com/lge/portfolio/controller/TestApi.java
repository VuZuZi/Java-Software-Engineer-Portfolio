package com.lge.portfolio.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestApi {

    @GetMapping("/test")
    public String test() {
        return "JWT OK";
    }
}