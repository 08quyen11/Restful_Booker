Cách cài đặt và triển khai hệ thống:
- Công cụ: Visual Code
- Link dự án Github: https://github.com/08quyen11/Restful_Booker.git
- Bước 1: Clone dự án về máy
- Mở Terminal chạy: 
- git clone https://github.com/08quyen11/Restful_Booker.git
- cd Restful_Booker
- Bước 2: Cài đặt và chạy Backend (Spring Boot):
	Mở thư mục backend trong VS Code:
	Cấu hình Database: Mở file: backend/src/main/resources/application.properties
    Sửa lại thông tin kết nối MySQL cho đúng:
      spring.datasource.password=123456     # ← thay bằng password MySQL của bạn
  
-	Bước 3: Tạo Database trong MySQL: 
      CREATE DATABASE restful_booker_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-	Bước 4: Chạy Backend:
      Trong VS Code: Click phải vào file DemoApplication.java → Run Java
  Hoặc dùng Terminal:
      cd backend
      ./mvnw spring-boot:run
- > Backend sẽ chạy ở cổng 8085: http://localhost:8085/swagger-ui/index.html
- > Frontend sẽ chạy ở cổng 8085: http://localhost:8085/index.html
