package com.example.bookstore.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, data);
    }
}