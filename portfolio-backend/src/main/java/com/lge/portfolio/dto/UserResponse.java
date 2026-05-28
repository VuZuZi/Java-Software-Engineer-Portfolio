package com.lge.portfolio.dto;

import com.lge.portfolio.entity.Role;
import com.lge.portfolio.entity.User;

public record UserResponse(
        Long id,
        String email,
        String name,
        String avatar,
        Role role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getAvatar(),
                user.getRole()
        );
    }
}
