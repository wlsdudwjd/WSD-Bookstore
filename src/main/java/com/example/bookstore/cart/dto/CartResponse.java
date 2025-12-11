package com.example.bookstore.cart.dto;

import java.util.List;

public record CartResponse(
        List<CartItemResponse> items,
        Integer totalAmount
) {
    public static CartResponse of(List<CartItemResponse> items) {
        int sum = items.stream()
                .mapToInt(CartItemResponse::subtotal)
                .sum();
        return new CartResponse(items, sum);
    }
}