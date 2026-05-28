package com.lge.portfolio.dto;

public record AuthResponse(
        String token,
        UserResponse user
) {
}
