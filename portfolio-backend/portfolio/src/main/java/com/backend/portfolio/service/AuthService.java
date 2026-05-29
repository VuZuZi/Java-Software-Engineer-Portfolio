package com.backend.portfolio.service;

import com.backend.portfolio.dto.response.AuthResponse;
import com.backend.portfolio.entity.User;
import com.backend.portfolio.repository.UserRepository;
import com.backend.portfolio.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public void register(String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setProvider("LOCAL");

        userRepository.save(user);
    }

    public AuthResponse login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // bean
        String token = jwtProvider.generateToken(user.getId(), user.getEmail(), user.getRole());

        return new AuthResponse(token, user.getId(), user.getEmail());
    }
}