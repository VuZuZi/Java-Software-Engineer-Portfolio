package com.lge.portfolio.controller;

import com.lge.portfolio.entity.Project;
import com.lge.portfolio.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")   // 👈 ROOT URL
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // GET /api/projects
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAll();
    }

    // POST /api/projects
    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectService.create(project);
    }

    // DELETE /api/projects/{id}
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.delete(id);
    }
}