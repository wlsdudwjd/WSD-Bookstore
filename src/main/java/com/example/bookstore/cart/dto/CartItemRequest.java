package com.example.bookstore.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequest(

        @NotNull
        Long bookId,

        @NotNull
        @Min(0)
        Integer quantity
) {
}
