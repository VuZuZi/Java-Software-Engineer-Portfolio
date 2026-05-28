package com.lge.portfolio.repository;

import com.lge.portfolio.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}