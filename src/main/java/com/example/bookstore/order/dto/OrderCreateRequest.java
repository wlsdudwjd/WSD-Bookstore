package com.example.bookstore.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateRequest(

        @NotEmpty(message = "주문 상품은 1개 이상이어야 합니다.")
        @Valid
        List<Item> items
) {

    public record Item(
            @NotNull(message = "도서 ID는 필수입니다.")
            Long bookId,

            @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
            int quantity
    ) {}
}