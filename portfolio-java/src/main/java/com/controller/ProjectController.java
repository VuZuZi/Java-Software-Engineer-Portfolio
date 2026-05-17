package com.controller;

import com.model.Project;
import com.repository.ProjectRepository;
import com.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller  // ← SỬA: @Controller thay vì @RestController
@RequestMapping("/projects")  // ← SỬA: đường dẫn chính
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;  // ← THÊM: Autowired repository

    @Autowired
    private ProjectService projectService;

    // ===== TRANG WEB (trả về HTML) =====

    @GetMapping
    public String listProjects(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        // Lấy user info từ Google
        if (oAuth2User != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("name", oAuth2User.getAttribute("name"));
            userInfo.put("email", oAuth2User.getAttribute("email"));
            userInfo.put("picture", oAuth2User.getAttribute("picture"));
            model.addAttribute("user", userInfo);
        }

        // Lấy danh sách projects từ database
        List<Project> projects = projectRepository.findAll();  // ← SỬA
        if (projects == null) {
            projects = new ArrayList<>();
        }
        model.addAttribute("projects", projects);

        return "projects/list";
    }

    @GetMapping("/create")
    public String showCreateForm(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        if (oAuth2User != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("name", oAuth2User.getAttribute("name"));
            userInfo.put("email", oAuth2User.getAttribute("email"));
            userInfo.put("picture", oAuth2User.getAttribute("picture"));
            model.addAttribute("user", userInfo);
        }
        model.addAttribute("project", new Project());
        return "projects/create";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project,
                                @AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User != null) {
            projectRepository.save(project);
        }
        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String projectDetail(@PathVariable String id,
                                @AuthenticationPrincipal OAuth2User oAuth2User,
                                Model model) {
        if (oAuth2User != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("name", oAuth2User.getAttribute("name"));
            userInfo.put("email", oAuth2User.getAttribute("email"));
            userInfo.put("picture", oAuth2User.getAttribute("picture"));
            model.addAttribute("user", userInfo);
        }

        Project project = projectRepository.findById(id).orElse(null);
        model.addAttribute("project", project);
        return "projects/detail";
    }

    // ===== REST API (trả về JSON) =====

    @GetMapping("/api/list")
    @ResponseBody
    public List<Project> getAllProjectsApi() {
        return projectService.getAllProjects();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Project getProjectByIdApi(@PathVariable String id) {
        return projectService.getProjectById(id);
    }

    @PostMapping("/api/{id}/apply")
    @ResponseBody
    public Object applyToProject(@PathVariable String id, @RequestParam String userId) {
        return projectService.applyToProject(id, userId);
    }
}