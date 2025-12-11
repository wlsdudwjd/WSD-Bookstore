package com.example.bookstore.common.api;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
        boolean success,
        T data
) {
    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }
}