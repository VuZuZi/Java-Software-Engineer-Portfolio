package com.backend.portfolio.controller;

import com.backend.portfolio.dto.response.ApiResponse;
import com.backend.portfolio.entity.User;
import com.backend.portfolio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile")
    public ApiResponse<Map<String, Object>> profile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());

        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("email", user.getEmail());
        data.put("role", user.getRole());
        data.put("provider", user.getProvider());

        return ApiResponse.success("Profile fetched successfully", data);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> info() {
        Map<String, Object> data = new HashMap<>();
        data.put("appName", "Portfolio Application");
        data.put("version", "1.0.0");
        data.put("endpoints", new String[]{"/profile", "/dashboard", "/settings"});

        return ApiResponse.success("User info fetched successfully", data);
    }
}