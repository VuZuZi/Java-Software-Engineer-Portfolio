package com.backend.portfolio.security.jwt;

import com.backend.portfolio.entity.User;
import com.backend.portfolio.repository.UserRepository;
import com.backend.portfolio.security.CustomUserDetails;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        try {

            String token = header.substring(7);

            if (!jwtProvider.validateToken(token)) {

                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtProvider.getEmail(token);

            User user = userRepository
                    .findByEmail(email)
                    .orElse(null);

            if (user != null
                    && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                CustomUserDetails userDetails =
                        new CustomUserDetails(user);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                auth.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);
            }

        } catch (JwtException ex) {

            log.warn("JWT error: {}", ex.getMessage());

        } catch (Exception ex) {

            log.error("Authentication error", ex);
        }

        filterChain.doFilter(request, response);
    }
}