package com.example.demo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username không được để trống")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password không được để trống")
    @Column(nullable = false)
    private String password;
}
