package com.example.demo.config; // Lưu ý: Nếu bạn không tạo thư mục config, hãy xóa chữ .config đi

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. Lấy đường dẫn thư mục hiện tại của dự án
        String userDir = System.getProperty("user.dir");
        
        // 2. Cấu hình: Khi trình duyệt gọi /images/** // thì Spring Boot sẽ trỏ vào thư mục vật lý "uploads" trên máy bạn
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + userDir + "/uploads/");
        
        // Dòng này rất quan trọng: Nó giúp ảnh hiện ngay mà không cần Restart server
    }
}