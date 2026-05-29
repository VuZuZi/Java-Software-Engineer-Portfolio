package com.backend.portfolio.controller;

import com.backend.portfolio.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public ApiResponse<?> test() {

        return new ApiResponse<>(
                true,
                "Backend running successfully",
                Map.of(
                        "serverTime", LocalDateTime.now(),
                        "status", "OK"
                )
        );
    }
}