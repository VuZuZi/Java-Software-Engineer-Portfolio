package com.controller;

import com.model.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LandingController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String landing(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        addUserToModel(oAuth2User, model);
        return "landing";
    }

    @GetMapping("/landing")
    public String landingPage(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        addUserToModel(oAuth2User, model);
        return "landing";
    }

    private void addUserToModel(OAuth2User oAuth2User, Model model) {
        if (oAuth2User == null) {
            return;
        }

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        String role = "USER";
        if (email != null) {
            User existingUser = userRepository.findByEmail(email).orElse(null);
            if (existingUser != null && existingUser.getRole() != null) {
                role = existingUser.getRole().name();
            }
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name != null ? name : "User");
        userInfo.put("email", email != null ? email : "email@example.com");
        userInfo.put("picture", picture != null ? picture : "https://via.placeholder.com/40");
        userInfo.put("role", role);

        model.addAttribute("user", userInfo);
    }
}