package com.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @GetMapping("/check-session")
    public String checkSession(@AuthenticationPrincipal OAuth2User user, HttpSession session) {
        if (user == null) {
            return "❌ Chưa đăng nhập! Session ID: " + session.getId();
        }
        return "✅ Đã đăng nhập: " + user.getAttribute("name") +
                "<br>Session ID: " + session.getId() +
                "<br>Session Creation Time: " + session.getCreationTime();
    }
}