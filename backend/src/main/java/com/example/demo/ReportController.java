package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "http://127.0.1:5500")
public class ReportController {
    private final BookingRepository bookingRepo;
    private final RoomRepository roomRepo;

    public ReportController(BookingRepository bookingRepo, RoomRepository roomRepo) {
        this.bookingRepo = bookingRepo;
        this.roomRepo = roomRepo;
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummaryReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("total_bookings", bookingRepo.count());
        report.put("total_rooms", roomRepo.count());
        return report;
    }
}