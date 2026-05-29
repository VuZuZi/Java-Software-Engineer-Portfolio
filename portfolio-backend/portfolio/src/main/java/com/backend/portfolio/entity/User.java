package com.backend.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    private String provider; // LOCAL / GOOGLE

    @Enumerated(EnumType.STRING)
    private Role role; // 🔥
}