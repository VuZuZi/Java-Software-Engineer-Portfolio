package com.lge.portfolio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
class RegisterRequest {
    private String email;
    private String password;
    private String name;
}
