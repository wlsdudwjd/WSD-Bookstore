package com.example.bookstore.health;

import com.example.bookstore.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> status = Map.of(
                "status", "OK",
                "time", LocalDateTime.now().toString(),
                "app", "bookstore-api",
                "version", "v1"
        );
        return ApiResponse.success("health ok", status);
    }
}
