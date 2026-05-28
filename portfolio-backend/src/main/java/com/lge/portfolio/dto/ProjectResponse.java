package com.lge.portfolio.dto;

import com.lge.portfolio.entity.Project;

public record ProjectResponse(
        Long id,
        String title,
        String description,
        String imageUrl,
        String sourceUrl,
        String liveUrl,
        String technologies
) {
    public static ProjectResponse from(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getImageUrl(),
                project.getSourceUrl(),
                project.getLiveUrl(),
                project.getTechnologies()
        );
    }
}
