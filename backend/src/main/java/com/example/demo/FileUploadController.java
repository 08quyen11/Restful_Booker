package com.example.demo; // <-- ĐỔI CHO KHỚP VỚI CÁC CONTROLLER KHÁC

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileUploadController {

    // Lưu thẳng ra thư mục "uploads" ở ngoài cùng project để Spring Boot không bị cache
// Sửa dòng cũ của bạn thành dòng này:
public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        File directory = new File(UPLOAD_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, fileName);
        
        Files.write(fileNameAndPath, file.getBytes());

        // Vẫn trả về /images/ để Frontend và DB hiểu
        return "/images/" + fileName;
    }
}