package com.backend.portfolio.dto.response;

import com.backend.portfolio.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserProfileResponse {

    private Long id;
    private String email;
    private Role role;
    private String provider;

    public UserProfileResponse() {
    }

    public UserProfileResponse(
            Long id,
            String email,
            Role role,
            String provider
    ) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.provider = provider;
    }

}