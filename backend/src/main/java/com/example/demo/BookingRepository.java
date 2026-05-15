package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Tìm các booking của phòng giao nhau với khoảng thời gian checkin - checkout
    // Ghi chú: Sử dụng logic checkin < existing.checkout VÀ checkout >
    // existing.checkin
    // Tham số :bookingId dùng để loại trừ chính đơn đó khi đang thực hiện PUT (Sửa)
    @Query("SELECT b FROM Booking b WHERE b.roomId = :roomId " +
            "AND (:bookingId IS NULL OR b.id != :bookingId) " +
            "AND b.checkin < :checkout AND b.checkout > :checkin")
    List<Booking> findOverlappingBookings(@Param("roomId") Long roomId,
            @Param("checkin") LocalDate checkin,
            @Param("checkout") LocalDate checkout,
            @Param("bookingId") Long bookingId);

    // Lấy tất cả booking của một phòng cụ thể để trả về danh sách ngày bận cho FE
    List<Booking> findByRoomId(Long roomId);
}