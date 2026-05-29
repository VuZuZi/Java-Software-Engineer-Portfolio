package com.backend.portfolio.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/profile")
    public String profile() {
        return "USER PROFILE";
    }
}