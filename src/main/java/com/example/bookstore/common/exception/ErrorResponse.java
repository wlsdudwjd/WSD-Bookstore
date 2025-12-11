package com.example.bookstore.common.exception;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ErrorResponse(
        LocalDateTime timestamp,
        String path,
        int status,
        String code,
        String message,
        Map<String, Object> details
) {
}