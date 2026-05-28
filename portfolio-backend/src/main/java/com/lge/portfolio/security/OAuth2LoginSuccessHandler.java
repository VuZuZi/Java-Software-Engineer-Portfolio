package com.lge.portfolio.security;

import com.lge.portfolio.entity.Role;
import com.lge.portfolio.entity.User;
import com.lge.portfolio.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        final String name = oauthUser.getAttribute("name") != null
                ? oauthUser.getAttribute("name")
                : "Unknown User";

        if (email == null) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }


        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    return userRepository.save(newUser);
                });

        if (user.getId() == null) {
            throw new RuntimeException("User ID is null after save");
        }

        // Generate token
        String token = jwtService.generateToken(user.getId());

        // Redirect về frontend
        response.sendRedirect("http://localhost:4200/oauth-success?token=" + token);
    }
}