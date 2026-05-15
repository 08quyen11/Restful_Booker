package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/booking")
@CrossOrigin(origins = "*")
public class BookingController {
    private final BookingRepository repo;

    public BookingController(BookingRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    // --- API LẤY CHI TIẾT 1 BOOKING ĐỂ ĐỔ VÀO FORM SỬA ---
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Booking> booking = repo.findById(id);
        if (booking.isPresent()) {
            return ResponseEntity.ok(booking.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy đơn đặt phòng!"));
    }

    // --- API LẤY DANH SÁCH CÁC NGÀY ĐÃ ĐẶT CỦA 1 PHÒNG (DÙNG CHO FE BLOCK LỊCH)
    // ---
    @GetMapping("/booked-dates/{roomId}")
    public ResponseEntity<List<Map<String, LocalDate>>> getBookedDates(@PathVariable Long roomId) {
        List<Booking> bookings = repo.findByRoomId(roomId);
        List<Map<String, LocalDate>> bookedRanges = new ArrayList<>();
        for (Booking b : bookings) {
            Map<String, LocalDate> range = new HashMap<>();
            range.put("checkin", b.getCheckin());
            range.put("checkout", b.getCheckout());
            bookedRanges.add(range);
        }
        return ResponseEntity.ok(bookedRanges);
    }

    // --- API POST: TẠO MỚI BOOKING (CÓ CHECK TRÙNG LỊCH) ---
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Booking booking, BindingResult result) {
        Map<String, String> errors = getValidationErrors(result);
        if (!errors.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

        // Logic 1: Trả phòng phải sau Nhận phòng
        if (booking.getCheckin() != null && booking.getCheckout() != null) {
            if (!booking.getCheckout().isAfter(booking.getCheckin())) {
                errors.put("checkout", "Ngày trả phòng phải sau ngày nhận phòng ít nhất 1 ngày!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            // Logic 2: Kiểm tra trùng lịch với các booking khác của cùng phòng đó
            List<Booking> overlaps = repo.findOverlappingBookings(
                    booking.getRoomId(), booking.getCheckin(), booking.getCheckout(), null);

            if (!overlaps.isEmpty()) {
                errors.put("dateConflict", "Phòng này đã có người đặt trong khoảng thời gian bạn chọn!");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(booking));
    }

    // --- API PUT: CẬP NHẬT BOOKING (CÓ CHECK TRÙNG LỊCH LOẠI TRỪ CHÍNH NÓ) ---
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Booking details, BindingResult result) {
        Map<String, String> errors = getValidationErrors(result);
        if (!errors.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

        Optional<Booking> opt = repo.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy đơn cần sửa!"));
        }

        if (details.getCheckin() != null && details.getCheckout() != null) {
            if (!details.getCheckout().isAfter(details.getCheckin())) {
                errors.put("checkout", "Ngày trả phòng phải sau ngày nhận phòng ít nhất 1 ngày!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            // Kiểm tra trùng lịch (truyền ID hiện tại vào để không bị tự trùng với chính
            // đơn cũ)
            List<Booking> overlaps = repo.findOverlappingBookings(
                    details.getRoomId(), details.getCheckin(), details.getCheckout(), id);

            if (!overlaps.isEmpty()) {
                errors.put("dateConflict", "Khoảng thời gian cập nhật bị trùng lịch với khách khác!");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
            }
        }

        Booking existing = opt.get();
        existing.setRoomId(details.getRoomId());
        existing.setFirstname(details.getFirstname());
        existing.setLastname(details.getLastname());
        existing.setPhone(details.getPhone());
        existing.setCheckin(details.getCheckin());
        existing.setCheckout(details.getCheckout());
        existing.setAdditionalneeds(details.getAdditionalneeds());
        existing.setTotalprice(details.getTotalprice());

        return ResponseEntity.ok(repo.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private Map<String, String> getValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }
        return errors;
    }
}