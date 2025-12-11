package com.example.bookstore.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private boolean isSuccess;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T payload;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object meta;

    public static <T> ApiResponse<T> success(String message, T payload) {
        return new ApiResponse<>(true, message, payload, null);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null, null);
    }

    public static <T> ApiResponse<T> success(String message, T payload, Object meta) {
        return new ApiResponse<>(true, message, payload, meta);
    }

    public static <T> ApiResponse<T> ok(T payload) {
        return success("요청이 성공했습니다.", payload);
    }

    public static <T> ApiResponse<T> created(T payload) {
        return success("요청이 성공했습니다.", payload);
    }

    public static ApiResponse<Void> noContent() {
        return success("요청이 성공했습니다.", null);
    }

    public static <T> ApiResponse<T> fail(String message, T payload) {
        return new ApiResponse<>(false, message, payload, null);
    }
}
