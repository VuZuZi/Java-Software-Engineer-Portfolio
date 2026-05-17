package com.repository;

import com.model.Project;
import com.model.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findByStatus(ProjectStatus status);

    @Query("SELECT p FROM Project p WHERE p.owner.id = :userId")
    List<Project> findByOwnerId(@Param("userId") String userId);
}