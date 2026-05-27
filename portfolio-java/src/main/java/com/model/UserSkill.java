package com.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_skills")
@Data
public class UserSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private Integer proficiencyPercent; // 0-100

    private Integer yearsOfExperience;
}