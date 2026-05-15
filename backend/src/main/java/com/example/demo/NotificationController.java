package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notification")
@CrossOrigin(origins = "*")
public class NotificationController {
    private static final int MAX_MESSAGE_CHARS = 256;
    private static final String CONTACT_MESSAGE_PREFIX = "Nội dung:";

    private final NotificationRepository repo;

    public NotificationController(NotificationRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Notification> getAllNotifications() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@Valid @RequestBody Notification notification, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        String content = extractContactMessageContent(notification.getMessage());
        if (countCharacters(content) > MAX_MESSAGE_CHARS) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", "Nội dung tin nhắn không được vượt quá 256 ký tự");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Notification savedNotification = repo.save(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", savedNotification.getId(),
                "message", "Tin nhắn đã được gửi thành công tới hệ thống."
        ));
    }

    private String extractContactMessageContent(String message) {
        int index = message.lastIndexOf(CONTACT_MESSAGE_PREFIX);
        if (index >= 0) {
            return message.substring(index + CONTACT_MESSAGE_PREFIX.length());
        }
        return message;
    }

    private int countCharacters(String text) {
        if (text == null) {
            return 0;
        }
        String value = text.trim();
        return value.codePointCount(0, value.length());
    }

    @PutMapping("/{id}/read")
    public Notification markAsRead(@PathVariable Long id) {
        Notification existingNotif = repo.findById(id).orElse(null);
        if (existingNotif != null) {
            existingNotif.setIsRead(true);
            return repo.save(existingNotif);
        }
        return null;
    }

    // --- THÊM PHẦN NÀY ĐỂ XÓA THÔNG BÁO ---
    @DeleteMapping("/{id}")
    public String deleteNotification(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return "Đã xóa thông báo ID: " + id;
        }
        return "Không tìm thấy thông báo!";
    }
}