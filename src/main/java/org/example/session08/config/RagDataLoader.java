package org.example.session08.config;

import org.example.session08.service.DocumentIngestionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class RagDataLoader {

    // Trỏ đến file tài liệu nội bộ nằm trong thư mục src/main/resources/docs/
    @Value("classpath:docs/quy_dinh_khao_thi.pdf")
    private Resource documentResource;

    @Bean
    public CommandLineRunner initDatabase(DocumentIngestionService ingestionService, JdbcTemplate jdbcTemplate) {
        return args -> {
            System.out.println("=> [Hệ thống] Đang kiểm tra trạng thái Vector Database...");

            try {
                // Kiểm tra xem bảng vector_store đã có bản ghi nào chưa
                Integer recordCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM vector_store", Integer.class);

                // Nếu bảng trống (0 bản ghi) -> Lần đầu tiên chạy ứng dụng
                if (recordCount != null && recordCount == 0) {
                    System.out.println("=> [Hệ thống] Database đang trống. Tiến hành nạp tài liệu mặc định (Bootstrap data)...");
                    ingestionService.ingestDocument(documentResource);
                    System.out.println("=> [Hệ thống] Hoàn tất nạp Vector! Sẵn sàng phục vụ.");
                }
                // Nếu đã có dữ liệu -> Bỏ qua để tiết kiệm Token và tránh trùng lặp
                else {
                    System.out.println("=> [Hệ thống] Database đã có sẵn " + recordCount + " bản ghi Vector. Bỏ qua bước nạp tài liệu.");
                }

            } catch (Exception e) {
                // Bắt lỗi trong trường hợp bảng vector_store chưa được tạo
                // (VD: Quên bật cờ initialize-schema trong file yml)
                e.printStackTrace();
                System.err.println("=> [Lỗi] Không thể kiểm tra bảng vector_store. Vui lòng đảm bảo cấu hình spring.ai.vectorstore.pgvector.initialize-schema=true đã được bật!");
            }
        };
    }


}