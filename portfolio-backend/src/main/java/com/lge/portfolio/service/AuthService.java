package com.lge.portfolio.service;

import com.lge.portfolio.dto.AuthRequest;
import com.lge.portfolio.dto.AuthResponse;
import com.lge.portfolio.dto.RegisterRequest;
import com.lge.portfolio.dto.UserResponse;
import com.lge.portfolio.entity.Role;
import com.lge.portfolio.entity.User;
import com.lge.portfolio.exception.DuplicateResourceException;
import com.lge.portfolio.exception.ResourceNotFoundException;
import com.lge.portfolio.repository.UserRepository;
import com.lge.portfolio.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email is already registered");
        }

        User user = User.builder()
                .email(email)
                .name(request.name().trim())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        return toAuthResponse(userRepository.save(user));
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(normalizeEmail(request.email()))
                .filter(foundUser -> foundUser.getPasswordHash() != null)
                .filter(foundUser -> passwordEncoder.matches(request.password(), foundUser.getPasswordHash()))
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        return toAuthResponse(user);
    }

    public UserResponse getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .map(UserResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    private AuthResponse toAuthResponse(User user) {
        return new AuthResponse(jwtService.generateToken(user), UserResponse.from(user));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
