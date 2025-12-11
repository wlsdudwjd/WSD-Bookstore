// 주문 요청에 들어오는 각 아이템
package com.example.bookstore.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull Long bookId,
        @Min(1) int quantity
) {}