package com.lgedv.portfolio.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Project {
    private String id;
    private String name;
    private String technologies;
    private String description;
    private String achievements;
    private String imageUrl;
    private String githubUrl;
    private String demoUrl;

    // Constructor đầy đủ
    public Project(String id, String name, String technologies,
                   String description, String achievements,
                   String imageUrl, String githubUrl, String demoUrl) {
        this.id = id;
        this.name = name;
        this.technologies = technologies;
        this.description = description;
        this.achievements = achievements;
        this.imageUrl = imageUrl;
        this.githubUrl = githubUrl;
        this.demoUrl = demoUrl;
    }
}