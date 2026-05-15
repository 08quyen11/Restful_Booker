package com.example.demo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên phòng không được để trống")
    @Size(min = 2, max = 50, message = "Tên phòng phải từ 2 đến 50 ký tự")
    @Column(name = "room_name", unique = true) // Ràng buộc duy nhất ở CSDL
    private String roomName;

    @NotBlank(message = "Loại phòng không được để trống")
    @Column(name = "room_type")
    private String roomType;

    @NotNull(message = "Giá phòng không được để trống")
    @Min(value = 1, message = "Giá phòng/đêm phải lớn hơn 0")
    @Column(name = "price_per_night")
    private Integer pricePerNight;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(columnDefinition = "TEXT")
    private String image;
}