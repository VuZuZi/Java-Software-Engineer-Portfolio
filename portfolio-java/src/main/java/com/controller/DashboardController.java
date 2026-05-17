package com.controller;

import com.model.Project;
import com.model.User;
import com.repository.ProjectRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        // Kiểm tra nếu chưa đăng nhập
        if (oAuth2User == null) {
            return "redirect:/login";
        }

        // Lấy thông tin từ OAuth2User
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        // Tạo Map chứa thông tin user
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name != null ? name : "User");
        userInfo.put("email", email != null ? email : "email@example.com");
        userInfo.put("picture", picture != null ? picture : "https://via.placeholder.com/40");

        // Đưa vào model
        model.addAttribute("user", userInfo);

        // Lưu user vào database nếu chưa có
        if (email != null) {
            User existingUser = userRepository.findByEmail(email).orElse(null);
            if (existingUser == null) {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setDisplayName(name);
                newUser.setAvatarUrl(picture);
                userRepository.save(newUser);
            }
        }

        // Thống kê (tạm thời dữ liệu mẫu)
        Map<String, Object> stats = new HashMap<>();
        List<Project> allProjects = projectRepository.findAll();
        stats.put("projectsCount", allProjects.size());
        stats.put("skillsCount", 10);
        stats.put("avgMatch", 75);
        model.addAttribute("stats", stats);

        // Danh sách dự án
        model.addAttribute("projects", allProjects);

        return "dashboard";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/dashboard";
    }
}