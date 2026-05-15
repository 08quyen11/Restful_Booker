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
import java.util.Optional;

@RestController
@RequestMapping("/room")
@CrossOrigin(origins = "*")
public class RoomController {
    private final RoomRepository repo;

    public RoomController(RoomRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        Optional<Room> room = repo.findById(id);
        if (room.isPresent())
            return ResponseEntity.ok(room.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy phòng!"));
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@Valid @RequestBody Room room, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getValidationErrors(result));
        if (isDuplicateRoomName(room.getRoomName(), null))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("roomName", "Tên phòng đã tồn tại!"));
        if (room.getIsAvailable() == null)
            room.setIsAvailable(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @Valid @RequestBody Room roomDetails,
            BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getValidationErrors(result));

        Optional<Room> optRoom = repo.findById(id);
        if (optRoom.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy phòng cần sửa!"));
        if (isDuplicateRoomName(roomDetails.getRoomName(), id))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("roomName", "Tên phòng cập nhật bị trùng lặp!"));

        Room existingRoom = optRoom.get();
        existingRoom.setRoomName(roomDetails.getRoomName());
        existingRoom.setRoomType(roomDetails.getRoomType());
        existingRoom.setPricePerNight(roomDetails.getPricePerNight());

        if (roomDetails.getImage() != null && !roomDetails.getImage().isEmpty()) {
            existingRoom.setImage(roomDetails.getImage());
        }
        if (roomDetails.getIsAvailable() != null) {
            existingRoom.setIsAvailable(roomDetails.getIsAvailable());
        }

        return ResponseEntity.ok(repo.save(existingRoom));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Đã xóa phòng thành công"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy phòng để xóa!"));
    }

    private Map<String, String> getValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

    private boolean isDuplicateRoomName(String roomName, Long currentRoomId) {
        return repo.findByRoomNameIgnoreCase(roomName.trim())
                .filter(room -> currentRoomId == null || !room.getId().equals(currentRoomId))
                .isPresent();
    }
}