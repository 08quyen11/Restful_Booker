package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AdminRepository adminRepository;

    public AuthController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> createToken(@RequestBody AuthRequest request) {
        Optional<Admin> admin = adminRepository.findByUsername(request.getUsername());

        if (admin.isPresent() && admin.get().getPassword().equals(request.getPassword())) {
            String token = UUID.randomUUID().toString().replace("-", "").substring(0, 15);
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}