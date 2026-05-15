package com.example.demo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Phòng đặt không được để trống")
    @Column(name = "room_id")
    private Long roomId;

    @NotBlank(message = "Họ/Tên đệm không được để trống")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = "Họ chỉ được chứa chữ cái, không chứa số hay ký tự đặc biệt")
    private String firstname;

    @NotBlank(message = "Tên không được để trống")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = "Tên chỉ được chứa chữ cái, không chứa số hay ký tự đặc biệt")
    private String lastname;
@NotBlank(message = "Số điện thoại không được để trống")
@Pattern(
    regexp = "^[0-9]+$",
    message = "Số điện thoại chỉ được nhập số"
)
private String phone;
    @NotNull(message = "Tổng tiền không hợp lệ")
    @Min(value = 0, message = "Tổng tiền không được âm")
    private Integer totalprice;

    private Boolean depositpaid;

    @NotNull(message = "Ngày nhận phòng bắt buộc chọn")
    @FutureOrPresent(message = "Ngày nhận phòng không được ở trong quá khứ")
    private LocalDate checkin;

    @NotNull(message = "Ngày trả phòng bắt buộc chọn")
    private LocalDate checkout;

    private String additionalneeds;
}