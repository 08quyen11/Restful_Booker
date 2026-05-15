package com.example.demo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    @Column(columnDefinition = "TEXT")
    private String message;

    private Boolean isRead = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}