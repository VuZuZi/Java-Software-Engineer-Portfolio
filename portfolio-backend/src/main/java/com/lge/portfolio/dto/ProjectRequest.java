package com.lge.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 120, message = "Title must be at most 120 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(max = 1000, message = "Description must be at most 1000 characters")
        String description,

        @Size(max = 500, message = "Image URL must be at most 500 characters")
        String imageUrl,

        @Size(max = 500, message = "Source URL must be at most 500 characters")
        String sourceUrl,

        @Size(max = 500, message = "Live URL must be at most 500 characters")
        String liveUrl,

        @Size(max = 300, message = "Technologies must be at most 300 characters")
        String technologies
) {
}
