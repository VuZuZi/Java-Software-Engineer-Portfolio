package com.lge.portfolio.service;

import com.lge.portfolio.dto.ProjectRequest;
import com.lge.portfolio.dto.ProjectResponse;
import com.lge.portfolio.entity.Project;
import com.lge.portfolio.exception.ResourceNotFoundException;
import com.lge.portfolio.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<ProjectResponse> getAll() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectResponse::from)
                .toList();
    }

    public ProjectResponse getById(Long id) {
        return ProjectResponse.from(findProject(id));
    }

    public ProjectResponse create(ProjectRequest request) {
        Project project = new Project();
        apply(request, project);
        return ProjectResponse.from(projectRepository.save(project));
    }

    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = findProject(id);
        apply(request, project);
        return ProjectResponse.from(projectRepository.save(project));
    }

    public void delete(Long id) {
        Project project = findProject(id);
        projectRepository.delete(project);
    }

    private Project findProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    private void apply(ProjectRequest request, Project project) {
        project.setTitle(request.title());
        project.setDescription(request.description());
        project.setImageUrl(request.imageUrl());
        project.setSourceUrl(request.sourceUrl());
        project.setLiveUrl(request.liveUrl());
        project.setTechnologies(request.technologies());
    }
}
