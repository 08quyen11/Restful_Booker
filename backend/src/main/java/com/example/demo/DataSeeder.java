package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final LocationRepository locationRepository;

    public DataSeeder(AdminRepository adminRepository, LocationRepository locationRepository) {
        this.adminRepository = adminRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public void run(String... args) {
        // Tạo tài khoản admin mặc định nếu chưa tồn tại
        if (adminRepository.findByUsername("admin").isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword("password123");
            adminRepository.save(admin);
            System.out.println("=== Đã tạo tài khoản admin mặc định: admin / password123 ===");
        } else {
            System.out.println("=== Tài khoản admin đã tồn tại trong DB ===");
        }

        if (locationRepository.count() == 0) {
            Location location = new Location();
            location.setTitle("Shady Meadows B&B");
            location.setDescription("Shady Meadows B&B nằm ở vị trí tuyệt đẹp giữa vùng nông thôn thanh bình, chỉ cách trung tâm thành phố vài phút lái xe. Địa điểm lý tưởng để khám phá cảnh quan thiên nhiên và thưởng thức bữa sáng từ nguyên liệu địa phương.");
            location.setAddressLine1("The Old Forge, Shady Street");
            location.setAddressLine2("Newfordburyshire, NE1 410S");
            location.setPhone("012 345 6789");
            location.setEmail("info@shadymeadowsbb.com");
            location.setCheckinTime("14:00");
            location.setCheckoutTime("11:00");
            location.setMapEmbedUrl("https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3151.835434509374!2d144.9537353!3d-37.8162159!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x6ad65d4c2b349649%3A0xb6899234e561db11!2sEnvato!5e0!3m2!1sen!2s!4v1234567890");
            locationRepository.save(location);
        }
    }
}
