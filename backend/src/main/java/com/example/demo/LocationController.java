package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/location")
@CrossOrigin(origins = "*")
public class LocationController {
    private final LocationRepository repo;

    public LocationController(LocationRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<?> getLocation() {
        return repo.findAll().stream().findFirst()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Chưa có thông tin location")));
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@Valid @RequestBody Location locationDetails, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getValidationErrors(result));
        }

        Location location = repo.findAll().stream().findFirst().orElse(new Location());
        location.setTitle(locationDetails.getTitle());
        location.setDescription(locationDetails.getDescription());
        location.setAddressLine1(locationDetails.getAddressLine1());
        location.setAddressLine2(locationDetails.getAddressLine2());
        location.setPhone(locationDetails.getPhone());
        location.setEmail(locationDetails.getEmail());
        location.setCheckinTime(locationDetails.getCheckinTime());
        location.setCheckoutTime(locationDetails.getCheckoutTime());
        location.setMapEmbedUrl(locationDetails.getMapEmbedUrl());

        return ResponseEntity.ok(repo.save(location));
    }

    private Map<String, String> getValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
