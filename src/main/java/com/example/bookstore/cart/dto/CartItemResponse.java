package com.example.bookstore.cart.dto;

import com.example.bookstore.cart.entity.CartItem;

public record CartItemResponse(
        Long cartItemId,
        Long bookId,
        String title,
        Integer quantity,
        Integer unitPrice,
        Integer subtotal
) {
    public static CartItemResponse from(CartItem item) {
        return new CartItemResponse(
                item.getCartItemId(),
                item.getBook().getBookId(),
                item.getBook().getTitle(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }
}