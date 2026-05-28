package com.lge.portfolio.config;

import com.lge.portfolio.security.JwtAuthFilter;
import com.lge.portfolio.security.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for API
                .csrf(csrf -> csrf.disable())

                // Authorization
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/error",
                                "/auth/**",
                                "/oauth2/**",
                                "/login/**",
                                "/api/projects/**"   // ✅ đặt trước anyRequest
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // OAuth2 Login
                .oauth2Login(oauth -> oauth
                        .successHandler(oAuth2LoginSuccessHandler)
                )

                // Logout (optional but recommended)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                )

                // JWT Filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}