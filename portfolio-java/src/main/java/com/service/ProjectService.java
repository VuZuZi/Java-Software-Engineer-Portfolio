package com.service;

import com.model.Project;
import com.model.User;
import com.repository.ProjectRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public Map<String, Object> applyToProject(String projectId, String userId) {
        Project project = getProjectById(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tính match score dựa trên kỹ năng
        int matchScore = calculateMatchScore(user, project);

        Map<String, Object> result = new HashMap<>();
        result.put("project", project);
        result.put("matchScore", matchScore);
        result.put("eligible", matchScore >= 60);

        if (matchScore >= 60) {
            user.getAppliedProjects().add(project);
            userRepository.save(user);
            result.put("message", "Chúc mừng! Bạn đã đủ điều kiện ứng tuyển");
        } else {
            result.put("message", "Kỹ năng của bạn chưa đáp ứng yêu cầu dự án");
        }

        return result;
    }

    private int calculateMatchScore(User user, Project project) {
        // Logic tính điểm match giữa kỹ năng user và yêu cầu project
        // Demo: trả về random 0-100
        return (int) (Math.random() * 100);
    }
}