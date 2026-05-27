package com.model;  // ← giữ nguyên

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String role;
    private String technologies;
    private String imageUrl;       // Lưu URL ảnh từ Cloudinary
    private String imagePublicId;  // Lưu public_id để xóa sau
    private String logoUrl;
    private String logoPublicId;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "project_required_skills",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> requiredSkills = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.OPEN;

    private String githubLink;
}
