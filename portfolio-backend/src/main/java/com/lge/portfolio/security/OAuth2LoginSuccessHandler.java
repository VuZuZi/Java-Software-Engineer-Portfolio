package com.lge.portfolio.security;

import com.lge.portfolio.entity.Role;
import com.lge.portfolio.entity.User;
import com.lge.portfolio.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.frontend-url:http://localhost:4200}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        if (email == null) {
            throw new IllegalStateException("Email not found from OAuth2 provider");
        }

        String name = oauthUser.getAttribute("name") == null
                ? "Unknown User"
                : oauthUser.getAttribute("name");
        String avatar = oauthUser.getAttribute("picture");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> User.builder()
                        .email(email)
                        .role(Role.USER)
                        .build());

        user.setName(name);
        user.setAvatar(avatar);
        user = userRepository.save(user);

        response.sendRedirect(frontendUrl + "/oauth-success?token=" + jwtService.generateToken(user));
    }
}
