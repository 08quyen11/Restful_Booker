package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên địa điểm không được để trống")
    private String title;

    @NotBlank(message = "Mô tả không được để trống")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Địa chỉ dòng 1 không được để trống")
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Giờ nhận phòng không được để trống")
    private String checkinTime;

    @NotBlank(message = "Giờ trả phòng không được để trống")
    private String checkoutTime;

    @NotBlank(message = "Link bản đồ không được để trống")
    @Column(columnDefinition = "TEXT")
    private String mapEmbedUrl;
}
