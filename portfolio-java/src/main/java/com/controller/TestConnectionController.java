package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class TestConnectionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test-db")
    public String testDatabase() {
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap("SELECT 1 as status, DATABASE() as database_name, NOW() as current_time");
            return "✅ KẾT NỐI THÀNH CÔNG!\n" +
                    "Database: " + result.get("database_name") + "\n" +
                    "Thời gian: " + result.get("current_time") + "\n" +
                    "Status: " + result.get("status");
        } catch (Exception e) {
            return "❌ KẾT NỐI THẤT BẠI!\nLỗi: " + e.getMessage();
        }
    }

    @GetMapping("/test-create-table")
    public String testCreateTable() {
        try {
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS test_table (" +
                    "id INT PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            return "✅ Bảng test_table đã được tạo thành công!";
        } catch (Exception e) {
            return "❌ Lỗi tạo bảng: " + e.getMessage();
        }
    }

    @GetMapping("/test-insert-data")
    public String testInsertData() {
        try {
            jdbcTemplate.execute("INSERT INTO test_table (id, name) VALUES (1, 'Test Connection')");
            return "✅ Dữ liệu đã được insert thành công!";
        } catch (Exception e) {
            return "❌ Lỗi insert: " + e.getMessage();
        }
    }
}