package com.example.bookstore.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateRequest(
        @NotNull(message = "bookId는 필수입니다.") Long bookId,
        @NotBlank(message = "content는 비워둘 수 없습니다.") String content,
        @Min(value = 1, message = "rating은 1 이상이어야 합니다.")
        @Max(value = 5, message = "rating은 5 이하여야 합니다.")
        int rating
) {
}
