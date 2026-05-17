package com.repository;  // ← Package đúng

import com.model.Project;      // ← Import entity
import com.model.ProjectStatus; // ← Import enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {
    // ↑ Kế thừa JpaRepository với <Entity, Kiểu của ID>

    // Method 1: Spring tự động tạo SQL
    // SELECT * FROM projects WHERE status = ?
    List<Project> findByStatus(ProjectStatus status);

    // Method 2: Dùng @Query để viết JPQL tùy chỉnh
    // SELECT * FROM projects WHERE owner_id = ?
    @Query("SELECT p FROM Project p WHERE p.owner.id = :userId")
    List<Project> findByOwnerId(@Param("userId") String userId);
}