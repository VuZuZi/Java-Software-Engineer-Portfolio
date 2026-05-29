package com.backend.portfolio.dto.response;


public class AuthResponse {
    private String accessToken;
    private Long userId;
    private String email;

    public AuthResponse(String token, Long userId, String email) {
        this.accessToken = token;
        this.userId = userId;
        this.email = email;
    }
}
