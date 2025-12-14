package com.example.bookstore.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private String status;      // "success" or "error"
    private String message;
    private String statusCode;  // e.g. "200", "400", ...

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // 성공 응답
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, "200", data);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>("success", message, "200", null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>("success", "요청이 성공했습니다.", "201", data);
    }

    public static ApiResponse<Void> noContent() {
        return new ApiResponse<>("success", "요청이 성공했습니다.", "204", null);
    }

    // 실패 응답
    public static <T> ApiResponse<T> error(String message, String statusCode) {
        return new ApiResponse<>("error", message, statusCode, null);
    }
}
