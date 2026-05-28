package com.lge.portfolio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 120, message = "Name must be at most 120 characters")
        String senderName,

        @Email(message = "Email is invalid")
        @NotBlank(message = "Email is required")
        @Size(max = 160, message = "Email must be at most 160 characters")
        String senderEmail,

        @NotBlank(message = "Subject is required")
        @Size(max = 160, message = "Subject must be at most 160 characters")
        String subject,

        @NotBlank(message = "Message is required")
        @Size(max = 2000, message = "Message must be at most 2000 characters")
        String content
) {
}
