package com.controller;

import com.model.Project;
import com.model.User;
import com.model.Role;
import com.repository.ProjectRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private static final String DEFAULT_AVATAR = "https://via.placeholder.com/40";
    private static final int DEFAULT_SKILLS_COUNT = 10;
    private static final int DEFAULT_AVG_MATCH = 75;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        if (oAuth2User == null) {
            return "redirect:/login";
        }

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        model.addAttribute("user", buildUserInfo(email, name, picture));
        saveUserIfNotExists(email, name, picture);
        model.addAttribute("stats", buildStats());
        model.addAttribute("projects", projectRepository.findAll());

        return "dashboard";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/check-role")
    @ResponseBody
    public String checkRole(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return "❌ Chưa đăng nhập!";
        }

        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "❌ Không tìm thấy user!";
        }

        String role = user.getRole() != null ? user.getRole().name() : "NULL";

        return "📧 Email: " + email + "\n" +
                "🔑 Role: " + role + "\n" +
                "👑 isAdmin: " + (role.equals("ADMIN") ? "✅ YES" : "❌ NO");
    }

    private Map<String, Object> buildUserInfo(String email, String name, String picture) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name != null ? name : "User");
        userInfo.put("email", email != null ? email : "email@example.com");
        userInfo.put("picture", picture != null ? picture : DEFAULT_AVATAR);
        userInfo.put("role", getUserRole(email));
        return userInfo;
    }

    private String getUserRole(String email) {
        if (email == null) {
            return "USER";
        }
        return userRepository.findByEmail(email)
                .map(user -> {
                    String role = user.getRole() != null ? user.getRole().name() : "USER";
                    System.out.println("📌 Email: " + email + " - Role: " + role);
                    return role;
                })
                .orElse("USER");
    }

    private void saveUserIfNotExists(String email, String name, String picture) {
        if (email == null) {
            return;
        }

        if (userRepository.findByEmail(email).isEmpty()) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setDisplayName(name);
            newUser.setAvatarUrl(picture);
            newUser.setRole(Role.USER);
            userRepository.save(newUser);
            System.out.println("✅ Đã tạo user mới: " + email + " với role USER");
        }
    }

    private Map<String, Object> buildStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("projectsCount", projectRepository.findAll().size());
        stats.put("skillsCount", DEFAULT_SKILLS_COUNT);
        stats.put("avgMatch", DEFAULT_AVG_MATCH);
        return stats;
    }
}